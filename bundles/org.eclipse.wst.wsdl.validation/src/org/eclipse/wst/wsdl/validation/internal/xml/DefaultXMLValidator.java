/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.StandardParserConfiguration;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.eclipse.wst.wsdl.validation.internal.ValidationMessageImpl;
import org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolutionResult;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;
import org.w3c.dom.DOMError;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.ibm.wsdl.Constants;

/**
 * The default implementation of IXMLValidator.
 */
public class DefaultXMLValidator implements IXMLValidator
{
  protected String uri;
  protected URIResolver uriResolver = null;
  protected List errors = new ArrayList();
  protected StringBuffer schemaLocationString = new StringBuffer();
  protected List ignoredNamespaceList = new ArrayList();
  
  protected InputStream inputStream = null;
  
  protected String currentErrorKey = null;
  protected Object[] currentMessageArguments = null;
  
  protected boolean isChildOfDoc = false;
  protected XMLGrammarPool grammarPool = null; 

/**
   * Constructor.
   */
  public DefaultXMLValidator()
  {
    super();
    
    ignoredNamespaceList.add(Constants.NS_URI_XSD_1999);
    ignoredNamespaceList.add(Constants.NS_URI_XSD_2000);
    ignoredNamespaceList.add(Constants.NS_URI_XSD_2001);
  }

  /**
   * @see org.eclipse.validate.wsdl.xmlconformance.IXMLValidatorAction#setFile(IFile)
   */
  public void setFile(String uri)
  {
    this.uri = uri;
  }
  
  public void setURIResolver(URIResolver uriResolver)
  {
  	this.uriResolver = uriResolver;
  }
  
  public void setGrammarPool(XMLGrammarPool grammarPool)
  {
	this.grammarPool = grammarPool;
  }

  /**
   * @see org.eclipse.validate.wsdl.xmlconformance.IXMLValidatorAction#run()
   */
  public void run()
  {
    // Validate the XML file.
    try
    {
      Reader reader1 = null; // Used for validation parse.
      
      InputSource validateInputSource; 
     
      
      if (this.inputStream != null)
      {    
      

        String string = createStringForInputStream(inputStream);
        reader1 = new StringReader(string);
          
        validateInputSource = new InputSource(inputStream);
        validateInputSource.setCharacterStream(reader1);
      } else
      { validateInputSource = new InputSource(uri);
      }
      
      XMLReader reader = createXMLReader();
      reader.parse(validateInputSource);
    }
    catch (SAXParseException e)
    {
      // No need to add an error here. SAXParseExceptions are reported by the error reporter.
    }
    catch (IOException e)
    {
      // TODO: Log exception.
    }
    catch (Exception e)
    {
      // TODO: Log exception.
      //System.out.println(e);
    }
  }
  
  final String createStringForInputStream(InputStream inputStream)
  {
    // Here we are reading the file and storing to a stringbuffer.
    StringBuffer fileString = new StringBuffer();
    try
    {
      InputStreamReader inputReader = new InputStreamReader(inputStream);
      BufferedReader reader = new BufferedReader(inputReader);
      char[] chars = new char[1024];
      int numberRead = reader.read(chars);
      while (numberRead != -1)
      {
        fileString.append(chars, 0, numberRead);
        numberRead = reader.read(chars);
      }
    }
    catch (Exception e)
    {
      //TODO: log error message
      //e.printStackTrace();
    }
    return fileString.toString();
  }

  /**
   * @see org.eclipse.validate.wsdl.xmlconformance.IXMLValidatorAction#hasErrors()
   */
  public boolean hasErrors()
  {
    return !errors.isEmpty();
  }

  /**
   * @see org.eclipse.validate.wsdl.xmlconformance.IXMLValidatorAction#getErrorList()
   */
  public List getErrors()
  {
    return errors;
  }
  
  public void addError(String message, int line, int column, String uri)
  {
	  errors.add(new ValidationMessageImpl(message, line, column, ValidationMessageImpl.SEV_WARNING, uri, currentErrorKey, currentMessageArguments));
  }

  /**
   * The handler for the SAX parser. This handler will obtain the WSDL
   * namespace, handle errors and resolve entities.
   */
  protected class XMLConformanceDefaultHandler extends DefaultHandler
  {
    /**
     * @see org.xml.sax.ErrorHandler#error(SAXParseException)
     */
    public void error(SAXParseException arg0) throws SAXException
    {
     String tempURI = arg0.getSystemId();
     if (inputStream!= null && arg0.getSystemId() == null)
     {
       //mh: In this case we are validating a stream so the URI may be null in this exception
       tempURI = uri;       
     }
     addError(arg0.getMessage(), arg0.getLineNumber(), arg0.getColumnNumber(), tempURI);
    }

    /**
     * @see org.xml.sax.ErrorHandler#fatalError(SAXParseException)
     */
    public void fatalError(SAXParseException arg0) throws SAXException
    {
      addError(arg0.getMessage(), arg0.getLineNumber(), arg0.getColumnNumber(), arg0.getSystemId());
    }

    /**
     * @see org.xml.sax.ErrorHandler#warning(SAXParseException)
     */
    public void warning(SAXParseException arg0) throws SAXException
    {
      addError(arg0.getMessage(), arg0.getLineNumber(), arg0.getColumnNumber(), arg0.getSystemId());
    }
	
	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(localName.equals("documentation") && (uri.equals(Constants.NS_URI_WSDL) || uri.equals(Constants.NS_URI_XSD_2001)|| uri.equals(Constants.NS_URI_XSD_1999) || uri.equals(Constants.NS_URI_XSD_2000)))
		{
		  isChildOfDoc = false;
		}
		super.endElement(uri, localName, qName);
	}
	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if(localName.equals("documentation") && (uri.equals(Constants.NS_URI_WSDL) || uri.equals(Constants.NS_URI_XSD_2001)|| uri.equals(Constants.NS_URI_XSD_1999) || uri.equals(Constants.NS_URI_XSD_2000)))
		{
		  isChildOfDoc = true;
		}
		super.startElement(uri, localName, qName, atts);
	}

    /**
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String,
     *      java.lang.String)
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException
    {
      // If we're currently examining a subelement of the 
	  // WSDL or schema documentation element we don't want to resolve it
	  // as anything is allowed as a child of documentation.
	  if(isChildOfDoc)
	  {
	    return new InputSource();
	  }
    if(publicId == null)
    {
      publicId = systemId;
    }
      
    IURIResolutionResult result = uriResolver.resolve("", publicId, systemId);
    String uri = result.getPhysicalLocation();
    if (uri != null && !uri.equals(""))
    {
	  try
	  {
//	    String entity = systemId;
//		if(publicId != null)
//		{
//		  entity = publicId;
//		 }
		URL entityURL = new URL(uri);
        InputSource is = new InputSource(result.getLogicalLocation());
		is.setByteStream(entityURL.openStream());
        if (is != null)
        {
          return is;
        }
	  }
	  catch(Exception e)
	  {
		 // Do nothing.
	  }
    }
      // This try/catch block with the IOException is here to handle a difference
      // between different versions of the EntityResolver interface.
      try
      {
        InputSource is = super.resolveEntity(publicId, systemId);
        if(is == null)
        {
          throw new IOException();
        }
        return is;
      }
      catch(IOException e)
      {
      }
      return null;
    }

  }

  /**
   * @see org.eclipse.validate.wsdl.xmlconformance.IXMLValidator#setSchemaLocation(String,
   *      String)
   */
  public void setSchemaLocation(String namespace, String location)
  {
    if (namespace != null && location != null && !namespace.equalsIgnoreCase("") && !location.equalsIgnoreCase(""))
    {
      schemaLocationString.append(" ").append(namespace).append(" ").append(formatURI(location));
    }
  }
  
  
  /**
   * @param inputStream - set the inputStream to validate
   */
  public void setInputStream(InputStream inputStream)
  {
      this.inputStream = inputStream;
  }

  /**
   * Remove space characters from a String and replace them with %20.
   * 
   * @param uri -
   *            the uri to format
   * @return the formatted string
   */
  protected String formatURI(String uri)
  {
    String space = "%20";
    StringBuffer newUri = new StringBuffer(uri);
    int newUriLength = newUri.length();

    // work backwards through the stringbuffer in case it grows to the
    // point
    // where the last space would be missed.
    for (int i = newUriLength - 1; i >= 0; i--)
    {
      if (newUri.charAt(i) == ' ')
      {
        newUri.replace(i, i + 1, space);
      }
    }

    return newUri.toString();
  }
  
  protected class XMLValidatorParserConfiguration extends StandardParserConfiguration
  {
    public XMLErrorHandler getErrorHandler() 
    {
	  return new XMLValidatorErrorHandler();
	}

	public XMLEntityResolver getEntityResolver() {
    	return new XMLEntityResolver()
    	  {

    	   
    	    
    	    /* (non-Javadoc)
    	     * @see org.apache.xerces.xni.parser.XMLEntityResolver#resolveEntity(org.apache.xerces.xni.XMLResourceIdentifier)
    	     */
    	    public XMLInputSource resolveEntity(XMLResourceIdentifier rid) throws XNIException, IOException
    	    {
//    	      XMLInputSource result = null;     
//    	      
//    	      // TODO cs : Lawrence's XMLConformanceDefaultHandler seems to need to ingore some entities
//    	      // that are part of documentation etc.  I think this resolver needs to do that
//    	      // since the XMLConformanceDefaultHandler resolver is no longer active.
//    	      if (uriResolver != null)
//    	      {         
//    	        String pid = rid.getPublicId() != null ? rid.getPublicId() : rid.getNamespace();
//    	        String systemId = uriResolver.resolve(rid.getBaseSystemId(), pid, rid.getLiteralSystemId());
//    	        result = new XMLInputSource(rid.getPublicId(), systemId, rid.getBaseSystemId());               
//    	      }
//    	      return result;
    	      
//    	    If we're currently examining a subelement of the 
    		  // WSDL or schema documentation element we don't want to resolve it
    		  // as anything is allowed as a child of documentation.
    		  if(isChildOfDoc)
    		  {
    		    return new XMLInputSource(rid);
    		  }
    		  String systemId = rid.getLiteralSystemId();
    		  if(systemId == null)
    		  {
    			systemId = rid.getNamespace();
    		  }
    		  String publicId = rid.getPublicId();
    	    if(publicId == null)
    	    {
    	      publicId = systemId;
    	    }
    	      
    	    IURIResolutionResult result = uriResolver.resolve("", publicId, systemId);
    	    String uri = result.getPhysicalLocation();
    	    if (uri != null && !uri.equals(""))
    	    {
    		  try
    		  {
//    		    String entity = systemId;
//    			if(publicId != null)
//    			{
//    			  entity = publicId;
//    			 }
    			URL entityURL = new URL(uri);
    	        XMLInputSource is = new XMLInputSource(rid.getPublicId(), systemId, result.getLogicalLocation());
    			is.setByteStream(entityURL.openStream());
    	        if (is != null)
    	        {
    	          return is;
    	        }
    		  }
    		  catch(Exception e)
    		  {
    			 // Do nothing.
    		  }
    	    }
    	      // This try/catch block with the IOException is here to handle a difference
    	      // between different versions of the EntityResolver interface.
//    	      try
//    	      {
//    	        InputSource is = super.resolveEntity(publicId, systemId);
//    	        if(is == null)
//    	        {
//    	          throw new IOException();
//    	        }
//    	        return is;
//    	      }
//    	      catch(IOException e)
//    	      {
//    	      }
    	      return null;
    	    }
    	    
    	  }; 
	}

	/* (non-Javadoc)
     * @see org.apache.xerces.parsers.DTDConfiguration#createErrorReporter()
     */
    protected XMLErrorReporter createErrorReporter()
    {
      return new XMLErrorReporter()
      {
        /* (non-Javadoc)
         * @see org.apache.xerces.impl.XMLErrorReporter#reportError(java.lang.String, java.lang.String, java.lang.Object[], short)
         */
        public void reportError(String domain, String key, Object[] arguments,
            short severity) throws XNIException
        {
          currentErrorKey = key;
          currentMessageArguments = arguments;
          super.reportError(domain, key, arguments, severity);
        }
      };
    }
  }
  
  /**
   * Create an XML Reader.
   * 
   * @return The newly created XML reader or null if unsuccessful.
   * @throws Exception
   */
  protected XMLReader createXMLReader() throws Exception
  {     
    SAXParser reader = null;
    try
    {
      reader = new org.apache.xerces.parsers.SAXParser(new XMLValidatorParserConfiguration());
      
      XMLConformanceDefaultHandler conformanceDefaultHandler = new XMLConformanceDefaultHandler();
      reader.setErrorHandler(conformanceDefaultHandler);
      reader.setContentHandler(conformanceDefaultHandler);
      
      // Older Xerces versions will thrown a NPE if a null grammar pool is set.
      if(grammarPool != null)
      {
        reader.setProperty(org.apache.xerces.impl.Constants.XERCES_PROPERTY_PREFIX + org.apache.xerces.impl.Constants.XMLGRAMMAR_POOL_PROPERTY, grammarPool);
      }
      reader.setProperty(org.apache.xerces.impl.Constants.XERCES_PROPERTY_PREFIX + org.apache.xerces.impl.Constants.ENTITY_RESOLVER_PROPERTY, new MyEntityResolver(uriResolver));
      reader.setFeature(org.apache.xerces.impl.Constants.XERCES_FEATURE_PREFIX + org.apache.xerces.impl.Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE, false);
      reader.setFeature(org.apache.xerces.impl.Constants.SAX_FEATURE_PREFIX + org.apache.xerces.impl.Constants.NAMESPACES_FEATURE, true);
      reader.setFeature(org.apache.xerces.impl.Constants.SAX_FEATURE_PREFIX + org.apache.xerces.impl.Constants.NAMESPACE_PREFIXES_FEATURE, true);
	  reader.setFeature(org.apache.xerces.impl.Constants.SAX_FEATURE_PREFIX + org.apache.xerces.impl.Constants.VALIDATION_FEATURE, true);
	  reader.setFeature(org.apache.xerces.impl.Constants.XERCES_FEATURE_PREFIX + org.apache.xerces.impl.Constants.SCHEMA_VALIDATION_FEATURE, true);
    } 
    catch(Exception e)
    { 
      // TODO: Log error.
      //e.printStackTrace();
    }
    return reader;
  } 
  
  /**
   * A custom entity resolver that uses the URI resolver specified to resolve entities.
   */
  protected class MyEntityResolver implements XMLEntityResolver 
  {
    private URIResolver uriResolver;
    
    /**
     * Constructor.
     * 
     * @param uriResolver The URI resolver to use with this entity resolver.
     */
    public MyEntityResolver(URIResolver uriResolver)
    {
      this.uriResolver = uriResolver;
    }
    
    /* (non-Javadoc)
     * @see org.apache.xerces.xni.parser.XMLEntityResolver#resolveEntity(org.apache.xerces.xni.XMLResourceIdentifier)
     */
    public XMLInputSource resolveEntity(XMLResourceIdentifier rid) throws XNIException, IOException
    {
      // If we're currently examining a subelement of the 
	  // WSDL or schema documentation element we don't want to resolve it
	  // as anything is allowed as a child of documentation.
	  if(isChildOfDoc)
	  {
	    return new XMLInputSource(rid);
	  }
	  
	  String ns = rid.getNamespace();
	  if(ns != null && ignoredNamespaceList.contains(ns))
	  {
		return new XMLInputSource(rid);
	  }
	  
	  String systemId = rid.getLiteralSystemId();
	  if(systemId == null)
	  {
		systemId = ns;
	  }
	  String publicId = rid.getPublicId();
      if(publicId == null)
      {
        publicId = systemId;
      }
      
      IURIResolutionResult result = uriResolver.resolve("", publicId, systemId);
      String uri = result.getPhysicalLocation();
      if (uri != null && !uri.equals(""))
      {
	    try
	    {
		  URL entityURL = new URL(uri);
          XMLInputSource is = new XMLInputSource(rid.getPublicId(), systemId, result.getLogicalLocation());
		  is.setByteStream(entityURL.openStream());
          if (is != null)
          {
            return is;
          }
	    }
	    catch(Exception e)
	    {
		  // No need to report this error. Simply continue below.
	    }
      }
      return null;
    }
  }  
  
  protected class XMLValidatorErrorHandler implements XMLErrorHandler
  {
	  
	/**
	   * Add an error message.
	   * 
	   * @param error The error message to add.
	   * @param line The line location of the error.
	   * @param column The column location of the error.
	   * @param uri The URI of the file containing the error.
	   */
	private void addValidationMessage(String key, XMLParseException exception, int severity)
	{
		if (severity == DOMError.SEVERITY_WARNING)
        {
			errors.add(new ValidationMessageImpl(exception.getLocalizedMessage(), exception.getLineNumber(), exception.getColumnNumber(), ValidationMessageImpl.SEV_WARNING, uri, key, currentMessageArguments));
        }
        else
        {
        	errors.add(new ValidationMessageImpl(exception.getLocalizedMessage(), exception.getLineNumber(), exception.getColumnNumber(), ValidationMessageImpl.SEV_ERROR, uri, key, currentMessageArguments));
        }
	  
	}

	public void error(String domain, String key, XMLParseException exception) throws XNIException 
	{
		addValidationMessage(key, exception, DOMError.SEVERITY_ERROR);
		
	}

	public void fatalError(String domain, String key, XMLParseException exception) throws XNIException 
	{
		addValidationMessage(key, exception, DOMError.SEVERITY_FATAL_ERROR);
		
	}

	public void warning(String domain, String key, XMLParseException exception) throws XNIException 
	{
		addValidationMessage(key, exception, DOMError.SEVERITY_WARNING);
		
	}
	  
  }
  
  protected class MyXMLErrorReporter extends XMLErrorReporter
  {

	public XMLErrorHandler getErrorHandler() {
		// TODO Auto-generated method stub
		return new XMLValidatorErrorHandler();
	}

	/* (non-Javadoc)
       * @see org.apache.xerces.impl.XMLErrorReporter#reportError(java.lang.String, java.lang.String, java.lang.Object[], short)
       */
      public void reportError(String domain, String key, Object[] arguments,
          short severity) throws XNIException
      {
        currentErrorKey = key;
        currentMessageArguments = arguments;
        super.reportError(domain, key, arguments, severity);
      }
    }
    
  
  
}

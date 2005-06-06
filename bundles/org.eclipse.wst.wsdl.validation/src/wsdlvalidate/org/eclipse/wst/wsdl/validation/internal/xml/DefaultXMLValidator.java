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
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.StandardParserConfiguration;
import org.apache.xerces.xni.XNIException;
import org.eclipse.wst.wsdl.validation.internal.ValidationMessageImpl;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.ibm.wsdl.Constants;

/**
 * The default implementation of IXMLValidator.
 */
public class DefaultXMLValidator implements IXMLValidator
{
  private final String _APACHE_FEATURE_CONTINUE_AFTER_FATAL_ERROR =
    "http://apache.org/xml/features/continue-after-fatal-error";
  private final String _APACHE_FEATURE_NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
  private final String _APACHE_FEATURE_NAMESPACES = "http://xml.org/sax/features/namespaces";
  private final String _APACHE_FEATURE_VALIDATION = "http://xml.org/sax/features/validation";
  private final String _APACHE_FEATURE_VALIDATION_SCHEMA = "http://apache.org/xml/features/validation/schema";
  private final String _APACHE_PROPERTY_EXTERNAL_SCHEMALOCATION =
    "http://apache.org/xml/properties/schema/external-schemaLocation";
  private final String DEFINITIONS = "definitions";
  
  protected static final String IGNORE_ALWAYS = "IGNORE_ALWAYS";
  protected static final String PREMATURE_EOF = "PrematureEOF";
  
  protected Hashtable ingoredErrorKeyTable = new Hashtable();

  protected String uri;
  protected URIResolver uriResolver = null;
  protected List errors = new ArrayList();
  protected StringBuffer schemaLocationString = new StringBuffer();
  protected List ignoredNamespaceList = new ArrayList();
  
  protected InputStream inputStream = null;
  
  protected String currentErrorKey = null;
  protected Object[] currentMessageArguments = null;
  
  protected boolean isChildOfDoc = false;
//  protected String wsdlNamespace = null;




/**
   * Constructor.
   */
  public DefaultXMLValidator()
  {
    super();
    
    ignoredNamespaceList.add(Constants.NS_URI_XSD_1999);
    ignoredNamespaceList.add(Constants.NS_URI_XSD_2000);
    ignoredNamespaceList.add(Constants.NS_URI_XSD_2001);
    
    ingoredErrorKeyTable.put(PREMATURE_EOF, IGNORE_ALWAYS);
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

  /**
   * @see org.eclipse.validate.wsdl.xmlconformance.IXMLValidatorAction#run()
   */
  public void run()
  {
    // Validate the XML file.
    try
    {
      Reader reader1 = null; // Used for the preparse.
      Reader reader2 = null; // Used for validation parse.
      
      InputSource validateInputSource; 
     
      
      if (this.inputStream != null)
      {    
      

        String string = createStringForInputStream(inputStream);
        reader1 = new StringReader(string);
        reader2 = new StringReader(string); 
          
        validateInputSource = new InputSource(inputStream);
        validateInputSource.setCharacterStream(reader2);
      } else
      { validateInputSource = new InputSource(uri);
      }
      
      preparse(uri, reader1);
      
      SAXParser saxparser = createSAXParser();
      XMLConformanceDefaultHandler handler = new XMLConformanceDefaultHandler();

      saxparser.setErrorHandler(handler);
	  saxparser.setEntityResolver(handler);
	  saxparser.setContentHandler(handler);
      
      saxparser.parse(validateInputSource);
//      wsdlNamespace = handler.getWSDLNamespace();
    }
    catch (SAXParseException e)
    {
      addError(e.getMessage(), e.getLineNumber(), e.getColumnNumber(), uri);
    }
    catch (IOException e)
    {
    }
    catch (Exception e)
    {
      //System.out.println(e);
    }
  }

  /**
   * Create and configure a SAX parser.
   * 
   * @return The new SAX parser.
   */
  protected SAXParser createSAXParser()
  {
    SAXParser saxParser = null;
    try
    {
      //SAXParserFactory parserfactory = new SAXParserFactoryImpl();
      try
      { MyStandardParserConfiguration configuration = new MyStandardParserConfiguration();
        saxParser = new org.apache.xerces.parsers.SAXParser(configuration);
        saxParser.setFeature(_APACHE_FEATURE_CONTINUE_AFTER_FATAL_ERROR, true);
        saxParser.setFeature(_APACHE_FEATURE_NAMESPACE_PREFIXES, true);
        saxParser.setFeature(_APACHE_FEATURE_NAMESPACES, true);
        saxParser.setFeature(_APACHE_FEATURE_VALIDATION, true);
        saxParser.setFeature(_APACHE_FEATURE_VALIDATION_SCHEMA, true);
      }
      catch (SAXNotRecognizedException e)
      {
      }
      catch (SAXNotSupportedException e)
      {
      }
      if (schemaLocationString.length() > 0)
      {
        saxParser.setProperty(_APACHE_PROPERTY_EXTERNAL_SCHEMALOCATION, schemaLocationString.toString());
      }
    }
    catch (FactoryConfigurationError e)
    {
    }
    catch (SAXNotRecognizedException e)
    {
    }
    catch (SAXNotSupportedException e)
    {
    }
//    catch (SAXException e)
//    {
//    }
    return saxParser;
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
   * Preparse the file to find all of the namespaces that are defined in order
   * to specify the schemalocation.
   * 
   * @param uri The uri of the file to parse.
   */
  protected void preparse(String uri, Reader characterStream)
  {
      javax.xml.parsers.SAXParser saxParser = null;
    try
    {
        
      InputSource inputSource; 
      
      if (characterStream != null)
      {   
          inputSource = new InputSource(uri);
          inputSource.setCharacterStream(characterStream);
      }
      else
      {
          inputSource = new InputSource(uri);
      }
      
      SAXParserFactory parserfactory = new SAXParserFactoryImpl();

      parserfactory.setFeature(_APACHE_FEATURE_NAMESPACE_PREFIXES, true);
      parserfactory.setFeature(_APACHE_FEATURE_NAMESPACES, true);

      saxParser = parserfactory.newSAXParser();
      SchemaStringHandler handler = new SchemaStringHandler(uri);
      
      saxParser.parse(inputSource, handler);
    }
    catch (FactoryConfigurationError e)
    {
    }
    catch (SAXNotRecognizedException e)
    {
    }
    catch (ParserConfigurationException e)
    {
    }
    catch (SAXNotSupportedException e)
    {
    }
    catch (SAXException e)
    {
    }
    catch (IOException e)
    {
    }
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

  /**
   * Add an error message.
   * 
   * @param error The error message to add.
   * @param line The line location of the error.
   * @param column The column location of the error.
   * @param uri The URI of the file containing the error.
   */
  protected void addError(String error, int line, int column, String uri)
  {
    errors.add(new ValidationMessageImpl(error, line, column, ValidationMessageImpl.SEV_ERROR, uri, currentErrorKey, currentMessageArguments));
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
      
    String uri = uriResolver.resolve(null, publicId, systemId);
    if (uri != null && !uri.equals(""))
    {
		  try
		  {
	      String entity;
		    entity = systemId;
		    if(publicId != null)
		    {
			    entity = publicId;
		    }
		    URL entityURL = new URL(uri);
        InputSource is = new InputSource(entity);
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

  /**
   * A handler used in preparsing to get the schemaLocation string.
   */
  protected class SchemaStringHandler extends DefaultHandler
  {
    private final String XMLNS = "xmlns";
	  private final String TARGETNAMESPACE = "targetNamespace";
    private String baselocation = null;

    public SchemaStringHandler(String baselocation)
    {
      this.baselocation = baselocation;
    }
    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localname, String arg2, Attributes attributes) throws SAXException
    {
      if (localname.equals(DEFINITIONS))
      {
        String targetNamespace = attributes.getValue(TARGETNAMESPACE);
        int numAtts = attributes.getLength();
        for (int i = 0; i < numAtts; i++)
        {

          String attname = attributes.getQName(i);
          if (attname.startsWith(XMLNS))
          {
            String namespace = attributes.getValue(i);
            if(!(namespace.equals(targetNamespace) || ignoredNamespaceList.contains(namespace)))
            {
              String resolvedURI = namespace;
              setSchemaLocation(namespace, resolvedURI);
            }
          }
        }

      }
      super.startElement(uri, localname, arg2, attributes);

    }
  }

  
  protected class MyStandardParserConfiguration extends StandardParserConfiguration
  {
    public MyStandardParserConfiguration()
    {
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
  
  
}

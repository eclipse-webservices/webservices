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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.eclipse.core.resources.IFile;
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
      preparse(uri);
      SAXParser saxparser = createSAXParser();
      XMLConformanceDefaultHandler handler = new XMLConformanceDefaultHandler();
      saxparser.parse(uri, handler);
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
      SAXParserFactory parserfactory = new SAXParserFactoryImpl();
      try
      {
        parserfactory.setFeature(_APACHE_FEATURE_CONTINUE_AFTER_FATAL_ERROR, true);
        parserfactory.setFeature(_APACHE_FEATURE_NAMESPACE_PREFIXES, true);
        parserfactory.setFeature(_APACHE_FEATURE_NAMESPACES, true);
        parserfactory.setFeature(_APACHE_FEATURE_VALIDATION, true);
        parserfactory.setFeature(_APACHE_FEATURE_VALIDATION_SCHEMA, true);
      }
      catch (ParserConfigurationException e)
      {
      }
      catch (SAXNotRecognizedException e)
      {
      }
      catch (SAXNotSupportedException e)
      {
      }
      parserfactory.setNamespaceAware(true);
      parserfactory.setValidating(true);
      saxParser = parserfactory.newSAXParser();

      if (schemaLocationString.length() > 0)
      {
        saxParser.getXMLReader().setProperty(_APACHE_PROPERTY_EXTERNAL_SCHEMALOCATION, schemaLocationString.toString());
      }
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
    return saxParser;
  }

  /**
   * Preparse the file to find all of the namespaces that are defined in order
   * to specify the schemalocation.
   * 
   * @param uri The uri of the file to parse.
   */
  protected void preparse(String uri)
  {
    SAXParser saxParser = null;
    try
    {
      SAXParserFactory parserfactory = new SAXParserFactoryImpl();

      parserfactory.setFeature(_APACHE_FEATURE_NAMESPACE_PREFIXES, true);
      parserfactory.setFeature(_APACHE_FEATURE_NAMESPACES, true);

      saxParser = parserfactory.newSAXParser();
      SchemaStringHandler handler = new SchemaStringHandler(uri);
      saxParser.parse(uri, handler);
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
    errors.add(new ValidationMessageImpl(error, line, column, ValidationMessageImpl.SEV_ERROR, uri));
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
      addError(arg0.getMessage(), arg0.getLineNumber(), arg0.getColumnNumber(), arg0.getSystemId());
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
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String,
     *      java.lang.String)
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException
    {
      String uri = uriResolver.resolve(null, publicId, systemId);
      if (uri != null && !uri.equals(""))
      {
        InputSource is = new InputSource(uri);
        if (is != null)
        {
          return is;
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

//    /**
//     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
//     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
//     */
//    public void startElement(String uri, String localname, String arg2, Attributes attributes) throws SAXException
//    {
//      if (localname.equals(DEFINITIONS))
//      {
//        wsdlURI = uri;
//      }
//      super.startElement(uri, localname, arg2, attributes);
//
//    }

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
        
        int numAtts = attributes.getLength();
        for (int i = 0; i < numAtts; i++)
        {

          String attname = attributes.getQName(i);
          if (attname.startsWith(XMLNS))
          {
            String namespace = attributes.getValue(i);
            if(!ignoredNamespaceList.contains(namespace))
            {
              String resolvedURI = uriResolver.resolve(baselocation, namespace, namespace);
              if(resolvedURI != null)
              {
                setSchemaLocation(namespace, resolvedURI);
              }
            }
          }
        }

      }
      super.startElement(uri, localname, arg2, attributes);

    }
  }

//  /**
//   * @see org.eclipse.wsdl.validate.internal.xml.IXMLValidator#getWSDLNamespace()
//   */
//  public String getWSDLNamespace()
//  {
//    return wsdlNamespace;
//  }

}

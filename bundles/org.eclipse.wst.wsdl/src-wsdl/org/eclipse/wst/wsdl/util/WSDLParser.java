/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.wst.wsdl.WSDLPlugin;
import org.eclipse.xsd.XSDPlugin;
import org.eclipse.xsd.util.XSDConstants;
import org.eclipse.xsd.util.XSDParser;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;


/**
 * The <b>SAX Parser</b> for the WSDL model. This class' main responsibility is
 * to compute line/column information for all elements in the source XML
 * document. This information is stored in a map of each node to its user data.
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this
 * API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 * 
 * [ISSUE] Most of this class' implementation is a clone of XSDParser. There are
 * plans to work on closely integrating this class and XSDParser to provide for
 * the most efficient processing of inline schemas.
 */
public class WSDLParser extends DefaultHandler implements LexicalHandler
{
  /**
   * The name of the user data part holding end column location of an element.
   */
  private static final String END_COLUMN = "endColumn"; //$NON-NLS-1$

  /**
   * The name of the user data part holding end line location of an element.
   */
  private static final String END_LINE = "endLine"; //$NON-NLS-1$

  /**
   * The name of the user data part holding start column location of an element.
   */
  private static final String START_COLUMN = "startColumn"; //$NON-NLS-1$

  /**
   * The name of the user data part holding start line location of an element.
   */
  private static final String START_LINE = "startLine"; //$NON-NLS-1$

  /**
   * Holds pairs Node -> Map with user data.
   * 
   * @see #END_COLUMN
   * @see #END_LINE
   * @see #START_COLUMN
   * @see #START_LINE
   * 
   * A WeakHashMap is used in order to simplify this map's maintenance. As nodes
   * are garbage collected when not needed anymore, the JVM will take care of
   * removing entries from this map.
   */
  protected static final Map userDataMap = Collections.synchronizedMap(new WeakHashMap());

  /**
   * Returns the column at which the given node ends.
   * 
   * @param node
   *          the node to query.
   * @return the column at which the given node ends.
   */
  public static int getEndColumn(Node node)
  {
    Integer result = (Integer)getUserData(node).get(END_COLUMN);
    return result == null ? 1 : result.intValue();
  }

  /**
   * Returns the line at which the given node ends.
   * 
   * @param node
   *          the node to query.
   * @return the line at which the given node ends.
   */
  public static int getEndLine(Node node)
  {
    Integer result = (Integer)getUserData(node).get(END_LINE);
    return result == null ? 1 : result.intValue();
  }

  /**
   * Returns the column at which the given node starts.
   * 
   * @param node
   *          the node to query.
   * @return the column at which the given node starts.
   */
  public static int getStartColumn(Node node)
  {
    Integer result = (Integer)getUserData(node).get(START_COLUMN);
    return result == null ? 1 : result.intValue();
  }

  /**
   * Returns the line at which the given node starts.
   * 
   * @param node
   *          the node to query.
   * @return the line at which the given node starts.
   */
  public static int getStartLine(Node node)
  {
    Integer result = (Integer)getUserData(node).get(START_LINE);
    return result == null ? 1 : result.intValue();
  }

  /**
   * Returns the user data associated with the node. If the node has no user
   * data, a new empty map is created.
   * 
   * @param node
   *          the node to query.
   * @return the user data associated with the node.
   */
  public static Map getUserData(Node node)
  {
    Map result = (Map)userDataMap.get(node);
    if (result == null)
    {
      result = new HashMap();
      userDataMap.put(node, result);
    }
    return result;
  }

  protected int column;

  List diagnostics = new ArrayList();

  protected Document document;

  protected Element element;

  protected String encoding;

  protected boolean inSchema;

  protected int line;

  protected Locator locator;

  protected SAXParser saxParser;

  protected Stack stack = new Stack();

  /**
   * Default constructor.
   */
  public WSDLParser()
  {
    saxParser = createSAXParser();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
   */
  public void characters(char[] characters, int start, int length) throws SAXException
  {
    Text textNode = document.createTextNode(new String(characters, start, length));
    element.appendChild(textNode);
    saveLocation();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
   */
  public void comment(char[] characters, int start, int length) throws SAXException
  {
    Comment commentNode = document.createComment(new String(characters, start, length));
    if (element == null)
    {
      document.appendChild(commentNode);
    }
    else
    {
      element.appendChild(commentNode);
    }
    saveLocation();
  }

  /**
   * Creates an empty DOM Document.
   * 
   * @return an empty DOM Document or null is a JAXP misconfiguration error
   *         occurs.
   */
  private Document createDocument()
  {
    try
    {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      documentBuilderFactory.setValidating(false);
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      return documentBuilder.newDocument();
    }
    catch (ParserConfigurationException exception)
    {
      WSDLPlugin.INSTANCE.log(exception);
      return null;
    }
  }

  /**
   * Creates the SAXParser instance used for parsing the source WSDL XML
   * document.
   * 
   * @return a configured SAXParser instance or null if an exception occurs.
   *         Problems are reported through the diagnostics collection.
   */
  private SAXParser createSAXParser()
  {
    SAXParser saxParser = null;
    try
    {
      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
      saxParserFactory.setNamespaceAware(true);
      saxParserFactory.setValidating(false);

      saxParserFactory.setFeature("http://xml.org/sax/features/validation", false); //$NON-NLS-N$
      saxParserFactory.setFeature("http://xml.org/sax/features/namespaces", true); //$NON-NLS-N$
      saxParserFactory.setFeature("http://xml.org/sax/features/namespace-prefixes", true); //$NON-NLS-N$

      saxParser = saxParserFactory.newSAXParser();

      saxParser.setProperty("http://xml.org/sax/properties/lexical-handler", this); //$NON-NLS-N$
    }
    catch (SAXException exception)
    {
      fatalError(exception);
    }
    catch (ParserConfigurationException exception)
    {
      fatalError(exception);
    }

    return saxParser;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ext.LexicalHandler#endCDATA()
   */
  public void endCDATA()
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#endDocument()
   */
  public void endDocument()
  {
    element = null;
    saveLocation();
    this.locator = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ext.LexicalHandler#endDTD()
   */
  public void endDTD()
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    saveLocation();

    Map extendedAttributes = null;

    if (inSchema)
    {
      extendedAttributes = XSDParser.getUserData(element);
    }
    else
    {
      extendedAttributes = getUserData(element);
    }

    extendedAttributes.put(END_LINE, new Integer(line));
    extendedAttributes.put(END_COLUMN, new Integer(column));

    if (isSchemaElement(uri, localName))
    {
      inSchema = false;
    }

    element = (Element)stack.pop();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
   */
  public void endEntity(String name)
  {
  }

  public void error(SAXParseException exception)
  {
    WSDLDiagnosticImpl diagnostic = new WSDLDiagnosticImpl();
    diagnostic.setSeverity(WSDLDiagnosticSeverity.ERROR_LITERAL);
    diagnostic.setMessage(WSDLPlugin.INSTANCE.getString("_UI_IOError_message", new Object []{ exception.getMessage() }));
    diagnostic.setLine(exception.getLineNumber());
    diagnostic.setColumn(exception.getColumnNumber());
    diagnostics.add(diagnostic);
  }

  protected void fatalError(IOException exception)
  {
    WSDLDiagnosticImpl diagnostic = new WSDLDiagnosticImpl();
    diagnostic.setSeverity(WSDLDiagnosticSeverity.FATAL_LITERAL);
    diagnostic.setMessage(WSDLPlugin.INSTANCE.getString("_UI_IOError_message", new Object []{ exception.getMessage() }));
    diagnostics.add(diagnostic);
  }

  protected void fatalError(ParserConfigurationException exception)
  {
    WSDLDiagnosticImpl diagnostic = new WSDLDiagnosticImpl();
    diagnostic.setSeverity(WSDLDiagnosticSeverity.FATAL_LITERAL);
    diagnostic.setMessage(WSDLPlugin.INSTANCE.getString("_UI_ParserError_message", new Object []{ exception.getMessage() }));
    diagnostics.add(diagnostic);
  }

  public void fatalError(SAXException exception)
  {
    WSDLDiagnosticImpl diagnostic = new WSDLDiagnosticImpl();
    diagnostic.setSeverity(WSDLDiagnosticSeverity.FATAL_LITERAL);
    diagnostic.setMessage(WSDLPlugin.INSTANCE.getString("_UI_ParserError_message", new Object []{ exception.getMessage() }));
    diagnostics.add(diagnostic);
  }

  public void fatalError(SAXParseException exception)
  {
    WSDLDiagnosticImpl diagnostic = new WSDLDiagnosticImpl();
    diagnostic.setSeverity(WSDLDiagnosticSeverity.FATAL_LITERAL);
    diagnostic.setMessage(WSDLPlugin.INSTANCE.getString("_UI_ParserError_message", new Object []{ exception.getMessage() }));
    diagnostic.setLine(exception.getLineNumber());
    diagnostic.setColumn(exception.getColumnNumber());
    diagnostics.add(diagnostic);
  }

  /**
   * Provides a collection with the diagnostics generated during parsing.
   * 
   * @return a Collection of {@link WSDLDiagnostic} objects.
   */
  public Collection getDiagnostics()
  {
    return diagnostics;
  }

  /**
   * Provides the DOM document created by parsing the WSDL document.
   * 
   * @return the resulting DOM document.
   */
  public Document getDocument()
  {
    return document;
  }

  /**
   * Determines if an element is the root XML schema element.
   * 
   * @param uri
   *          the URI to test.
   * @param localName
   *          the element's local name.
   * @return true if the element is the root XML schema element, false
   *         otherwise.
   */
  private boolean isSchemaElement(String uri, String localName)
  {
    if (uri.equals(XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001) && localName.equals(XSDConstants.SCHEMA_ELEMENT_TAG))
    {
      return true;
    }
    return false;
  }

  public void parse(InputSource inputSource)
  {
    try
    {
      saxParser.parse(inputSource, this);
    }
    catch (IOException exception)
    {
      fatalError(exception);
    }
    catch (SAXException exception)
    {
      if (diagnostics.isEmpty())
      {
        fatalError(exception);
      }
    }
  }

  /**
   * Parses the XML content read from the given input stream.
   * 
   * @param inputStream
   *          the source input stream. Must not be null.
   */
  public void parse(InputStream inputStream)
  {
    try
    {
      saxParser.parse(new InputSource(inputStream), this);
    }
    catch (IOException exception)
    {
      fatalError(exception);
    }
    catch (SAXException exception)
    {
      if (diagnostics.isEmpty())
      {
        fatalError(exception);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#processingInstruction(java.lang.String,
   *      java.lang.String)
   */
  public void processingInstruction(String target, String data)
  {
    Node processingInstruction = document.createProcessingInstruction(target, data);

    if (stack.isEmpty())
    {
      document.appendChild(processingInstruction);
    }
    else
    {
      element.appendChild(processingInstruction);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#resolveEntity(java.lang.String,
   *      java.lang.String)
   */
  public InputSource resolveEntity(String publicId, String systemId) throws SAXException
  {
    InputSource inputSource;
    if ("-//W3C//DTD XMLSCHEMA 200102//EN".equalsIgnoreCase(publicId))
    {
      inputSource = new InputSource(XSDPlugin.INSTANCE.getBaseURL() + "cache/www.w3.org/2001/XMLSchema.dtd");
      inputSource.setPublicId(publicId);
    }
    else if (systemId != null && systemId.startsWith("file://bundleentry:"))
    {
      inputSource = new InputSource(systemId.substring(7));
      inputSource.setPublicId(publicId);
    }
    else
    {
      try
      {
        inputSource = super.resolveEntity(publicId, systemId);
        if (false)
        {
          throw new IOException();
        }
      }
      catch (IOException exception)
      {
        throw new SAXException(exception);
      }
    }

    return inputSource;
  }

  /**
   * Saves the current line and column numbers.
   */
  protected void saveLocation()
  {
    line = locator.getLineNumber();
    column = locator.getColumnNumber();

    // The crimson parser seems to give poor coodinates and is 0-based for line
    // count.

    if (column == -1)
    {
      column = 1;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#setDocumentLocator(org.xml.sax.Locator)
   */
  public void setDocumentLocator(Locator locator)
  {
    this.locator = locator;
    super.setDocumentLocator(locator);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ext.LexicalHandler#startCDATA()
   */
  public void startCDATA()
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#startDocument()
   */
  public void startDocument()
  {
    saveLocation();
    document = createDocument();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void startDTD(String name, String publicId, String systemId)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    if (!inSchema)
    {
      inSchema = isSchemaElement(uri, localName);
    }

    Element newElement = document.createElementNS(uri, qName);

    for (int index = 0, count = attributes.getLength(); index < count; ++index)
    {
      String attributeURI = attributes.getURI(index);
      String attributeQName = attributes.getQName(index);
      String attributeValue = attributes.getValue(index);

      if (attributeQName.equals("xmlns") || attributeQName.startsWith("xmlns:")) //$NON-NLS-1$ //$NON-NLS-2$
      {
        attributeURI = XSDConstants.XMLNS_URI_2000;
      }
      else if ("".equals(attributeURI)) //$NON-NLS-1$
      {
        attributeURI = null;
      }

      newElement.setAttributeNS(attributeURI, attributeQName, attributeValue);
    }

    if (stack.isEmpty())
    {
      document.appendChild(newElement);
    }
    else
    {
      element.appendChild(newElement);
    }

    stack.push(element);
    element = newElement;

    // Make sure we location attributes end up in the XSDParser's user data if
    // we are inside an inline schema. If not, they should go in the
    // WSDLParser's user data.

    Map extendedAttributes = null;

    if (inSchema)
    {
      extendedAttributes = XSDParser.getUserData(element);
    }
    else
    {
      extendedAttributes = getUserData(element);
    }
    extendedAttributes.put(START_LINE, new Integer(line));
    extendedAttributes.put(START_COLUMN, new Integer(column));

    saveLocation();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
   */
  public void startEntity(String name)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#warning(org.xml.sax.SAXParseException)
   */
  public void warning(SAXParseException exception)
  {
    WSDLDiagnosticImpl diagnostic = new WSDLDiagnosticImpl();
    diagnostic.setSeverity(WSDLDiagnosticSeverity.WARNING_LITERAL);
    diagnostic.setMessage("DOM:" + exception.getMessage());
    diagnostic.setLine(exception.getLineNumber());
    diagnostic.setColumn(exception.getColumnNumber());
    diagnostics.add(diagnostic);
  }
}

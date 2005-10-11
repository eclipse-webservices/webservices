/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public final class XMLUtils
{
  /**
   * Serialize an XML Element into a String.
   * @param e Element to be serialized.
   * @param omitXMLDeclaration boolean representing whether or not to omit the XML declaration.
   * @return String representation of the XML document fragment.
   */
  public static final String serialize(Element e,boolean omitXMLDeclaration)
  {
    if (e != null)
    {
      try
      {
		DOMSource domSource = new DOMSource(e);
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, ((omitXMLDeclaration) ? "yes" : "no"));
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.ENCODING, HTMLUtils.UTF8_ENCODING);
		serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.transform(domSource, new	StreamResult(baos));
		baos.close();
		return new String(baos.toByteArray(), HTMLUtils.UTF8_ENCODING);
      }
      catch (Throwable t)
      {
      }
    }
    return null;
  }
    
  /**
   * Serialize an XML Element into a String.
   * @param df DocumentFragment to be serialized.
   * @param omitXMLDeclaration boolean representing whether or not to omit the XML declaration.
   * @return String representation of the XML document fragment.
   */  
  public static final String serialize(DocumentFragment df,boolean omitXMLDeclaration)
  {
    if (df != null)
    {
      try
      {
		DOMSource domSource = new DOMSource(df);
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, ((omitXMLDeclaration) ? "yes" : "no"));
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.ENCODING, HTMLUtils.UTF8_ENCODING);
		serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializer.transform(domSource, new	StreamResult(baos));
		baos.close();
		return new String(baos.toByteArray(), HTMLUtils.UTF8_ENCODING);
      }
      catch (Throwable t)
      {
      }
    }
    return null;
  }
  
  /**
   * Create a new XML Document.
   * @param docBuilder DocumentBuilder. Setting this to null will create a new DocumentBuilder.
   * @return Document
   */
  public static final Document createNewDocument(DocumentBuilder docBuilder) throws ParserConfigurationException
  {
    if (docBuilder == null)
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(false);
      factory.setValidating(false);
      docBuilder = factory.newDocumentBuilder();
    }
    Document doc = docBuilder.newDocument();
    return doc;
  }

  /**
   * Convert the String representation of an Element into an Element
   * @param String representation of an Element.
   * @return Element
   */
  public static Element stringToElement(String s) throws ParserConfigurationException, SAXException, UnsupportedEncodingException, IOException {
    return stringToElement(s, false);
  }

  /**
   * Convert the String representation of an Element into an Element
   * @param String representation of an Element.
   * @param boolean set whether the return Element should be namespace aware.
   * @return Element
   */
  public static Element stringToElement(String s, boolean namespaceAware) throws ParserConfigurationException, SAXException, UnsupportedEncodingException, IOException
  {
    return byteArrayToElement(s.getBytes(HTMLUtils.UTF8_ENCODING), namespaceAware);
  }

  /**
   * Convert the byte array representation of an Element into an Element
   * @param byte[] representation of an Element.
   * @param boolean set whether the return Element should be namespace aware.
   * @return Element
   */
  public static Element byteArrayToElement(byte[] b, boolean namespaceAware) throws ParserConfigurationException, SAXException, UnsupportedEncodingException, IOException
  {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    docBuilderFactory.setNamespaceAware(namespaceAware);
    docBuilderFactory.setValidating(false);
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.parse(new ByteArrayInputStream(b));
    return doc.getDocumentElement();
  }
}

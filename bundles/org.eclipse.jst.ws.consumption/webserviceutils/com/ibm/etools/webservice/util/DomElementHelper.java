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
/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package webserviceutils.org.eclipse.jst.ws.util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;

/**
 * @author gilberta
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DomElementHelper {


  public static org.w3c.dom.Element createDomElementFromXMLString(java.lang.String xmlString )
  {
    java.io.StringReader stringReader = new java.io.StringReader(xmlString); 
    org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource(stringReader); 
    try
    {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document document = docBuilder.parse(inputSource);
      return document.getDocumentElement();
    }
    catch (Throwable t)
    {
      return null;
    }
  }

  public static java.lang.String domWriter(org.w3c.dom.Node node,java.lang.StringBuffer buffer)
  {
    if ( node == null ) {
      return "";
    }
	int type = node.getNodeType();
	switch ( type ) {
	  case org.w3c.dom.Node.DOCUMENT_NODE: {
	  buffer.append(JspUtils.markup("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") + "<br>");
	  domWriter(((org.w3c.dom.Document)node).getDocumentElement(),buffer);
	  break;
	}
	  case org.w3c.dom.Node.ELEMENT_NODE: {
	  buffer.append(JspUtils.markup("<" + node.getNodeName()));
	  org.w3c.dom.Attr attrs[] = sortAttributes(node.getAttributes());
	  for ( int i = 0; i < attrs.length; i++ ) {
	    org.w3c.dom.Attr attr = attrs[i];
	    buffer.append(" " + attr.getNodeName() + "=\"" + JspUtils.markup(attr.getNodeValue()) + "\"");
	  }
	    buffer.append(JspUtils.markup(">"));
	    org.w3c.dom.NodeList children = node.getChildNodes();
	    if ( children != null ) {
	      int len = children.getLength();
		  for ( int i = 0; i < len; i++ ) {
		    if(((org.w3c.dom.Node)children.item(i)).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
			  buffer.append("<br>");
			domWriter(children.item(i),buffer);
		  }
		}
		buffer.append(JspUtils.markup("</" + node.getNodeName() + ">"));
		break;
      }
	  case org.w3c.dom.Node.ENTITY_REFERENCE_NODE: {
	    org.w3c.dom.NodeList children = node.getChildNodes();
	    if ( children != null ) {
		  int len = children.getLength();
		  for ( int i = 0; i < len; i++ )
	      {
		    buffer.append(children.item(i));
		  }
		}
		break;
	  }
	  case org.w3c.dom.Node.CDATA_SECTION_NODE: {
	    buffer.append(JspUtils.markup(node.getNodeValue()));
	    break;
      }
	  case org.w3c.dom.Node.TEXT_NODE:{
	    buffer.append(JspUtils.markup(node.getNodeValue()));
	    break;
	  }
	  case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:{
		buffer.append(JspUtils.markup("<?"));
		buffer.append(node.getNodeName());
		String data = node.getNodeValue();
		if ( data != null && data.length() > 0 ){
	      buffer.append(" ");
		  buffer.append(data);
		}
		buffer.append(JspUtils.markup("?>"));
		  break;
		}
	  }
	  return buffer.toString();
    }
 
	public static org.w3c.dom.Attr[] sortAttributes(org.w3c.dom.NamedNodeMap attrs)
	{
	  int len = (attrs != null) ? attrs.getLength() : 0;
	  org.w3c.dom.Attr array[] = new org.w3c.dom.Attr[len];
	  for ( int i = 0; i < len; i++ ){
	   array[i] = (org.w3c.dom.Attr)attrs.item(i);
	  }
	  for ( int i = 0; i < len - 1; i++ ) {
	    String name  = array[i].getNodeName();
	    int index = i;
	    for ( int j = i + 1; j < len; j++ ) {
	      String curName = array[j].getNodeName();
		  if ( curName.compareTo(name) < 0 ) {
		    name  = curName;
			index = j;
		  }
		}
		if ( index != i ) {
		  org.w3c.dom.Attr temp = array[i];
		  array[i] = array[index];
		  array[index] = temp;
		}
	  }
	  return (array);
    }
 

}

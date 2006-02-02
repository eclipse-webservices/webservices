/*******************************************************************************
 * Copyright (c) 2005,2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060131   123963 andyzhai@ca.ibm.com - Andy Zhai
 *******************************************************************************/
package org.eclipse.jst.ws.util;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * @author gilberta
 */
public class SoapElementHelper
{
	public static javax.xml.soap.SOAPElement createSOAPElementFromXMLString(String xmlString) throws ParserConfigurationException, IOException, SAXException
	{
		java.io.StringReader stringReader = new java.io.StringReader(xmlString); 
		org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource(stringReader); 
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		SAXParser parser = factory.newSAXParser();
		SoapElementSaxHandler handler = new SoapElementSaxHandler();
		parser.parse(inputSource,handler);
		return handler.getSOAPElement();    
	}

	public static java.lang.String soapElementWriter(javax.xml.soap.SOAPElement node,java.lang.StringBuffer buffer)
	{
		if (node == null ) {
			return "";
		}
		
		buffer.append(JspUtils.markup("<" + node.getElementName().getLocalName()));
		java.util.Iterator attrs = node.getAllAttributes();
		while(attrs.hasNext()) {
			javax.xml.soap.Name attr = (javax.xml.soap.Name)attrs.next();
			buffer.append(" " + attr.getQualifiedName() + "=\"" + JspUtils.markup(node.getAttributeValue(attr)) + "\"");
		}
		buffer.append(JspUtils.markup(">"));
		java.util.Iterator children = node.getChildElements();
		if ( children != null ) {
			while(children.hasNext()){
				javax.xml.soap.Node childNode = (javax.xml.soap.Node)children.next();
				if(childNode instanceof javax.xml.soap.SOAPElement){
					buffer.append("<br>");
					soapElementWriter((javax.xml.soap.SOAPElement)childNode,buffer);
				}
				else
					buffer.append(JspUtils.markup(((javax.xml.soap.Text)childNode).getValue())); 
			}
			buffer.append(JspUtils.markup("</" + node.getElementName().getLocalName() + ">"));
		}
		return buffer.toString();
	}
}

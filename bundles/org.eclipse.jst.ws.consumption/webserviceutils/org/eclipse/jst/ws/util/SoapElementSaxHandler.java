/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author xinzhai
 */
public class SoapElementSaxHandler extends DefaultHandler
{
	//Stores all values of pairs of prefix and URI in the document
	private HashMap prefixURIMapping = new HashMap();
	//Stores a list of URIs for a particular SOAP element
	private ArrayList uris = new ArrayList();
	//Represents the whole(root) SOAP document
	private SOAPElement rootElement = null;
	//Represents the current SOAP element where SAX parser is reading
	private SOAPElement currentElement = null;
	private SOAPFactory soapFactory;
	
	public SOAPElement getSOAPElement()
	{
		return rootElement;
	}
	
	public void startDocument() throws SAXException
	{
		try 
		{
			soapFactory = SOAPFactory.newInstance();
		}
		catch (SOAPException e) 
		{
			throw new SAXException("Can't create a SOAPFactory instance", e);
		}	
	}

	public void startPrefixMapping(String prefix, String uri)
	{
		prefixURIMapping.put(uri,prefix);
		uris.add(uri);
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		String str = String.valueOf(ch);
		//Add non-trivial text as a text node
		if (length > 1)
		{
			try 
			{
				currentElement.addTextNode(str.substring(start,start+length));
			} 
			catch (SOAPException e) 
			{
				throw new SAXException("Can't add a text node into SOAPElement from text", e);
			}
		}
	}
	
	public void endElement (String uri, String localName, String qName)
	{
		if (currentElement != rootElement )
		{
			currentElement = currentElement.getParentElement();
		}
	}
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
	{
		String prefix = (String)prefixURIMapping.get(namespaceURI);
		try
		{
			// Create and/or add child element
			if (rootElement == null && currentElement == null)
			{
				rootElement = soapFactory.createElement(localName,prefix,namespaceURI);
				currentElement = rootElement;
			} 
			else
			{
				currentElement = currentElement.addChildElement(localName,prefix,namespaceURI);
			}
			
			// Add namespace declaration
			if (uris.size() > 0)
			{
				for (int i = 0; i < uris.size(); i++)
				{
					String uri = (String)uris.get(i);
					String pre = (String)prefixURIMapping.get(uri);
					currentElement.addNamespaceDeclaration(pre,uri);
				}
				// Need to reset uris as we will use it for next element.
				uris.clear(); 
			}
			
			// Add attibutes
			for (int i =0; i<atts.getLength(); i++)
			{
				Name attriName;
				if (atts.getURI(i) != null)
				{
					String attriPre = (String)prefixURIMapping.get(atts.getURI(i));
					attriName = soapFactory.createName(atts.getLocalName(i),attriPre,atts.getURI(i));
				}
				else
				{
					attriName = soapFactory.createName(atts.getLocalName(i));
				}
				currentElement.addAttribute(attriName, atts.getValue(i));
			}
		}
		catch (SOAPException e) 
		{
			throw new SAXException(e);
		}
	}
}


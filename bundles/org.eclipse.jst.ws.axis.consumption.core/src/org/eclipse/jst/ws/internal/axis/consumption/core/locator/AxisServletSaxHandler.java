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
 * 20060517   140832 andyzhai@ca.ibm.com - Andy Zhai
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.consumption.core.locator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AxisServletSaxHandler extends DefaultHandler
{
	private final static String AXIS_SERVLET_CLASS_NAME = "org.apache.axis.transport.http.AxisServlet";
	
	private final static String SERVLET_CLASS_NAME = "servlet-class";
	
	private boolean isThereAxisServlet = false;
	
	private String currentElement = null;
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
	{
		currentElement = localName;
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if ( !isThereAxisServlet && SERVLET_CLASS_NAME.equals(currentElement.trim()) && String.valueOf(ch).indexOf(AXIS_SERVLET_CLASS_NAME) > -1)
		{
			isThereAxisServlet = true;
		}
	}
	
	public boolean isThereAxisServlet()
	{
		return this.isThereAxisServlet;
	}
}

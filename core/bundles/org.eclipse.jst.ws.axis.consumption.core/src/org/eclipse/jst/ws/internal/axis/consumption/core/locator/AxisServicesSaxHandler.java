/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060517   140832 andyzhai@ca.ibm.com - Andy Zhai
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.consumption.core.locator;

import java.util.List;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AxisServicesSaxHandler extends DefaultHandler
{
	private Vector webServicesNames = new Vector();
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
	{
		if (localName.trim().toLowerCase().equals("service"))
		{	
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getLocalName(i).trim().toLowerCase().equals("name"))
				{
					String serviceName = atts.getValue(i);
		            webServicesNames.add(serviceName);
				}
			}
		}
	}
	
	public List getWebServicesNames()
	{
		return this.webServicesNames;
	}
}

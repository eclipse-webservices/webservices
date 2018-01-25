/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20081208   257618 mahutch@ca.ibm.com - Mark Hutchinson, Add Mechanism for Adopters to map Services to WSDL URLs
 *******************************************************************************/
package org.eclipse.wst.ws.internal.wsfinder;

public class WSDLURLStringWrapper {

	public String wsdlURL;
	
	public WSDLURLStringWrapper(String wsdlURL) {
		this.wsdlURL = wsdlURL;
	}
	
	public String getWSDLURLString() {
		return wsdlURL;
	}
}

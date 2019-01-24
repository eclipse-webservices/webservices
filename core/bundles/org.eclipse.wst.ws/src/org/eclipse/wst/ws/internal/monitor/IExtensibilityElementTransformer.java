/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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
 * 20080715   240722 makandre@ca.ibm.com - Andrew Mak, Cannot setup TCP/IP Monitor for soap12 endpoints
 *******************************************************************************/

package org.eclipse.wst.ws.internal.monitor;

import javax.wsdl.extensions.ExtensibilityElement;

/**
 * A transformer that takes an endpoint extensibility element and returns
 * the endpoint location URL as a String.
 */
public interface IExtensibilityElementTransformer {

	/**
	 * Given an endpoint in the form of a WSDL extensibility element, returns
	 * the endpoint location URL as a String.
	 * 
	 * @param element An endpoint extensibility element.
	 * @return The endpoint location URL.
	 */
	public String transform(ExtensibilityElement element);
}

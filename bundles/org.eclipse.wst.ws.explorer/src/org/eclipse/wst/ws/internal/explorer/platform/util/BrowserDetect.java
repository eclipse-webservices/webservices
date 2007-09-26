/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070305   117034 makandre@ca.ibm.com - Andrew Mak, Web Services Explorer should support SOAP Headers
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper class used to detect the type of browser used. 
 */
public class BrowserDetect {
	
	/**
	 * Determine if Microsoft Internet Explorer is used.
	 * 
	 * @param request The HTTP request.
	 * @return True if the client is a Microsoft Internet Explorer web browser, false otherwise.
	 */
	public static final boolean isMicrosoftInternetExplorer(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		
		if (userAgent != null && userAgent.toLowerCase().indexOf("msie") != -1)
			return true;
		
		return false;
	}
}

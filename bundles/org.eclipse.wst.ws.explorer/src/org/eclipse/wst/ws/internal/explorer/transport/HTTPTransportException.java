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
 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.transport;

import java.util.Map;

/**
 * A type of TransportException that can be thrown when the transport protocol
 * is HTTP.  The status code of the HTTP response is captured in this
 * exception and passed up to the Web Services Explorer.
 */
public class HTTPTransportException extends TransportException {

	private static final long serialVersionUID = 8277180731798858877L;
	
	private int statusCode;
	private Map headers;
	
	/**
	 * Constructor.
	 * 
	 * @param statusCode The HTTP status code.
	 * @param message A message about the problem that occurred.
	 * @param headers A map of the HTTP headers.
	 */
	public HTTPTransportException(int statusCode, String message, Map headers) {
		super(message);
		this.statusCode = statusCode;
		this.headers = headers;
	}
	
	/**
	 * Returns the HTTP status code used to create this exception.
	 * 
	 * @return The HTTP status code.
	 */
	public int getStatusCode() {
		return statusCode;
	}
	
	/**
	 * Retrieve the HTTP header for the given key
	 * 
	 * @param key The key value.
	 * @return The HTTP header value for key, or null if there is no such header.
	 */
	public String getHeader(String key) {
	    Object value = headers.get(key);
	    if (value != null)
	    	return value.toString();
	    else
	    	return null;
	}
}

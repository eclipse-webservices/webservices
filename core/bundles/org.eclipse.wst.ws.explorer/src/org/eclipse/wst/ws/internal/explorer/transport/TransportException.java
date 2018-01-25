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

/**
 * An general purpose exception to indicate a problem has occurred when
 * invoking a web service.
 */
public class TransportException extends RuntimeException {

	private static final long serialVersionUID = -1502247230726021403L;

	/**
	 * Constructor.
	 * 
	 * @param message A message about the problem that occurred.
	 */
	public TransportException(String message) {
		super(message);
	}
	
	/**
	 * Constructor that accepts a message and a cause.
	 * 
	 * @param message A message about the problem that occurred.
	 * @param cause The cause for this exception.
	 */
	public TransportException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor that accepts a cause.
	 * 
	 * @param cause The cause for this exception.
	 */
	public TransportException(Throwable cause) {
		super(cause);
	}
}

/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions;

/**
 * Exception used when a method cannot be exposed as WS.
 * 
 * @author Georgi Vachkov
 */
public class MethodNotExposableException extends Exception {

	private static final long serialVersionUID = 406641585441090966L;

	private final String localizedMessage;

	/**
	 * Constructor.
	 * 
	 * @param message
	 * @param localizedMessage
	 */
	public MethodNotExposableException(String message, String localizedMessage) {
		super(message);
		this.localizedMessage = localizedMessage;
	}

	/**
	 * Provides localized message of exception 
	 * */
	@Override
	public String getLocalizedMessage() {
		return localizedMessage;
	}

}

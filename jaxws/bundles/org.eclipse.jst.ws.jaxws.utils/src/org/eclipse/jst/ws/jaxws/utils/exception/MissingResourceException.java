/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils.exception;


/**
 * Indicates that a resource of any kind couldn't be found but this was expected.
 * 
 * @author Joerg Dehmel
 */
public class MissingResourceException extends LocalizedException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an instance with a message and a localized message.
	 * 
	 * @param message
	 *            error message
	 * @param localizedMessage
	 *            localized message
	 */
	public MissingResourceException(final String message, final String localizedMessage)
	{
		super(message, localizedMessage);		
	}

	/**
	 * Creates an instance with message, localized message and causing exception.
	 * 
	 * @param message
	 *            error message
	 * @param localizedMessage
	 *            localized message
	 * @param cause
	 *            causing exception
	 */
	public MissingResourceException(final String message, final String localizedMessage, final Throwable cause)
	{
		super(message, localizedMessage, cause);		
	}

}

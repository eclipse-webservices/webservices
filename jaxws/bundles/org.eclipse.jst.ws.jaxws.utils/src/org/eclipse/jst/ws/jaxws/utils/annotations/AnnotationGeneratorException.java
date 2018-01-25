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
package org.eclipse.jst.ws.jaxws.utils.annotations;

import org.eclipse.jst.ws.jaxws.utils.exception.LocalizedException;

/**
 * Exception thrown by annotation generator in case when unexpected condition is met
 * 
 * @author Plamen Pavlov
 */

public class AnnotationGeneratorException extends LocalizedException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor getting error message, localized message and {@link Throwable} to be wrapped
	 * 
	 * @param message
	 * @param localizedMessage 
	 * @param t
	 */
	public AnnotationGeneratorException(final String message, final String localizedMessage, final Throwable t)
	{
		super(message, localizedMessage, t);	
	}

	/**
	 * Constructor with error message and a localized message.
	 * 
	 * @param message
	 * @param localizedMessage
	 */
	public AnnotationGeneratorException(final String message, final String localizedMessage)
	{
		super(message, localizedMessage);		
	}

}

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

import java.text.MessageFormat;

import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimeMessages;

/**
 * Exception used to describe that the constructor cannot be exposed in WS.
 * 
 * @author Georgi Vachkov
 */
public class ConstructorNotExposableException extends MethodNotExposableException
{

	private static final long serialVersionUID = 4504742824495057956L;

	/**
	 * Constructor
	 * 
	 * @param methodName
	 */
	public ConstructorNotExposableException(String methodName)
	{
		super(MessageFormat.format("Constructor {0} cannot be exposed as web service operation", methodName), //$NON-NLS-1$
				MessageFormat.format(JaxWsDomRuntimeMessages.ConstructorNotExposableException_Error_Message, methodName));
	}
}

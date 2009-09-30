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
 * Used when an method is not public and that is why it cannot be exposed as WS.
 * 
 * @author Georgi Vachkov
 */
public class MethodNotPublicException extends MethodNotExposableException
{
	private static final long serialVersionUID = -4792342187698558687L;

	/**
	 * Constructor.
	 * 
	 * @param methodName
	 */
	public MethodNotPublicException(String methodName)
	{
		super(MessageFormat.format("Method {0} is not public and cannot be exposed as a web service operation", methodName), //$NON-NLS-1$
				MessageFormat.format(JaxWsDomRuntimeMessages.MethodNotPublicException_Error_Message , methodName)); 
	}
}

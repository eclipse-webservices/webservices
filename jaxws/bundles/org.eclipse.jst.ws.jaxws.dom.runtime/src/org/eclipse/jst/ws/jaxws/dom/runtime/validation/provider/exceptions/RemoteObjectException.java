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
 * Exception wich is used to define that the RT class implements java.rmi.Remote
 * 
 * @author Georgi Vachkov
 */
public class RemoteObjectException extends InadmissableTypeException
{
	static final long serialVersionUID = System.currentTimeMillis();

	/**
	 * Constructor.
	 * 
	 * @param className -
	 *            RT class
	 */
	public RemoteObjectException(String className)
	{
		super(MessageFormat.format("Class {0} is a remote Object", className), //$NON-NLS-1$
				MessageFormat.format(JaxWsDomRuntimeMessages.RemoteObjectException_Error_Message, className));
	}

}

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
 * Exception used in RT validation when an RT class has multy inheritance via extention and implementation.
 */
public class InheritanceAndImplementationExecption extends InadmissableTypeException
{
	static final long serialVersionUID = System.currentTimeMillis();

	/**
	 * Construction.
	 * 
	 * @param className
	 * @param interfaceName
	 */
	public InheritanceAndImplementationExecption(String className, String interfaceName)
	{
		super(MessageFormat.format("Class {0} extends {1} and implements custom interface (multiple inheritance is not allowed)", className, interfaceName), //$NON-NLS-1$
				MessageFormat.format(JaxWsDomRuntimeMessages.InheritanceAndImplementationExecption_Error_Message, className, interfaceName));
	}
}

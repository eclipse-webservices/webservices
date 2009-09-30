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
 * Exception used in case RT class implements more that 1 interface wich is not allowed.
 * 
 * @author Georgi Vachkov
 */
public class MultipleImplementationException extends InadmissableTypeException
{
	static final long serialVersionUID = System.currentTimeMillis();

	/**
	 * Construstor.
	 * 
	 * @param className -
	 *            RT class name
	 * @param interface1 -
	 *            implemented interface 1
	 * @param interface2 -
	 *            implemented interface 2
	 */
	public MultipleImplementationException(String className, String interface1, String interface2)
	{
		super(MessageFormat.format("Class {0} implements both interface {1} and interface {2} (multiple inheritance is not allowed)", className, interface1, interface2), //$NON-NLS-1$
				MessageFormat.format(JaxWsDomRuntimeMessages.MultipleImplementationException_Error_Message, className, interface1, interface2));
	}
}

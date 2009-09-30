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
 * Exception used when an interface is used as RT class.
 * 
 * @author Georgi Vachkov
 */
public class InterfacesNotSupportedException extends InadmissableTypeException
{

	private static final long serialVersionUID = 1L;

	/**
	 * Construction.
	 * 
	 * @param interfaceFQName
	 */
	public InterfacesNotSupportedException(String interfaceFQName)
	{
		super(MessageFormat.format("{0} is a interface, and JAXB cannot handle interfaces", interfaceFQName), //$NON-NLS-1$
				MessageFormat.format(JaxWsDomRuntimeMessages.InterfacesNotSupportedException_Error_Message, interfaceFQName));
	}
}

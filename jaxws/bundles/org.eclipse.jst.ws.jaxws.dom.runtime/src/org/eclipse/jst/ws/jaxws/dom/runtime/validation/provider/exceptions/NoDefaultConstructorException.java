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
 * Exception for RT class when there is no default constructor in class.
 * 
 * @author Georgi Vachkov
 */
public class NoDefaultConstructorException extends InadmissableTypeException
{
	static final long serialVersionUID = System.currentTimeMillis();

	/**
	 * Constructor
	 * 
	 * @param typeName -
	 *            the name of RT class
	 */
	public NoDefaultConstructorException(String typeName)
	{
		super(MessageFormat.format("Class {0} does not have a default constructor", typeName), //$NON-NLS-1$
				MessageFormat.format(JaxWsDomRuntimeMessages.EndpointTypeValidator_ERR_MSG_CONSTRUCTOR, typeName));
		
		
	}
}

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
 * Class used when type is searched to define that the class was not found.
 * 
 * @author Georgi Vachkov
 * 
 */
public class TypeNotFoundException extends InadmissableTypeException
{
	static final long serialVersionUID = System.currentTimeMillis();

	/**
	 * Constructor.
	 * 
	 * @param className -
	 *            the name of the class that is not found
	 */
	public TypeNotFoundException(String className)
	{
		super(MessageFormat.format("Class {0} cannot be found", className), //$NON-NLS-1$
				MessageFormat.format(JaxWsDomRuntimeMessages.TypeNotFoundException_Error_Message, className));
	}
}

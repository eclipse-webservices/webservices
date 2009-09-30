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
 * Exception used in case when an RT class contains inner class that does not fulfil the requirements.
 * 
 * @author Georgi Vachkov
 */
public class HasInadmisableInnerTypesException extends InadmissableTypeException
{
	static final long serialVersionUID = System.currentTimeMillis();

	/**
	 * Constructor. Creates meaningfull message using <tt>className</tt>
	 * 
	 * @param className
	 */
	public HasInadmisableInnerTypesException(String className)
	{
		super(MessageFormat.format("Class {0} has inner types that are not public and static", className) //$NON-NLS-1$
				,
				MessageFormat.format(JaxWsDomRuntimeMessages.HasInadmisableInnerTypesException_Error_Message, className));
	}
}

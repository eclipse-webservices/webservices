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
 * Exception used when a RT class is abstract and is not implemented.
 * 
 * @author Georgi Vachkov
 */
public class AbstractClassNotImplementedException extends InadmissableTypeException
{

	private static final long serialVersionUID = -5871458329068966581L;

	/**
	 * Construction.
	 * 
	 * @param classFQName
	 */
	public AbstractClassNotImplementedException(String classFQName)
	{
		super(MessageFormat.format("Abstract class {0} cannot be used as runtime class because it is not extended by non abstract class which is suitable for a runtime class", classFQName), //$NON-NLS-1$
				MessageFormat.format(JaxWsDomRuntimeMessages.AbstractClassNotImplementedException_Error_Message, classFQName));
		
		
	}
}

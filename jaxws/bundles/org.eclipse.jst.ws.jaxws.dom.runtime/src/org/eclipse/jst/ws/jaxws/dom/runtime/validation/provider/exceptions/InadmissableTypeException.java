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

/**
 * Abstract class for exceptions in type validation.
 * 
 * @author Georgi Vachkov
 */
public abstract class InadmissableTypeException extends Exception
{
	static final long serialVersionUID = System.currentTimeMillis();
	final String localizedMessage;

	/**
	 * Constructor.
	 * 
	 * @param reason -
	 *            the error mesage
	 */
	public InadmissableTypeException(String reason, String localizedMessage)
	{
		super(reason);
		this.localizedMessage = localizedMessage;
	}

	@Override
	public String getLocalizedMessage() {
		return localizedMessage;
	}
	
	
}

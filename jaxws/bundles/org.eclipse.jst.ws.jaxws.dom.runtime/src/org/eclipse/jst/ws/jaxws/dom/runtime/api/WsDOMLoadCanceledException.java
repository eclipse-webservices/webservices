/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.api;

import org.eclipse.jst.ws.jaxws.utils.ContractChecker;


/**
 * Exception thrown in case the loading of WS DOM has been cancelled
 *  
 * @author Georgi Vachkov
 */
public class WsDOMLoadCanceledException extends Exception 
{
	private static final long serialVersionUID = 1L;
	private final String localizedMessage;

	/**
	 * Constructs exception with ability to provide localized message
	 * @param message - the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method)
	 * @param localizedMessage - the localized message (which is saved for later retrieval
     *         by the {@link #getLocalizedMessage()} method)
	 * 
	 * @throws NullPointerException  - in case localised message is null
	 * */
	public WsDOMLoadCanceledException(final String message, final String localizedMessage) {
		super(message);
		ContractChecker.nullCheckParam(localizedMessage, "localizedMessage"); //$NON-NLS-1$
		this.localizedMessage = localizedMessage;
	}
	
	/**
	 * Returns localized message with which the exception was constructed.*/
	@Override
	public String getLocalizedMessage() {
		return localizedMessage;
	}
}

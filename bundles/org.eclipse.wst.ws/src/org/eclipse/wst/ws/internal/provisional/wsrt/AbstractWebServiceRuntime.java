/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.provisional.wsrt;

import org.eclipse.wst.command.internal.provisional.ICommandFactory;

public abstract class AbstractWebServiceRuntime implements IWebServiceRuntime {

	public abstract IWebService getWebService();
	/**
	 */
	public abstract IWebServiceClient getWebServiceClient();
	/**
	 */
	public ICommandFactory announce(IWebService webService)
	{
		//TODO provide a boiler-plate announce implementation.
		return null;
	}
}

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

package org.eclipse.jst.ws.internal.provisional.wsrt;

import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.ICommandFactory;


public abstract class AbstractWebService implements IWebService {
	
	private WebServiceInfo info;

	public AbstractWebService(WebServiceInfo info) {
		this.info = info;
	}
	

	public WebServiceInfo getWebServiceInfo() {
		return info;
	}

	public abstract ICommandFactory assemble(Environment env, IContext ctx, ISelection sel,
			String module, String ear);
	

	public abstract ICommandFactory deploy(Environment env, IContext ctx, ISelection sel,
			String module, String ear);
	

	public abstract ICommandFactory develop(Environment env, IContext ctx, ISelection sel,
			String module, String ear);
		

	public abstract ICommandFactory install(Environment env, IContext ctx, ISelection sel,
			String module, String ear);
	

	public abstract ICommandFactory run(Environment env, IContext ctx, ISelection sel,
			String module, String ear);
}

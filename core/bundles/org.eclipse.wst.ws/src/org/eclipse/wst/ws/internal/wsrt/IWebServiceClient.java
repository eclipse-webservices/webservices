/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.wsrt;

import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.common.environment.IEnvironment;

public interface IWebServiceClient {
	
	/**
	 */
	public WebServiceClientInfo getWebServiceClientInfo();
	
	public ICommandFactory develop(IEnvironment env, IContext ctx, ISelection sel, String project, String earProject);
	public ICommandFactory deploy(IEnvironment env, IContext ctx, ISelection sel, String project, String earProject);
	public ICommandFactory assemble(IEnvironment env, IContext ctx, ISelection sel, String project, String earProject);
	public ICommandFactory install(IEnvironment env, IContext ctx, ISelection sel, String project, String earProject);
	public ICommandFactory run(IEnvironment env, IContext ctx, ISelection sel, String project, String earProject);	

}

/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.wst.command.internal.env.core.ICommandFactory;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.ws.internal.wsrt.AbstractWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.ISelection;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;

public class TestWebServiceClient extends AbstractWebServiceClient {
  
  public TestWebServiceClient(WebServiceClientInfo clientInfo){
	super(clientInfo);  
  }
  
  public ICommandFactory assemble(IEnvironment env, IContext ctx, ISelection sel,
      String project, String earProject){
    return null;	  
  }
			
  public ICommandFactory deploy(IEnvironment env, IContext ctx, ISelection sel,
      String project, String earProject){
    return null;	  
  }
			

  public ICommandFactory develop(IEnvironment env, IContext ctx, ISelection sel,
		      String project, String earProject){
    return null;	  
  }
				
  public ICommandFactory install(IEnvironment env, IContext ctx, ISelection sel,
		      String project, String earProject){
    return null;
  }
			

  public ICommandFactory run(IEnvironment env, IContext ctx, ISelection sel,
		      String project, String earProject){
    return null;	  
  }
  
}

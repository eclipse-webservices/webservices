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

package org.eclipse.jst.ws.internal.creation.ui.extension;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ui.command.StartServerCommand;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.IWebService;

public class PreServiceRunCommand extends AbstractDataModelOperation 
{
	private IWebService				webService_;
	private IContext				context_;
	
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
		  IStatus status = Status.OK_STATUS;
	      if (context_.getRun())
		  {
	            IEnvironment environment = getEnvironment();
				
				StartServerCommand command = new StartServerCommand();
				command.setServerInstanceId(webService_.getWebServiceInfo().getServerInstanceId());
	            command.setEnvironment( environment );
				status = command.execute( monitor, null );
				if (status.getSeverity()==Status.ERROR)
				{
					environment.getStatusHandler().reportError(status);			  
				}
		  }
		  
		  return status;			
	  }
		
	  public void setWebService( IWebService webService )
	  {
		  webService_ = webService;  
	  }
	  
	  public void setContext (IContext context)
	  {
		  context_ = context;
	  }

}

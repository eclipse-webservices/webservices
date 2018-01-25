/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.extension;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ui.command.StartServerCommand;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceClient;

public class PreClientRunCommand extends AbstractDataModelOperation 
{
  private IWebServiceClient webServiceClient_;
  private IContext context_;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
	  IStatus status = Status.OK_STATUS;
	 
	  if (context_ != null && context_.getRun())
	  {
	    IEnvironment environment = getEnvironment();   
	    
	    StartServerCommand command = new StartServerCommand();
	    command.setServerInstanceId(webServiceClient_.getWebServiceClientInfo().getServerInstanceId());
	    command.setEnvironment( environment );
	    status = command.execute( monitor, null );
	    if (status.getSeverity()==Status.ERROR)
	    {
	      environment.getStatusHandler().reportError( status );       
	    } 
	  }
	  
    return status;	  
  }
	  

  public void setWebService( IWebServiceClient webServiceClient )
  {
    webServiceClient_ = webServiceClient;  
  }
  
  public void setContext(IContext context)
  {
	  context_=context;
  }
}

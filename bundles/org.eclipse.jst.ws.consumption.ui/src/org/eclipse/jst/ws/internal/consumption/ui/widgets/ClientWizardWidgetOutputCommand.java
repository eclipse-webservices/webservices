/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class ClientWizardWidgetOutputCommand extends AbstractDataModelOperation
{    
  private boolean           testService_;
  private boolean monitorService;
  private ResourceContext   resourceContext_;
  private TypeRuntimeServer clientIds_;
  private boolean installClient_;
  
  public boolean getTestService()
  {
    return testService_;
  }

  public void setTestService( boolean testService )
  {
    testService_ = testService;  
  }
  
  public void setInstallClient( boolean installClient)
  {
    installClient_ = installClient;  
  }
  
  public boolean getInstallClient()
  {
	return installClient_;  
  }
  
  public boolean getMonitorService()
  {
    return monitorService;
  }
  
  public void setMonitorService(boolean monitorService)
  {
    this.monitorService = monitorService;
  }
  
  public ResourceContext getResourceContext()
  {
    
    return resourceContext_;
  }
  
  public void setResourceContext( ResourceContext context )
  {
    resourceContext_ = context;  
  }
  
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {    
    return clientIds_;  
  }
  
  public void setClientTypeRuntimeServer( TypeRuntimeServer clientIds )
  {
    clientIds_ = clientIds;
  }

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
    return Status.OK_STATUS;
  }
}
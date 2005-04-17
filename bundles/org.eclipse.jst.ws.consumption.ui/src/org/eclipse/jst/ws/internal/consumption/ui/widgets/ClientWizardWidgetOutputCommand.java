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

import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceContext;


public class ClientWizardWidgetOutputCommand extends SimpleCommand
{    
  private boolean           testService_;
  private boolean monitorService;
  private ResourceContext   resourceContext_;
  private TypeRuntimeServer clientIds_;
  
  public boolean getTestService()
  {
    return testService_;
  }

  public void setTestService( boolean testService )
  {
    testService_ = testService;  
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
}
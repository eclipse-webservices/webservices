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
package org.eclipse.jst.ws.internal.creation.ui.widgets;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ClientWizardWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeTypeRegistry;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;


public class ServerWizardWidgetDefaultingCommand extends ClientWizardWidgetDefaultingCommand
{    
  private TypeRuntimeServer typeRuntimeServer_;
  private IStructuredSelection initialSelection_;
  
  public Status execute(Environment env)
  {
    WebServiceServerRuntimeTypeRegistry registry = WebServiceServerRuntimeTypeRegistry.getInstance();
    String                       type     = getScenarioContext().getWebServiceType();                       
    String                       runtime  = registry.getDefaultRuntimeValueFor( type );
    String                       server   = registry.getDefaultServerValueFor( type );
    
    typeRuntimeServer_ = new TypeRuntimeServer();
    
    typeRuntimeServer_.setTypeId( type );
    typeRuntimeServer_.setRuntimeId( runtime );
    typeRuntimeServer_.setServerId( server );
    
    //Default the typeId from the initial selection
    String[] typeIds = registry.getWebServiceTypeBySelection(initialSelection_);
    if (typeIds!=null && typeIds.length>0)
    {
      typeRuntimeServer_.setTypeId(typeIds[0]);
    }
    
    return new SimpleStatus("");
  }
  
  public void setInitialSelection(IStructuredSelection selection)
  {
    initialSelection_ = selection;
  }
  
  public TypeRuntimeServer getServiceTypeRuntimeServer()
  { 
    return typeRuntimeServer_;
  }

  public Boolean getStartService()
  {
    return new Boolean( getScenarioContext().getStartWebService() );  
  }
  
  public Boolean getPublishService()
  {
    return new Boolean( getScenarioContext().getLaunchWebServiceExplorer() );
  }

  public Boolean getGenerateProxy()
  {
    return new Boolean( getScenarioContext().getGenerateProxy() );  
  }
  
  public Boolean getMonitorService()
  {
    return new Boolean(getScenarioContext().getMonitorWebService());
  }
}
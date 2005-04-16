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

import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.context.ResourceContext;


public class ClientWizardWidgetDefaultingCommand extends SimpleCommand
{    
  public Boolean getTestService()
  {
    return new Boolean( getScenarioContext().getTestWebService() );
  }
  
  public Boolean getMonitorService()
  {
    return new Boolean( getScenarioContext().getMonitorWebService());
  }

  public ResourceContext getResourceContext()
  { 
    return WebServicePlugin.getInstance().getResourceContext();
  }
  
  // Current ScenarioContext is used to default the first page of the wizard.  The 
  // properties in ScenarioContext are mapped individual.  Therefore, ScenarioContext
  // should not also be mapped.
  protected ScenarioContext getScenarioContext()
  {
    return WebServicePlugin.getInstance().getScenarioContext();
  }
  
  // TODO Set client name defaults here.  
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {
    // rskreg
		//WebServiceClientTypeRegistry registry = WebServiceClientTypeRegistry.getInstance();
    String                       type     = getScenarioContext().getClientWebServiceType();
    //String                       runtime  = registry.getAllClientRuntimes()[0];
		String                       runtime  = WebServiceRuntimeExtensionUtils.getAllClientRuntimes()[0];
    //String                       server   = registry.getAllClientServerFactoryIds()[0];
		String                       server   = WebServiceRuntimeExtensionUtils.getAllClientServerFactoryIds()[0];
    TypeRuntimeServer            result   = new TypeRuntimeServer();
		// rskreg
    
    result.setTypeId( type );
    result.setRuntimeId( runtime );
    result.setServerId( server ); 
    
    return result;  
  }
}
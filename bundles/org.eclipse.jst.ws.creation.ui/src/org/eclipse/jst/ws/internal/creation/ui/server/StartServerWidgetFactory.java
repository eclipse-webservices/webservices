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

package org.eclipse.jst.ws.internal.creation.ui.server;

import org.eclipse.jst.ws.internal.creation.ui.widgets.ServerWizardWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.provisional.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.provisional.env.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

public class StartServerWidgetFactory implements INamedWidgetContributorFactory 
{
  private TypeRuntimeServer serverInfo_;
  
  public INamedWidgetContributor getFirstNamedWidget() 
  {
    IServer server = ServerCore.findServer(serverInfo_.getServerInstanceId() );
    
    if( server != null && server.getServerState() != IServer.STATE_STARTED )
    {
      return new StartServerWidgetContributor( server );
    }
    
	return null;
  }

  public INamedWidgetContributor getNextNamedWidget( INamedWidgetContributor widgetContributor) 
  {
	return null;
  }

  public void registerDataMappings(DataMappingRegistry dataRegistry) 
  {
    dataRegistry.addMapping( ServerWizardWidgetDefaultingCommand.class, "ServiceTypeRuntimeServer", StartServerWidgetFactory.class);
  }
  
  public void setServiceTypeRuntimeServer( TypeRuntimeServer serverInfo )
  {
    serverInfo_ = serverInfo;  
  }
}

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

package org.eclipse.jst.ws.internal.consumption.ui.server;

import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ClientExtensionDefaultingCommand;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.provisional.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.provisional.env.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

public class StartClientWidgetFactory implements INamedWidgetContributorFactory 
{
  private TypeRuntimeServer serverInfo_;
  
  public INamedWidgetContributor getFirstNamedWidget() 
  {
    IServer server = null;
    
    if( serverInfo_ != null )
    {
      server = ServerCore.findServer(serverInfo_.getServerInstanceId() );
    }
    
    if( server != null && server.getServerState() != IServer.STATE_STARTED )
    {
      return new StartClientWidgetContributor( server );
    }
    
	return null;
  }

  public INamedWidgetContributor getNextNamedWidget( INamedWidgetContributor widgetContributor) 
  {
	return null;
  }

  public void registerDataMappings(DataMappingRegistry dataRegistry) 
  {
    dataRegistry.addMapping( ClientExtensionDefaultingCommand.class, "ClientTypeRuntimeServer", StartClientWidgetFactory.class);
  }
  
  public void setClientTypeRuntimeServer( TypeRuntimeServer serverInfo )
  {
    serverInfo_ = serverInfo;  
  }
}

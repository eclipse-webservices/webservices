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

import org.eclipse.jst.ws.internal.creation.ui.extension.PreServiceDevelopCommand;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.wsrt.IWebService;

public class StartServerWidgetFactory implements INamedWidgetContributorFactory 
{
  private IWebService webservice_;
  
  public INamedWidgetContributor getFirstNamedWidget() 
  {
    IServer server = null;
    
    if( webservice_ != null )
    {
      server = ServerCore.findServer(webservice_.getWebServiceInfo().getServerInstanceId() );
    }
    
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
	dataRegistry.addMapping( PreServiceDevelopCommand.class, "WebService", StartServerWidgetFactory.class );
  }
  
  public void setWebService( IWebService webservice )
  {
    webservice_ = webservice;  
  }
}

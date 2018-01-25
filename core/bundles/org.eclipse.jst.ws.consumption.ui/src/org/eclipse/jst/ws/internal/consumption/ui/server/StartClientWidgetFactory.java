/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20080722   240231 gilberta@ca.ibm.com - Gilbert Andrews
 * 
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.server;

import org.eclipse.jst.ws.internal.consumption.ui.extension.PreClientDevelopCommand;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.INamedWidgetContributorFactory;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceClient;

public class StartClientWidgetFactory implements INamedWidgetContributorFactory 
{
  private IWebServiceClient webserviceClient_;
  private IContext context_;
  
  public INamedWidgetContributor getFirstNamedWidget() 
  {
    IServer server = null;
    
    if( webserviceClient_ != null && webserviceClient_.getWebServiceClientInfo().getServerInstanceId() != null  && context_.getRun())
    {
      server = ServerCore.findServer(webserviceClient_.getWebServiceClientInfo().getServerInstanceId() );
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
	dataRegistry.addMapping( PreClientDevelopCommand.class, "WebService", StartClientWidgetFactory.class );
	dataRegistry.addMapping( PreClientDevelopCommand.class, "Context", StartClientWidgetFactory.class );
  }
  
  public void setWebService( IWebServiceClient webserviceClient )
  {
    webserviceClient_ = webserviceClient;  
  }
  
  public void setContext ( IContext context )
  {
    context_ = context;  
  }
}

/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
package org.eclipse.wst.command.internal.env.eclipse;

import org.eclipse.wst.command.internal.env.core.CommandManager;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.EnvironmentService;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.environment.uri.SimpleURIFactory;
import org.eclipse.wst.common.environment.uri.IURIFactory;
import org.eclipse.wst.common.environment.uri.IURIScheme;


/**
 * This class implements an IEnvironment class for the Eclipse IEnvironment.
 * This IEnvironment currently supports the "platform" protocol and the "file"
 * protocol.
 *
 */
public class EclipseEnvironment implements BaseEclipseEnvironment
{
  private CommandManager   commandManager_  = null;
  private SimpleURIFactory uriFactory_      = null;
  private ResourceContext  resourceContext_ = null;
  private IStatusHandler    statusHandler_   = null;
  private ILog              logger_          = null;
  
  public EclipseEnvironment( CommandManager  commandManager, 
 		                         ResourceContext resourceContext,
						                 IStatusHandler   statusHandler )
  {
    IURIScheme eclipseScheme = EnvironmentService.getEclipseScheme();
    IURIScheme fileScheme    = EnvironmentService.getFileScheme();
    
    commandManager_  = commandManager;
    resourceContext_ = resourceContext;
    uriFactory_      = new SimpleURIFactory();
    statusHandler_   = statusHandler;
    
    uriFactory_.registerScheme( "platform", eclipseScheme );
    uriFactory_.registerScheme( "file", fileScheme );
  }
  
  /**
   * @see org.eclipse.wst.command.internal.env.core.common.IEnvironment#getCommandManager()
   */
  public CommandManager getCommandManager()
  {
    return commandManager_;
  }

  /**
   * @see org.eclipse.wst.command.internal.env.core.common.IEnvironment#getLog()
   */
  public ILog getLog()
  {
	  if( logger_ == null )
    {  
      logger_ = EnvironmentService.getEclipseLog(); 
    };
	
    return logger_;
  }

  /**
   * 
   * @param logger the new logger for this environment.
   */
  public void setLog( ILog logger )
  {
	logger_ = logger;  
  }
   
  /**
   * @see org.eclipse.wst.command.internal.env.core.common.IEnvironment#getStatusHandler()
   */
  public IStatusHandler getStatusHandler()
  {
    return statusHandler_;
  }

  /** (non-Javadoc)
   * @see org.eclipse.wst.command.internal.env.core.common.IEnvironment#getURIFactory()
   */
  public IURIFactory getURIFactory()
  {
    return uriFactory_;
  }

  /**
   * @return returns a ResourceContext for this IEnvironment.
   */
  public ResourceContext getResourceContext()
  {
    return resourceContext_;
  }
  
  public void setCommandManager( CommandManager manager )
  {
	commandManager_ = manager;  
  }
}

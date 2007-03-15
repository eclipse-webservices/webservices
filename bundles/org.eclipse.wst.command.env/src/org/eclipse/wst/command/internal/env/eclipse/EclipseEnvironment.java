/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070314   176886 pmoogk@ca.ibm.com - Peter Moogk
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
  private CommandManager        commandManager_  = null;
  private SimpleURIFactory      uriFactory_      = null;
  private ResourceContext       resourceContext_ = null;
  private IEclipseStatusHandler statusHandler_   = null;
  private ILog                  logger_          = null;
  
  public EclipseEnvironment( CommandManager        commandManager, 
 		                     ResourceContext       resourceContext,
						     IEclipseStatusHandler statusHandler )
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

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
package org.eclipse.wst.command.internal.env.ui.eclipse;

import org.eclipse.wst.command.env.core.CommandManager;
import org.eclipse.wst.command.env.core.common.JavaCompiler;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.ProgressMonitor;
import org.eclipse.wst.command.env.core.common.StatusHandler;
import org.eclipse.wst.command.env.core.context.ResourceContext;
import org.eclipse.wst.command.env.core.uri.SimpleURIFactory;
import org.eclipse.wst.command.env.core.uri.URIFactory;
import org.eclipse.wst.command.env.core.uri.file.FileScheme;
import org.eclipse.wst.command.internal.env.eclipse.BaseEclipseEnvironment;
import org.eclipse.wst.command.internal.env.eclipse.EclipseLog;
import org.eclipse.wst.command.internal.env.eclipse.EclipseScheme;


/**
 * This class implements an Environment class for the Eclipse Environment.
 * This Environment currently supports the "platform" protocol and the "file"
 * protocol.
 *
 */
public class EclipseEnvironment implements BaseEclipseEnvironment
{
  private CommandManager   commandManager_  = null;
  private SimpleURIFactory uriFactory_      = null;
  private ResourceContext  resourceContext_ = null;
  private ProgressMonitor  monitor_         = null;
  private StatusHandler    statusHandler_   = null;
  
  public EclipseEnvironment( CommandManager  commandManager, 
		                     ResourceContext resourceContext,
					         ProgressMonitor monitor,
						     StatusHandler   statusHandler )
  {
    commandManager_  = commandManager;
    resourceContext_ = resourceContext;
    uriFactory_      = new SimpleURIFactory();
    monitor_         = monitor;
    statusHandler_   = statusHandler;
    
    uriFactory_.registerScheme( "platform", new EclipseScheme( this ) );
    uriFactory_.registerScheme( "file", new FileScheme() );
  }
  
  /**
   * @see org.eclipse.wst.command.env.core.common.Environment#getCommandManager()
   */
  public CommandManager getCommandManager()
  {
    return commandManager_;
  }

  /**
   * @see org.eclipse.wst.command.env.core.common.Environment#getJavaCompiler()
   */
  public JavaCompiler getJavaCompiler()
  {
    return null;
  }

  /**
   * @see org.eclipse.wst.command.env.core.common.Environment#getLog()
   */
  public Log getLog()
  {
    return new EclipseLog();
  }

  /**
   * @see org.eclipse.wst.command.env.core.common.Environment#getProgressMonitor()
   */
  public ProgressMonitor getProgressMonitor()
  {
    return monitor_;
  }

 
  /**
   * @see org.eclipse.wst.command.env.core.common.Environment#getStatusHandler()
   */
  public StatusHandler getStatusHandler()
  {
    return statusHandler_;
  }

  /** (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.Environment#getURIFactory()
   */
  public URIFactory getURIFactory()
  {
    return uriFactory_;
  }

  /**
   * @return returns a ResourceContext for this Environment.
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

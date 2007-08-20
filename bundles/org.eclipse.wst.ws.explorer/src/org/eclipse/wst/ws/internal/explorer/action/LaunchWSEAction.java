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
 * 20070820   191585 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.wst.command.internal.env.core.context.TransientResourceContext;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseStatusHandler;
import org.eclipse.wst.ws.internal.explorer.LaunchOption;
import org.eclipse.wst.ws.internal.explorer.LaunchOptions;
import org.eclipse.wst.ws.internal.explorer.WSExplorerLauncherCommand;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;


public class LaunchWSEAction implements IWorkbenchWindowActionDelegate
{
  public void run(IAction action)
  {
    EclipseStatusHandler             handler     = new EclipseStatusHandler();
    TransientResourceContext         context     = new TransientResourceContext();
    EclipseEnvironment               environment = new EclipseEnvironment( null, context, handler ); 
    WSExplorerLauncherCommand        cmd         = new WSExplorerLauncherCommand();
    
    String stateLocation = ExplorerPlugin.getInstance().getPluginStateLocation();
	String defaultFavoritesLocation = ExplorerPlugin.getInstance().getDefaultFavoritesLocation();
	LaunchOption[] launchOptions = new LaunchOption[] {
		new LaunchOption(LaunchOptions.STATE_LOCATION,stateLocation),
		new LaunchOption(LaunchOptions.DEFAULT_FAVORITES_LOCATION,defaultFavoritesLocation)
	};
    cmd.setLaunchOptions(launchOptions);
    cmd.setEnvironment( environment );
	  cmd.execute( null, null );
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
   */
  public void dispose() {  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
   */
  public void init(IWorkbenchWindow window) {  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection) {  }
}

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
package org.eclipse.jst.ws.internal.consumption.ui.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jst.ws.internal.ui.wse.LaunchWebServicesExplorerCommand;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.wst.command.env.core.context.TransientResourceContext;
import org.eclipse.wst.command.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.env.ui.eclipse.EclipseProgressMonitor;
import org.eclipse.wst.command.env.ui.eclipse.EclipseStatusHandler;


public class LaunchWSEAction implements IWorkbenchWindowActionDelegate
{
  public void run(IAction action)
  {
    EclipseStatusHandler             handler     = new EclipseStatusHandler();
    EclipseProgressMonitor           monitor     = new EclipseProgressMonitor();
    TransientResourceContext         context     = new TransientResourceContext();
    EclipseEnvironment               environment = new EclipseEnvironment( null, context, monitor, handler ); 
    LaunchWebServicesExplorerCommand cmd         = new LaunchWebServicesExplorerCommand();
    
    cmd.execute( environment );
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

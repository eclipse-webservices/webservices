/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.eclipse;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.command.internal.provisional.env.core.common.ProgressMonitor;


public class EclipseProgressMonitor implements ProgressMonitor
{
  protected IProgressMonitor monitor_;
  
  public EclipseProgressMonitor()
  {
  }
  
  /**
   * Sets an eclipse IProgressMonitor to this monitor. 
   * @param monitor
   */
  public void setMonitor( IProgressMonitor monitor )
  {
    monitor_ = monitor;  
  }
  
  /**
   * 
   * @return returns the Eclipse IProgressMonitor for this monitor.
   */
  public IProgressMonitor getMonitor()
  {
  	return monitor_;
  }
  
  /**
   * @see org.eclipse.env.common.ProgressMonitor#getChildProgressMonitor()
   */
  public ProgressMonitor getChildProgressMonitor()
  {
    return null;
  }

  /**
   * @see org.eclipse.env.common.ProgressMonitor#isCancelRequested()
   */
  public boolean isCancelRequested()
  {
    return monitor_ == null ? false : monitor_.isCanceled();
  }

  /**
   * @see org.eclipse.env.common.ProgressMonitor#report(java.lang.String)
   */
  public void report(String progress)
  {
    if( monitor_ != null ) 
    {
      Display currentThreadDisplay = Display.findDisplay( Thread.currentThread() );
      
      if( currentThreadDisplay != null )
      {
        monitor_.beginTask( progress, IProgressMonitor.UNKNOWN );
      }
    }
  }
}

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
package org.eclipse.jst.ws.internal.axis.consumption.ui.env;

import org.eclipse.wst.command.env.core.common.ProgressMonitor;

/**
 * @author rsinha
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class J2EEProgressMonitor implements ProgressMonitor
{
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.ProgressMonitor#report(java.lang.String)
   */
  public void report(String progress)
  {
    // TODO Auto-generated method stub
  }
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.ProgressMonitor#getChildProgressMonitor()
   */
  public ProgressMonitor getChildProgressMonitor()
  {
    // TODO Auto-generated method stub
    return null;
  }
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.ProgressMonitor#isCancelRequested()
   */
  public boolean isCancelRequested()
  {
    // TODO Auto-generated method stub
    return false;
  }
}

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

import org.eclipse.wst.command.env.core.CommandManager;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.JavaCompiler;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.ProgressMonitor;
import org.eclipse.wst.command.env.core.common.StatusHandler;
import org.eclipse.wst.command.env.core.uri.SimpleURIFactory;
import org.eclipse.wst.command.env.core.uri.URIFactory;


/**
*
*/
public class J2EEEnvironment implements Environment
{
  J2EELog j2eeLog_;
  J2EEProgressMonitor j2eeProgressMonitor_;
  J2EEStatusHandler j2eeStatusHandler_;
  SimpleURIFactory simpleURIFactory_;
  J2EECommandManager j2eeCommandManager_;
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.Environment#getLog()
   */
  public Log getLog()
  {
    if (j2eeLog_ == null)
    {
      j2eeLog_ = new J2EELog();
    }
    return j2eeLog_;
    
  }
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.Environment#getProgressMonitor()
   */
  public ProgressMonitor getProgressMonitor()
  {
    if (j2eeProgressMonitor_==null)
    {
      j2eeProgressMonitor_ = new J2EEProgressMonitor();
      
    }
    return j2eeProgressMonitor_;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.Environment#getStatusHandler()
   */
  public StatusHandler getStatusHandler()
  {
    if (j2eeStatusHandler_==null)
    {
      j2eeStatusHandler_ = new J2EEStatusHandler();
    }
    return j2eeStatusHandler_;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.Environment#getURIFactory()
   */
  public URIFactory getURIFactory()
  {
    if (simpleURIFactory_ == null)
    { 
      simpleURIFactory_ = new SimpleURIFactory();
    }
    return simpleURIFactory_;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.Environment#getJavaCompiler()
   */
  public JavaCompiler getJavaCompiler()
  {
    // TODO Auto-generated method stub
    return null;
  }
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.common.Environment#getCommandManager()
   */
  public CommandManager getCommandManager()
  {
    if(j2eeCommandManager_ == null)
    {
      j2eeCommandManager_ = new J2EECommandManager(); 
    }
    return j2eeCommandManager_;
  }
}

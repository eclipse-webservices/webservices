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
package org.eclipse.jst.ws.internal.common;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.ProgressMonitor;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.context.ResourceContext;
import org.eclipse.wst.command.env.core.context.TransientResourceContext;
import org.eclipse.wst.command.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.env.ui.eclipse.EclipseProgressMonitor;



/**
 * 
 */
public final class EnvironmentUtils
{
  /**
   * @param env
   * @return IProgressMonitor
   */
  public static IProgressMonitor getIProgressMonitor(Environment env)
  {
  	 ProgressMonitor monitor = env.getProgressMonitor();
     	 IProgressMonitor eclipseMonitor = null;
     	 if (monitor instanceof EclipseProgressMonitor)
     	 {
     	 	eclipseMonitor = ((EclipseProgressMonitor)monitor).getMonitor();
     	 }
     	 return eclipseMonitor;
  }
  
  
   
  /**
   * 
   * @param env This should be an EclipseEnvironment.
   * @return Returns a ResourceContext.
   */ 
  public static ResourceContext getResourceContext( Environment env )
  {
    ResourceContext context = null;
    
    if( env instanceof EclipseEnvironment )
    {
      context = ((EclipseEnvironment)env).getResourceContext();
    }
    else
    {
      context = new TransientResourceContext();
    }
    
    return context;
  }
  
  /**
   * @param istatus
   * @return Status
   */

  /**
   * TODO: Delete. There's a replacement in the env plugin. 
   */
  public static Status convertIStatusToStatus(IStatus istatus)
  {
    Status status;
    String message = istatus.getMessage();
    IStatus[] children = istatus.getChildren();
    int noOfChildren = children.length;
    if (noOfChildren > 0)
    {
      Status[] statusChildren = new Status[noOfChildren];
      for (int i=0;i<noOfChildren;i++)
      {
        statusChildren[i] = convertIStatusToStatus(children[i]);
      }

      status = new SimpleStatus("",message,statusChildren);
    }
    else
    {
      int severity = istatus.getSeverity();
      int statusSeverity = Status.OK;
      switch (severity)
      {
        case IStatus.ERROR:
          statusSeverity = Status.ERROR;
        	break;
        case IStatus.WARNING:
          statusSeverity = Status.WARNING;
        	break;
        case IStatus.INFO	:
          statusSeverity = Status.INFO;
        	break;      	
        case IStatus.OK:
          statusSeverity = Status.OK;
        	break;      	
        default:
      }
      Throwable e = istatus.getException();
      status = new SimpleStatus("",message,statusSeverity,e);
    }
      
    return status;
  }

  /**
   * TODO: Delete. There's a replacement in the env plugin. 
   */
  public static IStatus convertStatusToIStatus(Status status, String pluginId)
  {
    IStatus istatus;
    String message = status.getMessage();
    Throwable throwable = status.getThrowable();
    if (status.hasChildren())
    {
      Status[] children = status.getChildren();
      int noOfChildren = children.length;
      IStatus[] istatusChildren = new IStatus[noOfChildren];
      for (int i=0;i<noOfChildren;i++)
      {
        istatusChildren[i] = convertStatusToIStatus(children[i], pluginId);
      }

      istatus = new MultiStatus(pluginId,0,istatusChildren,message,throwable);
    }
    else
    {
      int severity = status.getSeverity();
      int istatusSeverity = IStatus.OK;
      switch (severity)
      {
        case Status.ERROR:
          istatusSeverity = IStatus.ERROR;
        	break;
        case Status.WARNING:
          istatusSeverity = IStatus.WARNING;
        	break;
        case Status.INFO:
          istatusSeverity = IStatus.INFO;
        	break;      	
        case Status.OK:
          istatusSeverity = IStatus.OK;
        	break;      	
        default:
      }      
      istatus = new org.eclipse.core.runtime.Status(istatusSeverity,pluginId,0,message,throwable);
    }
      
    return istatus;
  }  
  
}

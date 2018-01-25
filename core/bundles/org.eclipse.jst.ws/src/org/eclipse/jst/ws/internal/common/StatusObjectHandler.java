/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;


public class StatusObjectHandler
{
  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  private static final int MAX_DETAILS = 16;

  public static IStatus generateTreeStatus (IStatus status) 
  	{
    //
    // There could be lots of Throwables, CoreExceptions,
    // WrappedExceptions and IStatus objects beneath the
    // given status. Find them.
    //
    LinkedList statusList = new LinkedList();
    appendStatus(MAX_DETAILS, statusList, status, status.getSeverity());
    if (statusList.size() > 0)
    {
      IStatus newStatus = (IStatus)statusList.removeFirst();
      if (statusList.size() > 0)
      {
        IStatus[] secondaryStatusArray = new IStatus[statusList.size()];
        int n = 0;
        Iterator i = statusList.iterator();
        while (i.hasNext())
        {
          secondaryStatusArray[n++] = (IStatus)i.next();
        }
        newStatus = new MultiStatus(status.getPlugin(),status.getCode(),secondaryStatusArray,status.getMessage(),status.getException());
      }
      return newStatus;
    }
    else 
    	return null;
  }

  //
  // Appends zero or more IStatus objects to the list of IStatus objects
  // based upon an IStatus.
  //
  private static int appendStatus ( int max, List statusList, IStatus status, int severity )
  {
    if (max > 0 && status != null)
    {
      //
      // Append an IStatus object for the given status' message.
      //
      if (status.getMessage() != null)
      {
        max = append(statusList,max,severity,status.getPlugin(),status.getCode(),status.getMessage(),status.getException());
      }
      //
      // Append the IStatus' Throwable, if any.
      //
      max = appendThrowable(max,statusList,status.getException(),severity);
      //
      // If the IStatus is a multi status, append the children.
      //
      IStatus[] children = status.getChildren();
      for (int i=0; i<children.length && max>0; i++)
      {
        max = appendStatus(max,statusList,children[i],severity);
      }
    }
    return max;
  }

  //
  // Appends zero or more IStatus objects to the list of IStatus objects
  // based upon a Throwable. WrappedExceptions and CoreExceptions are
  // given special treatment.
  //
  private static int appendThrowable ( int max, List statusList, Throwable throwable, int severity )
  {
    if (max > 0 && throwable != null)
    {
      //
      // Append an IStatus object for the exception's message.
      //
      if (throwable.getLocalizedMessage() != null)
      {
        max = append(statusList,max,severity,WebServicePlugin.ID,0,throwable.getLocalizedMessage(),throwable);
      }
      //
      // If the exception is a WrappedException, append the wrapped Exception.
      //
      if (throwable instanceof WrappedException)
      {
        WrappedException wexc = (WrappedException)throwable;
        max = appendThrowable(max,statusList,wexc.exception(),severity);
      }
      //
      // If the exception is a CoreException, append the enclosed IStatus.
      //
      if (throwable instanceof CoreException)
      {
        CoreException cexc = (CoreException)throwable;
        max = appendStatus(max,statusList,cexc.getStatus(),severity);
      }
    }
    return max;
  }

  //
  // Creates and appends an IStatus to the list of IStatus objects.
  //
  private static int append ( List statusList, int max, int severity, String id, int code, String message, Throwable throwable )
  {
    if (max > 0)
    {
      IStatus status = new Status(severity,id,code,message,throwable);
      statusList.add(status);
      max--;
    }
    return max;
  }
}

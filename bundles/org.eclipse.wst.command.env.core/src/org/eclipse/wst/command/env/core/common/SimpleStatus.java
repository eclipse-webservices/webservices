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
package org.eclipse.wst.command.env.core.common;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * A usually sufficient implementation of Status.
 */
public class SimpleStatus implements Status
{
  
  private List children_ = new LinkedList();
  private String id_;
  private String message_;
  private int severity_;
  private Throwable throwable_;

  /**
   * Method SimpleStatus.
   * @param id
   */
  public SimpleStatus ( String id )
  {
    this(id,"",OK,null);
  }

  /**
   * Method SimpleStatus.
   * @param id
   * @param message
   * @param severity
   */
  public SimpleStatus ( String id, String message, int severity )
  {
    this(id,message,severity,null);
  }

  /**
   * Method SimpleStatus.
   * @param id
   * @param message
   * @param children
   */
  public SimpleStatus ( String id, String message, Status[] children )
  {
    this(id,message,OK,null);
    for (int i=0; i<children.length; i++)
    {
      children_.add(children[i]);
    }
  }

  /**
   * Method SimpleStatus.
   * @param id
   * @param message
   * @param severity
   * @param throwable
   */
  public SimpleStatus ( String id, String message, int severity, Throwable throwable )
  {
    id_ = id;
    message_ = message;
    severity_ = severity;
    throwable_ = throwable;
  }

  /**
   * @see Status#getId()
   */
  public String getId ()
  {
    return id_;
  }

  /**
   * @see Status#getMessage()
   */
  public String getMessage ()
  {
    return message_;
  }

  /**
   * @see Status#getSeverity()
   */
  public int getSeverity ()
  {
    int severity = severity_;
    if (children_.size() > 0)
    {
      Iterator i = children_.iterator();
      while (i.hasNext())
      {
        Status status = (Status)i.next();
        int childSeverity = status.getSeverity();
        if (childSeverity > severity)
        {
          severity = childSeverity;
        }
      }
    }
    return severity;
  }

  /**
   * @see Status#getThrowable()
   */
  public Throwable getThrowable ()
  {
    return throwable_;
  }

  /**
   * @see Status#hasChildren()
   */
  public boolean hasChildren ()
  {
    return children_.size() > 0;
  }

  /**
   * @see Status#getChildren()
   */
  public Status[] getChildren ()
  {
    return (Status[])children_.toArray(new Status[0]);
  }

  /**
   * @see Status#matches(int)
   */
  public boolean matches ( int mask )
  {
    return (getSeverity() | mask) != 0;
  }

  /**
   * Method addChild.
   * @param status
   */
  public void addChild ( Status status )
  {
    children_.add(status);
  }
}

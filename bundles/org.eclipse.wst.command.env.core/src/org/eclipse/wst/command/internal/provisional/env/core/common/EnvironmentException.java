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
package org.eclipse.wst.command.internal.provisional.env.core.common;

import org.eclipse.core.runtime.IStatus;

/**
 * This is the exception class for conditions raised by the Environment.
 */
public class EnvironmentException extends Exception
{
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3978983275899402036L;
  
  protected IStatus status = null;
  
  /**
   * Creates a new EnvironmentException.
   */
  public EnvironmentException ()
  {
    super();
  }

  /**
   * Creates a new EnvironmentException.
   */
  public EnvironmentException ( IStatus status )
  {
    super(status == null ? null : status.getMessage());
    this.status = status;
  }

  /**
   * Returns the Status object.
   */
  public IStatus getStatus()
  {
    return status;
  }
}

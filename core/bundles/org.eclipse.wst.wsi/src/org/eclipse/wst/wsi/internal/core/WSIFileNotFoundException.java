/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core;

/**
 * Signals that an attempt to open the file denoted by a specified 
 * pathname has failed.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class WSIFileNotFoundException extends WSIException
{

  /**
   * Static needed for serializable class
   */
  private static final long serialVersionUID = -1916010444601666401L;

/**
   * Constructor for WSIFileNotFoundException.
   */
  public WSIFileNotFoundException()
  {
    super();
  }

  /**
   * Constructor for WSIFileNotFoundException.
   * @param msg  the detail message.
  
   */
  public WSIFileNotFoundException(String msg)
  {
    super(msg);
  }

  /**
   * Constructor for WSIFileNotFoundException.
   * @param msg        the detail message.
   * @param throwable  initial exception thrown.
   */
  public WSIFileNotFoundException(String msg, Throwable throwable)
  {
    super(msg, throwable);
  }

}

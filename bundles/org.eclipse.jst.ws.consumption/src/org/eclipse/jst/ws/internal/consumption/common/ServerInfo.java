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
/*
 * Created on Mar 25, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.jst.ws.internal.consumption.common;

/**
 *
 */
public class ServerInfo
{
  String serverFactoryId;
  String serverInstanceId;
  /**
   * @return Returns the serverFactoryId.
   */
  public String getServerFactoryId()
  {
    return serverFactoryId;
  }
  /**
   * @param serverFactoryId The serverFactoryId to set.
   */
  public void setServerFactoryId(String serverFactoryId)
  {
    this.serverFactoryId = serverFactoryId;
  }
  /**
   * @return Returns the serverInstanceId.
   */
  public String getServerInstanceId()
  {
    return serverInstanceId;
  }
  /**
   * @param serverInstanceId The serverInstanceId to set.
   */
  public void setServerInstanceId(String serverInstanceId)
  {
    this.serverInstanceId = serverInstanceId;
  }
}

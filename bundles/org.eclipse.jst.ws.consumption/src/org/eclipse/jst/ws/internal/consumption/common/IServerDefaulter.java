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

package org.eclipse.jst.ws.internal.consumption.common;

import org.eclipse.core.resources.IProject;


/**
 * Implement this interface to provide logic that can be plugged into
 * the server defaulting algorithm. 
 * (See the org.eclipse.jst.ws.consumption.serverDefaulter extension point)  
 */
public interface IServerDefaulter
{
  
  /**
   * @param project
   * @return ServerInfo. 
   * If no existing server or server type is recommended, return null.
   * If a server type is recommended, return an instance of ServerInfo with a non-null
   * non-empty String for the serverFactoryId and null for the serverInstanceId.
   * If a server instance is recommended, return an instance of ServerInfo with a non-null
   * non-empty String for the serverFactoryId and the serverInstanceId.
   */
  public ServerInfo recommendDefaultServer(IProject project);
  
}

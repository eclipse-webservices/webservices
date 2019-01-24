/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ext.test;

import java.util.List;
import org.eclipse.wst.server.core.IServer;

public interface WebServiceTestFinishCommand 
{

  /**
   * If the command needs a server this is the chosen 
   * client serverID
   * @param sampleServerTypeID
   */
  public void setServerTypeID(String serviceServerTypeID);
  
  /**
   * This is the IServer if required
   * @param sampleExistingServer
   */
  public void setExistingServer(IServer serviceExistingServer);
  
  /**
   * This is the endpoints if monitor service is enabled
   * @param endpoints
   */
  public void setEndpoint(List endpoints);
  
}

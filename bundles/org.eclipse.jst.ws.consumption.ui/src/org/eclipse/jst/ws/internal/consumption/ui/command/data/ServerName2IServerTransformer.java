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
package org.eclipse.jst.ws.internal.consumption.ui.command.data;

import org.eclipse.wst.command.internal.provisional.env.core.data.Transformer;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

public class ServerName2IServerTransformer implements Transformer
{
  public Object transform(Object value)
  {
    {
      String serverName = value.toString();
      IServer[] servers = ServerCore.getServers();
      if (servers != null && servers.length!=0)
      {
        for (int i = 0; i < servers.length; i++)
        {
          IServer server = (IServer)servers[i];
          if ((server.getName()).equals(serverName))
            return server;
        }
      }
    }
    return null;
  }
}

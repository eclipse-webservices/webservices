/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

public class CheckProjectOKCommand extends SimpleCommand {

  private String serviceServerTypeId_;

  private MessageUtils msgUtils_;

  /**
   * Default CTOR
   */
  public CheckProjectOKCommand() {
    String pluginId = "org.eclipse.jst.ws.consumption";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);

  }

  public Status execute(Environment env) {
    Status status = new SimpleStatus("");
    
    String typeid = serviceServerTypeId_;
    IServer[] servers = ServerCore.getServers();
//    Iterator iterator = servers.iterator();
    boolean viewOpen = false;
    String serverName = null;

    for (int i=0;i<servers.length;i++) {
      IServer server = (IServer) servers[i];

      // If the configuration editor is open for the server that we are
      // deploying
      // to we will prompt the user to close it.
      if (server != null && server.getServerType().getId().equals(typeid)) {
        viewOpen = true;
        serverName = server.getName();
        break;
      }
    }

    if (viewOpen) {
      String errorMessage = msgUtils_.getMessage("MSG_ERROR_SERVER_VIEW_OPEN", new String[] { serverName});
      return new SimpleStatus("", errorMessage, Status.ERROR, null);
      
    }
    
    return status;
  }

  /**
   * @param serviceServerId
   *            The serviceServer factory Id to set.
   */
  public void setServiceServerTypeID(String serviceServerId) {
    this.serviceServerTypeId_ = serviceServerId;
  }
}

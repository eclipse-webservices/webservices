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

package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

public class CheckProjectOKCommand extends AbstractDataModelOperation 
{

  private String serviceServerTypeId_;


  /**
   * Default CTOR
   */
  public CheckProjectOKCommand() {
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  { 
    IStatus status = Status.OK_STATUS;
    
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
      String errorMessage = NLS.bind(ConsumptionMessages.MSG_ERROR_SERVER_VIEW_OPEN, new String[] { serverName});
      return StatusUtils.errorStatus( errorMessage );
      
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

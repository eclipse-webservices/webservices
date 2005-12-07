/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.creation.ui.command;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

public class PublishServerCommand extends AbstractDataModelOperation
{
  private WebServiceInfo wsInfo;
  
  public PublishServerCommand(WebServiceInfo wsInfo)
  {
    this.wsInfo = wsInfo;
  }

  public IStatus execute(IProgressMonitor monitor, IAdaptable adaptable) 
  {
    if (wsInfo != null)
    {
      String serverInstanceId = wsInfo.getServerInstanceId();
      if (serverInstanceId != null)
      {
        IServer server = ServerCore.findServer(serverInstanceId);
        if (server != null)
        {
          return server.publish(IServer.PUBLISH_INCREMENTAL, monitor);
        }
      }
    }
    return Status.OK_STATUS;
  }
}

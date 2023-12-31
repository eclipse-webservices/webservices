/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerPort;
import org.eclipse.wst.server.core.internal.IMonitoredServerPort;
import org.eclipse.wst.server.core.internal.IServerMonitorManager;
import org.eclipse.wst.server.core.internal.ServerMonitorManager;

public class CreateMonitorCommand extends AbstractDataModelOperation
{

  private static final String WEB_SERVICES = "Web services";

  private Boolean monitorService;

  private String serviceServerInstanceId;

  private Integer monitoredPort;

  public CreateMonitorCommand() {
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
    
    if (monitorService != null && monitorService.booleanValue()) {
      IServer server = ServerCore.findServer(serviceServerInstanceId);
      if (server != null) {

        // IMonitorableServer monitorableServer = (IMonitorableServer)
        // serverDelegate;
        ServerPort[] ports = server.getServerPorts(null);
        ServerPort port = null;
        for (int it = 0; it < ports.length; it++) {
          ServerPort p = ports[it];
          String protocol = p.getProtocol();
          if (protocol != null && protocol.trim().toLowerCase().equals("http")) {
            port = p;
            break;
          }
        }
        if (port != null) {
          IServerMonitorManager serverMonitorManager = ServerMonitorManager.getInstance();
          IMonitoredServerPort[] monitoredPorts = serverMonitorManager.getMonitoredPorts(server);
          for (int it2 = 0; it2 < monitoredPorts.length; it2++) {
            IMonitoredServerPort imsPort = monitoredPorts[it2];
            if (port.getPort() == imsPort.getServerPort().getPort() && hasContentWebServices(imsPort)) // port
            // already
            // being
            // monitored
            {
              if (!imsPort.isStarted()) {
                try {
                  serverMonitorManager.startMonitor(imsPort);
                }
                catch (CoreException ce) {
                  IStatus error = StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_UNABLE_TO_START_MONITOR,
                                                           new Object[] { String.valueOf(port.getPort()), server.getName()}), ce);
                  env.getStatusHandler().reportError(error);
                  return error;
                }
              }
              monitoredPort = new Integer(imsPort.getMonitorPort());
              return Status.OK_STATUS;
            }
          }
          try {
            IMonitoredServerPort imsPort = serverMonitorManager.createMonitor(server, port, -1, new String[] { WEB_SERVICES});
            serverMonitorManager.startMonitor(imsPort);
            monitoredPort = new Integer(imsPort.getMonitorPort());
            return Status.OK_STATUS;
          }
          catch (CoreException ce) {
            IStatus error = StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_UNABLE_TO_START_MONITOR, new Object[] {
                                                     String.valueOf(port.getPort()), server.getName()}), ce);
            env.getStatusHandler().reportError(error);
            return error;
          }
        }

        else {
          IStatus info = StatusUtils.infoStatus( NLS.bind(ConsumptionMessages.MSG_INFO_MONITORING_NOT_SUPPORTED,
                                                 new Object[] { server.getName()}) );
          env.getStatusHandler().reportInfo(info);
          return info;
        }
      }
    }
    return Status.OK_STATUS;
  }

  private boolean hasContentWebServices(IMonitoredServerPort imsPort) {
    String[] contents = imsPort.getContentTypes();
    for (int i = 0; i < contents.length; i++)
      if (WEB_SERVICES.equals(contents[i])) return true;
    return false;
  }

  public void setServiceTypeRuntimeServer(TypeRuntimeServer typeRuntimeServer) {
    this.serviceServerInstanceId = typeRuntimeServer.getServerInstanceId();
  }

  public void setServiceServerInstanceId(String serviceServerInstanceId) {
    this.serviceServerInstanceId = serviceServerInstanceId;
  }

  public Integer getMonitoredPort() {
    return monitoredPort;
  }

  public void setMonitorService(Boolean value) {
    monitorService = value;
  }
}

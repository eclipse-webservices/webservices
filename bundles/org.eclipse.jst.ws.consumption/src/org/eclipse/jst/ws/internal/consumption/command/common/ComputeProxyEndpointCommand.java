/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060728   145426 kathy@ca.ibm.com - Kathy Chan
 * 20080613   237116 makandre@ca.ibm.com - Andrew Mak, Web service monitoring fails on https endpoint
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.command.common;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class ComputeProxyEndpointCommand extends AbstractDataModelOperation
{
  private Boolean monitorService;
  private Integer monitoredPort;
  private String proxyEndpoint;
  private String endpoint;

  public ComputeProxyEndpointCommand()
  {
  }

  /**
   * @param monitoredPort The monitoredPort to set.
   */
  public void setMonitoredPort(Integer monitoredPort)
  {
    this.monitoredPort = monitoredPort;
  }
  /**
   * @param monitorService The monitorService to set.
   */
  public void setMonitorService(Boolean monitorService)
  {
    this.monitorService = monitorService;
  }
  
  public String getEndpoint()
  {
  	if (monitorService != null && monitorService.booleanValue() && monitoredPort != null)
  	{
  		// Use the endpoint that matches with the proxy the extension passes to us if it is set
  		if (proxyEndpoint != null) {
  			String location = proxyEndpoint;
  			if (location.startsWith("https://"))
            	location = "http://" + location.substring(8);
  			try
			{
  				URL url = new URL(location);
  				url = new URL(url.getProtocol(), url.getHost(), monitoredPort.intValue(), url.getFile());
  				return url.toString();
			}
  			catch (MalformedURLException murle)
			{
  				int protocolIndex = location.indexOf("://");
                if (protocolIndex != -1)
                {
                  String protocol = location.substring(0, protocolIndex+3);
                  int hostPortIndex = location.indexOf('/', protocolIndex+3);
                  String file;
                  if (hostPortIndex == -1)
                  {
                    hostPortIndex = location.length();
                    file = "";
                  }
                  else
                    file = location.substring(hostPortIndex, location.length());
                  String hostPort = location.substring(protocolIndex+3, hostPortIndex);
                  int hostIndex = hostPort.indexOf(':');
                  String host;
                  if (hostIndex != -1)
                    host = hostPort.substring(0, hostIndex+1);
                  else
                    host = hostPort + ':';
                  String newPort = String.valueOf(monitoredPort.intValue());
                  StringBuffer endpoint = new StringBuffer(protocol);
                  endpoint.append(host);
                  endpoint.append(newPort);
                  endpoint.append(file);
                  return endpoint.toString();
                }
			}
  		} else {
  			return endpoint;
  		}
  	}
  	return null;
  }

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
    return Status.OK_STATUS;
  }
  
  /**
   * @param proxyEndpoint The proxyEndpoint to set.
   */
  public void setProxyEndpoint(String proxyEndpoint) {
  	this.proxyEndpoint = proxyEndpoint;
  }
  /**
   * @param endpoint The endpoint to set.
   */
  public void setEndpoint(String endpoint) {
  	this.endpoint = endpoint;
  }
}

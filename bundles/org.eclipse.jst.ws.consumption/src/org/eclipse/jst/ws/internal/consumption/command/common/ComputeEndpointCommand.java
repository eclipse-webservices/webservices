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
package org.eclipse.jst.ws.internal.consumption.command.common;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;

import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

public class ComputeEndpointCommand extends SimpleCommand
{
  private WebServicesParser webServicesParser;
  private String wsdlURI;
  private Boolean monitorService;
  private Integer monitoredPort;

  public ComputeEndpointCommand()
  {
    super("org.eclipse.jst.ws.internal.consumption.command.common.ComputeEndpointCommand", "org.eclipse.jst.ws.internal.consumption.command.common.ComputeEndpointCommand");
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
  /**
   * @param webServicesParser The webServicesParser to set.
   */
  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    this.webServicesParser = webServicesParser;
  }
  /**
   * @param wsdlURI The wsdlURI to set.
   */
  public void setWsdlURI(String wsdlURI)
  {
    this.wsdlURI = wsdlURI;
  }
  
  public String getEndpoint()
  {
    if (monitorService != null && monitorService.booleanValue() && monitoredPort != null)
    {
      Definition def = webServicesParser.getWSDLDefinition(wsdlURI);
      if (def != null)
      {
        for (Iterator it = def.getServices().values().iterator(); it.hasNext();)
        {
          Service service = (Service)it.next();
          for (Iterator it2 = service.getPorts().values().iterator(); it2.hasNext();)
          {
            Port port = (Port)it2.next();
            for (Iterator it3 = port.getExtensibilityElements().iterator(); it3.hasNext();)
            {
              ExtensibilityElement ext = (ExtensibilityElement)it3.next();
              if (ext instanceof SOAPAddress)
              {
                String location = ((SOAPAddress)ext).getLocationURI();
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
              }
            }
          }
        }
      }
    }
    return null;
  }
}
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
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.soap.SOAPAddress;

import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.common.StatusException;
import org.eclipse.wst.internet.monitor.core.IMonitor;
import org.eclipse.wst.internet.monitor.core.IMonitorWorkingCopy;
import org.eclipse.wst.internet.monitor.core.MonitorCore;
import org.eclipse.wst.server.core.util.SocketUtil;
import org.eclipse.wst.ws.parser.wsil.WebServicesParser;

public class GetMonitorCommand extends SimpleCommand
{
  private boolean monitorService;
  private boolean create;
  private WebServicesParser webServicesParser;
  private String wsdlURI;
  private List endpoints;

  public GetMonitorCommand()
  {
    super("org.eclipse.jst.ws.internal.consumption.command.common.GetMonitorCommand", "org.eclipse.jst.ws.internal.consumption.command.common.GetMonitorCommand");
    monitorService = true;
    create = true;
  }

  public Status execute(Environment env)
  {
    endpoints = new Vector();
    if (monitorService && webServicesParser != null && wsdlURI != null && wsdlURI.length() > 0)
    {
      Definition definition = webServicesParser.getWSDLDefinition(wsdlURI);
      if (definition != null)
      {
        Map services = definition.getServices();
        for (Iterator servicesIt = services.values().iterator(); servicesIt.hasNext();)
        {
          Service service = (Service)servicesIt.next();
          Map ports = service.getPorts();
          for (Iterator portsIt = ports.values().iterator(); portsIt.hasNext();)
          {
            Port wsdlPort = (Port)portsIt.next();
            List extElements = wsdlPort.getExtensibilityElements();
            for (Iterator extElementsIt = extElements.iterator(); extElementsIt.hasNext();)
            {
              ExtensibilityElement extElement = (ExtensibilityElement)extElementsIt.next();
              String endpoint = null;
              URL endpointURL = null;
              if (extElement instanceof SOAPAddress)
                endpoint = ((SOAPAddress)extElement).getLocationURI();
              else if (extElement instanceof HTTPAddress)
                endpoint = ((HTTPAddress)extElement).getLocationURI();
              if (endpoint != null)
              {
                try
                {
                  endpointURL = new URL(endpoint);
                }
                catch (MalformedURLException murle)
                {
                }
              }
              if (endpointURL != null)
              {
                String protocol = endpointURL.getProtocol();
                String host = endpointURL.getHost();
                int port = endpointURL.getPort();
                if (port == -1)
                {
                  if ("http".equalsIgnoreCase(protocol))
                    port = 80;
                  else if ("https".equalsIgnoreCase(protocol))
                    port = 443;
                }
                if (protocol != null && protocol.startsWith("http") && host != null && host.length() > 0 && port != -1)
                {
                  IMonitor m = null;
                  IMonitor[] monitors = MonitorCore.getMonitors();
                  for (int i=0; i<monitors.length; i++)
                  {
                    if (host.equalsIgnoreCase(monitors[i].getRemoteHost()) && port == monitors[i].getRemotePort())
                    {
                      m = monitors[i];
                      break;
                    }
                  }
                  if (m == null && create)
                  {
                    int monitoredPort = SocketUtil.findUnusedPort(5000, 15000);
                    IMonitorWorkingCopy monitorWorkingCopy = MonitorCore.createMonitor();
                    monitorWorkingCopy.setLocalPort(monitoredPort);
                    monitorWorkingCopy.setRemoteHost(host);
                    monitorWorkingCopy.setRemotePort(port);
                    monitorWorkingCopy.setProtocol("HTTP");
                    try
                    {
                      m = monitorWorkingCopy.save();
                    }
                    catch (Throwable t)
                    {
                      Status warning = new SimpleStatus(WebServiceConsumptionPlugin.ID, WebServiceConsumptionPlugin.getMessage("MSG_ERROR_UNABLE_TO_START_MONITOR", new Object[]{String.valueOf(port), endpoint}), Status.WARNING, t);
                      try
                      {
                        if (env != null)
                          env.getStatusHandler().report(warning);
                      }
                      catch (StatusException se)
                      {
                      }
                    }
                  }
                  if (m != null)
                  {
                    try
                    {
                      if (!m.isRunning())
                        m.start();
                      StringBuffer sb = new StringBuffer(endpointURL.getProtocol());
                      sb.append("://localhost:");
                      sb.append(String.valueOf(m.getLocalPort()));
                      sb.append(endpointURL.getFile());
                      endpoints.add(sb.toString());
                    }
                    catch (Throwable t)
                    {
                      Status warning = new SimpleStatus(WebServiceConsumptionPlugin.ID, WebServiceConsumptionPlugin.getMessage("MSG_ERROR_UNABLE_TO_START_MONITOR", new Object[]{String.valueOf(port), endpoint}), Status.WARNING, t);
                      try
                      {
                        if (env != null)
                          env.getStatusHandler().report(warning);
                      }
                      catch (StatusException se)
                      {
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return new SimpleStatus("");
  }

  public void setMonitorService(boolean monitorService)
  {
    this.monitorService = monitorService;
  }

  public void setDefinition(Definition definition)
  {
  }

  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    this.webServicesParser = webServicesParser;
  }
  
  public void setWsdlURI(String wsdlURI)
  {
    this.wsdlURI = wsdlURI;
  }

  public void setCreate(boolean create)
  {
    this.create = create;
  }

  public List getEndpoints()
  {
    return endpoints;
  }
}
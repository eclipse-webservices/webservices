/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080709   237116 makandre@ca.ibm.com - Andrew Mak, Web service monitoring fails on https endpoint
 * 20080715   240722 makandre@ca.ibm.com - Andrew Mak, Cannot setup TCP/IP Monitor for soap12 endpoints
 *******************************************************************************/
package org.eclipse.wst.ws.internal.monitor;

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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.StatusException;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.internet.monitor.core.internal.provisional.IMonitor;
import org.eclipse.wst.internet.monitor.core.internal.provisional.IMonitorWorkingCopy;
import org.eclipse.wst.internet.monitor.core.internal.provisional.MonitorCore;
import org.eclipse.wst.server.core.util.SocketUtil;
import org.eclipse.wst.ws.internal.WstWSPluginMessages;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

public class GetMonitorCommand extends AbstractDataModelOperation {
	private boolean monitorService;

	private boolean create;

	private WebServicesParser webServicesParser;

	private String wsdlURI;

	private List endpoints;
	
	private String proxyEndpoint;

	public GetMonitorCommand() {
		monitorService = true;
		create = true;
	}

	public IStatus execute(IProgressMonitor monitor, IAdaptable adaptable) {
		IEnvironment env = getEnvironment();

		endpoints = new Vector();
		if (monitorService && webServicesParser != null && wsdlURI != null && wsdlURI.length() > 0) {
			Definition definition = webServicesParser.getWSDLDefinition(wsdlURI);
			if (definition != null) {
				Map services = definition.getServices();
				for (Iterator servicesIt = services.values().iterator(); servicesIt.hasNext();) {
					Service service = (Service) servicesIt.next();
					Map ports = service.getPorts();
					for (Iterator portsIt = ports.values().iterator(); portsIt.hasNext();) {
						Port wsdlPort = (Port) portsIt.next();
						List extElements = wsdlPort.getExtensibilityElements();
						for (Iterator extElementsIt = extElements.iterator(); extElementsIt.hasNext();) {
							ExtensibilityElement extElement = (ExtensibilityElement) extElementsIt.next();
							String endpoint = null;
							URL endpointURL = null;
							if (extElement instanceof SOAPAddress)
								endpoint = ((SOAPAddress) extElement).getLocationURI();
							else if (extElement instanceof HTTPAddress)
								endpoint = ((HTTPAddress) extElement).getLocationURI();
							else
								endpoint = ExtensibilityElementTransformerRegistry.INSTANCE.transform(extElement);
							if (endpoint != null) {
								try {
									endpointURL = new URL(endpoint);
								}
								catch (MalformedURLException murle) {
								}
							}
							if (endpointURL != null) {
								String protocol = endpointURL.getProtocol();
								String host = endpointURL.getHost();
								int port = endpointURL.getPort();
								if (port == -1) {
									if ("http".equalsIgnoreCase(protocol))
										port = 80;
									else if ("https".equalsIgnoreCase(protocol))
										port = 443;
								}
								if ("https".equalsIgnoreCase(protocol))
				                {
				                	IStatus warning = StatusUtils.warningStatus( NLS.bind(WstWSPluginMessages.MSG_ERROR_UNABLE_TO_START_MONITOR, new Object[]{String.valueOf(port), endpoint}));
				                	try
				                    {
				                      if (env != null)
				                        env.getStatusHandler().report(warning);
				                    }
				                    catch (StatusException se)
				                    {
				                    }
				                }
				                else if (protocol != null && protocol.equalsIgnoreCase("http") && host != null && host.length() > 0 && port != -1) {
									IMonitor m = null;
									IMonitor[] monitors = MonitorCore.getMonitors();
									for (int i = 0; i < monitors.length; i++) {
										if (host.equalsIgnoreCase(monitors[i].getRemoteHost()) && port == monitors[i].getRemotePort()) {
											m = monitors[i];
											break;
										}
									}
									if (m == null && create) {
										int monitoredPort = SocketUtil.findUnusedPort(5000, 15000);
										IMonitorWorkingCopy monitorWorkingCopy = MonitorCore.createMonitor();
										monitorWorkingCopy.setLocalPort(monitoredPort);
										monitorWorkingCopy.setRemoteHost(host);
										monitorWorkingCopy.setRemotePort(port);
										monitorWorkingCopy.setProtocol("HTTP");
										try {
											m = monitorWorkingCopy.save();
										}
										catch (Exception t) {
											IStatus warning = StatusUtils.warningStatus(NLS.bind(WstWSPluginMessages.MSG_ERROR_UNABLE_TO_START_MONITOR, new Object[]{String.valueOf(port), endpoint}), t);
											try {
												if (env != null)
													env.getStatusHandler().report(warning);
											}
											catch (StatusException se) {
											}
										}
									}
									if (m != null) {
										try {
											if (!m.isRunning())
												m.start();
											// Use the endpoint that matches with the proxy the extension passes to us if it is set
				                      		if (proxyEndpoint != null) {
				                      			try {
				                      				endpointURL = new URL(proxyEndpoint);
				                      			} catch (MalformedURLException murle)
				    			                { 
				                      				// ignore proxy endpoint 
				    			                }
				                      		}
											StringBuffer sb = new StringBuffer(endpointURL.getProtocol());
											sb.append("://localhost:");
											sb.append(String.valueOf(m.getLocalPort()));
											sb.append(endpointURL.getFile());
											endpoints.add(sb.toString());
										}
										catch (Exception t) {
											IStatus warning = StatusUtils.warningStatus(NLS.bind(WstWSPluginMessages.MSG_ERROR_UNABLE_TO_START_MONITOR, new Object[]{String.valueOf(port), endpoint}), t);
											try {
												if (env != null)
													env.getStatusHandler().report(warning);
											}
											catch (StatusException se) {
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
		return Status.OK_STATUS;
	}

	public void setMonitorService(boolean monitorService) {
		this.monitorService = monitorService;
	}

	public void setDefinition(Definition definition) {
	}

	public void setWebServicesParser(WebServicesParser webServicesParser) {
		this.webServicesParser = webServicesParser;
	}

	public void setWsdlURI(String wsdlURI) {
		this.wsdlURI = wsdlURI;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}

	public List getEndpoints() {
		return endpoints;
	}
	
	/**
	 * @param proxyEndpoint The proxyEndpoint to set.
	 */
	public void setProxyEndpoint(String proxyEndpoint) {
	  	this.proxyEndpoint = proxyEndpoint;
	}
}

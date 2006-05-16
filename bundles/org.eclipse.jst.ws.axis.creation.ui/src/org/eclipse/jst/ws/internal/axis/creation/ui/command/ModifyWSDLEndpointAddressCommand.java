/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060515   115225 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;

import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLWriter;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLFactoryImpl;

public class ModifyWSDLEndpointAddressCommand extends AbstractDataModelOperation {

	private String serverInstanceId;
	private String serverFactoryId;
	private IProject serviceProject;
	private String wsdlURI;
	private WebServicesParser webServicesParser;
	public final String SERVICE_EXT = "/services/"; //$NON-NLS-1$
	private JavaWSDLParameter javaWSDLParam;

	public IStatus execute(IProgressMonitor monitor, IAdaptable adaptable)  {
		IStatus status;
		IEnvironment environment = getEnvironment();
		try {
			// the correct project URL has already been obtained, no need to modify <soap:address> location URI
			if (!javaWSDLParam.isGuessProjectURL()) { 
				return Status.OK_STATUS;
			}

			String projectURL = null;		

			IServer server = null;
			if(serverInstanceId!=null) {
				server = ServerCore.findServer(serverInstanceId);
			}

			if(server!=null) {
				projectURL = ServerUtils.getEncodedWebComponentURL(serviceProject, serverFactoryId, server);
			} else {
				projectURL = ServerUtils.getEncodedWebComponentURL(serviceProject, serverFactoryId);
			}

			if(projectURL==null || projectURL.trim().length()==0) {
				status = StatusUtils.errorStatus(NLS.bind(AxisCreationUIMessages.MSG_ERROR_PROJECT_URL, new String[] {serviceProject.getName()}));
				environment.getStatusHandler().reportError(status);
				return status;
			}

			if (javaWSDLParam.getProjectURL() == projectURL) {
				return Status.OK_STATUS;
			} 

			javaWSDLParam.setProjectURL(projectURL);  // project URL needed for Axis deploy

			// modify the <soap:address> location URI to reflect the correct project URL
			if(wsdlURI!=null) {							
				boolean modified = false;							
				if (webServicesParser == null) {
					webServicesParser = new WebServicesParserExt();
				}
				Definition definition = webServicesParser.getWSDLDefinition(wsdlURI);
				Map services = definition.getServices();
				for (Iterator it = services.values().iterator(); it.hasNext();) {
					Service ser = (Service)it.next();
					Map ports = ser.getPorts();
					for (Iterator it2 = ports.values().iterator(); it2.hasNext();) {
						Port p = (Port)it2.next();
						for (Iterator it3 = p.getExtensibilityElements().iterator(); it3.hasNext();) {
							Object obj = it3.next();
							if (obj instanceof SOAPAddress) {
								SOAPAddress soapAddress = (SOAPAddress)obj;
								String wsdlSoapAddress = soapAddress.getLocationURI();

								// check if the WSDL SOAP address has the right project URL, if not, update it
								if(wsdlSoapAddress!=null) {
									if (!wsdlSoapAddress.startsWith(projectURL)) {
										// e.g. "http://tempuri.org/services/ServiceName" becomes "/services/ServiceName" 
										String servicesString = wsdlSoapAddress.substring(wsdlSoapAddress.indexOf(SERVICE_EXT));
										soapAddress.setLocationURI(projectURL + servicesString);
										modified = true;
									}

								}
							}
						}
					}
				}

				if (modified) {
					// update the WSDL file and refresh in workspace
					WSDLFactory wsdlFactory = new WSDLFactoryImpl();
					WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
					OutputStream os = environment.getURIFactory().newURI(wsdlURI.toString()).getOutputStream();
					wsdlWriter.writeWSDL(definition, os);
					os.close();

					URL wsdlURL = new URL (wsdlURI);
					IResource wsdlFile = ResourceUtils.findResourceAtLocation(wsdlURL.getPath(), serviceProject);
					if (wsdlFile != null) {
						wsdlFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
					}
				}
			} else {
				status = StatusUtils.errorStatus(NLS.bind(AxisCreationUIMessages.MSG_ERROR_MODIFY_ENDPOINT, new String[] {wsdlURI}));
				environment.getStatusHandler().reportError(status);
				return status;
			}

		} catch(Exception e) {
			status = StatusUtils.errorStatus(NLS.bind(AxisCreationUIMessages.MSG_ERROR_MODIFY_ENDPOINT, new String[] {wsdlURI}));
			environment.getStatusHandler().reportError(status);
			return status;
		}

		return Status.OK_STATUS;
	}


	public void setServerInstanceId(String serverId) {
		this.serverInstanceId = serverId;
	}

	public void setServerFactoryId(String serverFactoryId) {
		this.serverFactoryId = serverFactoryId;
	}

	public void setServiceProject(IProject serviceProject) {
		this.serviceProject = serviceProject;
	}

	public void setWsdlURI(String wsdlURI) {
		this.wsdlURI = wsdlURI;
	}

	public void setWebServicesParser(WebServicesParser parser) {
		webServicesParser = parser;
	}


	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam)
	{
		this.javaWSDLParam = javaWSDLParam;
	}

	public JavaWSDLParameter getJavaWSDLParam()
	{
		return javaWSDLParam;
	}

}

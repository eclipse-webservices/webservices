/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.command;

import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.parser.wsil.WebServicesParser;

/**
 * 
 * AxisClientDefaultingCommand
 *  
 */
public class AxisClientDefaultingCommand extends SimpleCommand 
{
    private boolean isClientScenario_  = true;
	private boolean customizeMappings_ = false;
	private boolean generateProxy_ = true;
	private String clientRuntimeId_;
	private JavaWSDLParameter javaWSDLParam_;
	private IProject proxyProject_ = null;
	private IProject clientProjectEAR_ = null;
	private String wsdlURL_;
	private boolean testProxySelected_;
	private IServer clientExistingServer_;
	private String clientServer_;
	private String clientServerTypeId_;
	private boolean clientIsExistingServer_;
	private String proxyProjectFolder_;
	private WebServicesParser webServicesParser_;
	
	
	public AxisClientDefaultingCommand() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.command.env.core.Command#execute(org.eclipse.wst.command.env.core.common.Environment)
	 */
	public Status execute(Environment environment) {
		Status status = new SimpleStatus("");
		
		clientExistingServer_ = getServerFromServerLabel();
		if (clientExistingServer_ != null) {
			clientServerTypeId_ = clientExistingServer_.getServerType().getId();
		} else {
			//TODO get the factory id for the type.
		}
		//javaWSDLParam
		javaWSDLParam_ = new JavaWSDLParameter();
		
		// proxyProjectFolber_
		
		return status;
	}

	public void setClientRuntimeID(String clientRuntimeId) {
		clientRuntimeId_ = clientRuntimeId;
	}

	public String getClientRuntimeID() {
		return this.clientRuntimeId_;
	}

	/**
	 * @return Returns the javaWSDLParam.
	 */
	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam_;
	}

	/**
	 * @return Returns the clientProject.
	 */
	public IProject getClientProject() {
		return proxyProject_;
	}

	/**
	 * @param clientProject
	 *            The clientProject to set.
	 */
	public void setClientProject(IProject clientProject) {
		this.proxyProject_ = clientProject;
	}

	/**
	 * @return Returns the clientProjectEAR.
	 */
	public IProject getClientProjectEAR() {
		return clientProjectEAR_;
	}

	/**
	 * @param clientProjectEAR
	 *            The clientProjectEAR to set.
	 */
	public void setClientProjectEAR(IProject clientProjectEAR) {
		this.clientProjectEAR_ = clientProjectEAR;
	}

	/**
	 * @return Returns the testProxySelected.
	 */
	public boolean getTestProxySelected() {
		return testProxySelected_;
	}

	/**
	 * @param testProxySelected
	 *            The testProxySelected to set.
	 */
	public void setTestProxySelected(boolean testProxySelected) {
		this.testProxySelected_ = testProxySelected;
	}

	/**
	 * @return Returns the wsdlURL.
	 */
	public String getWsdlURL() {
	  return wsdlURL_;
	}

	/**
	 * @param wsdlURL
	 *            The wsdlURL to set.
	 */
	public void setWsdlURL(String wsdlURL) {
		this.wsdlURL_ = wsdlURL;
	}

	/**
	 * @return Returns the clientServer.
	 */
	public IServer getClientExistingServer() {
		return clientExistingServer_;
	}

	/**
	 * @param clientServer
	 *            The clientServer to set.
	 */
	public void setClientServer(String clientServer) {
		this.clientServer_ = clientServer;
	}

	/**
	 * @return Returns the clientIsExistingServer.
	 */
	public boolean isClientIsExistingServer() {
		return clientIsExistingServer_;
	}

	/**
	 * @param clientIsExistingServer
	 *            The clientIsExistingServer to set.
	 */
	public void setClientIsExistingServer(boolean clientIsExistingServer) {
		this.clientIsExistingServer_ = clientIsExistingServer;
	}

	public boolean getCustomizeClientMappings() {
		return customizeMappings_;
	}

	public void setCustomizeClientMappings(boolean value) {
		customizeMappings_ = value;
	}

	private IServer getServerFromServerLabel() {
		if (true)
		// rsk revisit if (clientIsExistingServer_)
		{
			// Maybe this should be in WebServiceServerRuntimeTypeRegistry
			Vector serverIds = new Vector();
			{
				IServer[] servers = ServerCore.getServers();
				if (servers != null && servers.length!=0) {
					for (int i = 0; i < servers.length; i++) {
						IServer server = (IServer) servers[i];
						if ((server.getName()).equals(clientServer_))
							return server;
					}
				}
			}
			//
		} else {
			//TODO create the server
		}
		return null;
	}
	/**
	 * @return Returns the proxyProjectFolder.
	 */
	public String getProxyProjectFolder() {
		if (proxyProject_!=null) {
			proxyProjectFolder_ = ResourceUtils.getJavaSourceLocation(proxyProject_).toString();
		}		
		return proxyProjectFolder_;  
	}

	/**
	 * @return Returns the webServicesParser.
	 */
	public WebServicesParser getWebServicesParser() {
		return webServicesParser_;
	}
	/**
	 * @param webServicesParser The webServicesParser to set.
	 */
	public void setWebServicesParser(WebServicesParser webServicesParser) {
		this.webServicesParser_ = webServicesParser;
	}
	/**
	 * @return Returns the generateProxy.
	 */
	public boolean getGenerateProxy() {
		return generateProxy_;
	}
	/**
	 * @param generateProxy The generateProxy to set.
	 */
	public void setGenerateProxy(boolean generateProxy) {
		this.generateProxy_ = generateProxy;
	}
	
    /**
     * @return Returns the isClientScenario_.
     */
    public boolean getIsClientScenario()
    {
      return isClientScenario_;
    }
    
    /**
     * @param isClientScenario_ The isClientScenario_ to set.
     */
    public void setIsClientScenario(boolean isClientScenario)
    {
      isClientScenario_ = isClientScenario;
    }
    
    /**
     * @param setEndpointMethod The setEndpointMethod to set.
     */
    public String getSetEndpointMethod()
    {
      return "setEndpoint";
    }
}

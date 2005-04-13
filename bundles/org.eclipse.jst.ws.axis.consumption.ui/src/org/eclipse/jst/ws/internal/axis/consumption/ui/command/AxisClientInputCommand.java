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
package org.eclipse.jst.ws.internal.axis.consumption.ui.command;


import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.parser.wsil.WebServicesParser;


public class AxisClientInputCommand extends SimpleCommand {

	private static String LABEL = "TASK_LABEL_AXIS_CLIENT_INPUT";
	private static String DESCRIPTION = "TASK_DESC_AXIS_CLIENT_INPUT";
	
	private IWebServiceClient wsc_;
	private String serverProject_; 
	private IContext context_;

	  private String serviceServerTypeID_;
	  
		private boolean generateProxy_ = true;
		private JavaWSDLParameter javaWSDLParam_;
		private String clientProject_ = null;
		private String wsdlURL_;
		private IServer clientExistingServer_;
		private String clientServer_;
		private WebServicesParser webServicesParser_;
		
	  private MessageUtils msgUtils_;
	  
		/**
		* Default CTOR
		*/
		public AxisClientInputCommand() {
			String       pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
		    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
		    setName (msgUtils_.getMessage(LABEL));
			setDescription( msgUtils_.getMessage(DESCRIPTION));
		}
		
		public AxisClientInputCommand(IWebServiceClient wsc, IContext context, String module) {
			String       pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
		    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
		    setName (msgUtils_.getMessage(LABEL));
			setDescription( msgUtils_.getMessage(DESCRIPTION));
			wsc_ = wsc;
			context_ = context;
			clientProject_ = module; 
		}
		
	  public Status execute(Environment env)
	  {
	    
		generateProxy_ = context_.getClient();
		wsdlURL_ = wsc_.getWebServiceClientInfo().getWsdlURL();
		clientServer_ = wsc_.getWebServiceClientInfo().getServerInstanceId();
		
		return new SimpleStatus("");
	  }

	public String getClientServer() {
		return clientServer_;
	}

	public String getClientProject() {
		return clientProject_;
	}

	public WebServicesParser getWebServicesParser() {
		return webServicesParser_;
	}

	public String getWsdlURL() {
		return wsdlURL_;
	}
	  
		
}

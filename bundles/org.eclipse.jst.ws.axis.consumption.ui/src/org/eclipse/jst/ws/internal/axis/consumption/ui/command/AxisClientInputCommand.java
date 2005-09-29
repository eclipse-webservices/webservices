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


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;


public class AxisClientInputCommand extends EnvironmentalOperation {
	
	private IWebServiceClient wsc_;
	private String serverProject_; 
	private String serverModule_;
	private IContext context_;

	  private String serviceServerTypeID_;
	  
		private boolean generateProxy_ = true;
		private JavaWSDLParameter javaWSDLParam_;
		private String clientProject_ = null;
		private String clientModule_ = null;
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
		}
		
		public AxisClientInputCommand(IWebServiceClient wsc, IContext context, String project, String module) {
			String       pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
		    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
			wsc_ = wsc;
			context_ = context;
			clientProject_ = project; 
			clientModule_ = module;
		}
		
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{	    
		generateProxy_ = context_.getClient();
		wsdlURL_ = wsc_.getWebServiceClientInfo().getWsdlURL();
		clientServer_ = wsc_.getWebServiceClientInfo().getServerInstanceId();
		
		return Status.OK_STATUS;
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

	public String getClientModule() {
		return clientModule_;
	}
	  
		
}

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
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.provisional.wsrt.IContext;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;


public class AxisClientOutputCommand extends SimpleCommand {

	private static String LABEL = "TASK_LABEL_AXIS_CLIENT_OUTPUT";
	private static String DESCRIPTION = "TASK_DESC_AXIS_CLIENT_OUTPUT";
	
	private IWebServiceClient wsc_;
	private String proxyBean_;
		
	  private MessageUtils msgUtils_;
	  
		/**
		* Default CTOR
		*/
		public AxisClientOutputCommand() {
			String       pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
		    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
		    setName (msgUtils_.getMessage(LABEL));
			setDescription( msgUtils_.getMessage(DESCRIPTION));
		}
		
		public AxisClientOutputCommand(IWebServiceClient wsc, IContext context, String module) {
			String       pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
		    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
		    setName (msgUtils_.getMessage(LABEL));
			setDescription( msgUtils_.getMessage(DESCRIPTION));
			wsc_ = wsc;
		}
		
	  public Status execute(Environment env)
	  {		
		wsc_.getWebServiceClientInfo().setImplURL(proxyBean_);
		return new SimpleStatus("");
	  }

	public void setProxyBean(String proxyBean) {
		this.proxyBean_ = proxyBean;
	}
	  
		
}

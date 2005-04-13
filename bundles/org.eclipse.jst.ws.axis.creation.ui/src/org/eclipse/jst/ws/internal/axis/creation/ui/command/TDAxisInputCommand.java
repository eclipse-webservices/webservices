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
package org.eclipse.jst.ws.internal.axis.creation.ui.command;


import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;


public class TDAxisInputCommand extends SimpleCommand {

	private static String LABEL = "TASK_LABEL_TD_AXIS_INPUT";
	private static String DESCRIPTION = "TASK_DESC_TD_AXIS_INPUT";
	
	private IWebService ws_;
	private String serverProject_; 

	  private String serverServer_;
	  private String wsdlURI_;
	  
	  private MessageUtils msgUtils_;
	  
		/**
		* Default CTOR
		*/
		public TDAxisInputCommand() {
			String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
		    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
		    setName (msgUtils_.getMessage(LABEL));
			setDescription( msgUtils_.getMessage(DESCRIPTION));
		}
		
		public TDAxisInputCommand(IWebService ws, String module) {
			String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
		    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
		    setName (msgUtils_.getMessage(LABEL));
			setDescription( msgUtils_.getMessage(DESCRIPTION));
			ws_ = ws;
			serverProject_ = module; 
		}
		
	  public Status execute(Environment env)
	  {
	    
	  	String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
	    
		String serverServer = ws_.getWebServiceInfo().getServerInstanceId();	
		wsdlURI_ = ws_.getWebServiceInfo().getWsdlURL();
		
		return new SimpleStatus("");
	  }
	  
		/**
		   * @return Returns the serverProject.
		   */
		  public String getServerProject()
		  {
			  return serverProject_;
		  }

		  public String getServerServer()
		  {
		    return serverServer_;
		  }

		public String getWsdlURI() {
			return wsdlURI_;
		}
}

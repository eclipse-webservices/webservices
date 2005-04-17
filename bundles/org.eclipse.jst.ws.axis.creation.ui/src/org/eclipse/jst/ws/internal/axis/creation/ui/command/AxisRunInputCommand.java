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

import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.creation.ui.wsrt.AxisWebService;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;

public class AxisRunInputCommand extends SimpleCommand{
	
	private static String LABEL = "TASK_LABEL_BU_AXIS_INPUT";
	private static String DESCRIPTION = "TASK_DESC_BU_AXIS_INPUT";
	
	private AxisWebService ws_;
	private JavaWSDLParameter javaWSDLParam_;
	private String serverProject_; 
	private String serverModule_;
	  
	private MessageUtils msgUtils_;
	
	public AxisRunInputCommand() {
		String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
	    setName (msgUtils_.getMessage(LABEL));
		setDescription( msgUtils_.getMessage(DESCRIPTION));
	}
	
	public AxisRunInputCommand(AxisWebService ws, String project, String module) {
		String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
	    setName (msgUtils_.getMessage(LABEL));
		setDescription( msgUtils_.getMessage(DESCRIPTION));
		ws_ = ws;
		serverProject_ = project; 
		serverModule_ = module;
	}
	
	 public Status execute(Environment env)
	  {
	    
	  	String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
	    
		javaWSDLParam_ = ws_.getAxisWebServiceInfo().getJavaWSDLParameter();
		
		return new SimpleStatus("");
	  }
	
	 /**
	   * @return Returns the serverProject.
	   */
	  public String getServerProject()
	  {
		  return serverProject_;
	  }
	  
	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam_;
	}
	
	
}

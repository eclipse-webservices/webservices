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


import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jem.internal.plugin.JavaEMFNature;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.Method;
import org.eclipse.jem.java.impl.JavaClassImpl;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.common.JavaResourceFilter;
import org.eclipse.jst.ws.internal.consumption.datamodel.validate.ValidationManager;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeTypeRegistry;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.selection.SelectionListChoices;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;
import org.eclipse.wst.ws.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.parser.wsil.WebServicesParser;


public class BUAxisInputCommand extends SimpleCommand {

	private static String LABEL = "TASK_LABEL_BU_AXIS_INPUT";
	private static String DESCRIPTION = "TASK_DESC_BU_AXIS_INPUT";
	
	private IWebService ws_;
	private String serverProject_; 

	  private String serviceServerTypeID_;
	  
	  private MessageUtils msgUtils_;
	  
		/**
		* Default CTOR
		*/
		public BUAxisInputCommand() {
			String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
		    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
		    setName (msgUtils_.getMessage(LABEL));
			setDescription( msgUtils_.getMessage(DESCRIPTION));
		}
		
		public BUAxisInputCommand(IWebService ws, String module) {
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
	    
		String serverInstanceId = ws_.getWebServiceInfo().getServerInstanceId();
		String serverFactoryId = ws_.getWebServiceInfo().getServerFactoryId();
		
		IServer serviceExistingServer=null;
		
		if (serverInstanceId != null) { // server exists
	    	serviceExistingServer = ServerCore.findServer(serverInstanceId);
			if (serviceExistingServer != null)
		    {
		      serviceServerTypeID_ = serviceExistingServer.getServerType().getId();
		    }
	    }
	    else
	    {
	    	serviceServerTypeID_ = serverFactoryId;
	    }
		
		return new SimpleStatus("");
	  }
	  
		/**
		   * @return Returns the serverProject.
		   */
		  public String getServerProject()
		  {
//		    return serviceChoices_.getList().getSelection();
			  return serverProject_;
		  }

		  public String getServiceServerTypeID()
		  {
		    return serviceServerTypeID_;
		  }
}

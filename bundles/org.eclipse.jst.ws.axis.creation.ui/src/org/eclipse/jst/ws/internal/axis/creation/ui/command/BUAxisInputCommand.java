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


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;


public class BUAxisInputCommand extends AbstractDataModelOperation {
	
	private IWebService ws_;
	private String serverProject_; 
	private String serverModule_;
	private String javaBeanName_;

	  private String serviceServerTypeID_;
	  
		/**
		* Default CTOR
		*/
		public BUAxisInputCommand() {
		}
		
		public BUAxisInputCommand(IWebService ws, String project, String module) {
			ws_ = ws;
			serverProject_ = project; 
			serverModule_ = module;	}
		
		public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
		{
	       
		String serverInstanceId = ws_.getWebServiceInfo().getServerInstanceId();
		String serverFactoryId = ws_.getWebServiceInfo().getServerFactoryId();
		javaBeanName_ = ws_.getWebServiceInfo().getImplURL();
		
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
		
		return Status.OK_STATUS;
	  }
	  
		/**
		   * @return Returns the serverProject.
		   */
		  public String getServerProject()
		  {
			  return serverProject_;
		  }

		  public String getServiceServerTypeID()
		  {
		    return serviceServerTypeID_;
		  }

		public String getServerModule() {
			return serverModule_;
		}

		public String getJavaBeanName() {
			return javaBeanName_;
		}
}

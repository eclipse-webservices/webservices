/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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
 * 20060330 128827   kathy@ca.ibm.com - Kathy Chan
 * 20061004   159356 kathy@ca.ibm.com - Kathy Chan, Get correct module root URL based on server chosen
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;


public class TDAxisInputCommand extends AbstractDataModelOperation {
	
	private IWebService ws_;
	private String serverProject_; 

	  private String serverServer_;
      private String serviceServerTypeID_; 
	  private IServer serviceExistingServer_ = null;
	  private String wsdlURI_;
	  private WebServiceInfo webServiceInfo_;
	  	  
		/**
		* Default CTOR
		*/
		public TDAxisInputCommand() {
		}
		
		public TDAxisInputCommand(IWebService ws, String project) {
			ws_ = ws;
			serverProject_ = project;
		}
		
		public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
		{	    
		serverServer_ = ws_.getWebServiceInfo().getServerInstanceId();

		String serverInstanceId = ws_.getWebServiceInfo().getServerInstanceId();
		String serverFactoryId = ws_.getWebServiceInfo().getServerFactoryId();
		if (serverInstanceId != null) { // server exists
	    	serviceExistingServer_ = ServerCore.findServer(serverInstanceId);
			if (serviceExistingServer_ != null)
		    {
		      serviceServerTypeID_ = serviceExistingServer_.getServerType().getId();
		    }
	    }
	    else
	    {
	    	serviceServerTypeID_ = serverFactoryId;
	    }
		
		wsdlURI_ = ws_.getWebServiceInfo().getWsdlURL();
		webServiceInfo_ = ws_.getWebServiceInfo();
		
		return Status.OK_STATUS;
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
          
          public String getServiceServerTypeID()
          {
            return serviceServerTypeID_;
          }          

		public String getWsdlURI() {
			return wsdlURI_;
		}

		public WebServiceInfo getWebServiceInfo() {
			return webServiceInfo_;
		}
		
		public IServer getServiceExistingServer()
		  {
		    return serviceExistingServer_;
		  }

}

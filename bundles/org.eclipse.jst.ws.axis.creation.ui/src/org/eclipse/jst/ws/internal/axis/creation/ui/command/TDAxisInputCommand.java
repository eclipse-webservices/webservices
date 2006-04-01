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
 * 20060330 128827   kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;


public class TDAxisInputCommand extends AbstractDataModelOperation {
	
	private IWebService ws_;
	private String serverProject_; 

	  private String serverServer_;
      private String serviceServerTypeID_; 
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
        serviceServerTypeID_ = ws_.getWebServiceInfo().getServerFactoryId();
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

}

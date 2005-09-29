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
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;


public class TDAxisInputCommand extends EnvironmentalOperation {
	
	private IWebService ws_;
	private String serverProject_; 
	private String serverModule_;

	  private String serverServer_;
	  private String wsdlURI_;
	  	  
		/**
		* Default CTOR
		*/
		public TDAxisInputCommand() {
		}
		
		public TDAxisInputCommand(IWebService ws, String project, String module) {
			ws_ = ws;
			serverProject_ = project;
			serverModule_ = module;
		}
		
		public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
		{	    
		serverServer_ = ws_.getWebServiceInfo().getServerInstanceId();	
		wsdlURI_ = ws_.getWebServiceInfo().getWsdlURL();
		
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

		public String getWsdlURI() {
			return wsdlURI_;
		}

		public String getServerModule() {
			return serverModule_;
		}
}

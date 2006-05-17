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
 * 20060419   132905 cbrealey@ca.ibm.com - Chris Brealey          
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.context.AxisEmitterContext;
import org.eclipse.jst.ws.internal.axis.consumption.core.plugin.WebServiceAxisConsumptionCorePlugin;
import org.eclipse.jst.ws.internal.conformance.JAXRPCWebServiceAnalyzer;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.StatusException;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.wsrt.IWebService;


public class BUAxisInputCommand extends AbstractDataModelOperation {
	
	private IWebService ws_;
	private String serverProject_; 
	private String javaBeanName_;

	  private String serviceServerTypeID_;
	  
		/**
		* Default CTOR
		*/
		public BUAxisInputCommand() {
		}
		
		public BUAxisInputCommand(IWebService ws, String project) {
			ws_ = ws;
			serverProject_ = project; 
		}
		
		public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
		{
	       
		String serverInstanceId = ws_.getWebServiceInfo().getServerInstanceId();
		String serverFactoryId = ws_.getWebServiceInfo().getServerFactoryId();
		javaBeanName_ = ws_.getWebServiceInfo().getImplURL();
		
		// The following "if" block drives validation of the given
		// Java service class (javaBeanName_) against JAX-RPC if the
		// JAX-RPC validation preference is switched on.
		// For the most part, the JAX-RPC analyzer returns warnings
		// for violations of the JAX-RPC spec, and errors only if
		// actual JDT model navigation fails due to bad classpaths.
		// This allows users the choice of heeding or ignoring the
		// messages returned by the analyzer.
		IEnvironment environment = getEnvironment();
		AxisEmitterContext context = WebServiceAxisConsumptionCorePlugin.getInstance().getAxisEmitterContext();
		if (javaBeanName_ != null && context.isValidateAgainstJAXRPCEnabled())
		{
			JAXRPCWebServiceAnalyzer analyzer = new JAXRPCWebServiceAnalyzer();
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(serverProject_); 
			if (project != null)
			{
				IStatus status = analyzer.analyze(project,javaBeanName_,monitor);
				if (!status.isOK())
				{
					try
					{
						environment.getStatusHandler().report(status);
					}
					catch (StatusException e)
					{
						return new Status(IStatus.ERROR,"org.eclipse.jst.ws",0,"",null);
					}
				}
			}
		}
		
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

		public String getJavaBeanName() {
			return javaBeanName_;
		}
}

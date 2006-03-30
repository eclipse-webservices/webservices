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
 * 20060330   124667 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.creation.ui.wsrt.AxisWebService;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class AxisRunInputCommand extends AbstractDataModelOperation{
	
	private AxisWebService ws_;
	private JavaWSDLParameter javaWSDLParam_;
	private String serverProject_; 
	private String serverInstanceId_; 
	private String serverFactoryId_;
	private String wsdlURI_;
	
	public AxisRunInputCommand() {
	}
	
	public AxisRunInputCommand(AxisWebService ws, String project) {
		ws_ = ws;
		serverProject_ = project; 
	}
	
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
	 	javaWSDLParam_ = ws_.getAxisWebServiceInfo().getJavaWSDLParameter();
	 	serverInstanceId_ = ws_.getWebServiceInfo().getServerInstanceId();
	 	serverFactoryId_ = ws_.getWebServiceInfo().getServerFactoryId();
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
	  
	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam_;
	}

	public String getServerInstanceId() {
		return serverInstanceId_;
	}	
	
	public String getServerFactoryId() {
		return serverFactoryId_;
	}

	public String getWsdlURI() {
		return wsdlURI_;
	}	
}

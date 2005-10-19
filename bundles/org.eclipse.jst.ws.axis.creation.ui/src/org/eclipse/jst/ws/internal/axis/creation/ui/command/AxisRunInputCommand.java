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
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.creation.ui.wsrt.AxisWebService;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class AxisRunInputCommand extends AbstractDataModelOperation{
	
	private AxisWebService ws_;
	private JavaWSDLParameter javaWSDLParam_;
	private String serverProject_; 
	
	public AxisRunInputCommand() {
	}
	
	public AxisRunInputCommand(AxisWebService ws, String project) {
		ws_ = ws;
		serverProject_ = project; 
	}
	
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
	 	javaWSDLParam_ = ws_.getAxisWebServiceInfo().getJavaWSDLParameter();
		
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
	
	
}

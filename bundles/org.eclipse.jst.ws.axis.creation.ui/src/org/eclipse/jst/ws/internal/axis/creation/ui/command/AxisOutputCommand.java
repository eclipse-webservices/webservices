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
import org.eclipse.jst.ws.internal.axis.creation.ui.wsrt.AxisWebServiceInfo;
import org.eclipse.wst.command.internal.provisional.env.core.AbstractDataModelOperation;


public class AxisOutputCommand extends AbstractDataModelOperation {

	private AxisWebService ws_;
	private JavaWSDLParameter javaWSDLParam_;

	private String            wsdlURI_;
	  	  
		/**
		* Default CTOR
		*/
		public AxisOutputCommand() {
		}
		
		public AxisOutputCommand(AxisWebService ws) {
			ws_ = ws;
		}
		
		public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
		{
		  	IStatus status = Status.OK_STATUS;  	
		  	ws_.getWebServiceInfo().setWsdlURL(wsdlURI_);
			AxisWebServiceInfo axisWSInfo = new AxisWebServiceInfo();
			axisWSInfo.setJavaWSDLParameter(javaWSDLParam_);
			ws_.setAxisWebServiceInfo(axisWSInfo);		    
		  	
		  	return status;      	
		  }

		  /**
		   * @param wsdlURI
		   *            The wsdlURI to set.
		   */
		  public void setWsdlURI(String wsdlURI)
		  {
		    wsdlURI_ = wsdlURI;
		  }

		public void setJavaWSDLParam (JavaWSDLParameter javaWSDLParam_) {
			this.javaWSDLParam_ = javaWSDLParam_;
		}
		  
}

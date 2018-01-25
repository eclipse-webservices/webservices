/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc., IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080621   210817 samindaw@wso2.com - Saminda Wijeratne, Setting the proxyBean and proxyEndPoint values
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.consumption.core.command;


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceClient;


public class Axis2ClientOutputCommand extends AbstractDataModelOperation {
	
	private IWebServiceClient wsc_;
	private String proxyBean_;
	private String proxyEndpoint_;
	  
		/**
		* Default CTOR
		*/
		public Axis2ClientOutputCommand() {
		}
		
		public Axis2ClientOutputCommand(IWebServiceClient wsc, IContext context) {
			wsc_ = wsc;
		}
		
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{	
		wsc_.getWebServiceClientInfo().setImplURL(proxyBean_);
		wsc_.getWebServiceClientInfo().setProxyEndpoint(proxyEndpoint_);
		return Status.OK_STATUS;
	  }

	public void setProxyBean(String proxyBean) {
		this.proxyBean_ = proxyBean;
	}

	public void setProxyEndpoint(String proxyEndpoint) {
		this.proxyEndpoint_ = proxyEndpoint;
	}
	  
		
}

/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080129   209411 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.creation.ui.wsrt;

import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.utils.ServicePingUtil;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class Axis2CheckWSDLCommand extends AbstractDataModelOperation{
	
	private Axis2WebService ws_;

	private String wsdlURI_;
	
	public Axis2CheckWSDLCommand() {
	}
	
	public Axis2CheckWSDLCommand(Axis2WebService ws) {
		ws_ = ws;

	}
	
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
	 	
	 	wsdlURI_ = ws_.getWebServiceInfo().getWsdlURL();
		try {
			ServicePingUtil servicePingUtil = new ServicePingUtil();
			servicePingUtil.connectToURL(wsdlURI_);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			if (e instanceof IOException) {
				try {
					throw new ExecutionException(e.getMessage());
				} catch (ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				e.printStackTrace();
			}
		}
		return Status.OK_STATUS;
	  }
	


	public String getWsdlURI() {
		return wsdlURI_;
	}	
}

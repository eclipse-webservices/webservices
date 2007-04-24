/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis2.creation.ui.wsrt;

import org.eclipse.jst.ws.internal.axis2.consumption.ui.wsrt.Axis2WebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.AbstractWebServiceRuntime;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceClient;
import org.eclipse.wst.ws.internal.wsrt.WebServiceClientInfo;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

public class Axis2WebServiceRuntime extends AbstractWebServiceRuntime
{

	public IWebService getWebService(WebServiceInfo info){
		return new Axis2WebService(info);
	}

	public IWebServiceClient getWebServiceClient(WebServiceClientInfo info){
		return new Axis2WebServiceClient(info);
	}
	
}

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
 * 20060221   128905 kathy@ca.ibm.com - Kathy Chan
 * 20060330   128827 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsrt;

import org.eclipse.wst.ws.internal.common.MergeUtils;

public class WebServiceInfo {

	private WebServiceState state;
	private java.lang.String serverFactoryId;
	private java.lang.String serverInstanceId;
	private java.lang.String webServiceRuntimeId;
	private java.lang.String wsdlURL;
	private java.lang.String endPointURL;
	private java.lang.String implURL;
	private java.lang.String[] implURLs;
	
	public java.lang.String getEndPointURL()
	{
		return endPointURL;
	}
	public void setEndPointURL(java.lang.String endPointURL)
	{
		this.endPointURL = endPointURL;
	}
	public java.lang.String getImplURL()
	{
		return implURL;
	}
	public void setImplURL(java.lang.String implURL)
	{
		this.implURL = implURL;
	}
	public java.lang.String getServerFactoryId()
	{
		return serverFactoryId;
	}
	public void setServerFactoryId(java.lang.String serverFactoryId)
	{
		this.serverFactoryId = serverFactoryId;
	}
	public java.lang.String getServerInstanceId()
	{
		return serverInstanceId;
	}
	public void setServerInstanceId(java.lang.String serverInstanceId)
	{
		this.serverInstanceId = serverInstanceId;
	}
	public WebServiceState getState()
	{
		return state;
	}
	public void setState(WebServiceState state)
	{
		this.state = state;
	}
	public java.lang.String getWebServiceRuntimeId()
	{
		return webServiceRuntimeId;
	}
	public void setWebServiceRuntimeId(java.lang.String webServiceRuntimeId)
	{
		this.webServiceRuntimeId = webServiceRuntimeId;
	}
	public java.lang.String getWsdlURL()
	{
		return wsdlURL;
	}
	public void setWsdlURL(java.lang.String wsdlURL)
	{
		this.wsdlURL = wsdlURL;
	}
	public java.lang.String[] getImplURLs()
	{
		return implURLs;
	}
	public void setImplURLs(java.lang.String[] implURLs)
	{
		this.implURLs = implURLs;
		MergeUtils.storeMergeModels(implURLs);
	}
	
}

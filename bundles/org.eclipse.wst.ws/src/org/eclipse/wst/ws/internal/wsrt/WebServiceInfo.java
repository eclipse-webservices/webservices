/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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
 * 20070509   182274 kathy@ca.ibm.com - Kathy Chan
 * 20070815   199626 kathy@ca.ibm.com - Kathy Chan
 * 20071130   203826 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsrt;

import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentMergeContext;
import org.eclipse.wst.ws.internal.util.UniversalPathTransformer;


public class WebServiceInfo {

	private WebServiceState state;
	private String serverFactoryId;
	private String serverInstanceId;
	private String serverRuntimeId;
	private String webServiceRuntimeId;
	private String wsdlURL;
	private String endPointURL;
	private String implURL;
	private String[] implURLs;
	private boolean serverCreated = false;
	
	private IMerger merger;
	
	public String getEndPointURL()
	{
		return endPointURL;
	}
	public void setEndPointURL(String endPointURL)
	{
		this.endPointURL = endPointURL;
	}
	public String getImplURL()
	{
		return implURL;
	}
	public void setImplURL(String implURL)
	{
		this.implURL = implURL;
	}
	public String getServerFactoryId()
	{
		return serverFactoryId;
	}
	public void setServerFactoryId(String serverFactoryId)
	{
		this.serverFactoryId = serverFactoryId;
	}
	public String getServerInstanceId()
	{
		return serverInstanceId;
	}
	public void setServerInstanceId(String serverInstanceId)
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
	public String getWebServiceRuntimeId()
	{
		return webServiceRuntimeId;
	}
	public void setWebServiceRuntimeId(String webServiceRuntimeId)
	{
		this.webServiceRuntimeId = webServiceRuntimeId;
	}
	public String getWsdlURL()
	{
		return wsdlURL;
	}
	public void setWsdlURL(String wsdlURL)
	{
		this.wsdlURL = wsdlURL;
	}
	public String[] getImplURLs()
	{
		return implURLs;
	}
	public void setImplURLs(String[] implURLs)
	{
		this.implURLs = implURLs;
		loadMerger();
		
	}
	public IMerger getMerger() {
		return merger;
	}
	public void setMerger(IMerger merger) {
		this.merger = merger;
	}
	
	private void loadMerger() {
		if (merger != null) {
			PersistentMergeContext mergeContext = WSPlugin.getInstance().getMergeContext();
			if (mergeContext.isSkeletonMergeEnabled()) {
				merger.load(UniversalPathTransformer.toFiles(implURLs));
			}
		}
	}
	
	/**
	 * @return Indicated whether the framework had created 
	 * the server instance referenced by getServerInstanceId
	 */
	public boolean isServerCreated() {
		return serverCreated;
	}
	
	/**
	 * @param serverCreated True if the framework had created 
	 * the server instance referenced by getServerInstanceId
	 */
	public void setServerCreated(boolean serverCreated) {
		this.serverCreated = serverCreated;
	}
	public String getServerRuntimeId() {
		return serverRuntimeId;
	}
	public void setServerRuntimeId(String serverRuntimeId) {
		this.serverRuntimeId = serverRuntimeId;
	}
	
}

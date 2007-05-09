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
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsrt;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentMergeContext;
import org.eclipse.wst.ws.internal.util.UniversalPathTransformer;


public class WebServiceInfo {

	private WebServiceState state;
	private java.lang.String serverFactoryId;
	private java.lang.String serverInstanceId;
	private java.lang.String webServiceRuntimeId;
	private java.lang.String wsdlURL;
	private java.lang.String endPointURL;
	private java.lang.String implURL;
	private java.lang.String[] implURLs;
	
	private IMerger merger;
	private IStatus loadMergerStatus;
	
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
				IFile file = null;
				ArrayList fileList = new ArrayList();
				for (int i = 0; i < implURLs.length; i++) {
					String url = implURLs[i];
					// Convert the urls to Eclipse IFiles in workspace.  Null is returned if url is not in workspace. 
					file = UniversalPathTransformer.toFile(url);
					if (file != null) {
						fileList.add(file);
					}
				}
				IFile[] files = (IFile[]) fileList.toArray( new IFile[0]);
				loadMergerStatus = merger.load(files);
			}
		}
	}
	public IStatus getLoadMergerStatus() {
		return loadMergerStatus;
	}
	
}

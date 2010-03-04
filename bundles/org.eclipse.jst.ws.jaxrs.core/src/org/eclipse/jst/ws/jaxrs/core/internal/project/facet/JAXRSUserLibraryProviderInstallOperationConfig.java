/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import org.eclipse.jst.j2ee.internal.common.classpath.WtpUserLibraryProviderInstallOperationConfig;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class JAXRSUserLibraryProviderInstallOperationConfig extends
		WtpUserLibraryProviderInstallOperationConfig {

	public JAXRSUserLibraryProviderInstallOperationConfig() {
		// TODO Auto-generated constructor stub
	}
	
	
	private boolean isSharedLibrary = true;
	private boolean isDeploy = false;
	private IDataModel model = null;
	
	public void setSharedLibrary(boolean isSharedLibrary)
	{
	  this.isSharedLibrary = isSharedLibrary;
//	  setIncludeWithApplicationEnabled(!isSharedLibrary);
	}
	
	public boolean isSharedLibrary()
	{
		return this.isSharedLibrary;
	}

	public void setIsDeploy(boolean isDeploy) {
		this.isDeploy = isDeploy;
		setIncludeWithApplicationEnabled(isDeploy);
	}

	public boolean isDeploy() {
		return this.isDeploy;
	}

	public void setModel(IDataModel model) {
		this.model = model;
	}

	public IDataModel getModel() {
		return model;
	}
	
	

}

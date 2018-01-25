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
 * 20100310   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import org.eclipse.jst.common.project.facet.core.libprov.user.UserLibraryProviderInstallOperationConfig;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class JAXRSSharedLibraryProviderInstallOperationConfig extends UserLibraryProviderInstallOperationConfig implements IJAXRSSharedLibraryProviderInstallOperationConfig  {
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

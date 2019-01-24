/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20100310   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import org.eclipse.wst.common.frameworks.datamodel.IDataModel;


public interface IJAXRSSharedLibraryProviderInstallOperationConfig {
	
	public void setSharedLibrary(boolean isSharedLibrary);
	
	public boolean isSharedLibrary();

	public void setIsDeploy(boolean isDeploy);

	public boolean isDeploy();

	public void setModel(IDataModel model);

	public IDataModel getModel();

}

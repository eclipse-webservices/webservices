/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public interface SharedLibraryConfigurator {
	String getName();
	void setName(String value);
	void installSharedLibs(IProject webProject, IProject earProject, IProgressMonitor monitor, List<String> libraryNames) throws CoreException;
	void installSharedLibs(IProject webProject, IProject earProject, IProgressMonitor monitor, String JAXRSLibraryID) throws CoreException;
	void unInstallSharedLibs(IProject webProject);
	boolean getIsSharedLibSupported(IProject webProject, IProject earProject, boolean addToEAR, String JAXRSLibraryID);
	void setRuntimeID(String value);
	String getRuntimeID();
	boolean getSelected();
	void setSelected(boolean value);

} 
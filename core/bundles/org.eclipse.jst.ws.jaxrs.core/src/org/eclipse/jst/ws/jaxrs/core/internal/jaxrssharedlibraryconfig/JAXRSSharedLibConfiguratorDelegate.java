/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
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
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 * 20101123   330916 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS - facet install should consider Web project associated with multiple EARs
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class JAXRSSharedLibConfiguratorDelegate {

	/**
	 * Constructs an instance.
	 */
	public JAXRSSharedLibConfiguratorDelegate() {
		super();
	}
	/**
	 * @deprecated - Use org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig.JAXRSSharedLibConfiguratorDelegate.installSharedLibs(IProject, IProject, IProgressMonitor, List<String>)
	 */
	protected abstract void installSharedLibs(IProject webProject, IProject earProject, IProgressMonitor monitor, String JAXRSLibraryID);
	/**
	 * @param project to uninstall shared libraries from
	 */
	protected abstract void unInstallSharedLibs(IProject webProject);
	/**
	 * @param project to check if shared libraries are supported
	 * @param JAXRSLibraryID the ID of the JAX-RS implementation library being used 
	 * @return true if shared library support is available
	 */
	protected abstract boolean sharedLibSupported(IProject webProject, IProject earProject, boolean addToEAR, String JAXRSLibraryID);
	 /**
	 * @param webProject to install shared libraries to
	 * @param earProject to install shared libraries to
	 * @param monitor to monitor progress
	 * @param JAXRSLibraryID the ID of the library being used by the project (
	 */
	protected abstract void installSharedLibs(IProject webProject, IProject earProject, IProgressMonitor monitor, List<String> libraryNames);
	
}

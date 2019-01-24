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
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;

public class SharedLibraryConfiguratorImpl implements SharedLibraryConfigurator {
	private IConfigurationElement config_element;
	/**
	 * Key of the 'jaxrsSharedLibraryConfiguratorDelegate' attribute of the extension point.
	 */
	public final static String DELEGATE = "jaxrsSharedLibraryConfiguratorDelegate"; //$NON-NLS-1$
	boolean selected = false;
	String name;
	String runtimeID;
	public SharedLibraryConfiguratorImpl(IConfigurationElement config_element) {
		this.config_element = config_element;
	}
	public boolean getIsSharedLibSupported(IProject webProject, IProject earProject, boolean addToEAR, String JAXRSLibraryID) {
		JAXRSSharedLibConfiguratorDelegate sharedLibConfigurator = null;

			try {
				sharedLibConfigurator = (JAXRSSharedLibConfiguratorDelegate) config_element
						.createExecutableExtension(DELEGATE);
			} catch (CoreException e) {
				//not much we want to do here, just return not suported
			}
		if (sharedLibConfigurator != null) {
			return sharedLibConfigurator.sharedLibSupported(webProject, earProject, addToEAR, JAXRSLibraryID);
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public String getRuntimeID() {
		return runtimeID;
	}

	public void installSharedLibs(IProject webProject, IProject earProject, IProgressMonitor monitor, String JAXRSLibraryID) throws CoreException {
		JAXRSSharedLibConfiguratorDelegate sharedLibConfigurator = null;

		sharedLibConfigurator = (JAXRSSharedLibConfiguratorDelegate) config_element
					.createExecutableExtension(DELEGATE);
		if (sharedLibConfigurator != null) {
			sharedLibConfigurator.installSharedLibs(webProject, earProject, monitor, JAXRSLibraryID);
		}
	}


	public void setName(String value) {
		this.name = value;

	}

	public void unInstallSharedLibs(IProject webProject) {
		JAXRSSharedLibConfiguratorDelegate sharedLibConfigurator = null;

			try {
				sharedLibConfigurator = (JAXRSSharedLibConfiguratorDelegate) config_element
						.createExecutableExtension(DELEGATE);
			} catch (CoreException e) {
				//not much we want to do, libraries not uninstalled
			}
		if (sharedLibConfigurator != null) {
			sharedLibConfigurator.unInstallSharedLibs(webProject);
		}
	}

	public void setRuntimeID(String value) {
		this.runtimeID = value;
		
	}

	public boolean getSelected() {
		return this.selected;
	}

	public void setSelected(boolean value) {
		this.selected = value;
		
	}

	public void installSharedLibs(IProject webProject, IProject earProject,
			IProgressMonitor monitor, List<String> libraryNames)
			throws CoreException {
		JAXRSSharedLibConfiguratorDelegate sharedLibConfigurator = null;

		sharedLibConfigurator = (JAXRSSharedLibConfiguratorDelegate) config_element
					.createExecutableExtension(DELEGATE);
		if (sharedLibConfigurator != null) {
			sharedLibConfigurator.installSharedLibs(webProject, earProject, monitor, libraryNames);
		}
	
	}
	

}

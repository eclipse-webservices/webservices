/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
 *******************************************************************************/package org.eclipse.jst.ws.jaxrs.core.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
import org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration.JAXRSLibraryConfigurationHelper;

/**
 * Initialize JAXRS Libraries as classpath containers
 */
public class JAXRSLibrariesContainerInitializer extends
		ClasspathContainerInitializer {

	private static final String MISSING_LIBRARY = Messages.JAXRSLibrariesContainerInitializer_missing_library;

	/**
	 * Constructor
	 */
	public JAXRSLibrariesContainerInitializer() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.ClasspathContainerInitializer#initialize(org.eclipse
	 * .core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
	 */
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {
		if (isJAXRSLibraryContainer(containerPath)) {
			String libId = containerPath.lastSegment();

			JAXRSLibrary ref = JAXRSLibraryRegistryUtil.getInstance()
					.getJAXRSLibraryRegistry().getJAXRSLibraryByID(libId);
			if (ref != null) {
				JAXRSLibraryClasspathContainer container = new JAXRSLibraryClasspathContainer(
						ref);
				JavaCore.setClasspathContainer(containerPath,
						new IJavaProject[] { project },
						new IClasspathContainer[] { container }, null);
			}
		}
	}

	private boolean isJAXRSLibraryContainer(IPath path) {
		return path != null
				&& path.segmentCount() == 2
				&& JAXRSLibraryConfigurationHelper.JAXRS_LIBRARY_CP_CONTAINER_ID
						.equals(path.segment(0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jdt.core.ClasspathContainerInitializer#
	 * canUpdateClasspathContainer(org.eclipse.core.runtime.IPath,
	 * org.eclipse.jdt.core.IJavaProject)
	 */
	public boolean canUpdateClasspathContainer(IPath containerPath,
			IJavaProject project) {
		return isJAXRSLibraryContainer(containerPath);
	}

	/**
	 * @see org.eclipse.jdt.core.ClasspathContainerInitializer#getDescription(org.eclipse.core.runtime.IPath,
	 *      org.eclipse.jdt.core.IJavaProject)
	 */
	public String getDescription(IPath containerPath, IJavaProject project) {
		if (isJAXRSLibraryContainer(containerPath)) {
			String id = containerPath.lastSegment();
			JAXRSLibrary libref = JAXRSLibraryRegistryUtil.getInstance()
					.getJAXRSLibraryRegistry().getJAXRSLibraryByID(id);
			String displayText = id;

			if (libref == null) {
				displayText = displayText + " " + MISSING_LIBRARY; //$NON-NLS-1$
			}

			return displayText;
		}
		return super.getDescription(containerPath, project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.ClasspathContainerInitializer#getComparisonID(org
	 * .eclipse.core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
	 */
	public Object getComparisonID(IPath containerPath, IJavaProject project) {
		return containerPath;
	}

}

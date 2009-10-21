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
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.jaxrsappconfig;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.jaxrs.core.internal.IJAXRSCoreConstants;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * JAXRSAppConfigUtils provides utility methods useful in processing of a JAXRS
 * application configuration.
 * 
 * <p>
 * <b>Provisional API - subject to change</b>
 * </p>
 * 
 */
public class JAXRSAppConfigUtils {


	/**
	 * Tests if the passed IProject instance is a valid JAXRS project in the
	 * following ways:
	 * <ul>
	 * <li>project is not null and is accessible,</li>
	 * <li>project has the JAXRS facet set on it.</li>
	 * </ul>
	 * 
	 * @param project
	 *            IProject instance to be tested.
	 * @return true if the IProject instance is a valid JAXRS project, else
	 *         false.
	 */
	public static boolean isValidJAXRSProject(IProject project) {
		boolean isValid = false;
		IProjectFacetVersion projectFacet = getProjectFacet(project);
		if (projectFacet != null) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * Get the facet version for the project
	 * 
	 * @param project
	 * @return the project facet version or null if could not be found or if
	 *         project is not accessible
	 */
	@SuppressWarnings("unchecked")
	public static IProjectFacetVersion getProjectFacet(IProject project) {
		// check for null or inaccessible project
		if (project != null && project.isAccessible()) {
			// check for JAXRS facet on project
			try {
				IFacetedProject facetedProject = ProjectFacetsManager
						.create(project);
				if (facetedProject != null) {
					Set projectFacets = facetedProject.getProjectFacets();
					Iterator itProjectFacets = projectFacets.iterator();
					while (itProjectFacets.hasNext()) {
						IProjectFacetVersion projectFacetVersion = (IProjectFacetVersion) itProjectFacets
								.next();
						if (IJAXRSCoreConstants.JAXRS_FACET_ID
								.equals(projectFacetVersion.getProjectFacet()
										.getId())) {
							return projectFacetVersion;
						}
					}
				}
			} catch (CoreException ce) {
				// log error
				JAXRSCorePlugin
						.log(IStatus.ERROR, ce.getLocalizedMessage(), ce);
			}
		}
		return null;
	}

	/**
	 * Gets an IVirtualFolder instance which represents the root context's web
	 * content folder.
	 * 
	 * @param project
	 *            IProject instance for which to get the folder.
	 * @return IVirtualFolder instance which represents the root context's web
	 *         content folder.
	 */
	public static IVirtualFolder getWebContentFolder(IProject project) {
		IVirtualFolder folder = null;
		IVirtualComponent component = ComponentCore.createComponent(project);
		if (component != null) {
			folder = component.getRootFolder();
		}
		return folder;
	}

	/**
	 * Gets an IPath instance representing the path of the passed IFile instance
	 * relative to the web content folder.
	 * 
	 * @param file
	 *            IFile instance for which a path is required.
	 * @return IPath instance representing the path relative to the web content
	 *         folder.
	 */
	public static IPath getWebContentFolderRelativePath(IFile file) {
		IPath path = null;
		if (file != null) {
			IVirtualFolder webContentFolder = getWebContentFolder(file
					.getProject());
			if (webContentFolder != null) {
				IPath webContentPath = webContentFolder
						.getProjectRelativePath();
				IPath filePath = file.getProjectRelativePath();
				int matchingFirstSegments = webContentPath
						.matchingFirstSegments(filePath);
				path = filePath.removeFirstSegments(matchingFirstSegments);
			}
		}
		return path;
	}

}

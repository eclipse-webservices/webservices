/*******************************************************************************
 * Copyright (c) 2013 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetUtils;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * Utility methods used by facet install / uninstall /version change delegates.
 * 
 * @author ian.trimble@oracle.com
 */
public class JAXRSFacetDelegateUtils {

	/**
	 * Tests if specified project is a dynamic web project;
	 * 
	 * @param project Project to be tested.
	 * @return <code>true</code> if project is a dynamic web project, else <code>false</code>.
	 * @throws CoreException On error during testing.
	 */
	public static boolean isDynamicWebProject(IProject project) throws CoreException {
		boolean isWebProject = false;
		if (project != null) {
			final IFacetedProject facetedProject = ProjectFacetsManager.create(project);
			if (facetedProject != null) {
				isWebProject = facetedProject.hasProjectFacet(WebFacetUtils.WEB_FACET);
			}
		}
		return isWebProject;
	}

}

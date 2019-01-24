/*******************************************************************************
 * Copyright (c) 2013, 2019 IBM Corporation and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
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

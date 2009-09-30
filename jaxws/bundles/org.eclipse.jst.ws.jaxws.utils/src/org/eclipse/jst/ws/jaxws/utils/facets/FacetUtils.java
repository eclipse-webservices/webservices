/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils.facets;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;


public class FacetUtils implements IFacetUtils
{
	/**
	 * @throws NullPointerException
	 *             if <c>project</c> or <c>facetIds</c> is null
	 */
	public boolean hasAnyFacet(IProject project, String... facetIds) throws CoreException
	{
		ContractChecker.nullCheckParam(project);
		ContractChecker.nullCheckParam(facetIds);

		if (!isFacetedProject(project))
		{
			return false;
		}

		final IFacetedProject facetedProject = getFacetedProject(project);
		for (String facetId : facetIds)
		{
			final IProjectFacet facet = ProjectFacetsManager.getProjectFacet(facetId);
			if (facetedProject.hasProjectFacet(facet))
			{
				return true;
			}
		}
		return false;
	}

	public boolean hasFacetWithVersion(IProject project, String facetVersion, String facetId) throws CoreException
	{
		return hasFacetWithVersion(project, facetVersion, facetId, true);
	}

	public boolean isFacetedProject(IProject project) throws CoreException
	{
		Set<IFacetedProject> facetedProjects = getAllWorkspaceFacetedProjects();
		final Iterator<IFacetedProject> i = facetedProjects.iterator();
		while (i.hasNext())
		{
			final IFacetedProject fp = (IFacetedProject) i.next();
			if (project.equals(fp.getProject()))
			{
				return true;
			}
		}
		return false;
	}

	public boolean hasFacetWithVersion(IProject project, String facetVersion, String facetId, boolean acceptHigherFacetVersion) throws CoreException
	{
		ContractChecker.nullCheckParam(project);
		ContractChecker.nullCheckParam(facetVersion);
		ContractChecker.nullCheckParam(facetId);

		if (!isFacetedProject(project))
		{
			return false;
		}

		Set<IProjectFacetVersion> allFacetsVersions = getProjectFacets(project);
		for (IProjectFacetVersion ver : allFacetsVersions)
		{
			if (ver.getProjectFacet().getId().equals(facetId))
			{
				return isFacetAcceptable(ver, facetVersion, acceptHigherFacetVersion);
			}
		}

		return false;
	}

	private boolean isFacetAcceptable(IProjectFacetVersion facetVersion, String facetVersionId, boolean acceptHigherFacetVersion)
									throws CoreException
	{
		assert facetVersion != null;

		// Check for exact version
		if (!acceptHigherFacetVersion)
		{
			return facetVersion.getVersionString().equals(facetVersionId);
		}

		Comparator<String> comparator = facetVersion.getProjectFacet().getVersionComparator();
		String currentFacetVersion = facetVersion.getVersionString();
		return comparator.compare(currentFacetVersion, facetVersionId) >= 0;
	}

	private IFacetedProject getFacetedProject(IProject project) throws CoreException
	{
		return ProjectFacetsManager.create(project);
	}

	private Set<IProjectFacetVersion> getProjectFacets(IProject project) throws CoreException
	{
		assert isFacetedProject(project);

		return getFacetedProject(project).getProjectFacets();
	}

	private Set<IFacetedProject> getAllWorkspaceFacetedProjects() throws CoreException
	{
		return ProjectFacetsManager.getFacetedProjects();
	}

}

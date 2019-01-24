/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
 * 20090303   242635 mahutch@ca.ibm.com - Mark Hutchinson, Remove unnecessary UI dependencies from org.eclipse.jst.ws.consumption
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.common;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;

public interface IFacetOperationDelegate {

	/**
	  * Adds the provided set of facet versions to the provided faceted project
	  * 
	  * @param fproject A faceted project which exists in the workspace
	  * @param projectFacetVersions A set containing elements of type {@link IProjectFacetVersion}
	  * @return An IStatus with a severity of IStatus.OK if the facet 
	  * versions were added successfully. Otherwise, an IStatus with a severity of
	  * IStatus.ERROR. 
	  */
	 public IStatus addFacetsToProject(final IFacetedProject fproject, final Set projectFacetVersions);
	 
	 /**
	   * Creates a new faceted project with the provided name
	   * 
	   * @param projectName A String containing the name of the project to be created
	   * @return An IStatus with a severity of IStatus.OK if the faceted project
	   * was created successfully or if a project of the provided name already
	   * exists in the workspace. Otherwise, an IStatus with severity of
	   * IStatus.ERROR. 
	   */
	  public IStatus createNewFacetedProject(final String projectName);
	  
	  /**
	   * Sets the provided set of facets as fixed on the faceted project
	   * 
	   * @param fProject A faceted project which exists in the workspace
	   * @param fixedFacets A set containing elements of type {@link IProjectFacet}
	   * @return An IStatus with a severity of IStatus.OK if the facets 
	   * were successfully set as fixed facets on the faceted project. 
	   * Otherwise, an IStatus with a severity of IStatus.ERROR.
	   * 
	   * @see IFacetedProject#setFixedProjectFacets
	   */
	  public IStatus setFixedFacetsOnProject(final IFacetedProject fProject, final Set fixedFacets);
	  
	  /**
	   * Binds the faceted project to the facet runtime
	   * 
	   * @param fProject A faceted project which exists in the workspace
	   * @param fRuntime A facet runtime
	   * @return An IStatus with a severity of IStatus.OK if the faceted project
	   * was bound to the facet runtime successfully. Otherwise, an IStatus with severity of
	   * IStatus.ERROR. 
	   */
	  public IStatus setFacetRuntimeOnProject(final IFacetedProject fProject, final IRuntime fRuntime);
	
}

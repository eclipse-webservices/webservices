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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
	
public interface IFacetUtils
{
	/**
	 * Constant for EJB30 facet ID
	 */
	public static final String EJB_30_FACET_ID = "jst.ejb"; //$NON-NLS-1$
	
	/**
	 * Constant for EJB30 facet version 3.0
	 */
	public static final String EJB_30_FACET_VERSION = "3.0"; //$NON-NLS-1$
	
	/**
	 * Constant for Web 2.5 facet ID
	 */
	public static final String WEB_25_FACET_ID = "jst.web"; //$NON-NLS-1$
	
	/**
	 * Constant for Web facet version 2.5
	 */
	public static final String WEB_25_FACET_VERSION = "2.5"; //$NON-NLS-1$

	/**
	 * Constant for Application client facet ID
	 */
	public static final String APPCLIENT_25_FACET_ID = "jst.appclient"; //$NON-NLS-1$

	/**
	 * Constant for Application client facet version 2.5
	 */
	public static final String APPCLIENT_25_FACET_VERSION	= "5.0"; //$NON-NLS-1$

	/**
	 * Checks whether <code>project</code> has a facet with id <code>facetId</code> with version <code>facetVersion</code>
	 * In case the project specified is not a faceted project, false is returned 
	 * This is a convenience method that is equivalent to invoking hasFacetWithVersion(project, facetVersion, facetId, false)
	 * @param project the project
	 * @param facetVersion the facet version
	 * @param facetId the facet id
	 * @return true in case the project specified is a faceted project and has the facet with the version specified; false otherwise
	 * @throws CoreException
	 * @throws NullPointerException when any of the input parameters is null
	 * @see IFacetUtils#hasFacetWithVersion(IProject, String, String, boolean)
	 */
	public boolean hasFacetWithVersion(IProject project, String facetVersion, String facetId) throws CoreException;
	
	/**
	 * Checks whether <code>project</code> has a facet with id <code>facetId</code> with version <code>facetVersion</code>
	 * In case the project specified is not a faceted project, false is returned 
	 * @param project the project
	 * @param facetVersion the facet version
	 * @param facetId the facet id
	 * @param acceptHigherFacetVersion a flag indicating whether higher facet versions are acceptable
	 * @return true in case the project specified is a faceted project and has the facet with the version specified; false otherwise
	 * @throws CoreException
	 * @throws NullPointerException when any of the input parameters is null
	 */
	public boolean hasFacetWithVersion(IProject project, String facetVersion, String facetId, boolean acceptHigherFacetVersion) throws CoreException;

	/**
	 * Checks whether the project specified is a faceted project
	 * @param project the project
	 * @return
	 * @throws CoreException
	 * @throws NullPointerException if <code>project</code> is null
	 */
	public boolean isFacetedProject(IProject project) throws CoreException;
}

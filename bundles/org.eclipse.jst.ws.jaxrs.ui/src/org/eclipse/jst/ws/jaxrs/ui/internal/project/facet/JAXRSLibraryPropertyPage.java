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
 * 20091109   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;

import org.eclipse.jst.common.project.facet.ui.libprov.LibraryFacetPropertyPage;
import org.eclipse.jst.ws.jaxrs.core.internal.IJAXRSCoreConstants;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;


public final class JAXRSLibraryPropertyPage

    extends LibraryFacetPropertyPage
    
{
    public IProjectFacetVersion getProjectFacetVersion()
    {
        final IProjectFacet jaxrsFacet = ProjectFacetsManager.getProjectFacet( IJAXRSCoreConstants.JAXRS_FACET_ID ); 
        final IFacetedProject fproj = getFacetedProject();
        return fproj.getInstalledVersion( jaxrsFacet );
    }
    
}

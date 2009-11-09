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
 * 20091106   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.jst.common.project.facet.core.libprov.LibraryUninstallDelegate;
import org.eclipse.jst.ws.jaxrs.core.internal.IJAXRSCoreConstants;
import org.eclipse.wst.common.project.facet.core.ActionConfig;
import org.eclipse.wst.common.project.facet.core.IActionConfigFactory;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * Configuration for JAX-RS facet uninstall
 *
 */
public class JAXRSFacetUninstallConfig

    extends ActionConfig
    
{
    private LibraryUninstallDelegate librariesUninstallDelegate = null;
    
    /**
     * @return the library uninstall delegate
     */
    public LibraryUninstallDelegate getLibrariesUninstallDelegate()
    {
        return this.librariesUninstallDelegate;
    }

    @Override
    public void setFacetedProjectWorkingCopy( final IFacetedProjectWorkingCopy fpjwc )
    {
        super.setFacetedProjectWorkingCopy( fpjwc );
        init();
    }

    
    private void init()
    {
        final IFacetedProjectWorkingCopy fpjwc = getFacetedProjectWorkingCopy();
        IProjectFacetVersion fv = null;
        IFacetedProject fp = fpjwc.getFacetedProject();
        if (fp != null) {
        	Set<IProjectFacetVersion> facets = fp.getProjectFacets();
        	if (facets != null) {
        	Iterator<IProjectFacetVersion> it = facets.iterator();
        	while (it.hasNext()) {
        		IProjectFacetVersion next = it.next();
        		if (next.getProjectFacet().getId().equals(IJAXRSCoreConstants.JAXRS_FACET_ID)) {
        			fv = next;
        			break;
        		}
        	}
        	}
        	
        }
        if( this.librariesUninstallDelegate == null && fpjwc != null && fv != null)
        {
            this.librariesUninstallDelegate = new LibraryUninstallDelegate( fpjwc, fv );
        }
    }
    
    /**
     * The action configuration factory for the JAX-RS facet uninstall config
     *
     */
    public static final class Factory
        
        implements IActionConfigFactory
        
    {
        public Object create()
        {
            return new JAXRSFacetUninstallConfig();
        }
    }
    
}

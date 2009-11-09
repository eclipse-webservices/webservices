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
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.common.project.facet.core.libprov.ILibraryProvider;
import org.eclipse.jst.common.project.facet.core.libprov.LegacyLibraryProviderDetector;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderFramework;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
import org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration.JAXRSLibraryConfigurationHelper;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;

public final class LegacyJAXRSLibraryProviderDetector

    extends LegacyLibraryProviderDetector
    
{
    private static final String LEGACY_JAXRS_LIBRARY_PROVIDER_ID 
        = "legacy-jaxrs-library-provider"; //$NON-NLS-1$

    @Override
    public ILibraryProvider detect( final IProject project,
                                    final IProjectFacet facet )
    {
        try
        {
            final IJavaProject jproj = JavaCore.create( project );
            
            for( IClasspathEntry cpe : jproj.getRawClasspath() )
            {
                if( detect( cpe ) )
                {
                    return LibraryProviderFramework.getProvider( LEGACY_JAXRS_LIBRARY_PROVIDER_ID );
                }
            }
        }
        catch( Exception e )
        {
            JAXRSCorePlugin.log( e, e.getMessage() );
        }

        return null;
    }
    
    /**
     * @param cpe
     * @return true if the classpath entry is detected
     */
    public static boolean detect( final IClasspathEntry cpe )
    {
        if( cpe.getEntryKind() == IClasspathEntry.CPE_CONTAINER )
        {
            final IPath path = cpe.getPath();
            
            if( isJAXRSLibraryContainer( path ) ) 
            {
                String libId = path.lastSegment();
                JAXRSLibrary ref = JAXRSLibraryRegistryUtil.getInstance().getJAXRSLibraryRegistry().getJAXRSLibraryByID(libId);
                
                if( ref != null)
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private static boolean isJAXRSLibraryContainer(IPath path) {
        return path != null && path.segmentCount() == 2 && JAXRSLibraryConfigurationHelper.JAXRS_LIBRARY_CP_CONTAINER_ID.equals(path.segment(0));
    }
    
}

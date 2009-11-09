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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderOperation;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderOperationConfig;

public final class LegacyJAXRSLibraryProviderUninstallOperation

    extends LibraryProviderOperation
    
{
    public void execute( final LibraryProviderOperationConfig config,
                         final IProgressMonitor monitor )
    
        throws CoreException
        
    {
        monitor.beginTask( "", 1 ); //$NON-NLS-1$
        
        try
        {
            final IProject project = config.getFacetedProject().getProject();
            final IJavaProject jproj = JavaCore.create( project );
            final List<IClasspathEntry> newcp = new ArrayList<IClasspathEntry>();
            
            for( IClasspathEntry cpe : jproj.getRawClasspath() )
            {
                if( ! LegacyJAXRSLibraryProviderDetector.detect( cpe ) )
                {
                    newcp.add( cpe );
                }
            }
            
            final IClasspathEntry[] array = newcp.toArray( new IClasspathEntry[ newcp.size() ] );
            jproj.setRawClasspath( array, null );
            
            monitor.worked( 1 );
        }
        finally
        {
            monitor.done();
        }
    }
    
}

/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 * 20100319   306595 ericdp@ca.ibm.com - Eric D. Peters, several install scenarios fail for both user library & non-user library
 * 20100324   306937 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS Properties page- NPE after pressing OK
 * 20100407   304749 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS Facet: install fails when choosing <None> runtime
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderOperationConfig;
import org.eclipse.jst.j2ee.internal.common.classpath.WtpUserLibraryProviderInstallOperation;
import org.eclipse.jst.j2ee.project.EarUtilities;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig.SharedLibraryConfigurator;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig.SharedLibraryConfiguratorUtil;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.jst.server.core.FacetUtil;

public class JAXRSUserLibraryProviderInstallOperation extends WtpUserLibraryProviderInstallOperation
{

  public JAXRSUserLibraryProviderInstallOperation()
  {
    // TODO Auto-generated constructor stub
  }

  public void execute(final LibraryProviderOperationConfig libConfig, final IProgressMonitor monitor)

  throws CoreException

  {
    super.execute(libConfig, monitor);

    JAXRSUserLibraryProviderInstallOperationConfig cfg = (JAXRSUserLibraryProviderInstallOperationConfig) libConfig;
    // If config is null, we are on the properties page
    IDataModel config = cfg.getModel();

    IRuntime runtime = cfg.getFacetedProject().getPrimaryRuntime();
    IProject project = cfg.getFacetedProject().getProject();
    
    String targetRuntimeID = "";
    if (!onPropertiesPage(config)) {
      targetRuntimeID = config.getStringProperty(IJAXRSFacetInstallDataModelProperties.TARGETRUNTIME);
    } else  {
      org.eclipse.wst.server.core.IRuntime iruntime = FacetUtil.getRuntime(runtime);
      if (iruntime != null)
      {
        IRuntimeType rtType = iruntime.getRuntimeType();
        if (rtType != null)
        {
          targetRuntimeID = rtType.getId();
        }
      }
    }

    IProject[] ears = EarUtilities.getReferencingEARProjects(project);
    SharedLibraryConfiguratorUtil.getInstance();
    java.util.List<SharedLibraryConfigurator> configurators = SharedLibraryConfiguratorUtil.getConfigurators();
    Iterator<SharedLibraryConfigurator> sharedLibConfiguratorIterator = configurators.iterator();

    if (onPropertiesPage(config) || cfg.isDeploy() || (!cfg.isDeploy() && !cfg.isSharedLibrary()))
      return;

    while (sharedLibConfiguratorIterator.hasNext())
    {
      SharedLibraryConfigurator thisConfigurator = sharedLibConfiguratorIterator.next();
      if (targetRuntimeID.equals(thisConfigurator.getRuntimeID()))
      {
        IProject earProject = null;
        Boolean addToEar = null;
        if (!onPropertiesPage(config))
        {
          earProject = getEARProject(config);
          addToEar = getAddToEar(config);
        }
        else
        {
          if (ears.length > 0)
            earProject = ears[0];
          else 
            return;
          addToEar = new Boolean(true);
        }
        if (thisConfigurator.getIsSharedLibSupported(project, earProject, addToEar,  ((LibraryInstallDelegate)config.getProperty(IJAXRSFacetInstallDataModelProperties.LIBRARY_PROVIDER_DELEGATE)).getLibraryProvider().getId())) // libref.getID()
        {
          thisConfigurator.installSharedLibs(project, earProject, monitor, cfg.getLibraryNames()); // libref.getID()
          break;
        }
      }
    }
  }

private boolean onPropertiesPage(IDataModel config) {
	return config == null;
}

  private IProject getEARProject(IDataModel config)
  {
    String projName = config.getStringProperty(IJAXRSFacetInstallDataModelProperties.EARPROJECT_NAME);
    if (projName == null || "".equals(projName))
      return null;

    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projName);
    return project;
  }

  private boolean getAddToEar(IDataModel config)
  {
    return config.getBooleanProperty(IJAXRSFacetInstallDataModelProperties.ADD_TO_EAR);
  }

}

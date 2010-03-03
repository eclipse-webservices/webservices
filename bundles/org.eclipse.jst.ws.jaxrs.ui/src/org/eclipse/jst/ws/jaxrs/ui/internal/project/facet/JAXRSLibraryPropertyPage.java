/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
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
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.common.project.facet.ui.libprov.LibraryFacetPropertyPage;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.javaee.core.UrlPatternType;
import org.eclipse.jst.javaee.web.Servlet;
import org.eclipse.jst.javaee.web.ServletMapping;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.ws.jaxrs.core.internal.IJAXRSCoreConstants;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.IJAXRSFacetInstallDataModelProperties;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSJEEUtils;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

@SuppressWarnings("restriction")
public final class JAXRSLibraryPropertyPage

extends LibraryFacetPropertyPage

implements IJAXRSFacetInstallDataModelProperties

{

  private static final String SETTINGS_SERVLET = "servletName"; //$NON-NLS-1$
  private static final String SETTINGS_SERVLET_CLASSNAME = "servletClassname"; //$NON-NLS-1$
  private static final String SETTINGS_URL_MAPPINGS = "urlMappings"; //$NON-NLS-1$
  private static final String SETTINGS_URL_PATTERN = "pattern"; //$NON-NLS-1$

  private ServletInformationGroup servletInfoGroup;

  // Java EE
  private Servlet servlet = null;
  private ServletMapping servletMapping = null;
  // J2EE
  private org.eclipse.jst.j2ee.webapplication.Servlet j2eeServlet = null;
  private org.eclipse.jst.j2ee.webapplication.ServletMapping j2eeServletMapping = null;

  @Override
  protected Control createPageContents(Composite parent)
  {
    Control c = super.createPageContents(parent);

    servletInfoGroup = new ServletInformationGroup((Composite) c, SWT.NONE);
    initializeValues();

    return c;
  }

  public IProjectFacetVersion getProjectFacetVersion()
  {
    final IProjectFacet jaxrsFacet = ProjectFacetsManager.getProjectFacet(IJAXRSCoreConstants.JAXRS_FACET_ID);
    final IFacetedProject fproj = getFacetedProject();
    return fproj.getInstalledVersion(jaxrsFacet);
  }

  protected void initializeValues()
  {
//    LibraryInstallDelegate librariesInstallDelegate = getLibraryInstallDelegate();
//    JAXRSUserLibraryProviderInstallOperationConfig customConfig = (JAXRSUserLibraryProviderInstallOperationConfig) librariesInstallDelegate.getLibraryProviderOperationConfig();

    IFacetedProject facetedProject = getFacetedProject();
    Set set = facetedProject.getProjectFacets();

    IModelProvider provider = ModelProviderManager.getModelProvider(getProject());
    Object object = provider.getModelObject();
    if (object instanceof WebApp)
    {
      WebApp webApp = (WebApp) object;
      List servletMappings = webApp.getServletMappings();
      List servlets = webApp.getServlets();
      // Find the first servlet for now. Our dialog only supports one servlet.
      String servletName = null;
      for (Iterator i = servlets.iterator(); i.hasNext();)
      {
        Object o = i.next();
        if (o instanceof Servlet)
        {
          // init the servlet
          this.servlet = (Servlet) o;
          servletName = servlet.getServletName();
          if (servletName == null)
            servletName = "";
          servletInfoGroup.txtJAXRSServletName.setText(servletName);
          servletInfoGroup.txtJAXRSServletClassName.setText(servlet.getServletClass());
          break;
        }
      }
      // Find the servletMapping that corresponds to the servletName
      for (Iterator i = servletMappings.iterator(); i.hasNext();)
      {
        Object o = i.next();
        if (o instanceof ServletMapping)
        {
          // init the servletMapping
          this.servletMapping = (ServletMapping) o;
          servletInfoGroup.lstJAXRSServletURLPatterns.removeAll();
          if (servletName.equals(servletMapping.getServletName()))
          {
            for (Iterator p = servletMapping.getUrlPatterns().iterator(); p.hasNext();)
            {
              UrlPatternType pattern = (UrlPatternType) p.next();
              servletInfoGroup.lstJAXRSServletURLPatterns.add(pattern.getValue());
            }
          }
        }
      }
    }
  }

  public boolean performOk()
  {
    // This will update the libraries by calling the library provider delegate
    super.performOk();
    // Update the servlet properties
    createServletAndModifyWebXML(getProject(), null, new NullProgressMonitor());
    return true;
  }

  private void createServletAndModifyWebXML(final IProject project, final IDataModel config, final IProgressMonitor monitor)
  {
    IModelProvider provider = JAXRSUtils.getModelProvider(project);
    IPath webXMLPath = new Path("WEB-INF").append("web.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    List<String> listOfMappings = Arrays.asList(servletInfoGroup.lstJAXRSServletURLPatterns.getItems());

    if (JAXRSJEEUtils.isWebApp25(provider.getModelObject()))
    {
      provider.modify(new UpdateWebXMLForJavaEE(project, this.servlet, this.servletMapping, servletInfoGroup.txtJAXRSServletName.getText(), servletInfoGroup.txtJAXRSServletClassName.getText(), listOfMappings),
          doesDDFileExist(project, webXMLPath) ? webXMLPath : IModelProvider.FORCESAVE);
    }
    else
    // must be 2.3 or 2.4
    {
      provider.modify(new UpdateWebXMLForJ2EE(project, this.j2eeServlet, this.j2eeServletMapping, servletInfoGroup.txtJAXRSServletName.getText(), servletInfoGroup.txtJAXRSServletClassName.getText(), listOfMappings), webXMLPath);
    }
  }

  private boolean doesDDFileExist(IProject project, IPath webXMLPath)
  {
    return project.getProjectRelativePath().append(webXMLPath).toFile().exists();
  }

}

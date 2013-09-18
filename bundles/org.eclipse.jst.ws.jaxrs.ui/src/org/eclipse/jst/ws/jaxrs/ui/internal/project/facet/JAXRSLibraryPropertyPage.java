/*******************************************************************************
 * Copyright (c) 2009, 2011 IBM Corporation and others.
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
 * 20100319   306594 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet install fails for Web 2.3 & 2.4
 * 20100324   306937 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS Properties page- NPE after pressing OK
 * 20100325   307059 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS properties page- fields empty or incorrect
 * 20100408   308565 kchong@ca.ibm.com - Keith Chong, JAX-RS: Servlet name and class not updated
 * 20100413   307552 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS and Java EE 6 setup is incorrect
 * 20100512   311032 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS Property page- SWT exception when removing facet
 * 20100519   313576 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS tools- validation problems
 * 20100618   307059 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS properties page- fields empty or incorrect
 * 20110823   349718 kchong@ca.ibm.com - Keith Chong, Upon selection, the JAX-RS Configuration page is blank when Project Facets page has unapplied changes
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.common.project.facet.core.libprov.ILibraryProvider;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.jst.common.project.facet.ui.libprov.LibraryFacetPropertyPage;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.javaee.core.UrlPatternType;
import org.eclipse.jst.javaee.web.Servlet;
import org.eclipse.jst.javaee.web.ServletMapping;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.ws.jaxrs.core.internal.IJAXRSCoreConstants;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.IJAXRSFacetInstallDataModelProperties;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSFacetDelegateUtils;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSJ2EEUtils;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSJEEUtils;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSUtils;
import org.eclipse.jst.ws.jaxrs.ui.internal.IJAXRSUIConstants;
import org.eclipse.jst.ws.jaxrs.ui.internal.JAXRSUIPlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
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

  private IPath webXMLPath;
  private IModelProvider provider;
  private Object webAppObj;
  
  // J2EE
  private org.eclipse.jst.j2ee.webapplication.Servlet j2eeServlet = null;
  private org.eclipse.jst.j2ee.webapplication.ServletMapping j2eeServletMapping = null;
  private String initialInstallDelegateLibraryProviderID =""; //$NON-NLS-1$
  @Override
  protected Control createPageContents(Composite parent)
  {
    Control c = super.createPageContents(parent);
    LibraryInstallDelegate initialInstallDelegate = super.getLibraryInstallDelegate();
    if ( initialInstallDelegate != null ) {
    	ILibraryProvider initialLibraryProvider = initialInstallDelegate.getLibraryProvider();
    	if (initialLibraryProvider != null) {
    		String initID = initialLibraryProvider.getId();
    		initialInstallDelegateLibraryProviderID = (initID == null) ? initialInstallDelegateLibraryProviderID : initID; 
    	}
    }
    this.webXMLPath = new Path("WEB-INF").append("web.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    try {
		if(JAXRSFacetDelegateUtils.isDynamicWebProject(getProject())) {
		this.provider = JAXRSUtils.getModelProvider(getProject());
		if (provider != null)
			this.webAppObj = provider.getModelObject();
		if (doesDDFileExist(getProject(), this.webXMLPath)) {
		    servletInfoGroup = new ServletInformationGroup((Composite) c, SWT.NONE);
				servletInfoGroup.txtJAXRSServletName.addListener(SWT.Modify,
						new Listener() {
							public void handleEvent(Event arg0) {
								updateValidation();
							}
						});
				servletInfoGroup.txtJAXRSServletClassName.addListener(SWT.Modify,
						new Listener() {
							public void handleEvent(Event arg0) {
								updateValidation();
							}
						});
		    initializeValues();
		}
		}
	} catch (CoreException e) {
		JAXRSUIPlugin.log(IStatus.ERROR,NLS.bind(Messages.JAXRSFacetUninstallDelegate_ConfigErr,
				getProject().getName()), e);
	}
    return c;
  }

  public IProjectFacetVersion getProjectFacetVersion()
  {
    final IProjectFacet jaxrsFacet = ProjectFacetsManager.getProjectFacet(IJAXRSCoreConstants.JAXRS_FACET_ID);
    final IFacetedProject fproj = getFacetedProject();
    return fproj.getInstalledVersion(jaxrsFacet);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected void initializeValues()
  {
	List<ServletMapping> servletMappings = new ArrayList<ServletMapping>();
    if (webAppObj != null)
    {	
    	servletInfoGroup.lstJAXRSServletURLPatterns.removeAll();
    	//set defaults- in the rare case we do not find the servlet we will create another servlet entry
    	String servletName = JAXRSUtils.JAXRS_DEFAULT_SERVLET_NAME; 
    	String servletClass = JAXRSUtils.JAXRS_SERVLET_CLASS; 
    	//get id ofthe library provider being used
	    LibraryInstallDelegate installDelegate = super.getLibraryInstallDelegate();
		ILibraryProvider libraryProvider = installDelegate.getLibraryProvider();
		String id = "";
		if (libraryProvider != null) 
			id = libraryProvider.getId();
		if (JAXRSJEEUtils.isWebApp25or30(webAppObj)) {
			WebApp webApp = (WebApp) webAppObj;
			Servlet servlet = JAXRSJEEUtils.findJAXRSServlet(webApp, id);
			if (servlet != null) {
				servletMappings = webApp.getServletMappings();
				servletName = (servlet.getServletName() == null)  ?  servletName : servlet.getServletName();
				servletClass =(servlet.getServletClass() == null) ? servletClass : servlet.getServletClass();
			} else {
				//we did not find the servlet entry, set default value
				servletInfoGroup.lstJAXRSServletURLPatterns.add(JAXRSUtils.JAXRS_DEFAULT_URL_MAPPING);
			}
		} else {
			// 2.3 or 2.4 web app
			org.eclipse.jst.j2ee.webapplication.WebApp webApp = (org.eclipse.jst.j2ee.webapplication.WebApp) webAppObj;
			org.eclipse.jst.j2ee.webapplication.Servlet servlet = JAXRSJ2EEUtils
					.findJAXRSServlet(webApp, id);
			if (servlet != null) {
				this.j2eeServlet = servlet;
				servletMappings = webApp.getServletMappings();
				servletName = (servlet.getServletName() == null)  ?  servletName : servlet.getServletName();
				if (servlet.getServletClass() != null) {
					servletClass =(servlet.getServletClass().getQualifiedName() == null) ? servletClass : servlet.getServletClass().getQualifiedName();
				}
			} else {
				//we did not find the servlet entry, set default value
				servletInfoGroup.lstJAXRSServletURLPatterns.add(JAXRSUtils.JAXRS_DEFAULT_URL_MAPPING);
			}

		}
		servletInfoGroup.txtJAXRSServletName.setText(servletName);
        servletInfoGroup.txtJAXRSServletClassName.setText(servletClass);
      // Find the servletMapping that corresponds to the servletName
        if (JAXRSJEEUtils.isWebApp25or30(webAppObj)) {

      for (Iterator<ServletMapping> i = servletMappings.iterator(); i.hasNext();)
      {
        Object o = i.next();
        if (o instanceof ServletMapping)
        {
          // init the servletMapping
          ServletMapping next = (ServletMapping) o;
          if (servletName.equals(next.getServletName()))
          {
            for (Iterator p = next.getUrlPatterns().iterator(); p.hasNext();)
            {
              UrlPatternType pattern = (UrlPatternType) p.next();
              servletInfoGroup.lstJAXRSServletURLPatterns.add(pattern.getValue());
            }
          }
        }
      }
    } else {
        for (Iterator<ServletMapping> i = servletMappings.iterator(); i.hasNext();)
        {
          Object o = i.next();
          if (o instanceof org.eclipse.jst.j2ee.webapplication.ServletMapping)
          {
            // init the servletMapping
        	org.eclipse.jst.j2ee.webapplication.ServletMapping next = (org.eclipse.jst.j2ee.webapplication.ServletMapping) o;
            org.eclipse.jst.j2ee.webapplication.Servlet aServlet = next.getServlet();
            // the servlet mapping may not have an associated servlet since the user could have modified the file
            if (aServlet != null && servletName.equals(aServlet.getServletName()))
            {
        		this.j2eeServletMapping = next;
                String pattern = next.getUrlPattern();
                servletInfoGroup.lstJAXRSServletURLPatterns.add(new String(pattern));
            }
          }
        }
    }
    }
  }

  public boolean performOk()
  {
	    LibraryInstallDelegate installDelegate = super.getLibraryInstallDelegate();
	    if (installDelegate == null)
	    	//if null user has uninstalled the facet, no reason to update the project properties
	    	return true;
		ILibraryProvider libraryProvider = installDelegate.getLibraryProvider();
		if (libraryProvider != null) {
			String id = libraryProvider.getId();
			if (!initialInstallDelegateLibraryProviderID.equals(id)
					|| IJAXRSUIConstants.USER_LIBRARY_ID.equals(id)) {
				// This will update the libraries by calling the library
				// provider delegate
				super.performOk();
			}
		}
	  
    // Update the servlet properties
	try {
		if (JAXRSFacetDelegateUtils.isDynamicWebProject(getProject()) && doesDDFileExist(getProject(), webXMLPath)) {
		    createServletAndModifyWebXML(getProject(), null, new NullProgressMonitor());
		}
	} catch (CoreException e) {
		JAXRSUIPlugin.log(IStatus.ERROR,NLS.bind(Messages.JAXRSFacetUninstallDelegate_ConfigErr,
				getProject().getName()), e);
	}
    return true;
  }

  private void createServletAndModifyWebXML(final IProject project, final IDataModel config, final IProgressMonitor monitor)
  {
    List<String> listOfMappings = Arrays.asList(servletInfoGroup.lstJAXRSServletURLPatterns.getItems());
    if (JAXRSJEEUtils.isWebApp25or30(webAppObj))
    {
      provider.modify(new UpdateWebXMLForJavaEE(project, initialInstallDelegateLibraryProviderID , servletInfoGroup.txtJAXRSServletName.getText(), servletInfoGroup.txtJAXRSServletClassName.getText(), listOfMappings),
          IModelProvider.FORCESAVE);
    }
    else
    // must be 2.3 or 2.4
    {
      provider.modify(new UpdateWebXMLForJ2EE(project, this.j2eeServlet, this.j2eeServletMapping, servletInfoGroup.txtJAXRSServletName.getText(), servletInfoGroup.txtJAXRSServletClassName.getText(), listOfMappings), webXMLPath);
    }
  }

  private boolean doesDDFileExist(IProject project, IPath webXMLPath)
  { 
	  return getWebContentPath(project) == null ? false : project.getLocation().append(getWebContentPath(project).lastSegment()).append(webXMLPath).toFile().exists();
  }
  private IPath getWebContentPath(IProject project){
		IVirtualComponent component = ComponentCore.createComponent(project);
		IPath modulePath = component.getRootFolder().getWorkspaceRelativePath();
		return modulePath;
  }
	private IStatus validateServletInfo(String servletName, String ServletClassName) {
		if (servletName == null || servletName.trim().length() == 0) {
			String errorMessage = Messages.JAXRSFacetInstallDataModelProvider_ValidateServletName;
			return createErrorStatus(errorMessage);
		}
		if (ServletClassName == null || ServletClassName.trim().length() == 0) {
			String errorMessage = Messages.JAXRSFacetInstallDataModelProvider_ValidateServletClassName;
			return createErrorStatus(errorMessage);
		}
		return Status.OK_STATUS;
	}
	private IStatus createErrorStatus(String msg) {
		return new Status(IStatus.ERROR, JAXRSUIPlugin.PLUGIN_ID, msg);
	}
	protected IStatus performValidation() {
		IStatus superValidation = super.performValidation();
		if (superValidation.isOK())
			try {
				if (JAXRSFacetDelegateUtils.isDynamicWebProject(getProject()) && doesDDFileExist(getProject(), this.webXMLPath))
				  if (servletInfoGroup != null && !servletInfoGroup.isDisposed())
					  return validateServletInfo(servletInfoGroup.txtJAXRSServletName.getText(), servletInfoGroup.txtJAXRSServletClassName.getText());
				  else
				    return Status.OK_STATUS; // superValidation is ok
				else
					return Status.OK_STATUS;
			} catch (CoreException e) {
				String errorMessage = NLS.bind(
						Messages.JAXRSFacetUninstallDelegate_ProjectErr,
						getProject().getName());
				return createErrorStatus(errorMessage);
			}
		else {
			return superValidation;
		}
	}
}

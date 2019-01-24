/*******************************************************************************
 * Copyright (c) 2009, 2014 IBM Corporation and others.
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
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20091106   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100319   306594 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet install fails for Web 2.3 & 2.4
 * 20140813   441729 kchong@ca.ibm.com - Keith Chong, JAX-RS Facet install may fail to update the web.xml with servlet info. 
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.javaee.web.Servlet;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * JAXRS Facet Un-install Delegate for WTP faceted projects.
 * 
 * <li>removes JAXRS servlet, servlet-mappings and context-params <li>leaves
 * JAXRS configuration files on disk <li>removes JAXRS classpath containers
 * 
 */
public final class JAXRSFacetUninstallDelegate implements IDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.common.project.facet.core.IDelegate#execute(org.eclipse
	 * .core.resources.IProject,
	 * org.eclipse.wst.common.project.facet.core.IProjectFacetVersion,
	 * java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void execute(final IProject project, final IProjectFacetVersion fv,
			final Object config, final IProgressMonitor monitor)
			throws CoreException {
		{

			if (monitor != null) {
				monitor.beginTask("", 1); //$NON-NLS-1$
			}

			try {

				final boolean isDynamicWebProject = JAXRSFacetDelegateUtils.isDynamicWebProject(project);

				if (isDynamicWebProject) {
					// Before we do any de-configuration, verify that web.xml is
					// available for update
					IModelProvider provider = JAXRSUtils.getModelProvider(project);
					if (provider == null) {
						throw new JAXRSFacetException(NLS.bind(
								Messages.JAXRSFacetUninstallDelegate_ConfigErr,
								project.getName()));
					} else if (!(provider.validateEdit(null, null).isOK())) {
						throw new JAXRSFacetException(
								NLS
										.bind(
												Messages.JAXRSFacetUninstallDelegate_NonUpdateableWebXML,
												project.getName()));
					}
				}

				// Remove JAXRS Libraries
				( (JAXRSFacetUninstallConfig) config ).getLibrariesUninstallDelegate().execute( null );

				if (isDynamicWebProject) {
					// remove servlet stuff from web.xml
					uninstallJAXRSReferencesFromWebApp(project, monitor);
				}

				if (monitor != null) {
					monitor.worked(1);
				}

			} finally {
				if (monitor != null) {
					monitor.done();
				}
			}
		}
	}

	private void uninstallJAXRSReferencesFromWebApp(final IProject project,
			final IProgressMonitor monitor) {

		IModelProvider provider = JAXRSUtils.getModelProvider(project);
		Object webAppObj = provider.getModelObject();
		if (webAppObj != null) {
			IPath ddPath = new Path("WEB-INF").append("web.xml"); //$NON-NLS-1$ //$NON-NLS-2$
			if (JAXRSJEEUtils.isWebApp25orHigher(webAppObj)) {
				WebApp webApp = (WebApp) webAppObj;
				Servlet servlet = JAXRSJEEUtils.findJAXRSServlet(webApp);
				if (servlet == null)
					return;

				provider.modify(new RemoveJAXRSFromJavaEEWebAppOperation(
						project), ddPath);
			} else {// 2.3 or 2.4 web app
				org.eclipse.jst.j2ee.webapplication.WebApp webApp = (org.eclipse.jst.j2ee.webapplication.WebApp) webAppObj;
				org.eclipse.jst.j2ee.webapplication.Servlet servlet = JAXRSJ2EEUtils
						.findJAXRSServlet(webApp);
				if (servlet == null)
					return;

				provider
						.modify(
								new RemoveJAXRSFromJ2EEWebAppOperation(project),
								ddPath);
			}
		}
	}

	static class RemoveJAXRSFromJavaEEWebAppOperation implements Runnable {
		private IProject _project;

		RemoveJAXRSFromJavaEEWebAppOperation(final IProject project) {
			this._project = project;
		}

		public void run() {
			WebApp webApp = (WebApp) ModelProviderManager.getModelProvider(
					_project).getModelObject();
			Servlet servlet = JAXRSJEEUtils.findJAXRSServlet(webApp);

			// remove jax-rs url mappings
			JAXRSJEEUtils.removeURLMappings(webApp, servlet);
			// remove servlet
			removeJAXRSServlet(webApp, servlet);

		}

		private void removeJAXRSServlet(final WebApp webApp,
				final Servlet servlet) {
			webApp.getServlets().remove(servlet);
		}

	}

	static class RemoveJAXRSFromJ2EEWebAppOperation implements Runnable {
		private IProject _project;

		RemoveJAXRSFromJ2EEWebAppOperation(final IProject project) {
			this._project = project;
		}

		public void run() {
			org.eclipse.jst.j2ee.webapplication.WebApp webApp = (org.eclipse.jst.j2ee.webapplication.WebApp) ModelProviderManager
					.getModelProvider(_project).getModelObject();
			org.eclipse.jst.j2ee.webapplication.Servlet servlet = JAXRSJ2EEUtils
					.findJAXRSServlet(webApp);

			// remove jax-rs url mappings
			JAXRSJ2EEUtils.removeURLMappings(webApp, servlet);
			// remove servlet
			removeJAXRSServlet(webApp, servlet);

		}

		private void removeJAXRSServlet(
				final org.eclipse.jst.j2ee.webapplication.WebApp webApp,
				final org.eclipse.jst.j2ee.webapplication.Servlet servlet) {
			webApp.getServlets().remove(servlet);
		}

	}

}

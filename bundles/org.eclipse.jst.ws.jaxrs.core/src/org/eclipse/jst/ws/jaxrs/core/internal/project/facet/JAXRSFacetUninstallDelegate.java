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
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.common.project.facet.core.ClasspathHelper;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.javaee.web.Servlet;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration.JAXRSLibraryConfigurationHelper;
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

				// Remove JAXRS Libraries
//				removeJAXRSLibraries(project, fv, monitor);

				// Remove Runtime contributed JAXRS classpath entries
//				removeRuntimeContributedJAXRSClasspathEntries(project, fv,
//						monitor);

				// remove servlet stuff from web.xml
				uninstallJAXRSReferencesFromWebApp(project, monitor);

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

	/**
	 * Removes JAXRS Lib CP Containers from project
	 * 
	 * @param project
	 * @param monitor
	 */
	private void removeJAXRSLibraries(final IProject project,
			final IProjectFacetVersion fv, final IProgressMonitor monitor) {
		final IJavaProject jproj = JavaCore.create(project);
		List<IClasspathEntry> keptEntries = new ArrayList<IClasspathEntry>();
		try {
			IClasspathEntry[] entries = jproj.getRawClasspath();
			keptEntries = new ArrayList<IClasspathEntry>();
			for (int i = 0; i < entries.length; i++) {
				IClasspathEntry entry = entries[i];
				if (!(JAXRSLibraryConfigurationHelper
						.isJAXRSLibraryContainer(entry)))
					keptEntries.add(entry);
			}
		} catch (JavaModelException e) {
			JAXRSCorePlugin
					.log(
							e,
							"Cannot get classpath entries to remove JAXRS Libraries for: " + project.getName()); //$NON-NLS-1$
		}

		if (keptEntries.size() > 0) {
			try {
				jproj.setRawClasspath(keptEntries
						.toArray(new IClasspathEntry[0]), monitor);
			} catch (JavaModelException e) {
				JAXRSCorePlugin
						.log(e,
								"Exception occured while removing JAXRS Libraries during JAXRS Facet uninstall"); //$NON-NLS-1$
			}
		}

	}

	private void removeRuntimeContributedJAXRSClasspathEntries(
			final IProject project, final IProjectFacetVersion fv,
			final IProgressMonitor monitor) {
		try {
			ClasspathHelper.removeClasspathEntries(project, fv);
		} catch (CoreException e) {
			JAXRSCorePlugin
					.log(
							IStatus.ERROR,
							"Unable to remove server supplied implementation from the classpath.", e);//$NON-NLS-1$
		}
	}

	private void uninstallJAXRSReferencesFromWebApp(final IProject project,
			final IProgressMonitor monitor) {

		IModelProvider provider = JAXRSUtils.getModelProvider(project);
		Object webAppObj = provider.getModelObject();
		if (webAppObj != null) {
			IPath ddPath = new Path("WEB-INF").append("web.xml"); //$NON-NLS-1$ //$NON-NLS-2$
			if (isJavaEEWebApp(webAppObj)) {
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

	private boolean isJavaEEWebApp(final Object webAppObj) {
		if (webAppObj instanceof WebApp)
			return true;

		return false;

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
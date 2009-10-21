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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxrs.core.internal.IJAXRSCoreConstants;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryInternalReference;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;

/**
 * Provides a data model used by the JAXRS facet install.
 */
public class JAXRSFacetInstallDataModelProvider extends
		FacetInstallDataModelProvider implements
		IJAXRSFacetInstallDataModelProperties {

	private String errorMessage;

	@SuppressWarnings("unchecked")
	public Set<String> getPropertyNames() {
		Set<String> names = super.getPropertyNames();
		names.add(IMPLEMENTATION);
		names.add(DEPLOY_IMPLEMENTATION);
		names.add(SHAREDLIBRARY);
		names.add(EARPROJECT_NAME);
		names.add(WEBPROJECT_NAME);
		names.add(ADD_TO_EAR);
		names.add(TARGETRUNTIME);
		names.add(SERVLET_NAME);
		names.add(SERVLET_CLASSNAME);
		names.add(SERVLET_URL_PATTERNS);
		names.add(WEBCONTENT_DIR);

		names.add(IMPLEMENTATION_LIBRARIES);
		names.add(DEFAULT_IMPLEMENTATION_LIBRARY);

		return names;
	}

	public Object getDefaultProperty(String propertyName) {
		if (propertyName.equals(IMPLEMENTATION)) {
			if (JAXRSLibraryRegistryUtil.getInstance()
					.getJAXRSLibraryRegistry() == null)
				return null;
			return getDefaultImplementationLibrary();
		} else if (propertyName.equals(DEPLOY_IMPLEMENTATION)) {
			return Boolean.TRUE;
		} else if (propertyName.equals(SHAREDLIBRARY)) {
			return Boolean.FALSE;
		} else if (propertyName.equals(EARPROJECT_NAME)) {
			return null;
		} else if (propertyName.equals(WEBPROJECT_NAME)) {
			return null;
		} else if (propertyName.equals(ADD_TO_EAR)) {
			return Boolean.FALSE;
		} else if (propertyName.equals(TARGETRUNTIME)) {
			return null;
		} else if (propertyName.equals(SERVLET_NAME)) {
			return JAXRSUtils.JAXRS_DEFAULT_SERVLET_NAME;
		} else if (propertyName.equals(SERVLET_CLASSNAME)) {
			return JAXRSUtils.JAXRS_SERVLET_CLASS;
		} else if (propertyName.equals(SERVLET_URL_PATTERNS)) {
			return new String[] { JAXRSUtils.JAXRS_DEFAULT_URL_MAPPING };
		} else if (propertyName.equals(FACET_ID)) {
			return IJAXRSCoreConstants.JAXRS_FACET_ID;
		} else if (propertyName.equals(WEBCONTENT_DIR)) {
			return "WebContent"; //$NON-NLS-1$
		} else if (propertyName.equals(IMPLEMENTATION_LIBRARIES)) {
			return getDefaultJAXRSImplementationLibraries();
		} else if (propertyName.equals(DEFAULT_IMPLEMENTATION_LIBRARY)) {
			return getDefaultImplementationLibrary();
		}
		return super.getDefaultProperty(propertyName);
	}

	public IStatus validate(String name) {
		errorMessage = null;
		/*if (name.equals(IMPLEMENTATION)) {
			JAXRSLibraryInternalReference lib = (JAXRSLibraryInternalReference) getProperty(IMPLEMENTATION);
			IStatus status = validateImpl(lib.getLibrary());
			if (!OK_STATUS.equals(status))
				return status;

			return validateClasspath();
		} else */ if (name.equals(SERVLET_NAME)) {
			return validateServletName(getStringProperty(SERVLET_NAME));
		}
		return super.validate(name);
	}

	private IStatus createErrorStatus(String msg) {
		return new Status(IStatus.ERROR, JAXRSCorePlugin.PLUGIN_ID, msg);
	}

	private IStatus validateServletName(String servletName) {
		if (servletName == null || servletName.trim().length() == 0) {
			errorMessage = Messages.JAXRSFacetInstallDataModelProvider_ValidateServletName;
			return createErrorStatus(errorMessage);
		}

		return OK_STATUS;
	}

	private IStatus validateImpl(JAXRSLibrary impl) {
		if (impl == null) {
			errorMessage = Messages.JAXRSFacetInstallDataModelProvider_ValidateJAXRSImpl;
		}
		if (errorMessage != null) {
			return createErrorStatus(errorMessage);
		}
		return OK_STATUS;
	}

	private IStatus validateClasspath() {
		Set<String> jars = new HashSet<String>();
		if (doesProjectExist()) {
			// validate actual classpath by loading jars from cp
			try {
				IClasspathEntry[] entries = getJavaProject()
						.getResolvedClasspath(true);
				for (int i = 0; i < entries.length; i++) {
					IClasspathEntry entry = entries[i];
					if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
						jars.add(entry.getPath().makeAbsolute().toString());
					}
				}
			} catch (JavaModelException e) {
				// FIXME: what should we do in this case?
				JAXRSCorePlugin.log(e, "Error searching class path"); //$NON-NLS-1$
			}
		}
		// else as we do not have a javaProject yet, all we can do is validate
		// that there is no duplicate jars (absolute path)

		IStatus status = null;

		JAXRSLibraryInternalReference ref = null;
		ref = ((JAXRSLibraryInternalReference) getProperty(IJAXRSFacetInstallDataModelProperties.IMPLEMENTATION));
		if (ref != null) {
			status = checkForDupeArchiveFiles(
					jars,
					((JAXRSLibraryInternalReference) getProperty(IJAXRSFacetInstallDataModelProperties.IMPLEMENTATION))
							.getLibrary());
			if (!OK_STATUS.equals(status)) {
				return status;
			}
		} else {
			return createErrorStatus(Messages.JAXRSFacetInstallDataModelProvider_ClientImplValidationMsg);
		}

		return OK_STATUS;
	}

	private IJavaProject getJavaProject() {
		IProject proj = getProject();
		if (proj != null)
			return JavaCore.create(proj);
		return null;
	}

	private IProject getProject() {
		String projName = (String) getProperty(FACET_PROJECT_NAME);
		if (projName == null || "".equals(projName))
			return null;

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				projName);
		return project;
	}

	private boolean doesProjectExist() {
		IProject project = getProject();
		return (project != null) && project.exists();
	}

	@SuppressWarnings("unchecked")
	private IStatus checkForDupeArchiveFiles(Set<String> jars,
			JAXRSLibrary aJAXRSLib) {
		if (aJAXRSLib == null)
			return OK_STATUS;

		for (Iterator it = aJAXRSLib.getArchiveFiles().iterator(); it.hasNext();) {
			ArchiveFile jar = (ArchiveFile) it.next();
			if (jars.contains(jar.getResolvedSourceLocation())) {
				return createErrorStatus(NLS
						.bind(
								Messages.JAXRSFacetInstallDataModelProvider_DupeJarValidation,
								jar.getResolvedSourceLocation()));
			}
			jars.add(jar.getResolvedSourceLocation());
		}
		return OK_STATUS;
	}

	private List<JAXRSLibraryInternalReference> getDefaultJAXRSImplementationLibraries() {
		List<JAXRSLibraryInternalReference> list = new ArrayList<JAXRSLibraryInternalReference>();
		if (JAXRSLibraryRegistryUtil.getInstance().getJAXRSLibraryRegistry() != null) {
			JAXRSLibrary jaxrsLib = JAXRSLibraryRegistryUtil.getInstance()
					.getJAXRSLibraryRegistry().getDefaultImplementation();
			if (jaxrsLib != null) {
				JAXRSLibraryInternalReference prjJAXRSLib = new JAXRSLibraryInternalReference(
						jaxrsLib, true, true, false);
				list.add(prjJAXRSLib);
			}
		}
		return list;
	}

	private JAXRSLibraryInternalReference getDefaultImplementationLibrary() {
		if (JAXRSLibraryRegistryUtil.getInstance().getJAXRSLibraryRegistry() != null) {
			JAXRSLibrary jaxrsLib = JAXRSLibraryRegistryUtil.getInstance()
					.getJAXRSLibraryRegistry().getDefaultImplementation();
			return new JAXRSLibraryInternalReference(jaxrsLib, true, true, false);
		}
		return null;
	}

}

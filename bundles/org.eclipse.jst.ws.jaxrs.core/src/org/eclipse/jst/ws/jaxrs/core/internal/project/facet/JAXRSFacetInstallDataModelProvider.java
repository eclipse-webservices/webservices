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
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20091106   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 * 20100407   308401 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet wizard page - Shared-library option should be disabled
 * 20100413   307552 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS and Java EE 6 setup is incorrect
 * 20100519   313576 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS tools- validation problems
 * 20110823   355024 kchong@ca.ibm.com - Keith Chong, [JAXRS] JAXRSFacetInstallDataModelProvider dispose method does not remove all listeners it adds 
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.common.project.facet.core.libprov.IPropertyChangeListener;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.jst.ws.jaxrs.core.internal.IJAXRSCoreConstants;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryInternalReference;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * Provides a data model used by the JAXRS facet install.
 */
public class JAXRSFacetInstallDataModelProvider extends
		FacetInstallDataModelProvider implements
		IJAXRSFacetInstallDataModelProperties {

	private String errorMessage;
    private LibraryInstallDelegate libraryInstallDelegate = null;
    private IPropertyChangeListener propertyChangeListener = null;

	@SuppressWarnings("unchecked")
	public Set<String> getPropertyNames() {
		Set<String> names = super.getPropertyNames();
		names.add(SHAREDLIBRARY);
		names.add(EARPROJECT_NAME);
		names.add(WEBPROJECT_NAME);
		names.add(ADD_TO_EAR);
		names.add(TARGETRUNTIME);
		names.add(SERVLET_NAME);
		names.add(SERVLET_CLASSNAME);
		names.add(SERVLET_URL_PATTERNS);
		names.add(WEBCONTENT_DIR);
		names.add(LIBRARY_PROVIDER_DELEGATE);
		names.add(DEPLOY_IMPLEMENTATION);
		names.add(CONFIGURATION_PRESET);
		names.add(SERVER_IRUNTIME);
		names.add(EARPROJECTS);
		names.add(UPDATEDD);

		return names;
	}

	public Object getDefaultProperty(String propertyName) {

		if (propertyName.equals(EARPROJECT_NAME)) {
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
		} else if (propertyName.equals(LIBRARY_PROVIDER_DELEGATE)) {
			return this.libraryInstallDelegate;
		} else if (propertyName.equals(DEPLOY_IMPLEMENTATION)) {
			return Boolean.TRUE;
		} else if (propertyName.equals(CONFIGURATION_PRESET)) {
			return null;
		} else if (propertyName.equals(SERVER_IRUNTIME)) {
			return null;
		} else if (propertyName.equals(EARPROJECTS)) {
			return null;
		} else if (propertyName.equals(SHAREDLIBRARY)) {
			return false;
		} else if (propertyName.equals(DEPLOY_IMPLEMENTATION)) {
			return true;
		} else if (propertyName.equals(UPDATEDD)) {
			return true;
		}
		return super.getDefaultProperty(propertyName);
	}

	public IStatus validate(String name) {
		errorMessage = null;
		if (name.equals(LIBRARY_PROVIDER_DELEGATE)) {
			return ((LibraryInstallDelegate) getProperty(LIBRARY_PROVIDER_DELEGATE))
					.validate();
		} else if (name.equals(SERVLET_NAME) || name.equals(SERVLET_CLASSNAME) || name.equals(UPDATEDD)) {
			if (this.getBooleanProperty(IJAXRSFacetInstallDataModelProperties.UPDATEDD))
				return validateServletInfo(getStringProperty(SERVLET_NAME), getStringProperty(SERVLET_CLASSNAME));
			else 
				return super.validate(name);
		}
		return super.validate(name);
	}

	public boolean propertySet(final String propertyName,
			final Object propertyValue) {
		if (propertyName.equals(FACETED_PROJECT_WORKING_COPY)
				|| propertyName.equals(FACET_VERSION)) {
			initLibraryInstallDelegate();

			if (this.libraryInstallDelegate != null
					&& propertyName.equals(FACET_VERSION)) {
				final IProjectFacetVersion fv = (IProjectFacetVersion) getProperty(FACET_VERSION);
				this.libraryInstallDelegate.setProjectFacetVersion(fv);
			}
		}

		return super.propertySet(propertyName, propertyValue);
	}
    @SuppressWarnings("restriction")
	private void initLibraryInstallDelegate()
    {
        final IFacetedProjectWorkingCopy fpjwc = (IFacetedProjectWorkingCopy) getProperty( FACETED_PROJECT_WORKING_COPY );
        final IProjectFacetVersion fv = (IProjectFacetVersion) getProperty( FACET_VERSION );
        
        if( this.libraryInstallDelegate == null && fpjwc != null && fv != null )
        {
            this.libraryInstallDelegate = new LibraryInstallDelegate( fpjwc, fv );
            this.propertyChangeListener = new IPropertyChangeListener()
            {
                public void propertyChanged( final String property,
                                             final Object oldValue,
                                             final Object newValue )
                {
                    final IDataModel dm = getDataModel();

                    if( dm != null )
                    {
                        dm.notifyPropertyChange( LIBRARY_PROVIDER_DELEGATE, IDataModel.VALUE_CHG );
                    }
                }
            };
            this.libraryInstallDelegate.addListener(propertyChangeListener);
        }
    }
    
    @SuppressWarnings("restriction")
    public void dispose()
    {
    	if (this.libraryInstallDelegate != null)
    	{
    	  this.libraryInstallDelegate.removeListener(propertyChangeListener);
    	}
    	super.dispose(); // empty
    }

	private IStatus createErrorStatus(String msg) {
		return new Status(IStatus.ERROR, JAXRSCorePlugin.PLUGIN_ID, msg);
	}

	private IStatus validateServletInfo(String servletName, String servletClassName) {
		if (servletName == null || servletName.trim().length() == 0) {
			errorMessage = Messages.JAXRSFacetInstallDataModelProvider_ValidateServletName;
			return createErrorStatus(errorMessage);
		}
		if (servletClassName == null || servletClassName.trim().length() == 0) {
			errorMessage = Messages.JAXRSFacetInstallDataModelProvider_ValidateServletClassName;
			return createErrorStatus(errorMessage);
		}

		return OK_STATUS;
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

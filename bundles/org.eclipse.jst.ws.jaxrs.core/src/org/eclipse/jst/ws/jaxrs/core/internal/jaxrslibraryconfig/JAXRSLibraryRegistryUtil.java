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
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100304   304732 ericdp@ca.ibm.com - Eric D. Peters, NPE loading library extensions
 * 20100407   308401 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet wizard page - Shared-library option should be disabled
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSLibraryClasspathContainer;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryFactory;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.adapter.MaintainDefaultImplementationAdapter;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryPackageImpl;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.util.JAXRSLibraryRegistryResourceFactoryImpl;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.util.JAXRSLibraryRegistryResourceImpl;
import org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration.internal.JAXRSLibraryConfigurationHelper;
import org.eclipse.jst.ws.jaxrs.core.jaxrslibraryregistry.internal.PluginProvidedJAXRSLibraryCreationHelper2;

/**
 * A singleton maintains lists of implementation libraries in registry.
 * 
 * Each item in the lists contains a workingcopy of a JAX-RS library and
 * decorates with usage information such selection and deployment.
 * 
 * The lists are updated when there are changes in JAX-RS library registry.
 * 
 */
public class JAXRSLibraryRegistryUtil {
	private static JAXRSLibraryRegistryUtil instance = null;

	private List<JAXRSLibraryInternalReference> implLibs = null;

	// The NS URI of the JAX-RS Library Registry's Ecore package. (Must match
	// setting on package in Ecore model.)
	private static final String JAXRS_LIBRARY_REGISTRY_NSURI = "http://www.eclipse.org/webtools/jaxrs/schema/jaxrslibraryregistry.xsd"; //$NON-NLS-1$

	private static final String LIB_EXT_PT = "pluginProvidedJaxrsLibraries"; //$NON-NLS-1$

	// The JAX-RS Library Registry EMF resource instance.
	private static JAXRSLibraryRegistryResourceImpl JAXRSLibraryRegistryResource = null;

	// JAXRSLibraryRegistry singleton
	private JAXRSLibraryRegistry JAXRSLibraryRegistry;

	/**
	 * Private constructor
	 */
	private JAXRSLibraryRegistryUtil() {
		// nothing to do
	}

	/**
	 * Return the singleton instance of JAXRSLibraryRegistryUtil.
	 * 
	 * @return JAXRSLibraryRegistryUtil
	 */
	public synchronized static JAXRSLibraryRegistryUtil getInstance() {
		if (instance == null) {
			instance = new JAXRSLibraryRegistryUtil();
			instance.loadJAXRSLibraryRegistry();
		}
		return instance;
	}

	/**
	 * Convenience method to return the JAXRSLibraryRegistry instance.
	 * 
	 * @return jaxrsLibReg JAXRSLibraryRegistry
	 */
	public JAXRSLibraryRegistry getJAXRSLibraryRegistry() {
		return JAXRSLibraryRegistry;
	}

	/**
	 * Get the default JAXRS implementation library instance. A null is returned
	 * when there is no libraries in the registry.
	 * 
	 * @return JAXRSLibraryInternalReference
	 */
	public JAXRSLibraryInternalReference getDefaultJAXRSImplementationLibrary() {
		JAXRSLibrary dftImplLib = getJAXRSLibraryRegistry()
				.getDefaultImplementation();

		return ((dftImplLib != null) ? getJAXRSLibraryReferencebyID(dftImplLib
				.getID()) : null);
	}

	/**
	 * Get the working copy of JAXRS implementation libraries. The list is
	 * updated when there are changes in registry.
	 * 
	 * @return List
	 */
	List<JAXRSLibraryInternalReference> getJAXRSImplementationLibraries() {
		if (implLibs == null) {
			implLibs = wrapJAXRSLibraries(getJAXRSLibraryRegistry()
					.getImplJAXRSLibraries());
		} else {
			if (implLibs.size() != getJAXRSLibraryRegistry()
					.getImplJAXRSLibraries().size()
					|| isAnyLibraryChanged(implLibs)) {
				implLibs.clear();
				implLibs = wrapJAXRSLibraries(getJAXRSLibraryRegistry()
						.getImplJAXRSLibraries());
			}
		}
		return implLibs;
	}

	/**
	 * Get the JAXRSLibraryDecorator object from the provided ID. A null is
	 * returned no library matches the ID.
	 * 
	 * @param id
	 *            String
	 * @return JAXRSLibraryDecorator
	 */
	public JAXRSLibraryInternalReference getJAXRSLibraryReferencebyID(
			final String id) {
		Iterator<JAXRSLibraryInternalReference> it = getJAXRSImplementationLibraries()
				.iterator();
		JAXRSLibraryInternalReference crtItem = null;

		// search implementation libraries
		while (it.hasNext()) {
			crtItem = it.next();
			if (id.equals(crtItem.getID())) {
				return crtItem;
			}
		}
		return null;
	}

	/**
	 * Add a JAXRS Library into collection for either JAXRS implementation
	 * libraries. The decision is based on if a JAXRS Library is an
	 * implementation.
	 * 
	 * @param library
	 *            JAXRSLibraryLibraryReference
	 */
	public void addJAXRSLibrary(final JAXRSLibraryInternalReference library) {
		// Library is added only if it does not exist in registry
		if (library != null
				&& getJAXRSLibraryRegistry().getJAXRSLibraryByID(
						library.getID()) == null) {
			// Add the library working copy into workspace registry.
			JAXRSLibrary jaxrsLib = library.getLibrary();
			getJAXRSLibraryRegistry()
					.addJAXRSLibrary(jaxrsLib.getWorkingCopy());

			// Add library into the collection depends on its type.
			List<JAXRSLibraryInternalReference> list = getJAXRSImplementationLibraries();
			list.add(library);
		}
	}

	@SuppressWarnings("unchecked")
	private List<JAXRSLibraryInternalReference> wrapJAXRSLibraries(
			final EList libs) {
		List<JAXRSLibraryInternalReference> list = new ArrayList<JAXRSLibraryInternalReference>();
		if (libs != null) {
			JAXRSLibrary jaxrsLib;
			JAXRSLibraryInternalReference jaxrsLibDctr;

			Iterator it = libs.iterator();
			while (it.hasNext()) {
				jaxrsLib = (JAXRSLibrary) it.next();
				// Set selected , deployed , unshared initially
 				jaxrsLibDctr = new JAXRSLibraryInternalReference(jaxrsLib, 
						true, true, false);
				list.add(jaxrsLibDctr);
			}
		}
		return list;
	}

	private boolean isAnyLibraryChanged(
			final List<JAXRSLibraryInternalReference> list) {
		Iterator<JAXRSLibraryInternalReference> it = list.iterator();
		JAXRSLibraryInternalReference wclib = null; // working copy library
		JAXRSLibrary lib = null;

		while (it.hasNext()) {
			wclib = it.next();
			lib = getJAXRSLibraryRegistry().getJAXRSLibraryByID(wclib.getID());
			if (lib == null) { // removed. Hence, changed.
				return true;
			}
			if (wclib.getArchiveFiles().size() != lib.getArchiveFiles().size()) { // Archives
																					// changed..
				return true;
			}
			if (isAnyArchiveFileChanged(wclib.getArchiveFiles(), lib
					.getArchiveFiles())) { // Check archive file changes. I.e.,
											// name and location
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean isAnyArchiveFileChanged(final EList source, EList target) {
		ArchiveFile arSrc = null;
		Iterator it = source.iterator();
		while (it.hasNext()) {
			arSrc = (ArchiveFile) it.next();
			if (!findMatchedArchive(arSrc, target)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean findMatchedArchive(ArchiveFile source, EList list) {
		ArchiveFile target = null;
		Iterator it = list.iterator();
		while (it.hasNext()) {
			target = (ArchiveFile) it.next();
			if (target.equals(source)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the classpath entries for a JAXRS Library
	 * 
	 * @param lib
	 * @return IClasspathEntry[]
	 */
	@SuppressWarnings("unchecked")
	public IClasspathEntry[] getClasspathEntries(JAXRSLibrary lib) {
		// TODO: cache to optimize. probably belongs inside JAXRSLibrary model.
		ArrayList<IClasspathEntry> res = new ArrayList<IClasspathEntry>(lib
				.getArchiveFiles().size());
		for (Iterator it = lib.getArchiveFiles().iterator(); it.hasNext();) {
			ArchiveFile jar = (ArchiveFile) it.next();
			if (jar != null && jar.exists()) {
				IClasspathEntry entry = getClasspathEntry(jar);
				if (entry != null)
					res.add(entry);
			}
		}
		IClasspathEntry[] entries = res
				.toArray(new IClasspathEntry[res.size()]);
		return entries;
	}

	/**
	 * Create IClasspathEntry for ArchiveFile
	 * 
	 * @param jar
	 * @return IClasspathEntry
	 */
	public IClasspathEntry getClasspathEntry(ArchiveFile jar) {
		IClasspathEntry entry = null;
		if (jar != null && jar.exists()) {
			entry = JavaCore.newLibraryEntry(new Path(jar
					.getResolvedSourceLocation()), null, null);// , nu,
																// sourceAttachRoot,
																// accessRules,
																// extraAttributes,
																// false/*not
																// exported*/);
		}
		return entry;
	}

	/**
	 * Binds JAXRS Libraries to classpath containers when the library changes.
	 * 
	 * This method will deal with library/cp container renames by removing the
	 * old classpath container and then adding.
	 * 
	 * @param oldId
	 * @param newId
	 * @param monitor
	 * @throws JavaModelException
	 */
	public static void rebindClasspathContainerEntries(String oldId,
			String newId, IProgressMonitor monitor) throws JavaModelException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IJavaProject[] projects = JavaCore.create(root).getJavaProjects();
		IPath containerPath = new Path(
				JAXRSLibraryConfigurationHelper.JAXRS_LIBRARY_CP_CONTAINER_ID)
				.append(newId);
		IPath oldContainerPath = new Path(
				JAXRSLibraryConfigurationHelper.JAXRS_LIBRARY_CP_CONTAINER_ID)
				.append(oldId);

		JAXRSLibrary lib = JAXRSLibraryRegistryUtil.getInstance()
				.getJAXRSLibraryRegistry().getJAXRSLibraryByID(newId);
		List<IJavaProject> affectedProjects = new ArrayList<IJavaProject>();
		boolean removeAndAddBecauseOfRename = (!oldId.equals(newId));
		// find all projects using the old container name...
		for (int i = 0; i < projects.length; i++) {
			IJavaProject project = projects[i];
			IClasspathEntry[] entries = project.getRawClasspath();
			for (int k = 0; k < entries.length; k++) {
				IClasspathEntry curr = entries[k];
				if (curr.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
					if (oldContainerPath.equals(curr.getPath())) {
						affectedProjects.add(project);
						break;
					}
				}
			}
		}

		if (!affectedProjects.isEmpty()) {
			IJavaProject[] affected = affectedProjects
					.toArray(new IJavaProject[affectedProjects.size()]);
			IClasspathContainer[] containers = new IClasspathContainer[affected.length];
			removeAndAddBecauseOfRename = (!oldId.equals(newId));
			if (removeAndAddBecauseOfRename) {// not very pretty... remove and
												// add new container
				IClasspathEntry newEntry = JavaCore
						.newContainerEntry(containerPath);
				for (int i = 0; i < affected.length; i++) {
					IJavaProject project = affected[i];
					IClasspathEntry[] entries = project.getRawClasspath();
					List<IClasspathEntry> keptEntries = new ArrayList<IClasspathEntry>();
					// keep all entries except the old one
					for (int k = 0; k < entries.length; k++) {
						IClasspathEntry curr = entries[k];
						if (curr.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
							if (!oldContainerPath.equals(curr.getPath()))
								keptEntries.add(curr);
						} else {
							keptEntries.add(curr);
						}
					}
					// add new container entry
					keptEntries.add(newEntry);
					setRawClasspath(project, keptEntries, monitor);
				}

			} else {// rebind

				JAXRSLibraryClasspathContainer container = new JAXRSLibraryClasspathContainer(
						lib);
				containers[0] = container;

				JavaCore.setClasspathContainer(containerPath, affected,
						containers, monitor);
			}
		} else {
			if (monitor != null) {
				monitor.done();
			}
		}
	}

	/**
	 * Sets the raw classpath on a project and logs an error if it when a
	 * JavaModelException occurs
	 * 
	 * @param project
	 * @param cpEntries
	 * @param monitor
	 */
	public static void setRawClasspath(IJavaProject project,
			List<IClasspathEntry> cpEntries, IProgressMonitor monitor) {
		IClasspathEntry[] entries = cpEntries.toArray(new IClasspathEntry[0]);
		try {
			project.setRawClasspath(entries, monitor);
		} catch (JavaModelException e) {
			JAXRSCorePlugin.log(e, "Unable to set classpath for: "
					+ project.getProject().getName());
		}
	}

	/**
	 * Return the URI for the specified JAXRS Library Registry
	 * 
	 * @param registryVersion
	 * @return URI
	 * @throws MalformedURLException
	 */
	public static URI getRegistryURI(String registryVersion)
			throws MalformedURLException {
		URL jaxrsLibRegURL = new URL(Platform.getInstanceLocation().getURL(),
				registryVersion);
		return URI.createURI(jaxrsLibRegURL.toString());
	}

	/**
	 * Loads the JAXRSLibraryRegistry EMF object from plugin-specfic workspace
	 * settings location.
	 */
	private void loadJAXRSLibraryRegistry() {
		try {

			EPackage.Registry.INSTANCE.put(JAXRS_LIBRARY_REGISTRY_NSURI,
					JAXRSLibraryRegistryPackageImpl.init());
			URI jaxrsLibRegURI = getRegistryURI(".metadata/.plugins/org.eclipse.jst.ws.jaxrs.core/JAXRSLibraryRegistry.xml");

			JAXRSLibraryRegistryResourceFactoryImpl resourceFactory = new JAXRSLibraryRegistryResourceFactoryImpl();
			JAXRSLibraryRegistryResource = (JAXRSLibraryRegistryResourceImpl) resourceFactory
					.createResource(jaxrsLibRegURI);
			try {
				Map<String, Boolean> options = new HashMap<String, Boolean>();
				// disable notifications during load to avoid changing stored
				// default implementation
				options.put(XMLResource.OPTION_DISABLE_NOTIFY, Boolean.TRUE);
				JAXRSLibraryRegistryResource.load(options);
				JAXRSLibraryRegistry = (JAXRSLibraryRegistry) JAXRSLibraryRegistryResource
						.getContents().get(0);

//				loadJAXRSLibraryExtensions();

			} catch (IOException ioe) {
				// Create a new Registry instance
				JAXRSLibraryRegistry = JAXRSLibraryRegistryFactory.eINSTANCE
						.createJAXRSLibraryRegistry();
				JAXRSLibraryRegistryResource = (JAXRSLibraryRegistryResourceImpl) resourceFactory
						.createResource(jaxrsLibRegURI);
				JAXRSLibraryRegistryResource.getContents().add(
						JAXRSLibraryRegistry);
//				loadJAXRSLibraryExtensions();
				saveJAXRSLibraryRegistry();
			}
			// add adapter to maintain default implementation
			if (JAXRSLibraryRegistry != null) {
				// check that a default impl is set. if not pick first one if
				// available.
				JAXRSLibrary defLib = JAXRSLibraryRegistry
						.getDefaultImplementation();
				if (defLib == null
						&& JAXRSLibraryRegistry.getImplJAXRSLibraries().size() > 0) {
					JAXRSLibraryRegistry
							.setDefaultImplementation((JAXRSLibrary) JAXRSLibraryRegistry
									.getImplJAXRSLibraries().get(0));
					saveJAXRSLibraryRegistry();
				}
				JAXRSLibraryRegistry.eAdapters().add(
						MaintainDefaultImplementationAdapter.getInstance());

			}
		} catch (MalformedURLException mue) {
			JAXRSCorePlugin.log(IStatus.ERROR,
					Messages.JAXRSLibraryRegistry_ErrorCreatingURL, mue);
		}
	}

	// /////////////////////////////// Load and Save JAXRS Library Registry
	// ////////////////////////////////////////////////

	/**
	 * Creates library registry items from extension points.
	 */
	private void loadJAXRSLibraryExtensions() {
		try {
			IExtensionPoint point = Platform.getExtensionRegistry()
					.getExtensionPoint(JAXRSCorePlugin.PLUGIN_ID, LIB_EXT_PT);
			IExtension[] extensions = point.getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				IExtension ext = extensions[i];
				for (int j = 0; j < ext.getConfigurationElements().length; j++) {
					PluginProvidedJAXRSLibraryCreationHelper2 newLibCreator = new PluginProvidedJAXRSLibraryCreationHelper2(
							ext.getConfigurationElements()[j]);
					JAXRSLibrary newLib = newLibCreator.create();

					if (newLib != null) 
						JAXRSLibraryRegistry.addJAXRSLibrary(newLib);
				}
			}
		} catch (InvalidRegistryObjectException e) {
			JAXRSCorePlugin.log(IStatus.ERROR,
					Messages.JAXRSLibraryRegistry_ErrorLoadingFromExtPt, e);
		}
	}

	/**
	 * Saves the JAXRSLibraryRegistry EMF object from plugin-specfic workspace
	 * settings location. (Called from stop(BundleContext).)
	 * 
	 * @return true if save is successful
	 */
	public boolean saveJAXRSLibraryRegistry() {
		boolean saved = false;
		if (JAXRSLibraryRegistryResource != null) {
			try {
				JAXRSLibraryRegistryResource.save(Collections.EMPTY_MAP);
				saved = true;
			} catch (IOException ioe) {
				JAXRSCorePlugin.log(IStatus.ERROR,
						Messages.JAXRSLibraryRegistry_ErrorSaving, ioe);
			}
		} else {
			JAXRSCorePlugin.log(IStatus.ERROR,
					Messages.JAXRSLibraryRegistry_ErrorSaving);
		}
		return saved;
	}

}

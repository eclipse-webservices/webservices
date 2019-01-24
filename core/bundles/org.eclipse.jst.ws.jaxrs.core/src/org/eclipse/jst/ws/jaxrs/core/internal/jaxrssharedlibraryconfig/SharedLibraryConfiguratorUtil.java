/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
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
 * 20100407   308401 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet wizard page - Shared-library option should be disabled
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.IJAXRSFacetInstallDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * A singleton maintains lists of Shared library configurators 
 * 
 */
public class SharedLibraryConfiguratorUtil {
	private static SharedLibraryConfiguratorUtil instance = null;

	private static List<SharedLibraryConfigurator> configurators = new Vector<SharedLibraryConfigurator>();

	private static final String SHAREDLIB_EXT_PT = "jaxrsSharedLibrarySupport"; //$NON-NLS-1$


	/**
	 * Private constructor
	 */
	private SharedLibraryConfiguratorUtil() {
		// nothing to do
	}
	
	public static boolean isSharedLibSelectedByDefault(String targetRuntimeID) {
		if (targetRuntimeID == null)
			return false;

		SharedLibraryConfiguratorUtil.getInstance();
		java.util.List<SharedLibraryConfigurator> configurators = getConfigurators();

		Iterator<SharedLibraryConfigurator> sharedLibConfiguratorIterator = configurators
				.iterator();
		while (sharedLibConfiguratorIterator.hasNext()) {
			SharedLibraryConfigurator thisConfigurator = sharedLibConfiguratorIterator
					.next();
			if (targetRuntimeID.equals(thisConfigurator.getRuntimeID())) {
				return thisConfigurator.getSelected();
			}

		}

		return false;
	}

	public static boolean isSharedLibSupportAvailable(String libraryID,
			String targetRuntimeID, IProject webProject, IProject earProject,
			boolean addToEAR) {
		if (libraryID == null || targetRuntimeID == null
				|| libraryID.length() == 0 || libraryID.length() == 0)
			return false;

		SharedLibraryConfiguratorUtil.getInstance();
		java.util.List<SharedLibraryConfigurator> configurators = getConfigurators();

		Iterator<SharedLibraryConfigurator> sharedLibConfiguratorIterator = configurators
				.iterator();
		while (sharedLibConfiguratorIterator.hasNext()) {
			SharedLibraryConfigurator thisConfigurator = sharedLibConfiguratorIterator
					.next();
			if (targetRuntimeID.equals(thisConfigurator.getRuntimeID())) {
				if (thisConfigurator.getIsSharedLibSupported(webProject,
						earProject, addToEAR, libraryID)) {
					return true;
				}
			}

		}

		return false;
	}

	/**
	 * Return the singleton instance of SharedLibraryConfiguratorUtil.
	 * 
	 * @return SharedLibraryConfiguratorUtil
	 */
	public synchronized static SharedLibraryConfiguratorUtil getInstance() {
		if (instance == null) {
			instance = new SharedLibraryConfiguratorUtil();
			instance.loadSharedLibraryConfiguratorExtensions();
		}
		return instance;
	}

	/**
	 * Creates jax-rs shared library configurator items from extension points.
	 */
	private void loadSharedLibraryConfiguratorExtensions() {
		try {
			IExtensionPoint point = Platform.getExtensionRegistry()
					.getExtensionPoint(JAXRSCorePlugin.PLUGIN_ID, SHAREDLIB_EXT_PT);
			IExtension[] extensions = point.getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				IExtension ext = extensions[i];
				for (int j = 0; j < ext.getConfigurationElements().length; j++) {
					SharedLibraryConfiguratorCreationHelper newLibCreator = new SharedLibraryConfiguratorCreationHelper(
							ext.getConfigurationElements()[j]);
					SharedLibraryConfigurator newconfigurator = newLibCreator.create();

					if (newconfigurator != null) 
						configurators.add(newconfigurator);
				}
			}
		} catch (InvalidRegistryObjectException e) {
			JAXRSCorePlugin.log(IStatus.ERROR,
					Messages.JAXRSSharedLibraryConfigurator_ErrorLoadingFromExtPt, e);
		}
	}

	public static boolean getAddToEar(IDataModel model) {
		return  model.getBooleanProperty(IJAXRSFacetInstallDataModelProperties.ADD_TO_EAR);
	}
	public static IProject getEARProject(IDataModel model) {
		String projName = model.getStringProperty(IJAXRSFacetInstallDataModelProperties.EARPROJECT_NAME);
		if (projName == null || "".equals(projName))
			return null;
	
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				projName);
		return project;
	}
	public static IProject getWebProject(IDataModel model) {
		String projName = model.getStringProperty(IJAXRSFacetInstallDataModelProperties.WEBPROJECT_NAME);
		if (projName == null || "".equals(projName))
			return null;
	
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				projName);
		return project;
	}
	public static List<SharedLibraryConfigurator> getConfigurators() {
		return configurators;
	}

}

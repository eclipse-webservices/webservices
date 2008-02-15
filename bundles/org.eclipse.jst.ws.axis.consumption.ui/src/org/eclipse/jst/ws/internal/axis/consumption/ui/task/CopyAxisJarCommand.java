/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060509   125094 sengpl@ca.ibm.com - Seng Phung-Lu, Use WorkspaceModifyOperation
 * 20060515   115225 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20060517   142342 kathy@ca.ibm.com - Kathy Chan
 * 20060828	  155439 mahutch@ca.ibm.com - Mark Hutchinson
 * 20070501   184505 kathy@ca.ibm.com - Kathy Chan
 * 20070502   184505 kathy@ca.ibm.com - Kathy Chan, Update JAR sizes
 * 20071102   208620 kathy@ca.ibm.com - Kathy Chan, Update JAR sizes
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.task;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.ProgressUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.core.context.TransientResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.common.BundleUtils;


public class CopyAxisJarCommand extends AbstractDataModelOperation {

	public static String AXIS_JAR = "axis.jar"; //$NON-NLS-1$
	public static String AXIS_RUNTIME_PLUGIN_ID = "org.apache.axis"; //$NON-NLS-1$
	public static String COMMON_DISCOVERY_PLUGIN_ID = "org.apache.commons.discovery"; //$NON-NLS-1$
	public static String COMMON_DISCOVERY_JAR = "commons-discovery-0.2.jar"; //$NON-NLS-1$
	public static String JAVAX_XML_RPC_PLUGIN_ID = "javax.xml.rpc"; //$NON-NLS-1$
	public static String JAVAX_XML_RPC_JAR = "jaxrpc.jar"; //$NON-NLS-1$
	public static String JAVAX_XML_SOAP_PLUGIN_ID = "javax.xml.soap"; //$NON-NLS-1$
	public static String JAVAX_XML_SOAP_JAR = "saaj.jar"; //$NON-NLS-1$
	public static String JAVAX_WSDL_PLUGIN_ID = "javax.wsdl15"; //$NON-NLS-1$
	public static String JAVAX_WSDL_JAR = "wsdl4j-1.5.1.jar"; //$NON-NLS-1$
	public static String COMMON_LOGGING_PLUGIN_ID = "org.apache.commons.logging"; //$NON-NLS-1$
	public static String COMMON_LOGGING_JAR = "commons-logging.jar"; //$NON-NLS-1$
	private static long AXIS_JAR_SIZE = 1588063L;
	private static long COMMON_DISCOVERY_JAR_SIZE = 71451L;
	private static long JAVAX_XML_RPC_JAR_SIZE = 32361L;
	private static long JAVAX_XML_SOAP_JAR_SIZE = 19582L;
	private static long JAVAX_WSDL_JAR_SIZE = 127175L;
	private static long COMMON_LOGGING_JAR_SIZE = 39353L;

// Web Services Jars Used in previous Versions of WTP but now obsolete
	private static String[] OBSOLETE_JARS = new String[]{"commons-discovery.jar", "commons-logging-1.0.4.jar", "log4j-1.2.4.jar", "log4j-1.2.8.jar", "wsdl4j.jar", "axis-ant.jar"};

	public static String PATH_TO_JARS_IN_PLUGIN = "lib/";

	private IProject project;
	private Boolean projectRestartRequired_ = Boolean.FALSE;
	private IClasspathEntry[] oldClasspath;
	private ArrayList newJarNamesList = new ArrayList();

	/**
	 * Default CTOR;
	 */
	public CopyAxisJarCommand() {
	}

	/**
	 * Execute the command
	 */
	public IStatus execute(IProgressMonitor monitor, IAdaptable adaptable) {

		IEnvironment env = getEnvironment();
		IStatus status = Status.OK_STATUS;
		ProgressUtils.report(monitor, AxisConsumptionUIMessages.PROGRESS_INFO_COPY_AXIS_CFG);

		if (J2EEUtils.isWebComponent(project)) {
			copyAxisJarsToProject(project, status, env, monitor);
		}
		else {
			// Check if it's a plain old Java project
			if (J2EEUtils.isJavaComponent(project)) {
				status = addAxisJarsToBuildPath(project, env, monitor);
				if (status.getSeverity() == Status.ERROR) {
					env.getStatusHandler().reportError(status);
					return status;
				}
			}
			else {
				status = StatusUtils.errorStatus(AxisConsumptionUIMessages.MSG_WARN_NO_JAVA_NATURE);
				env.getStatusHandler().reportError(status);
				return status;
			}

		}

		return status;

	}

	private void copyAxisJarsToProject(IProject project, IStatus status, IEnvironment env, IProgressMonitor monitor) {

		IPath webModulePath = J2EEUtils.getWebContentPath(project);
		if (webModulePath == null) {
			status = StatusUtils.errorStatus(ConsumptionMessages.MSG_ERROR_PROJECT_NOT_FOUND);
			env.getStatusHandler().reportError(status);
			return;
		}

		deleteObsoleteJars(webModulePath);


		copyIFile(AXIS_RUNTIME_PLUGIN_ID, "lib/" + AXIS_JAR, webModulePath, "WEB-INF/lib/" + AXIS_JAR, status, env, monitor);
		if (status.getSeverity() == Status.ERROR) {
			return;
		}
		copyIFile(COMMON_DISCOVERY_PLUGIN_ID, "lib/" + COMMON_DISCOVERY_JAR, webModulePath, "WEB-INF/lib/" + COMMON_DISCOVERY_JAR, status, env, monitor);
		if (status.getSeverity() == Status.ERROR) {
			return;
		}
		copyIFile(JAVAX_XML_RPC_PLUGIN_ID, "lib/" + JAVAX_XML_RPC_JAR, webModulePath, "WEB-INF/lib/" + JAVAX_XML_RPC_JAR, status, env, monitor);
		if (status.getSeverity() == Status.ERROR) {
			return;
		}
		copyIFile(JAVAX_XML_SOAP_PLUGIN_ID, "lib/" + JAVAX_XML_SOAP_JAR, webModulePath, "WEB-INF/lib/" + JAVAX_XML_SOAP_JAR, status, env, monitor);
		if (status.getSeverity() == Status.ERROR) {
			return;
		}
		copyIFile(JAVAX_WSDL_PLUGIN_ID, "lib/" + JAVAX_WSDL_JAR, webModulePath, "WEB-INF/lib/" + JAVAX_WSDL_JAR, status, env, monitor);
		if (status.getSeverity() == Status.ERROR) {
			return;
		}

		copyPluginJar(COMMON_LOGGING_PLUGIN_ID, webModulePath, "WEB-INF/lib/" + COMMON_LOGGING_JAR, status, env, monitor);
		if (status.getSeverity() == Status.ERROR) {
			return;
		}
		return;
	}

	/**
	 * 
	 */
	private void copyIFile(String pluginId, String source, IPath targetPath, String targetFile, IStatus status, IEnvironment env, IProgressMonitor monitor) {
		IPath target = targetPath.append(new Path(targetFile));
		ProgressUtils.report(monitor, ConsumptionMessages.PROGRESS_INFO_COPYING_FILE);

		try {
			ResourceContext context = new TransientResourceContext();
			context.setOverwriteFilesEnabled(true);
			context.setCreateFoldersEnabled(true);
			context.setCheckoutFilesEnabled(true);
			URL sourceURL = BundleUtils.getURLFromBundle(pluginId, source);
			IFile resource = ResourceUtils.getWorkspaceRoot().getFile(target);
			if (!resource.exists()) {
				IFile file = FileResourceUtils.createFile(context, target, sourceURL.openStream(), monitor, env.getStatusHandler());
				if ((projectRestartRequired_.booleanValue() == false) && file.exists()) {
					projectRestartRequired_ = Boolean.TRUE;
				}

			}
		}
		catch (Exception e) {
			status = StatusUtils.errorStatus(AxisConsumptionUIMessages.MSG_ERROR_FILECOPY, e);
			env.getStatusHandler().reportError(status);

		}
	}

	/**
	 * Copy plugins that has been JARed
	 */
	private void copyPluginJar(String pluginId, IPath targetPath, String targetFile, IStatus status, IEnvironment env, IProgressMonitor monitor) {
		IPath target = targetPath.append(new Path(targetFile));
		ProgressUtils.report(monitor, ConsumptionMessages.PROGRESS_INFO_COPYING_FILE);

		try {
			ResourceContext context = new TransientResourceContext();
			context.setOverwriteFilesEnabled(true);
			context.setCreateFoldersEnabled(true);
			context.setCheckoutFilesEnabled(true);

			IPath jarPath = BundleUtils.getJarredPluginPath(pluginId);
			if (jarPath != null) {
				IFile resource = ResourceUtils.getWorkspaceRoot().getFile(target);

				if (!resource.exists()) {
					InputStream is = new FileInputStream(new File(jarPath.toString()));
					IFile file = FileResourceUtils.createFile(context, target, is, monitor, env.getStatusHandler());
					if ((projectRestartRequired_.booleanValue() == false) && file.exists()) {
						projectRestartRequired_ = Boolean.TRUE;
					}

				}
			}
		}
		catch (Exception e) {
			status = StatusUtils.errorStatus(AxisConsumptionUIMessages.MSG_ERROR_FILECOPY, e);
			env.getStatusHandler().reportError(status);

		}
	}

	/*
	 * Check for any obsolete Jars in WEB-INF/lib folder Obsolete jars would
	 * be found in projects migrated from older versions of WTP
	 */
	private void deleteObsoleteJars(IPath webModulePath) {
		// First check for Any jars that have names that are known to be
		// obsolete
		for (int i = 0; i < OBSOLETE_JARS.length; i++) {
			IPath path = webModulePath.append("WEB-INF/lib/" + OBSOLETE_JARS[i]);

			IFile resource = ResourceUtils.getWorkspaceRoot().getFile(path);
			if (resource.exists()) {
				deleteResource(resource);
			}
		}

		// delete older JARs of the same name
		deleteOldJar(webModulePath.append("WEB-INF/lib/" + AXIS_JAR), AXIS_JAR_SIZE);
		deleteOldJar(webModulePath.append("WEB-INF/lib/" + COMMON_DISCOVERY_JAR), COMMON_DISCOVERY_JAR_SIZE);
		deleteOldJar(webModulePath.append("WEB-INF/lib/" + JAVAX_XML_RPC_JAR), JAVAX_XML_RPC_JAR_SIZE);
		deleteOldJar(webModulePath.append("WEB-INF/lib/" + JAVAX_XML_SOAP_JAR), JAVAX_XML_SOAP_JAR_SIZE);
		deleteOldJar(webModulePath.append("WEB-INF/lib/" + JAVAX_WSDL_JAR), JAVAX_WSDL_JAR_SIZE);
		deleteOldJar(webModulePath.append("WEB-INF/lib/" + COMMON_LOGGING_JAR), COMMON_LOGGING_JAR_SIZE);

	}

	private void deleteOldJar(IPath jarPath, long jarSize) {
		IFile resource = ResourceUtils.getWorkspaceRoot().getFile(jarPath);
		if (resource.exists()) {
			// calculate the size of the resource by getting the java.io.File
			long fileSize = resource.getLocation().toFile().length();
			if (fileSize != jarSize) {
				deleteResource(resource);
			}
		}
	}

	private void deleteResource(IFile resource) { // delete the resource
		try {
			// System.out.println("Obsolete Jar!! " + resource.getName());
			resource.delete(true, null);
		}
		catch (Exception e) { // e.printStackTrace();
		}
	}


	/**
	 * Addes Axis JARs to the build path of Java project
	 * 
	 * @param env
	 * @param monitor
	 * @return
	 */
	public IStatus addAxisJarsToBuildPath(IProject project, IEnvironment env, IProgressMonitor monitor) {

		IStatus status;

		try {
			getJavaProjectClasspath(env, monitor);

			addNewJarEntry(PATH_TO_JARS_IN_PLUGIN + AXIS_JAR, getTheJarPath(AXIS_RUNTIME_PLUGIN_ID, PATH_TO_JARS_IN_PLUGIN + AXIS_JAR));
			addNewJarEntry(PATH_TO_JARS_IN_PLUGIN + COMMON_DISCOVERY_JAR, getTheJarPath(COMMON_DISCOVERY_PLUGIN_ID, PATH_TO_JARS_IN_PLUGIN + COMMON_DISCOVERY_JAR));
			addNewJarEntry(PATH_TO_JARS_IN_PLUGIN + JAVAX_XML_RPC_JAR, getTheJarPath(JAVAX_XML_RPC_PLUGIN_ID, PATH_TO_JARS_IN_PLUGIN + JAVAX_XML_RPC_JAR));
			addNewJarEntry(PATH_TO_JARS_IN_PLUGIN + JAVAX_XML_SOAP_JAR, getTheJarPath(JAVAX_XML_SOAP_PLUGIN_ID, PATH_TO_JARS_IN_PLUGIN + JAVAX_XML_SOAP_JAR));
			addNewJarEntry(PATH_TO_JARS_IN_PLUGIN + JAVAX_WSDL_JAR, getTheJarPath(JAVAX_WSDL_PLUGIN_ID, PATH_TO_JARS_IN_PLUGIN + JAVAX_WSDL_JAR));

			IPath commonLoggingJarPath = BundleUtils.getJarredPluginPath(COMMON_LOGGING_PLUGIN_ID);
			if (commonLoggingJarPath != null) {
				addNewJarEntry(commonLoggingJarPath.toString(), commonLoggingJarPath);
			}
			updateClasspath(monitor);
		}
		catch (Exception e) {
			status = StatusUtils.errorStatus(AxisConsumptionUIMessages.MSG_ERROR_BAD_BUILDPATH, e);
			return status;
		}

		return Status.OK_STATUS;
	}

	/**
	 * @param env
	 * @param monitor
	 * @return The Java project classpath
	 * @throws JavaModelException
	 */
	private IStatus getJavaProjectClasspath(IEnvironment env, IProgressMonitor monitor) throws JavaModelException {

		IStatus status = Status.OK_STATUS;

		IJavaProject javaProject_ = null;
		oldClasspath = null;
		javaProject_ = JavaCore.create(project);

		oldClasspath = javaProject_.getRawClasspath();

		return status;
	}

	/**
	 * Store new JAR name in newJarNamesList if it's not already on build path
	 * 
	 * @param jarName
	 *            name of the JAR
	 * @param jarPath
	 *            Absolute path to the JAR
	 */
	private void addNewJarEntry(String jarName, IPath jarPath) {

		boolean found = false;
		for (int i = 0; i < oldClasspath.length; i++) {
			found = oldClasspath[i].getPath().toString().toLowerCase().endsWith(jarName.toLowerCase());
			if (found) {
				break;
			}
		}

		if (!found) {
			newJarNamesList.add(new JarEntry(jarName, jarPath));
		}

	}

	/**
	 * Update the Java project classpath adding classpath from newJarNamesList
	 * to oldClasspath
	 * 
	 * @param monitor
	 * @return
	 * @throws JavaModelException
	 */
	private IStatus updateClasspath(IProgressMonitor monitor) throws JavaModelException {

		IStatus status = Status.OK_STATUS;

		if (newJarNamesList.size() > 0) {
			JarEntry[] newJarEntries = (JarEntry[]) newJarNamesList.toArray(new JarEntry[]{});

			IClasspathEntry[] newClasspath = new IClasspathEntry[oldClasspath.length + newJarEntries.length];
			int i = 0;
			// Add oldClasspath entries
			while (i < oldClasspath.length) {
				newClasspath[i] = oldClasspath[i];
				i++;
			}

			int m = 0;
			while (i < newClasspath.length) {
				newClasspath[i] = JavaCore.newLibraryEntry(newJarEntries[m].getJarPath(), null, null);
				m++;
				i++;
			}

			//
			// Then update the project classpath.
			//

			IJavaProject javaProject = JavaCore.create(project);
			javaProject.setRawClasspath(newClasspath, monitor);

		}

		return status;

	}

	//
	// Returns the local native pathname of the jar.
	//
	private IPath getTheJarPath(String pluginId, String theJar) throws MalformedURLException, IOException {
		if (pluginId != null) {
			URL localURL = Platform.asLocalURL(BundleUtils.getURLFromBundle(pluginId, theJar));
			return new Path(localURL.getFile());
		}
		else {
			return new Path(theJar);
		}

	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public boolean getProjectRestartRequired() {
		return projectRestartRequired_.booleanValue();
	}

	public class JarEntry {
		private String jarName;
		private IPath jarPath;

		public JarEntry(String name, IPath path) {
			jarName = name;
			jarPath = path;
		}

		public String getJarName() {
			return jarName;
		}

		public IPath getJarPath() {
			return jarPath;
		}

	}
}

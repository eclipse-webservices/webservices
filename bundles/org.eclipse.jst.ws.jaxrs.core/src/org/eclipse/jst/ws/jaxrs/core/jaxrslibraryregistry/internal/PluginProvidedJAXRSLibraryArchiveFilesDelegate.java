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
 * 20100420   309846 ericdp@ca.ibm.com - Eric D. Peters, Remove dead code related to e.p. pluginProvidedJaxrsLibraries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.jaxrslibraryregistry.internal;


/**
 * Abstract class to be used for supplying the jar file names for a plugin
 * provided JAXRS Library. <br>
 * Adopters must implement getArchiveFiles() making use of the
 * addArchiveFile(String pluginRootRelativePath) method. <br>
 * <em>Note: A plugin providing jaxrs libraries cannot be jarred at this time. </em>
 * <br>
 * <br>
 * ex. <br>
 * 
 * <pre>
 * &lt;code&gt;public void getArchiveFiles() {
 * 						addArchiveFile(&quot;/lib/JAR1.jar&quot;);
 * 						addArchiveFile(&quot;/lib/JAR2.jar&quot;);
 * 					}
 * 		&lt;/code&gt;
 * </pre>
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 * 
 */
public abstract class PluginProvidedJAXRSLibraryArchiveFilesDelegate {
	private PluginProvidedJAXRSLibraryCreationHelper2 helper;

	/**
	 * Constructs an instance.
	 */
	public PluginProvidedJAXRSLibraryArchiveFilesDelegate() {
		super();
	}

	/**
	 * Concrete delegate must implement this method to define jars in the
	 * library. Use addJarFile(String pluginRootRelativePath) within this method
	 * to add jars to the library.
	 * @throws Exception 
	 */
	public abstract void getArchiveFiles();

	/**
	 * Adds jar file to the library. Verification of file existence does not
	 * occur at this point.
	 * 
	 * Jar must be specified as being relative to the plugin. ex.
	 * "/lib/MyJar.jar" where the lib directory is a child of the root.
	 * 
	 * @param pluginRootRelativePath
	 */
	protected void addArchiveFile(String pluginRootRelativePath) {
		helper.addArchiveFile(pluginRootRelativePath);// getArchiveFiles().add(helper.createArchiveFile(pluginRootRelativePath));
	}
	
	
	protected void addArchiveFileFromFullPath(String path) throws Exception {
		helper.addArchiveFileFromFullPath(path);
	}

	/**
	 * Not to be implemented by subclasses
	 * 
	 * @param helper
	 */
	public void setCreationHelper(
			PluginProvidedJAXRSLibraryCreationHelper2 helper) {
		this.helper = helper;
	}
}

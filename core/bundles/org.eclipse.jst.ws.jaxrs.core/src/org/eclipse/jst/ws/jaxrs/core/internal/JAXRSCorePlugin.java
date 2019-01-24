/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
 *******************************************************************************/package org.eclipse.jst.ws.jaxrs.core.internal;

import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

/**
 * JAX-RS Core plugin.
 */
public class JAXRSCorePlugin extends Plugin {
	/**
	 * The plugin id
	 */
	public static final String PLUGIN_ID = "org.eclipse.jst.ws.jaxrs.core";
	// //$NON-NLS-1$

	// The shared instance.
	private static JAXRSCorePlugin plugin;

	private IPreferenceStore preferenceStore;

	private static IDialogSettings jaxrsUIsettings = null;

	/**
	 * The constructor.
	 */
	public JAXRSCorePlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 * 
	 * @param context
	 * @throws Exception
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 * 
	 * @param context
	 * @throws Exception
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		super.stop(context);

		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static JAXRSCorePlugin getDefault() {
		return plugin;
	}

	/**
	 * @param e
	 * @param msg
	 */
	public static void log(final Exception e, final String msg) {
		final ILog log = getDefault().getLog();

		log.log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, msg, e));
	}

	/**
	 * Logs using the default ILog implementation provided by getLog().
	 * 
	 * @param severity
	 *            Severity (IStatus constant) of log entry
	 * @param message
	 *            Human-readable message describing log entry
	 * @param ex
	 *            Throwable instance (can be null)
	 */
	public static void log(final int severity, final String message,
			final Throwable ex) {
		getDefault().getLog().log(
				new Status(severity, PLUGIN_ID, IStatus.OK, message, ex));
	}

	/**
	 * Logs using the default ILog implementation provided by getLog().
	 * 
	 * @param severity
	 *            Severity (IStatus constant) of log entry
	 * @param message
	 *            Human-readable message describing log entry
	 */
	public static void log(final int severity, final String message) {
		log(severity, message, null);
	}

	/**
	 * Logs a message for this plugin
	 * 
	 * @param message
	 * @param t
	 */
	public static void log(final String message, final Throwable t) {
		final ILog log = plugin.getLog();
		log.log(new Status(IStatus.ERROR, plugin.getBundle().getSymbolicName(),
				0, message, t));
	}

	public String getPluginID() {
		return PLUGIN_ID;
	}

	/**
	 * @return the preference store for this bundle 
	 */
	public IPreferenceStore getPreferenceStore() {
		// Create the preference store lazily.
		if (this.preferenceStore == null) {
			this.preferenceStore = new ScopedPreferenceStore(
					new InstanceScope(), getBundle().getSymbolicName());

		}
		return this.preferenceStore;
	}

	/**
	 * @param name
	 * @return the extension point called name for this bundle
	 */
	public IExtensionPoint getExtension(final String name) {
		return Platform.getExtensionRegistry().getExtensionPoint(
				plugin.getBundle().getSymbolicName(), name);
	}

	public IDialogSettings getJaxrsUISettings() {
		return jaxrsUIsettings;
	}

	public void setJAXRSUIDialogSettings(IDialogSettings jaxrsUISettings) {
		if (JAXRSCorePlugin.jaxrsUIsettings == null)
			JAXRSCorePlugin.jaxrsUIsettings = jaxrsUISettings;
	}
}

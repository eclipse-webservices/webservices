/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.performance;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class PerformancePlugin extends Plugin {
	//The shared instance.
	private static PerformancePlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;

  // TODO: These two constants should really be pulled from the plugin which declares it.
  // This will avoid "magic string" dependencies which can be easily broken.
  public static final String WSDL_EDITOR_ID = "org.eclipse.wst.wsdl.ui.internal.WSDLEditor"; //$NON-NLS-1$
  public static final String WSDL_VALIDATOR_ID = "org.eclipse.wst.wsdl.validation.wsdl"; //$NON-NLS-1$
  
  public static final String BUNDLE_ID = "org.eclipse.wst.wsdl.tests.performance";	//$NON-NLS-1$
	
  /**
	 * The constructor.
	 */
	public PerformancePlugin() {
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static PerformancePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = PerformancePlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("org.eclipse.wst.wsdl.tests.performance.PerformancePluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
}

/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IStartup;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ServicePolicyActivatorUI extends Plugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.wst.ws.service.policy.ui"; //$NON-NLS-1$

	// The shared instance
	private static ServicePolicyActivatorUI plugin;
	
	/**
	 * The constructor
	 */
	public ServicePolicyActivatorUI() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns an image descriptor for the named resource
	 * as relative to the plugin install location.
	 * @return An image descriptor, possibly null.
	 */
		public static ImageDescriptor getImageDescriptor(String name) {
			try {
				URL installURL = plugin.getBundle().getEntry("/"); //$NON-NLS-1$
				URL imageURL = new URL(installURL, name);
				return ImageDescriptor.createFromURL(imageURL);
			} catch (MalformedURLException e) {
	     return null;
	   }
	 }

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ServicePolicyActivatorUI getDefault() {
		return plugin;
	}
	
	/**
	 * Logs error message for this plugin.
	 * 
	 * @param message
	 * @param exc
	 */
  public static void logError( String message, Throwable exc )
  {
    IStatus status = new Status( IStatus.ERROR, PLUGIN_ID, 0, message, exc );
    
    getDefault().getLog().log(status);
  }

  /**
   * Called by Eclipse when it starts up.
   */
  public void earlyStartup()
  {
    // DO nothing.  All we want to do is get the plugin loaded at UI start time.
  }
}

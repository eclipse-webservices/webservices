/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.test;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ServicePolicytTestActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.wst.service.policy.test";

	// The shared instance
	private static ServicePolicytTestActivator plugin;
	
	/**
	 * The constructor
	 */
	public ServicePolicytTestActivator() {
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
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ServicePolicytTestActivator getDefault() {
		return plugin;
	}
	
  public static void logError( String message, Throwable exc )
  {
    IStatus status = new Status( IStatus.ERROR, PLUGIN_ID, 0, message, exc );
    
    getDefault().getLog().log(status);
  }
}

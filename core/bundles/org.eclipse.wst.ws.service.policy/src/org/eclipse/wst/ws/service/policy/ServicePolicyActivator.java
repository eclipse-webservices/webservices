/*******************************************************************************
 * Copyright (c) 2007, 2010 IBM Corporation and others.
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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 * 20100407 NPE in org.eclipse.wst.ws.service.policy.ServicePolicyActivator.logError(ServicePolicyActivator.java:75) ericdp@ca.ibm.com - Eric D. Peters, 308427
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ServicePolicyActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.wst.ws.service.policy"; //$NON-NLS-1$

	// The shared instance
	private static ServicePolicyActivator plugin;
	
	/**
	 * The constructor
	 */
	public ServicePolicyActivator() {
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
	public static ServicePolicyActivator getDefault() {
		return plugin;
	}
	
	/**
	 * Logs an error with this plugin.
	 * 
	 * @param message
	 * @param exc
	 */
  public static void logError( String message, Throwable exc )
  {
    IStatus status = new Status( IStatus.ERROR, PLUGIN_ID, 0, message, exc );
    
    ServicePolicyActivator spa = getDefault();
		if (spa != null) {
			ILog iLog = spa.getLog();
			if (iLog != null)
				iLog.log(status);
		}
    }
}

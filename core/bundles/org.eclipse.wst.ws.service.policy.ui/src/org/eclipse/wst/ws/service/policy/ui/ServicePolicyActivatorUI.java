/*******************************************************************************
 * Copyright (c) 2007, 2011 IBM Corporation and others.
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
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 * 20091009   277198 mahutch@ca.ibm.com - Mark Hutchinson, Remove IStartup code
 * 20110824   339317 kchong@ca.ibm.com - Keith Chong - ForbiddenReference in wsdl UI wizard
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ServicePolicyActivatorUI extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.wst.ws.service.policy.ui"; //$NON-NLS-1$
	public static final String WSI_COMPLIANCE_PROPERTY_PAGE_ID = "org.eclipse.wst.ws.internal.ui.wsi.properties.WSICompliancePropertyPage"; //$NON-NLS-1$

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
}

/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * WSO2 Inc - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070130   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the 
 * 											Axis2 runtime to the framework for 168762
 * 20070502   184302 sandakith@wso2.com - Lahiru Sandakith, Fix copyright for Axis2 plugins
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis2.consumption.ui.plugin;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class WebServiceAxis2ConsumptionUIPlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.jst.ws.axis2.consumption.ui";

	// The shared instance
	private static WebServiceAxis2ConsumptionUIPlugin plugin;
	
	/**
	 * The constructor
	 */
	public WebServiceAxis2ConsumptionUIPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static WebServiceAxis2ConsumptionUIPlugin getDefault() {
		return plugin;
	}

}

/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils.internal.plugin;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.IStartup;
import org.osgi.framework.BundleContext;

public class JaxwsUtilsPlugin extends Plugin implements IStartup
{
	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.jst.ws.jaxws.utils";

	// The shared instance
	private static JaxwsUtilsPlugin plugin;
	
	/**
	 * The constructor
	 */
	public JaxwsUtilsPlugin() {
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
	public static JaxwsUtilsPlugin getDefault() {
		return plugin;
	}

	public void earlyStartup()
	{
		// Nothing to do. However, this pugin has to start as early as possible since its services will be used for common logging
	}
}

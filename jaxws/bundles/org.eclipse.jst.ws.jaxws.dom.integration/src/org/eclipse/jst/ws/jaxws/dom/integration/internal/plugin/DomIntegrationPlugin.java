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
package org.eclipse.jst.ws.jaxws.dom.integration.internal.plugin;

import org.eclipse.jst.ws.jaxws.dom.integration.navigator.DOMAdapterFactoryLabelProvider;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class DomIntegrationPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.jst.ws.jaxws.dom.integration";

	// The shared instance
	private static DomIntegrationPlugin plugin;
	
	private DOMAdapterFactoryLabelProvider labelProvider;
	
	/**
	 * The constructor
	 */
	public DomIntegrationPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
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
	public static DomIntegrationPlugin getDefault() {
		return plugin;
	}

	public void setLabelProvider(final DOMAdapterFactoryLabelProvider labelProvider)
	{
		ContractChecker.nullCheckParam(labelProvider, "labelProvider"); //$NON-NLS-1$
		this.labelProvider = labelProvider;
	}
	
	public DOMAdapterFactoryLabelProvider getLabelProvider()
	{
		return labelProvider;
	}
	
}

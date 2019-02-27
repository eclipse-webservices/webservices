/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
 * 20070502  185208 sengpl@ca.ibm.com - Seng Phung-Lu      
 *******************************************************************************/
package org.eclipse.jst.ws.tests.performance.plugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class PerformancePlugin extends AbstractUIPlugin {
	//The shared instance.
	private static PerformancePlugin plugin;

	
	/**
	 * The constructor.
	 */
	public PerformancePlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static PerformancePlugin getDefault() {
		return plugin;
	}

}

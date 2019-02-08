/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.command.internal.env.plugin;

import org.eclipse.core.runtime.Plugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class EnvPlugin extends Plugin {

	//The shared instance.
	private static EnvPlugin instance;

	/**
	  * The identifier of the descriptor of this plugin in plugin.xml.
	  */
	  public static final String ID = "org.eclipse.wst.command.env";	
	
	/**
	 * The constructor.
	 */
	public EnvPlugin() {
		super();
		instance = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static EnvPlugin getInstance() {
		return instance;
	}
}

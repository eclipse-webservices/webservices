/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.wst.command.internal.env.ui.plugin;

import org.eclipse.core.runtime.Plugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class EnvUIPlugin extends Plugin {

	//The shared instance.
	private static EnvUIPlugin instance;

	/**
	 * The constructor.
	 */
	public EnvUIPlugin() {
		super();
		instance = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static EnvUIPlugin getInstance() {
		return instance;
	}
}

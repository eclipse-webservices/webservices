/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer;

/**
 * @author cbrealey@ca.ibm.com
 * 
 * This simple class is used to hold a single Web Services
 * Explorer launch option property and its value. Allowed
 * launch option properties are defined by
 * <code>{@link LaunchOptions}</code>.
 */
public class LaunchOption {
	private String key_;

	private String option_;

	/*
	 * Constructs a new launch option with the given
	 * property name and value. 
	 */
	public LaunchOption(String key, String option) {
		key_ = key;
		option_ = option;
	}

	/**
	 * Returns the property name of this launch option.
	 * @return The property name.
	 */
	public String getKey() {
		return key_;
	}

	/**
	 * Returns the property value of this launch option.
	 * @return The property value.
	 */
	public String getOption() {
		return option_;
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.core;

import org.eclipse.osgi.util.NLS;

public final class EnvironmentCoreMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.wst.command.internal.env.core.EnvironmentCore";//$NON-NLS-1$

	private EnvironmentCoreMessages() {
		// Do not instantiate
	}

	public static String MSG_ERROR_UNEXPECTED_ERROR;
	public static String TITLE_WARNING;
	public static String TITLE_ERROR;
	public static String TITLE_INFO;

	static {
		NLS.initializeMessages(BUNDLE_NAME, EnvironmentCoreMessages.class);
	}
}
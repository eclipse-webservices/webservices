/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal;

import org.eclipse.osgi.util.NLS;

public final class WstWSPluginMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.wst.ws.internal.WstWSPlugin";//$NON-NLS-1$

	private WstWSPluginMessages() {
		// Do not instantiate
	}

	public static String MSG_ERROR_UNABLE_TO_START_MONITOR;
	public static String IGNORE_LABEL;
	public static String IGNORE_DESCRIPTION;
	public static String IGNORE_ALL_LABEL;
	public static String IGNORE_ALL_DESCRIPTION;
	public static String CANCEL_LABEL;
	public static String CANCEL_DESCRIPTION;

	static {
		NLS.initializeMessages(BUNDLE_NAME, WstWSPluginMessages.class);
	}
}
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
package org.eclipse.jst.ws.internal;

import org.eclipse.osgi.util.NLS;

public final class WSPluginMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.internal.WSPlugin";//$NON-NLS-1$

	private WSPluginMessages() {
		// Do not instantiate
	}

	public static String MSG_ERROR_SERVER;
	public static String ANY_FILTER_NAME;
	public static String ANY_FILTER_DESC;

	static {
		NLS.initializeMessages(BUNDLE_NAME, WSPluginMessages.class);
	}
}
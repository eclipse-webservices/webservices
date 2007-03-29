/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060420   120714 kathy@ca.ibm.com - Kathy Chan
 * 20070125   171071 makandre@ca.ibm.com - Andrew Mak
 *******************************************************************************/
package org.eclipse.wst.ws.internal;

import org.eclipse.osgi.util.NLS;

public final class WstWSPluginMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.wst.ws.internal.WstWSPlugin";//$NON-NLS-1$

	private WstWSPluginMessages() {
		// Do not instantiate
	}

	public static String MSG_ERROR_UNABLE_TO_START_MONITOR;
	public static String MSG_UNABLE_TO_RESOLVE_PLATFORM_URI;
	public static String MSG_UNABLE_TO_RESOLVE_HTTP;
	public static String IGNORE_LABEL;
	public static String IGNORE_DESCRIPTION;
	public static String IGNORE_ALL_LABEL;
	public static String IGNORE_ALL_DESCRIPTION;
	public static String CANCEL_LABEL;
	public static String CANCEL_DESCRIPTION;
	public static String WSI_SSBP_ERROR;
	public static String WSI_SSBP_WARNING;
	public static String WSI_SSBP_INCOMPLIANT_RUNTIME;
	public static String WSI_AP_ERROR;
	public static String WSI_AP_WARNING;	
	public static String WSI_AP_INCOMPLIANT_RUNTIME;
	
	public static String MSG_ERROR_COPY_WSDL;
	public static String MSG_ERROR_TARGET_FOLDER_NOT_SPECIFIED;
	public static String MSG_ERROR_TARGET_FOLDER_NOT_FOUND;

	static {
		NLS.initializeMessages(BUNDLE_NAME, WstWSPluginMessages.class);
	}
}

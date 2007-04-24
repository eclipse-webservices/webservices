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
 * 20060728   151632 kathy@ca.ibm.com - Kathy Chan
 * 20070424   182376 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.internal.ui;

import org.eclipse.osgi.util.NLS;

public final class WstWSUIPluginMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.wst.ws.internal.ui.WstWSUIPlugin";//$NON-NLS-1$

	private WstWSUIPluginMessages() {
		// Do not instantiate
	}

	public static String PLUGIN_NAME;
	public static String PLUGIN_VENDOR;
	public static String PLUGIN_DESC;
	public static String PLUGIN_NEW_CATEGORY_NAME_WS;
	public static String PREFERENCE_CATEGORY_WSI;
	public static String STOP_NON_WSI;
	public static String WARN_NON_WSI;
	public static String IGNORE_NON_WSI;
	public static String FOLLOW_WSI_PREFERENCE;
	public static String TOOLTIP_PWSI_PAGE;
	public static String TOOLTIP_PWSI_RADIO_STOP_NON_WSI;
	public static String TOOLTIP_PWSI_RADIO_WARNING_NON_WSI;
	public static String TOOLTIP_PWSI_RADIO_IGNORE_NON_WSI;
	public static String TOOLTIP_PWSI_RADIO_FOLLOW_WSI_PREFERENCE;
	public static String LABEL_WSI_SSBP;
	public static String TOOLTIP_PWSI_SSBP_LABEL;
	public static String TOOLTIP_PWSI_SSBP_COMBO;
	public static String LABEL_WSI_AP;
	public static String TOOLTIP_PWSI_AP_LABEL;
	public static String TOOLTIP_PWSI_AP_COMBO;
	public static String LABEL_WSDLVAL;
	public static String LABEL_WSDLVAL_NONE;
	public static String LABEL_WSDLVAL_REMOTE;
	public static String LABEL_WSDLVAL_ALL;
	public static String TOOLTIP_PWSI_RADIO_WSDLVAL_NONE;
	public static String TOOLTIP_PWSI_RADIO_WSDLVAL_REMOTE;
	public static String TOOLTIP_PWSI_RADIO_WSDLVAL_ALL;
	public static String TOOLTIP_PWSI_WSDLVAL_LABEL;
  public static String WEBSERVICE_CATEGORY_PREF;

	static {
		NLS.initializeMessages(BUNDLE_NAME, WstWSUIPluginMessages.class);
	}
}

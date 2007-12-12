/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071025 196997   ericdp@ca.ibm.com - Eric Peters
 * 20071107 196997   ericdp@ca.ibm.com - Eric Peters
 * 20071120   209858 ericdp@ca.ibm.com - Eric Peters, Enhancing service policy framework and UI
 * 20071212   209858 ericdp@ca.ibm.com - Eric Peters, Enhancing service policy framework and UI
 *******************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui;

import org.eclipse.osgi.util.NLS;

public final class WstSPUIPluginMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.wst.ws.internal.service.policy.ui.WstSPUIPluginMessages";//$NON-NLS-1$

	private WstSPUIPluginMessages() {
		// Do not instantiate
	}

	public static String LABEL_SERVICEPOLICIES_DESCRIPTION;
	public static String LABEL_SERVICEPOLICIES_DEPENDENCIES;
	public static String TOOLTIP_PSP_DESCRIPTION;
	public static String TOOLTIP_PSP_DEPENDENCIES;
	public static String TOOLTIP_PSP_TREE;
	public static String TOOLTIP_PSP_DETAILSTREE;
	public static String SERVICEPOLICIES_DEPENDENCIES;
	public static String SERVICEPOLICIES_DEPENDENCY_ERROR;
	public static String SERVICEPOLICIES_DEPENDENCIES_NONE;
	public static String LINK_CONFIGWORKSPACE_SETTINGS;
	public static String LABEL_ENABLE_PROJECT_SETTINGS;
	public static String LABEL_QFLINK;
	public static String TEXT_QFIX_NOSUG;
	public static String TEXT_DETAILS;


	static {
		NLS.initializeMessages(BUNDLE_NAME, WstSPUIPluginMessages.class);
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060227   124392 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060329   128827 kathy@ca.ibm.com - Kathy Chan
 * 20060329   124667 kathy@ca.ibm.com - Kathy Chan
 * 20060331   128827 kathy@ca.ibm.com - Kathy Chan
 * 20060515   141398 cbrealey@ca.ibm.com - Chris Brealey
 * 20080324   220983 trungha@ca.ibm.com - Trung Ha
 *******************************************************************************/
package org.eclipse.jst.ws.internal.ui;

import org.eclipse.osgi.util.NLS;

public final class WSUIPluginMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.internal.ui.WSUIPlugin";//$NON-NLS-1$

	private WSUIPluginMessages() {
		// Do not instantiate
	}

	public static String PLUGIN_NAME;
	public static String PLUGIN_VENDOR;
	public static String PLUGIN_DESC;
	public static String PLUGIN_NEW_CATEGORY_NAME_WS;
	public static String PREFERENCE_CATEGORY_RESOURCE_MANAGEMENT;
	public static String PREFERENCE_CATEGORY_CODE_GENERATION;
	public static String PREFERENCE_CATEGORY_SCENARIO_DEFAULTS;
	public static String PREFERENCE_CATEGORY_PROJECT_TOPOLOGY;
	public static String PREFERENCE_CATEGORY_DRIVER_JARS;
	public static String BUTTON_LAUNCH_SAMPLE;
	public static String LABEL_MOVE_UP;
	public static String LABEL_MOVE_DOWN;
	public static String LABEL_MOVE_UP_2;
	public static String LABEL_MOVE_DOWN_2;
	public static String TOOLTIP_PPSD_PAGE;
	public static String TOOLTIP_PPSD_CHECKBOX_LAUNCH_SAMPLE;
	public static String TOOLTIP_CLIENT_TYPE_TABLE_VIEWER;
    public static String TOOLTIP_SERVICE_TYPE_TABLE_VIEWER;
	public static String TOOLTIP_MOVE_UP;
	public static String TOOLTIP_MOVE_DOWN;
	public static String TOOLTIP_PPRM_PAGE;
	public static String TOOLTIP_PPRM_CHECKBOX_OVERWRITE_FILES;
	public static String TOOLTIP_PPRM_CHECKBOX_CREATE_FOLDERS;
	public static String TOOLTIP_PPRM_CHECKBOX_CHECK_OUT;
	public static String TOOLTIP_PPRM_CHECKBOX_SKELETON_MERGE;
	public static String BUTTON_CREATE_FOLDERS;
	public static String BUTTON_OVERWRITE_FILES;
	public static String BUTTON_CHECKOUT_FILES;
	public static String BUTTON_SKELETON_MERGE;
	public static String ISTATUS_WITHOUT_EXCEPTION;
	public static String ISTATUS_WITH_EXCEPTION;
	public static String MSG_INFO_SAVED_STATUS;
	public static String DIALOG_TITLE_CLASS_BROWSE;
	public static String LABEL_CLASS_BROWSE;
	public static String DIALOG_TITLE_INTERFACE_BROWSE;
	public static String LABEL_SAMPLE_TYPES;
	public static String DIALOG_TITLE_RESOURCE_BROWSE;
	public static String LABEL_RESOURCE_FILTER;
	public static String TOOLTIP_DRES_COMBO_RESOURCE_TYPE;
	public static String TOOLTIP_DRES_TREE_RESOURCE;
	public static String TOOLTIP_PTPP_PAGE;
	public static String TOOLTIP_ENABLE_TWO_EARS;
	public static String LABEL_ENABLE_TWO_EARS;
	public static String LABEL_CLIENT_TYPE_NAME;
	public static String DIALOG_TITLE_RESOURCE_BROWSER;
	public static String TOOLTIP_RESOURCE_TREE;
    public static String LABEL_SERVICE_TYPE_NAME;

	static {
		NLS.initializeMessages(BUNDLE_NAME, WSUIPluginMessages.class);
	}
}

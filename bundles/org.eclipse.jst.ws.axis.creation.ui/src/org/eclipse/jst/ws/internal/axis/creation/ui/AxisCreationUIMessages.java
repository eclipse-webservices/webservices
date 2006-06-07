/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060329	 128827 kathy@ca.ibm.com  - Kathy Chan
 * 20060329	 124667 kathy@ca.ibm.com  - Kathy Chan
 * 20060607  144978 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui;

import org.eclipse.osgi.util.NLS;

public final class AxisCreationUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUI";//$NON-NLS-1$

	private AxisCreationUIMessages() {
		// Do not instantiate
	}

	public static String MSG_ERROR_PROJECT_URL;
	public static String MSG_ERROR_CANNOT_NO_JAVA_BEAN;
	public static String MSG_ERROR_NO_PROJECT;
	public static String MSG_ERROR_CANNOT_LOAD_JAVA_BEAN;
	public static String MSG_ERROR_JAVA_MOF_REFLECT_FAILED;
	public static String MSG_ERROR_READ_BEAN;
	public static String MSG_ERROR_UPDATE_AXIS_WSDD;
	public static String MSG_ERROR_JAVA_TO_METHOD;
	public static String MSG_ERROR_UPDATE_WEB_XML;
	public static String MSG_PLUGIN_FILE_URL;
	public static String PAGE_MSG_NO_FILE_SPECIFIED;
	public static String PAGE_MSG_NO_METHOD_SELECTED;
	public static String TOOLTIP_PBSC_PAGE;
	public static String TOOLTIP_PBCF_TEXT_URI;
	public static String TOOLTIP_PBCF_TEXT_WSDL_FOLDER;
	public static String TOOLTIP_PBCF_TEXT_WSDL_FILE;
	public static String LABEL_URI;
	public static String LABEL_OUTPUT_FOLDER_NAME;
	public static String LABEL_OUTPUT_FILE_NAME;
	public static String PAGE_TITLE_WSBEAN_CLASS;
	public static String PAGE_DESC_WSBEAN_CLASS;
	public static String LABEL_EXPLORE_MAPPINGS_BEAN2XML;
	public static String TOOLTIP_P2N_SHOW_MAPPINGS;
	public static String TOOLTIP_PBCF_PAGE;
	public static String TOOLTIP_PBME_TREE_METHODS;
	public static String PAGE_TITLE_WSBEAN_CONFIG;
	public static String PAGE_DESC_WSBEAN_CONFIG;
	public static String LABEL_STYLE_USE;
	public static String STYLE_RPC_LITERAL;
	public static String STYLE_DOC_LITERAL;
	public static String STYLE_RPC_ENCODED;
	public static String MSG_ERROR_COMPILER_LEVEL_NOT_COMPATIBLE;
	public static String MSG_ERROR_MODIFY_ENDPOINT;
	public static String MSG_ERROR_SKELETON_MERGE;
	public static String WSI_INCOMPLIANCE_RPC_ENCODED;

	static {
		NLS.initializeMessages(BUNDLE_NAME, AxisCreationUIMessages.class);
	}
}

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
package org.eclipse.jst.ws.internal.axis.consumption.ui;

import org.eclipse.osgi.util.NLS;

public final class AxisConsumptionUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUI";//$NON-NLS-1$

	private AxisConsumptionUIMessages() {
		// Do not instantiate
	}

	public static String MSG_ERROR_DEFAULT_BEAN;
	public static String MSG_ERROR_WSDL_NO_DEFINITION;
	public static String MSG_ERROR_WSDL_NO_PORT;
	public static String MSG_ERROR_WRITE_WSDL;
	public static String MSG_ERROR_IMPORT_WSDL;
	public static String MSG_ERROR_WSDL_LOCATION_NOT_SET;
	public static String MSG_WARN_NO_JAVA_NATURE;
	public static String MSG_ERROR_BAD_BUILDPATH;
	public static String PROGRESS_INFO_COPY_AXIS_CFG;
	public static String MSG_ERROR_FILECOPY;
	public static String MSG_ERROR_REFRESH_PROJECT;
	public static String TOOLTIP_PWJB_PAGE;
	public static String TOOLTIP_PWJB_TEXT_FOLDER;
	public static String TOOLTIP_PWJB_CHECKBOX_GENPROXY;
	public static String PAGE_TITLE_WS_AXIS_PROXY;
	public static String PAGE_DESC_WS_AXIS_PROXY;
	public static String CHECKBOX_GENPROXY;
	public static String LABEL_FOLDER_NAME;
	public static String PAGE_TITLE_WS_BEAN2XML;
	public static String PAGE_TITLE_WS_XML2PROXY;
	public static String PAGE_DESC_P2N_MAPPINGS;
	public static String LABEL_MAPPING_PAIRS;
	public static String TABLE_COLUMN_LABEL_PACKAGE;
	public static String TABLE_COLUMN_LABEL_NAMESPACE;
	public static String LABEL_EXPLORE_MAPPINGS_XML2BEAN;
	public static String TOOLTIP_N2P_SHOW_MAPPINGS;
	public static String MSG_ERROR_XML_FILE_OVERWRITE_DISABLED;

	static {
		NLS.initializeMessages(BUNDLE_NAME, AxisConsumptionUIMessages.class);
	}
}
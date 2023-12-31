/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070412   177005 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.core;

import org.eclipse.osgi.util.NLS;

public final class AxisConsumptionCoreMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCore";//$NON-NLS-1$

	private AxisConsumptionCoreMessages() {
		// Do not instantiate
	}

	public static String MSG_ERROR_PROJECT_URL_PARAM_NOT_SET;
	public static String MSG_ERROR_DEPLOY_FILE_PARAM_NOT_SET;
	public static String MSG_ERROR_AXIS_DEPLOY;
	public static String MSG_AXIS_DEPLOY;
	public static String MSG_GENERATE_WSDL;
	public static String MSG_ERROR_JAVA_WSDL_GENERATE;
	public static String MSG_PARSING_WSDL;
	public static String MSG_ERROR_WSDL_JAVA_GENERATE;
	public static String MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET;
	public static String MSG_ERROR_MOVE_RESOURCE;
	public static String MSG_ERROR_CREATE_TEMP_DIR;
  public static String MSG_ERROR_FOLDER_NOT_FOUND;
  
	static {
		NLS.initializeMessages(BUNDLE_NAME, AxisConsumptionCoreMessages.class);
	}
}

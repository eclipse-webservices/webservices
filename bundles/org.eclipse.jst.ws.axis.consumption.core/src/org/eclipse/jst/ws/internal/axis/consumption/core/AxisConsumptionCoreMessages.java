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

	static {
		NLS.initializeMessages(BUNDLE_NAME, AxisConsumptionCoreMessages.class);
	}
}
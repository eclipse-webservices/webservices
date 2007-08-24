/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070511   186440 sandakith@wso2.com - Lahiru Sandakith fix 186440
 * 20070517   187307 sandakith@wso2.com - Lahiru Sandakith
 * 20070518   187311 sandakith@wso2.com - Lahiru Sandakith, Fixing test resource addition
 * 20070601   190505 pmoogk@ca.ibm.com - Peter Moogk
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.consumption.core.messages;

import org.eclipse.osgi.util.NLS;

public final class Axis2ConsumptionUIMessages extends NLS {

	private static final String BUNDLE_NAME = 
		"org.eclipse.jst.ws.axis2.consumption.core.messages.Axis2ConsumptionUI";//$NON-NLS-1$
	
	private Axis2ConsumptionUIMessages() {
		// Do not instantiate
	}

	public static String LABEL_SERVICE_NAME_CAPTION;
	public static String LABEL_GENERATE_TESTCASE_CAPTION;
	public static String LABEL_DATABINDING_CAPTION;
	public static String LABEL_PORTNAME;
	public static String LABEL_GENERATE_ALL;
	public static String LABEL_PACKEGE_NAME;
	public static String LABEL_NAMESPACE;
	public static String LABEL_PACKAGE;
	public static String LABEL_CLIENT_SIDE;
	public static String LABEL_SYNC_AND_ASYNC;
	public static String LABEL_SYNC;
	public static String LABEL_ASYNC;
	
	public static String PAGE_TITLE_WS_AXIS2_PROXY;
	public static String PAGE_DESC_WS_AXIS2_PROXY;
	
	public static String ERROR_INVALID_FILE_READ_WRITEL;
	public static String ERROR_INVALID_WSDL_FILE_READ_WRITEL;
	public static String ERROR_CODEGEN_EXCEPTION;
	public static String ERROR_JUNIT_JAR_NOT_FOUND;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Axis2ConsumptionUIMessages.class);
	}
}

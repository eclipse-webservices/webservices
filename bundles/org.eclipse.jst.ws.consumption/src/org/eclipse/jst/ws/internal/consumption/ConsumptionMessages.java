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
 * 20060217   126757 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060401   128827 kathy@ca.ibm.com - Kathy Chan
 * 20080402   224433 kathy@ca.ibm.com - Kathy Chan, Properties file change
 * 20080505   182167 makandre@ca.ibm.com - Andrew Mak, Warning not issued when non-instantiable class is bypassed in sampe JSPs
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption;

import org.eclipse.osgi.util.NLS;

public final class ConsumptionMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.internal.consumption.Consumption";//$NON-NLS-1$

	private ConsumptionMessages() {
		// Do not instantiate
	}

	public static String MSG_ERROR_JTS_PROXY_HAS_NO_METHODS;
	public static String MSG_ERROR_JTS_PROXY_HAS_NO_DEFAULT;
	public static String MSG_ERROR_JTS_NO_PROXY_METHODS_PROCESSED;
	public static String MSG_WARN_JTS_UNSUPPORTED_TYPE;
	public static String MSG_WARN_JTS_UNSUPPORTED_INDEXED_PROPERTIES;
	public static String MSG_WARN_JTS_UNSUPPORTED_PARAMETERS_ARRAYS;
	public static String MSG_WARN_JTS_UNSUPPORTED_PARAMETERS_INPUTS;
	public static String MSG_ERROR_JTS_CYCLIC_BEAN;
	public static String MSG_WARN_JTS_PROXY_METHODS_OMITTED;
	public static String MSG_ERROR_SAMPLE_CREATION_CANCELED;
	public static String MSG_WARN_UNABLE_TO_FIND_PROXY;
	public static String MSG_ERROR_JTS_JSP_GEN;
	public static String MSG_WARN_JTS_NON_INSTANTIABLE_TYPE;
	public static String FILTER_MSG_ERROR_NULL_OBJECT;
	public static String FILTER_MSG_ERROR_NOT_FILE;
	public static String FILTER_MSG_ERROR_NOT_FOLDER;
	public static String JAVA_FILTER_NAME;
	public static String JAVA_FILTER_DESC;
	public static String JAVA_FILTER_MSG_ERROR_WRONG_EXTENSION;
	public static String WSDL_FILTER_NAME;
	public static String WSDL_FILTER_DESC;
	public static String WSDL_FILTER_MSG_ERROR_WRONG_EXTENSION;
	public static String ISD_FILTER_NAME;
	public static String ISD_FILTER_DESC;
	public static String ISD_FILTER_MSG_ERROR_WRONG_EXTENSION;
	public static String PROPERTIES_FILTER_NAME;
	public static String PROPERTIES_FILTER_DESC;
	public static String PROPERTIES_FILTER_MSG_ERROR_WRONG_EXTENSION;
	public static String FOLDER_FILTER_NAME;
	public static String FOLDER_FILTER_DESC;
	public static String PROGRESS_INFO_COPY_WEBSERVICE_UTILS;
	public static String PROGRESS_INFO_COPYING_FILE;
	public static String PROGRESS_INFO_START_WEB_PROJECT;
	public static String PROGRESS_INFO_PUBLISHING_SERVER;
	public static String PROGRESS_INFO_STARTING_SERVER;
	public static String PROGRESS_INFO_PUBLISH_WEB_PROJECT;
	public static String MSG_ERROR_PROJECT_NOT_FOUND;
	public static String MSG_ERROR_FILECOPY_WEBSERVICE_UTILS;
	public static String MSG_ERROR_INSTANCE_NOT_FOUND;
	public static String MSG_ERROR_SERVER;
	public static String MSG_ERROR_PUBLISH;
	public static String MSG_ERROR_ADD_MODULE;
	public static String MSG_ERROR_CREATE_SERVER;
	public static String MSG_ERROR_STUB_ONLY;
	public static String TASK_LABEL_CREATE_EJB_PROJECT;
	public static String TASK_DESC_CREATE_EJB_PROJECT;
	public static String MSG_ERROR_CANNOT_CREATE_EJB_PROJECT;
	public static String MSG_ERROR_CANNOT_CREATE_APP_CLIENT_PROJECT;
	public static String MSG_ERROR_CANNOT_CREATE_JAVA_PROJECT;
	public static String MSG_ERROR_CANNOT_CREATE_EAR_PROJECT;
	public static String TASK_LABEL_WEBSERVICE_DISABLE_VALIDATION;
	public static String TASK_DESC_WEBSERVICE_DISABLE_VALIDATION;
	public static String TASK_LABEL_WEBSERVICE_RESTORE_VALIDATION;
	public static String TASK_DESC_WEBSERVICE_RESTORE_VALIDATION;
	public static String TASK_LABEL_WEBSERVICE_DISABLE_BUILD;
	public static String TASK_DESC_WEBSERVICE_DISABLE_BUILD;
	public static String TASK_LABEL_WEBSERVICE_RESTORE_BUILD;
	public static String TASK_DESC_WEBSERVICE_RESTORE_BUILD;
	public static String MSG_ERROR_WEB_PROJECT_CREATE;
	public static String MSG_ERROR_SERVER_VIEW_OPEN;
	public static String MSG_WARN_JAVA_METHOD_START_WITH_UPPER_CASE;
	public static String MSG_WARN_METHOD_NAME_INVALID;
	public static String MSG_WARN_BOOLEAN_PROPERTY_ACCESSORS;
	public static String MSG_WARN_PACKAGE_NAME_HAS_UPPER_CASE;
	public static String WARN_BEAN_NAME_STARTS_WITH_LOWER_CASE;
	public static String LABEL_OK;
	public static String DESCRIPTION_OK;
	public static String LABEL_CANCEL;
	public static String DESCRIPTION_CANCEL;
	public static String MSG_ERROR_UNABLE_TO_START_MONITOR;
	public static String MSG_INFO_MONITORING_NOT_SUPPORTED;
	public static String COMMAND_LABEL_COPY_WSDL;
	public static String COMMAND_DESC_COPY_WSDL;
	public static String MSG_ERROR_COPY_WSDL;
	public static String MSG_ERROR_UNABLE_TO_ASSOCIATE;
	public static String MSG_ERROR_CREATE_FLEX_PROJET;
	public static String MSG_ERROR_CREATE_EJB_COMPONENT;
	public static String MSG_ERROR_CREATE_APPCLIENT_COMPONENT;
	public static String MSG_ERROR_COMPONENT_CREATION;
	public static String MSG_ERROR_PROJECT_CREATION;
    public static String MSG_ERROR_PROJECT_DOES_NOT_EXIST;
    public static String MSG_ERROR_PROJECT_IS_NOT_OPEN;
    public static String MSG_ERROR_ADDING_FACETS_TO_PROJECT;
    public static String MSG_ERROR_FIXED_FACETS;
    public static String MSG_FACETS;
    public static String MSG_ERROR_SETTING_RUNTIME;
    public static String MSG_ERROR_FILE_MERGE_LOAD;
	public static String MSG_ERROR_FILE_MERGE;
    

	static {
		NLS.initializeMessages(BUNDLE_NAME, ConsumptionMessages.class);
	}
}

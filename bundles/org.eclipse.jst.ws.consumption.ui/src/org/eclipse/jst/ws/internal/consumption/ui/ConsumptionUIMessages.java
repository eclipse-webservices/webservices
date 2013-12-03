/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060204 124143   rsinha@ca.ibm.com - Rupam Kuehner          
 * 20060204 124408   rsinha@ca.ibm.com - Rupam Kuehner     
 * 20060221 119111   rsinha@ca.ibm.com - Rupam Kuehner
 * 20060329 128069   rsinha@ca.ibm.com - Rupam Kuehner
 * 20060404 134913   sengpl@ca.ibm.com - Seng Phung-Lu 
 * 20060411   136134 kathy@ca.ibm.com - Kathy Chan
 * 20060417   136390/136159 joan@ca.ibm.com - Joan Haggarty
 * 20060413   135581 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060418   136335 joan@ca.ibm.com - Joan Haggarty
 * 20060420   136158 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060420   120714 kathy@ca.ibm.com - Kathy Chan
 * 20060420   135912 joan@ca.ibm.com - Joan Haggarty
 * 20060426   138647 joan@ca.ibm.com - Joan Haggarty
 * 20060510   141115 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060509   119296 pmoogk@ca.ibm.com - Peter Moogk
 * 20060719   139977 kathy@ca.ibm.com - Kathy Chan
 * 20071031   140518 joan@ca.ibm.com - Joan Haggarty
 * 20080318   213330 trungha@ca.ibm.com - Trung, Non-conventional Java naming prevents creating Web Services (client)
 * 20080326   171705 trungha@ca.ibm.com - Trung, improve AntTask errors report
 * 20080325   184761 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080416   215084 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080425   221232 gilberta@ca.ibm.com - Gilbert Andrews
 * 20100511   309395 mahutch@ca.ibm.com - Mark Hutchinson, WS Wizard Converting Java Project into Utility Project without any warning
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui;

import org.eclipse.osgi.util.NLS;

public final class ConsumptionUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUI";//$NON-NLS-1$

	private ConsumptionUIMessages() {
		// Do not instantiate
	}

	public static String PLUGIN_NEW_WIZARD_NAME_WS_CLIENT;
	public static String ACTION_GENERATE_JAVA_PROXY;
	public static String CHECKBOX_SHOW_GENERATE_JAVA_PROXY_DIALOG;
	public static String TOOLTIP_PPAD_CHECKBOX_WSDL2PROXY;
	public static String TOOLTIP_SRPF_COMBO_SERVER;
	public static String TOOLTIP_SRPF_COMBO_RUNTIME;
	public static String TOOLTIP_SRPF_COMBO_J2EE;
	public static String MSG_ERROR_TASK_EXCEPTED;
	public static String MSG_ERROR_NO_SERVER_RUNTIME;
	public static String MSG_ERROR_NO_SERVER_RUNTIME_INSTALLED;
	public static String LABEL_FOLDER_NAME;
	public static String LABEL_JSP_FOLDER_NAME;
	public static String LABEL_WEB_PROJECT_NAME;
	public static String LABEL_WEBSERVICETYPE;
	public static String LABEL_WEBSERVICEIMPL;
	public static String LABEL_WEBSERVICEDEF;
	public static String LABEL_J2EE_VERSION;
	public static String LABEL_NA;
	public static String BUTTON_BROWSE;
	public static String BUTTON_BROWSE_FILES;
	public static String BOTTOMUP_LABEL;
	public static String TOPDOWN_LABEL;
	public static String CLIENT_LABEL;
	public static String COMBINED_TYPE_AND_RUNTIME_LABEL;
	public static String WIZARD_TITLE_WSC;
	public static String PAGE_TITLE_WS_PROJECT;
	public static String PAGE_DESC_WS_SERVICE;
	public static String PAGE_DESC_WS_CLIENT;
	public static String BUTTON_START_WEB_PROJECT;
	public static String BUTTON_INSTALL_SERVICE_WEB_PROJECT;
	public static String BUTTON_INSTALL_CLIENT_WEB_PROJECT;
	public static String GROUP_SCENARIO_SERVICE;
	public static String GROUP_SCENARIO_CLIENT;
	public static String LABEL_WEBSERVICECLIENTTYPE;
	public static String PAGE_TITLE_WS_RUNTIME_SELECTION;
	public static String PAGE_DESC_WS_RUNTIME_SELECTION;
	public static String LABEL_SUMMARY;
	public static String LABEL_SUMMARY_NO_CLIENT;
	public static String LABEL_RUNTIMES_LIST;
	public static String LABEL_SERVERS_LIST;
	public static String LABEL_SERVERS_INSTANCES;
	public static String LABEL_EDIT_BUTTON;
	public static String LABEL_SERVICE_EAR_MODULE;
	public static String LABEL_CLIENT_EAR_MODULE;
	public static String LABEL_CLIENT_MODULE;
	public static String LABEL_SERVICE_MODULE;
	public static String LABEL_CLIENT_PROJECT;
	public static String LABEL_SERVICE_PROJECT;
	public static String LABEL_CLIENT_EAR_PROJECT;
	public static String LABEL_SERVICE_EAR_PROJECT;
	public static String LABEL_CLIENT_COMP_TYPE_WEB;
	public static String LABEL_CLIENT_COMP_TYPE_EJB;
	public static String LABEL_CLIENT_COMP_TYPE_APP_CLIENT;
	public static String LABEL_CLIENT_COMP_TYPE_CONTAINERLESS;
	public static String TOOLTIP_PWRS_TEXT_RUNTIME;
	public static String TOOLTIP_PWRS_TEXT_SERVER;
	public static String TOOLTIP_PWRS_J2EE_VERSION;
	public static String LABEL_SELECTION_VIEW_TITLE;
	public static String LABEL_SELECTION_VIEW_RUNTIME;
	public static String LABEL_SELECTION_VIEW_SERVER;
	public static String LABEL_SELECTION_VIEW_EXPLORE;
	public static String LABEL_TREE_EXISTING_SERVERS;
	public static String LABEL_TREE_SERVER_TYPES;
	public static String MSG_NO_OBJECT_SELECTION;
	public static String MSG_NO_SERVICE_SELECTION;
	public static String MSG_INVALID_SERVICE_DEF;
	public static String MSG_INVALID_SERVICE_IMPL;
	public static String MSG_INVALID_SRT_SELECTIONS;
	public static String MSG_WARN_SERVICE_IMPL_NAMING_CONVENTION;
	public static String MSG_NO_RUNTIME;
	public static String MSG_NO_SERVER;
	public static String MSG_ERROR_STUB_ONLY;
	public static String MSG_WARN_STUB_ONLY;
    public static String MSG_WARN_NO_SERVICE_SERVER;
    public static String MSG_WARN_NO_CLIENT_SERVER;
	public static String MSG_SERVER_TARGET_MISMATCH;
	public static String MSG_J2EE_MISMATCH;
	public static String MSG_SERVICE_PROJECT_EMPTY;
	public static String MSG_SERVICE_PROJECT_TYPE_EMPTY;
	public static String MSG_SERVICE_EAR_EMPTY;
	public static String MSG_CLIENT_PROJECT_EMPTY;
	public static String MSG_CLIENT_PROJECT_TYPE_EMPTY;
	public static String MSG_CLIENT_EAR_EMPTY;
	public static String MSG_INVALID_EJB_PROJECT;
	public static String MSG_INVALID_WEB_PROJECT;
	public static String MSG_INVALID_PROJECT_TYPE;
	public static String MSG_MODULE;
	public static String MSG_MODULE_NAME_AND_PROJECT_NAME_NOT_THE_SAME;
    public static String MSG_SERVICE_RUNTIME_DOES_NOT_SUPPORT_PROJECT;
    public static String MSG_SERVICE_SERVER_DOES_NOT_SUPPORT_JAVAPROJECT;
    public static String MSG_SERVICE_SERVER_DOES_NOT_SUPPORT_PROJECT;
    public static String MSG_SERVICE_RUNTIME_DOES_NOT_SUPPORT_TEMPLATE;
    public static String MSG_SERVICE_SERVER_DOES_NOT_SUPPORT_TEMPLATE;
    public static String MSG_CLIENT_RUNTIME_DOES_NOT_SUPPORT_PROJECT;
    public static String MSG_CLIENT_SERVER_DOES_NOT_SUPPORT_PROJECT;
    public static String MSG_CLIENT_RUNTIME_DOES_NOT_SUPPORT_TEMPLATE;
    public static String MSG_CLIENT_SERVER_DOES_NOT_SUPPORT_TEMPLATE;
    public static String MSG_CLIENT_SERVER_DOES_NOT_SUPPORT_PROJECT_JAVA_UTIL_VERSION;    
	public static String MSG_SAME_CLIENT_AND_SERVICE_EARS;
	public static String MSG_SAME_CLIENT_AND_SERVICE_COMPONENTS;
	public static String MSG_SAME_CLIENT_AND_SERVICE_PROJECTS;
	public static String MSG_WRONG_CLIENT_PROJECT_TYPE;
	public static String MSG_CLIENT_SUB;
	public static String MSG_SERVICE_SUB;
	public static String MSG_GENERAL_PROJECT_AND_EAR;
	public static String MSG_EAR_PROJECT_WILL_BE_CREATED;
	public static String MSG_EAR_WILL_BE_ASSOCIATED;
	public static String MSG_PROJECT_WILL_BE_CREATED;
	public static String MSG_EAR_WILL_BE_CREATED;
	public static String MSG_PROJECT_AND_EAR_CREATED;
	public static String PAGE_TITLE_WS_CLIENT_RUNTIME_SELECTION;
	public static String PAGE_DESC_WS_CLIENT_RUNTIME_SELECTION;
	public static String LABEL_CLIENT_SELECTION_VIEW_TITLE;
	public static String LABEL_CLIENT_TYPE;
	public static String LABEL_SERVICE_TYPE;
	public static String LABEL_WEB;
	public static String LABEL_EJB;
	public static String LABEL_NO_LABEL;
	public static String PAGE_MSG_VALIDATION_INTERNAL_ERROR;
	public static String PAGE_TITLE_WS_XML2BEAN;
	public static String PAGE_DESC_N2P_MAPPINGS;
	public static String MSG_MAPPING_DUPLICATE_ENTRIES;
	public static String BUTTON_GENERATE_PROXY;
	public static String PAGE_DESC_WSSKEL_CONFIG;
	public static String PAGE_TITLE_WSSKEL_CONFIG;
	public static String TOOLTIP_PBSC_TEXT_SKELETON_FOLDER;
	public static String TOOLTIP_PBSC_BUTTON_SKELETON_FOLDER_BROWSE;
	public static String LABEL_SKELETON_ROOT_NAME;
	public static String LABEL_BEAN_CLASS_NAME;
	public static String PAGE_MSG_BEAN_CANNOT_BE_EMPTY;
	public static String BUTTON_BROWSE_CLASSES;
	public static String PAGE_TITLE_WSTEST;
	public static String PAGE_DESC_WSTEST;
	public static String CHECKBOX_TEST_WEBSERVICE;
	public static String CHECKBOX_MONITOR_WEBSERVICE;
	public static String MSG_ERROR_UNABLE_TO_LAUNCH_WSDL_TEST;
	public static String BUTTON_LAUNCH_SERVICE_TEST;
	public static String PAGE_TITLE_WS_SAMPLE;
	public static String PAGE_DESC_WS_SAMPLE;
	public static String BUTTON_TEST;
	public static String LABEL_TEST_TYPES;
	public static String BUTTON_RUN_TEST;
	public static String LABEL_METHODS;
	public static String LABEL_JSP_PROJECT_NAME;
	public static String MSG_ERROR_JTS_PROXY_NOT_COMPILED;
	public static String MSG_ERROR_MALFORMED_URL;
	public static String BUTTON_SELECT_ALL;
	public static String BUTTON_DESELECT_ALL;
	public static String TOOLTIP_PBME_BUTTON_SELECT_ALL;
	public static String TOOLTIP_PBME_BUTTON_DESELECT_ALL;
	public static String PAGE_TITLE_WSDL_SELECTION;
	public static String PAGE_DESC_WSDL_SELECTION;
	public static String PAGE_TITLE_WS_PUBLISH;
	public static String PAGE_DESC_WS_PUBLISH;
	public static String PAGE_TITLE_WS_FIND;
	public static String PAGE_DESC_WS_FIND;
	public static String BUTTON_WS_PUBLISH;
	public static String BUTTON_WSWSCEN_PUBLISH;
	public static String BUTTON_WS_PUBLISH_PRIVATE_UDDI;
	public static String BUTTON_WS_FIND;
	public static String BUTTON_WS_FIND_PRIVATE_UDDI;
	public static String LABEL_PUBLIC_UDDI_REGISTRIES;
	public static String LABEL_EJB_BEAN_NAME;
	public static String TABLE_TITLE_EJB_BEAN_NAMES;
	public static String TABLE_TITLE_EJB_PROJECT_NAME;
	public static String LABEL_EAR_PROJECTS;
    public static String LABEL_SHOW_ALL_STATELESS_SESSION_EJBS;
	public static String CHECKBOX_OVERWRITE_FILES;
	public static String DIALOG_TITILE_SERVICE_PROJECT_SETTINGS;
	public static String DIALOG_TITILE_CLIENT_PROJECT_SETTINGS;
	public static String DIALOG_TITILE_SERVICE_IMPL_SELECTION;
	public static String DIALOG_TITILE_SERVICE_DEF_SELECTION;
	public static String TOOLTIP_WSWSCEN_SERVICEPROJECT_LINK;
	public static String TOOLTIP_WSWSCEN_CLIENTPROJECT_LINK;
	public static String TOOLTIP_WSWSCEN_SCALE_DEVELOP;
	public static String TOOLTIP_WSWSCEN_SCALE_ASSEMBLE;
	public static String TOOLTIP_WSWSCEN_SCALE_DEPLOY;
	public static String TOOLTIP_WSWSCEN_SCALE_INSTALL;
	public static String TOOLTIP_WSWSCEN_SCALE_RUN;
	public static String TOOLTIP_WSWSCEN_SCALE_TEST;
	public static String TOOLTIP_WSWSCEN_SCALE_SERVICE;
	public static String TOOLTIP_WSWSCEN_SCALE_CLIENT;
	public static String TOOLTIP_WSWSCEN_SCALE_CLIENT_ONLY;
	public static String TOOLTIP_WSWSCEN_TEXT_IMPL;
	public static String TOOLTIP_WSWSCEN_BUTTON_OVERWRITE_FILES;
	public static String TOOLTIP_WSWSCEN_BUTTON_BROWSE_IMPL;
	public static String TOOLTIP_EAR_PROJECTS;
	public static String TOOLTIP_TABLE_BEAN_NAMES;
	public static String TOOLTIP_PWWS_PAGE;
	public static String TOOLTIP_PBCL_PAGE;
	public static String TOOLTIP_PBCL_TEXT_BEAN_CLASS;
	public static String TOOLTIP_PBCL_BUTTON_BEAN_CLASS_BROWSE;
	public static String TOOLTIP_PBCL_BUTTON_BEAN_RESOURCE_BROWSE;
	public static String TOOLTIP_PSTP_COMBOBOX_TEST;
	public static String TOOLTIP_PSTP_LAUNCH_BUTTON;
	public static String TOOLTIP_PWRS_PAGE;
	public static String TOOLTIP_PWRS_LIST_RUNTIMES;
	public static String TOOLTIP_PWRS_LIST_SERVERS;
	public static String TOOLTIP_PWRS_RADIO_SERVER;
	public static String TOOLTIP_PWRS_RADIO_RUNTIME;
	public static String TOOLTIP_PWRS_RADIO_EXPLORE;
	public static String TOOLTIP_PWCR_COMBO_CLIENT_TYPE;
	public static String TOOLTIP_PWCR_COMBO_SERVICE_TYPE;
	public static String TOOLTIP_PWPR_COMBO_TYPE;
	public static String TOOLTIP_PWPR_GROUP_SCENARIO_SERVICE;
	public static String TOOLTIP_PWPR_CHECKBOX_GENERATE_PROXY;
	public static String TOOLTIP_PWPR_CHECKBOX_START_WEB_PROJECT;
	public static String TOOLTIP_PWPR_CHECKBOX_INSTALL_SERVICE_WEB_PROJECT;
	public static String TOOLTIP_PWPR_CHECKBOX_INSTALL_CLIENT_WEB_PROJECT;
	public static String TOOLTIP_PWPR_CHECKBOX_LAUNCH_WS;
	public static String TOOLTIP_PWPR_CHECKBOX_TEST_SERVICE;
	public static String TOOLTIP_PWPR_CHECKBOX_MONITOR_SERVICE;
	public static String TOOLTIP_PWPR_GROUP_SCENARIO_CLIENT;
	public static String TOOLTIP_PWPR_COMBO_CLIENTTYPE;
	public static String TOOLTIP_PWPB_PAGE;
	public static String TOOLTIP_PWPB_CHECKBOX_WS_LAUNCH;
	public static String PAGE_WSIL_IMPORT;
	public static String TITLE_WSIL_IMPORT;
	public static String DESC_WSIL_IMPORT;
	public static String PAGE_TITLE_WS_SELECTION;
	public static String PAGE_DESC_WS_SELECTION;
	public static String PAGE_MSG_LOADING_WEB_SERVICE_URI;
	public static String PAGE_MSG_INVALID_WEB_SERVICE_URI;
	public static String PAGE_MSG_NO_SUCH_FILE;
	public static String PAGE_MSG_SELECTION_MUST_BE_WSDL;
	public static String PAGE_MSG_INVALID_WSIL_FILE_NAME;
	public static String MSG_ERROR_URI_NOT_RESOLVABLE;
	public static String MSG_ERROR_WSDL_HAS_NO_SERVICE_ELEMENT;
	public static String LABEL_WSIL_URI;
	public static String LABEL_BROWSE;
	public static String LABEL_WSDL;
	public static String LABEL_WSDL_URI;
	public static String LABEL_IMPORT;
	public static String LABEL_ADD;
	public static String LABEL_REMOVE;
	public static String LABEL_WS_SELECTION;
	public static String LABEL_SELECT_WSDL;
	public static String LABEL_VALIDATE_MESSAGES;
	public static String TOOLTIP_WSIL_IMPORT_PAGE;
	public static String TOOLTIP_WSIL_TEXT_WSIL;
	public static String TOOLTIP_WSIL_BUTTON_BROWSE_WSIL;
	public static String TOOLTIP_WSIL_TABLE_WSDL;
	public static String TOOLTIP_PCON_PAGE;
	public static String TOOLTIP_PCON_TEXT_WS;
	public static String TOOLTIP_PCON_BUTTON_BROWSE_WS;
	public static String TOOLTIP_VALIDATE_TEXT_MESSAGE;
	public static String TOOLTIP_TABLE_VALIDATE_MESSAGE;
	public static String TABLE_COLUMN_VALIDATION_SEVERITY;
	public static String TABLE_COLUMN_VALIDATION_LINE;
	public static String TABLE_COLUMN_VALIDATION_COLUMN;
	public static String TABLE_COLUMN_VALIDATION_MESSAGE;
	public static String TOOLTIP_PWSM_PAGE;
	public static String TOOLTIP_PWSM_CHECKBOX_TEST;
	public static String TOOLTIP_PWSM_COMBOBOX_TEST;
	public static String TOOLTIP_PWSM_COMBOBOX_SERVER;
	public static String TOOLTIP_PWSM_COMBOBOX_SERVER_INSTANCE;
	public static String TOOLTIP_PWSM_CHECKBOX_LAUNCH;
	public static String TOOLTIP_PWSM_TEXT_JSP_FOLDER;
	public static String TOOLTIP_PWSM_BUTTON_JSP_FOLDER_BROWSE;
	public static String TOOLTIP_PWSM_BUTTON_SELECT_ALL;
	public static String TOOLTIP_PWSM_BUTTON_DESELECT_ALL;
	public static String TOOLTIP_PWSM_COMBO_PROJECT;
	public static String TOOLTIP_PWSM_TEXT_SAMPLE_FOLDER;
	public static String TOOLTIP_PWSM_TREE_METHODS;
	public static String TOOLTIP_PWSM_EAR_PROJECT;
	public static String DIALOG_TITLE_HTTP_BASIC_AUTH;
	public static String LABEL_URL;
	public static String LABEL_HTTP_BASIC_AUTH_USERNAME;
	public static String LABEL_HTTP_BASIC_AUTH_PASSWORD;
	public static String TOOLTIP_HTTP_BASIC_AUTH_USERNAME;
	public static String TOOLTIP_HTTP_BASIC_AUTH_PASSWORD;
	public static String MSG_ERROR_GENERATE_HANDLER_SKELETON;
	public static String MSG_ERROR_WRITE_FILE;
	public static String LABEL_HANDLERS_CONFIG;
	public static String LABEL_BUTTON_ADD;
	public static String LABEL_BUTTON_REMOVE;
	public static String LABEL_BUTTON_MOVE_UP;
	public static String LABEL_BUTTON_MOVE_DOWN;
	public static String LABEL_BUTTON_GEN_SKELETON;
	public static String LABEL_COMBO_SOURCE_LOC;
	public static String LABEL_COMBO_WS_CLIENT_REF;
	public static String LABEL_COMBO_WS_SERVICE_DESC;
    public static String MSG_TEXT_NUM_OF_SERVICES;
    public static String MSG_TEXT_NUM_OF_CLIENTS;
	public static String LABEL_HANDLER_NAME;
	public static String LABLE_HANDLER_CLASS;
	public static String LABEL_HANDLER_PORT;
	public static String DIALOG_TITLE_WS_ADD_HANDLER;
	public static String LABEL_TEXT_HANDLER_NAME;
	public static String LABEL_TEXT_HANDLER_CLASS;
	public static String LABEL_TEXT_HANDLER_PORT;
	public static String PAGE_TITLE_SERVICE_HDLR_CONFIG;
	public static String PAGE_DESC_SERVICE_HDLR_CONFIG;
	public static String PAGE_TITLE_CLIENT_HDLR_CONFIG;
	public static String PAGE_DESC_CLIENT_HDLR_CONFIG;
    public static String PAGE_DESC_MULTIPLE_SERVICES_CONFIG;
    public static String PAGE_DESC_MULTIPLE_CLIENTS_CONFIG;
	public static String TOOLTIP_EDIT_WS_HANDLERS;
	public static String TOOLTIP_BUTTON_GEN_SKELETON;
	public static String TOOLTIP_COMBO_SOURCE_LOC;
	public static String TOOLTIP_TEXT_HANDLER_NAME;
	public static String TOOLTIP_TEXT_HANDLER_CLASS;
	public static String TOOLTIP_TEXT_HANDLER_PORT;
	public static String TOOLTIP_WS_CLIENT_REF;
	public static String TOOLTIP_WS_SERVICE_DESC;
	public static String MSG_ERROR_UNABLE_TO_OPEN_JAVA_EDITOR;
	public static String MSG_WARN_IS_SERVICE_PROJECT;
	public static String MSG_USER_ABORTED;
	public static String MSG_MISSING_THIRD_PARTY_FILES;
	public static String MSG_WARNING_NO_SERVICE_ELEMENT;
	public static String STILL_VALIDATING_WSDL;
	public static String CANCEL_VALIDATION_LABEL;
	public static String CANCEL_VALIDATION_DESCRIPTION;
	public static String CANCEL_ALL_VALIDATION_LABEL;
	public static String CANCEL_ALL_VALIDATION_DESCRIPTION;
	public static String WAIT_VALIDATION_LABEL;
	public static String WAIT_VALIDATION_DESCRIPTION;
	public static String TASK_LABEL_CHECK_WSDL_VALIDATION;
	public static String TASK_DESC_CHECK_WSDL_VALIDATION;
	public static String MESSAGE_VALIDATE_NO_WSDL;
	public static String MESSAGE_VALIDATE_REMOTE_WSDL;
	public static String MESSAGE_VALIDATE_ALL_WSDL;
	public static String MESSAGE_VALIDATE_IN_PROGRESS;
	public static String ERROR_MESSAGES_IN_VALIDATION;
	public static String WARNING_MESSAGES_IN_VALIDATION;
	public static String WARNING_IF_CONTINUE;
	public static String VALIDATION_COMPLETED;
	public static String TOOLTIP_VALIDATE_TEXT_MESSAGE_SUMMARY;
	public static String LABEL_BUTTON_STOP_WSDL_VALIDATION;
	public static String TOOLTIP_STOP_VALIDATION_BUTTON;
	public static String PAGE_TITLE_WS_START_SERVER;
	public static String PAGE_DESC_WS_START_SERVER;
	public static String LABEL_START_SERVER_TEXT1;
	public static String LABEL_START_SERVER_TEXT2;
	public static String LABEL_START_SERVER_TEXT3;
	public static String LABEL_START_SERVER_TEXT4;
	public static String LABEL_START_SERVER_BUTTON;
	public static String TOOLTIP_START_SERVER_BUTTON;
	public static String TEXT_SERVER_STATUS;
	public static String TEXT_SERVER_MSG;
	public static String TEXT_SERVER_STARTED;
	public static String TEXT_SERVER_STARTING;
	public static String TEXT_SERVER_STOPPED;
	public static String MSG_INFO_ANT_RUNTIME_HEADING;
	public static String MSG_INFO_ANT_SERVER_RUNTIME;
	public static String MSG_INFO_ANT_CLIENT_RUNTIME;
	public static String MSG_INFO_ANT_SERVER_HEADING;
	public static String MSG_INFO_ANT_SERVER;
    public static String MSG_ERROR_WEB_SERVICE_CLIENTS_NOT_FOUND;
    public static String MSG_ERROR_WEB_SERVICES_NOT_FOUND;
    public static String MSG_ERROR_WSDD_NOT_FOUND;
    public static String MSG_CLIENT_CANNOT_ASSOCIATE;
    public static String MSG_SERVICE_CANNOT_ASSOCIATE;
    public static String MSG_PROJECT_MUST_EXIST;
    public static String MSG_ERROR_INVALID_MULTIPLE_SERVICE_SELECT;
    public static String MSG_ERROR_MODULE_DEPENDENCY;
    public static String MSG_INFO_WSDL_OPERATION_TIMED_OUT;
    public static String MSG_SERVER_NOT_FOUND_WARNING;
    public static String MSG_WARN_CONVERTED_TO_UTIL;
    public static String MSG_LOADING_WEB_SERVICE;


    
    
	static {
		NLS.initializeMessages(BUNDLE_NAME, ConsumptionUIMessages.class);
	}
}

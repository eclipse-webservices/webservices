/**********************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html 
 * Contributors:
 *    IBM Corporation - Initial API and implementation
 **********************************************************************/
package org.eclipse.wst.wsi.ui.internal;

import org.eclipse.osgi.util.NLS;
 
/**
 * Translated messages.
 */
public class Messages extends NLS 
{
    //--------------- Validation Wizard ---------------
	public static String VALIDATION_WIZARD_TITLE;

	//--------------- Validation Wizard (WSDL page) ---------------
	public static String WSDL_PAGE_FILE_SHELL_TEXT;
	public static String WSDL_PAGE_FILE_TITLE ;
	public static String WSDL_PAGE_FILE_DESCRIPTION;
	public static String WSDL_PAGE_SELECT_WSDL_FILENAME_HEADING;
	public static String WSDL_PAGE_SELECT_WSDL_FILENAME_EXPL;
	public static String WSDL_PAGE_INCLUDE_WSDL_BUTTON;
	public static String WSDL_PAGE_INCLUDE_WSDL_LABEL;
	public static String WSDL_PAGE_VALID_WSDL_LABEL;
	public static String WSDL_PAGE_WORKBENCH_BUTTON;
	public static String WSDL_PAGE_BROWSE_BUTTON;
	public static String WSDL_PAGE_SEARCH_UDDI_BUTTON;

	//--------------- Validation Wizard (WSDL content page) ---------------
	public static String WSDL_CONTENT_PAGE_SELECT_HEADING;
	public static String WSDL_CONTENT_PAGE_SELECT_EXPL;
	public static String WSDL_CONTENT_PAGE_GROUP_TEXT_ELEMENT;
	public static String WSDL_CONTENT_PAGE_GROUP_TEXT_TYPE;
	public static String WSDL_CONTENT_PAGE_RADIO_PORT;
	public static String WSDL_CONTENT_PAGE_RADIO_BINDING;
	public static String WSDL_CONTENT_PAGE_RADIO_PORT_TYPE;
	public static String WSDL_CONTENT_PAGE_RADIO_OPERATION;
	public static String WSDL_CONTENT_PAGE_RADIO_MESSAGE;
	public static String WSDL_CONTENT_PAGE_LABEL_NAME;
	public static String WSDL_CONTENT_PAGE_LABEL_NAMESPACE;
	public static String WSDL_CONTENT_PAGE_LABEL_PARENT;

	//--------------- Validation Wizard (Log page) ---------------
	public static String LOG_PAGE_SELECT_LOG_FILENAME_HEADING;
	public static String LOG_PAGE_SELECT_LOG_FILENAME_EXPL;

	// Actions
	public static String ACTION_WSI_VALIDATOR;

	//--------------- Errors ---------------
	public static String ERROR_UNABLE_TO_VALIDATE;
	public static String ERROR_VALIDATION_FAILED;
	public static String ERROR_THE_WSIMSG_FILE_IS_NOT_VALID;
	public static String ERROR_PROBLEMS_READING_WSIMSG_FILE;
	public static String ERROR_INVALID_LOG_FILE_EXTENSION;
	public static String WARNING_VALIDATION_WARNINGS_DETECTED;
	public static String WARNING_FILE_ALREADY_EXISTS;

	//--------------- Informational messages ---------------
	public static String INFO_VALIDATION_SUCEEDED;
	public static String INFO_THE_WSIMSG_FILE_IS_VALID;
	public static String INFO_NO_MESSAGES_TO_VALIDATE;

  static 
  {
	NLS.initializeMessages(WSIUIPlugin.PLUGIN_ID + ".internal.Messages", Messages.class);
  }
}

/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070305   117034 makandre@ca.ibm.com - Andrew Mak, Web Services Explorer should support SOAP Headers
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants;

public class WSDLActionInputs
{
  public static final String FRAMESET_COLS_PERSPECTIVE_CONTENT = "framesetColsPerspectiveContent";
  public static final String FRAMESET_ROWS_ACTIONS_CONTAINER = "framesetRowsActionsContainer";

  public static final String SESSION_ID = "sessionID";
  public static final String FRAGMENT_ID = "fragmentID";

  public static final String SOAP_ENVELOPE_TYPE = "soapEnvelopeType";
  public static final int SOAP_ENVELOPE_TYPE_REQUEST = 0;
  public static final int SOAP_ENVELOPE_TYPE_RESPONSE = 1;
  public static final String SOAP_RESPONSE_CACHED = "soapResponseCached";

  public static final String SUBMISSION_ACTION = "submissionAction";
  public static final String SUBMISSION_ACTION_FORM = "submissionActionForm";
  public static final String SUBMISSION_ACTION_BROWSE_FILE_HEADER = "submissionActionBrowseFileHeader";
  public static final String SUBMISSION_ACTION_BROWSE_FILE = "submissionActionBrowseFile";
  public static final String SUBMISSION_ACTION_SAVE_AS_HEADER = "submissionActionSaveAsHeader";
  public static final String SUBMISSION_ACTION_SAVE_AS = "submissionActionSaveAs";
  public static final String SELECTED_FILE_HEADER = "selectedFileHeader";
  public static final String SELECTED_FILE = "selectedFile";
  public static final String OPERATION_ELEMENT = "operationElement";

  public static final String END_POINT = "::endPoint";
  public static final String HTTP_BASIC_AUTH_USERNAME = "httpBasicAuthUsername";
  public static final String HTTP_BASIC_AUTH_PASSWORD = "httpBasicAuthPassword";
}

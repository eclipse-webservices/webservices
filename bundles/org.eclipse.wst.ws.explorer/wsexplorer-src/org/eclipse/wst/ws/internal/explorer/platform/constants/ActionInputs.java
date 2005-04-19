/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.constants;

public class ActionInputs
{
  // URL parm for all actions which are in the history list.
  public static final String ISHISTORY = "isHistory";

  // Utility constant for tree node identification.
  public static final String NODEID = "nodeId";

  // Utility constant for tool identification.
  public static final String TOOLID = "toolId";

  // Utility constant for view identification.
  public static final String VIEWID = "viewId";
  public static final int VIEWID_DEFAULT = -1;

  // Utility constant for view tool identification.
  public static final String VIEWTOOLID = "viewToolId";
  public static final int VIEWTOOLID_DEFAULT = -1;

  // Utility constants for transport classes
  public static final String TRANSPORT_CLASS_NAME = "org.uddi4j.TransportClassName";
  public static final String TRASPORT_CLASS = "org.uddi4j.transport.ApacheAxisTransport";
  //public static final String AXIS_TRANSPORT_CLASS = "org.uddi4j.transport.ApacheAxisTransport";
  //public static final String SOAP_TRANSPORT_CLASS = "org.uddi4j.transport.ApacheSOAPTransport";

  // ToggleNodeAction and its derivatives.
  public static final String OPEN = "open";
  public static final int OPEN_NODE = 1;
  public static final int CLOSE_NODE = 0;

  // ShowPerspectiveAction
  public static final String PERSPECTIVE = "perspective";
  public static final int PERSPECTIVE_UDDI = 0;
  public static final int PERSPECTIVE_WSIL = 1;
  public static final int PERSPECTIVE_WSDL = 2;
  public static final int PERSPECTIVE_FAVORITES = 3;

  // RetrieveHistoryAction
  public static final String JUMP = "jump";
  public static final int JUMP_FORWARD = 1;
  public static final int JUMP_BACK = -1;

  // LaunchWebServiceWizardAction
  public final static String WEB_SERVICE_WIZARD = "webServiceWizard";
  public final static int WEB_SERVICE_CLIENT_WIZARD = 0;
  public final static int WEB_SERVICE_SKELETON_WIZARD = 1;

  // ImportToWorkbenchAction
  public final static String WORKBENCH_PROJECT_NAME = "workbenchProjectName";
  public final static String IMPORT_FILE = "importWSDL";
  public final static String IMPORTED_FILE_NAME = "importedFileName";
  public final static String IMPORT_TO_WSIL = "importToWSIL";
  public final static String WSIL_FILE_NAME = "WSILFileName";
  public final static String IMPORTED_WSDL_URL = "importedWsdlUrl";
  public final static String IMPORT_ACTION = "importAction";

  // ProxyLoadAction
  public final static String TARGET_PAGE = "targetPage";

  // Session ID for pop ups whose browsers choose to ignore the session.
  public final static String SESSIONID = "sId";

  // WSDL Browser
  public static final String PROJECT = "project";
  public static final String WSDL_TYPE = "wsdlType";
  public static final int WSDL_TYPE_SERVICE_INTERFACE = 0;
  public static final int WSDL_TYPE_SERVICE = 1;
  public static final String QUERY_INPUT_WEBPROJECT_WSDL_URL = "webProjectWSDLURL";
  public static final String QUERY_INPUT_FAVORITE_WSDL_URL = "favoriteWSDLURL";
  public static final String QUERY_INPUT_WSDL_URL = "wsdlURL";

  // Calendar Browser
  public static final String CALENDAR_TYPE = "calendarType";
  public static final int CALENDAR_TYPE_DATE = 0;
  public static final int CALENDAR_TYPE_DATETIME = 1;
  public static final int CALENDAR_TYPE_GYEARMONTH = 2;
  public static final int CALENDAR_TYPE_GDAY = 3;
  public static final int CALENDAR_TYPE_GMONTHDAY = 4;

  // ResizeFrameAction
  public static final String FRAME_NAME = "frameName";

  // Other Actions
  public static final String CURRENT_TOOL_MANAGER = "currentToolManager";
  
  // Action Engine
  public static final String ACTION_ENGINE_SCENARIO = "actionEngineScenario";
  public static final String ACTION_ENGINE_MODE = "actionEngineMode";

  // MultipartFormDataParser
  public static final String MULTIPART_FORM_DATA_PARSER = "multipartFormDataParser";
}
/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd;

public class ASDEditorCSHelpIds {
	private static String pluginId = "org.eclipse.wst.wsdl.ui."; //$NON-NLS-1$
	
	/*-------------------------------------------------------------------------------
	New WSDL Wizard - Options page:
	-------------------------------------------------------------------------------*/
	public static String WSDL_WIZARD_OPTIONS_PAGE_TNS_TEXT = pluginId + "wsdlu0000"; //$NON-NLS-1$
	public static String WSDL_WIZARD_OPTIONS_PAGE_PREFIX_TEXT = pluginId + "wsdlu0001"; //$NON-NLS-1$
	public static String WSDL_WIZARD_OPTIONS_PAGE_CREATE_SKELETON_CHECKBOX = pluginId + "wsdlu0002"; //$NON-NLS-1$
	public static String WSDL_WIZARD_OPTIONS_PAGE_PROTOCOL_COMBO = pluginId + "wsdlu0003"; //$NON-NLS-1$

//	Soap Binding Options
	public static String DOC_LIT_RADIO = pluginId + "wsdlu0010"; //$NON-NLS-1$
	public static String RPC_LIT_RADIO = pluginId + "wsdlu0011"; //$NON-NLS-1$
	public static String RPC_ENCODED_RADIO = pluginId + "wsdlu0012"; //$NON-NLS-1$

//	HTTP Binding Options
	public static String HTTP_GET_RADIO = pluginId + "wsdlu0013"; //$NON-NLS-1$
	public static String HTTP_POST_RADIO = pluginId + "wsdlu0014"; //$NON-NLS-1$


	/*-------------------------------------------------------------------------------
	WSDL Editor - Design View
	-------------------------------------------------------------------------------*/
	public static String WSDL_DESIGN_VIEW_SERVICE_OBJECT = pluginId + "wsdlu0020";   //  Service Object                                          --> wsdlu0020 //$NON-NLS-1$
	public static String WSDL_DESIGN_VIEW_BINDING_OBJECT = pluginId + "wsdlu0021";   //  Binding Object                                          --> wsdlu0021 //$NON-NLS-1$
	public static String WSDL_DESIGN_VIEW_PORTTYPE_OBJECT = pluginId + "wsdlu0022";   //  PortType Object                                         --> wsdlu0022 //$NON-NLS-1$


	/*-------------------------------------------------------------------------------
	WSDL Editor - Outline View
	-------------------------------------------------------------------------------*/
	public static String WSDL_OUTLINE_VIEW_GROUP = pluginId + "wsdlu0023";   //  Imports, Types, Services, Bindings, Port Types, Messages (grouped together)             --> wsdlu0023 //$NON-NLS-1$


	/*-------------------------------------------------------------------------------
	WSDL Editor - Port Wizard
	-------------------------------------------------------------------------------*/
	public static String PORT_WIZARD = pluginId + "wsdlu0030"; //$NON-NLS-1$
	public static String PORT_WIZARD_NAME_TEXT = pluginId + "wsdlu0031";   //  not used - dup below //$NON-NLS-1$
	public static String PORT_WIZARD_BINDING_COMBO = pluginId + "wsdlu0032";   //  not used - dup below //$NON-NLS-1$
	public static String PORT_WIZARD_PROTOCOL_COMBO = pluginId + "wsdlu0033";   //  not used - dup below //$NON-NLS-1$


	/*-------------------------------------------------------------------------------
	WSDL Editor - Generate Binding Wizard
	-------------------------------------------------------------------------------*/
	public static String BINDING_WIZARD = pluginId + "wsdlu0040"; //$NON-NLS-1$
	public static String PROTOCOL_COMPONENT_NAME_TEXT = pluginId + "wsdlu0041"; //$NON-NLS-1$
	public static String PROTOCOL_COMPONENT_REF_COMBO = pluginId + "wsdlu0042"; //$NON-NLS-1$
	public static String PROTOCOL_COMPONENT_PROTOCOL_COMBO = pluginId + "wsdlu0043"; //$NON-NLS-1$
	public static String pROTOCOL_COMPONENT_OVERWRITE_CHECKBOX = pluginId + "wsdlu0044"; //$NON-NLS-1$

	
	/*-------------------------------------------------------------------------------
	WSDL Editor - Edit Namespaces Dialog --- common.ui
	-------------------------------------------------------------------------------*/
	public static String EDIT_NS_DIALOG = pluginId + "wsdlu0050";   //  Edit Namespaces Dialog                  "dialog"        --> wsdlu0050 //$NON-NLS-1$
	public static String EDIT_NS_DIALOG_TNS_TEXT = pluginId + "wsdlu0051";   //  Target Namespace                        "text"          --> wsdlu0051 //$NON-NLS-1$
	public static String EDIT_NS_DIALOG_DECLARATIONS_TABLE = pluginId + "wsdlu0052";   //  Namespace Declarations                  "table"         --> wsdlu0052 //$NON-NLS-1$

	public static String EDIT_NS_DIALOG_ADD_BUTTON = pluginId + "wsdlu0053";   //  Add...                                  "push button"   --> wsdlu0053 //$NON-NLS-1$
	public static String EDIT_NS_DIALOG_EDIT_BUTTON = pluginId + "wsdlu0054";   //  Edit...                                 "push button"   --> wsdlu0054 //$NON-NLS-1$
	public static String EDIT_NS_DIALOG_DELETE_BUTTON = pluginId + "wsdlu0055";   //  Delete                                  "push button"   --> wsdlu0055 //$NON-NLS-1$


	/*-------------------------------------------------------------------------------
	WSDL Editor - Add Namespace Declarations Dialog --- common.ui
	-------------------------------------------------------------------------------*/
	public static String ADD_NS_DECL = pluginId + "wsdlu0060";   //  Add Namespace Declarations Dialog       "dialog"        --> wsdlu0060 //$NON-NLS-1$
	public static String ADD_NS_DECL_SELECT_REG_NS_RADIO = pluginId + "wsdlu0061";   //  Select From Registered Namespaces       "radio"         --> wsdlu0061 //$NON-NLS-1$
	public static String ADD_NS_DECL_SPECIFY_NEW_NS_RADIO = pluginId + "wsdlu0062";   //  Specify New Namespace                   "radio"         --> wsdlu0062 //$NON-NLS-1$

	// Registered Namespaces:
	public static String ADD_NS_DECL_SELECT_NS_TO_ADD_TABLE = pluginId + "wsdlu0063";   //  Select the namespace declarations to add "table"        --> wsdlu0063 //$NON-NLS-1$

	// New Namespace:
	public static String ADD_NS_DECL_PREFIX_TEXT = pluginId + "";   //  Prefix:                                 "text"          -->(COMMON) wsdlu0064 //$NON-NLS-1$
	public static String ADD_NS_DECL_NAMESPACE_NAME_TEXT = pluginId + "";   //  Namespace Name:                         "text"          -->(COMMON) wsdlu0065 //$NON-NLS-1$
	public static String ADD_NS_DECL_LOCATION_HINT_TEXT = pluginId + "";   //  Location Hint:                          "text"          -->(COMMON) wsdlu0066 //$NON-NLS-1$
	public static String ADD_NS_DECL_BROWSE_BUTTON = pluginId + "";   //  Browse...                               "push button"   -->(COMMON) wsdlu0067 //$NON-NLS-1$
	
	
	/*-------------------------------------------------------------------------------
	WSDL Editor - New Namespace Information Dialog --- common.ui
	-------------------------------------------------------------------------------*/
	public static String NEW_NAMESPACE_INFO_DIALOG = pluginId + "wsdlu0068"; //$NON-NLS-1$

	public static String NAMESPACE_NAME = pluginId + "wsdlu0065"; //$NON-NLS-1$
	public static String PREFIX =         pluginId + "wsdlu0064"; //$NON-NLS-1$
	public static String LOCATION_HINT =  pluginId + "wsdlu0066"; //$NON-NLS-1$
	public static String BROWSE =         pluginId + "wsdlu0067"; //$NON-NLS-1$


	/*-------------------------------------------------------------------------------
	WSDL Editor - New Message Dialog
	-------------------------------------------------------------------------------*/
	public static String NEW_MESSAGE_DIALOG = pluginId + "wsdlu0080"; //$NON-NLS-1$
	public static String NEW_MESSAGE_DIALOG_NAME_TEXT = pluginId + "wsdlu0081";  // not used -- text will use "dialog" context Id //$NON-NLS-1$


	/*-------------------------------------------------------------------------------
	WSDL Editor - Specify Message Dialog --- common.ui
	-------------------------------------------------------------------------------*/
	public static String SPECIFY_MESSAGE_DIALOG = pluginId + "wsdlu0082"; //                "dialog"        --> wsdlu0082 //$NON-NLS-1$

	public static String SPECIFY_DIALOG_NAME_TEXT = pluginId + "wsdlu0070"; //   Name (? = character, * = any string):   "text"          -->(COMMON) wsdlu0070 //$NON-NLS-1$
	public static String SPECIFY_DIALOG_COMPONENTS_LIST = pluginId + "wsdlu0071";  //  Components                              "list"          -->(COMMON) wsdlu0071 //$NON-NLS-1$

	public static String SPECIFY_DIALOG_SEARCH_SCOPE = pluginId + "wsdlu0072"; //  Search Scope: //$NON-NLS-1$
	//Workspace, Enclosing Project, Current Resource, Working Sets (Grouped)          "radio"         -->(COMMON) wsdlu0072


	/*-------------------------------------------------------------------------------
	WSDL Editor - New Binding Dialog
	-------------------------------------------------------------------------------*/
	public static String NEW_BINDING_DIALOG = pluginId + "wsdlu0083"; //$NON-NLS-1$
	public static String NEW_BINDING_DIALOG_NAME_TEXT = pluginId + "wsdlu0084"; // not used -- text will use "dialog" context Id //$NON-NLS-1$

	
	/*-------------------------------------------------------------------------------
	WSDL Editor - Specify Binding Dialog --- common.ui
	-------------------------------------------------------------------------------*/
	public static String SPECIFY_BINDING_DIALOG = pluginId + "wsdlu0085";  //  Specify Binding Dialog                  "dialog"        --> wsdlu0085 //$NON-NLS-1$

	
	/*-------------------------------------------------------------------------------
	WSDL Editor - New PortType Dialog 
	-------------------------------------------------------------------------------*/
	public static String NEW_PORTTYPE_DIALOG = pluginId + "wsdlu0086"; //$NON-NLS-1$
	public static String NEW_PORTTYPE_DIALOG_NAME_TEXT = pluginId + "wsdlu0087"; // not used -- text will use "dialog" context Id //$NON-NLS-1$

	
	/*-------------------------------------------------------------------------------
	WSDL Editor - Specify Port Type Dialog --- common.ui
	-------------------------------------------------------------------------------*/
	public static String SPECIFY_PORTTYPE_DIALOG = pluginId + "wsdlu0088"; //  Specify Port Type Dialog                "dialog"        --> wsdlu0088 //$NON-NLS-1$

	
	/*-------------------------------------------------------------------------------
	WSDL Editor - Properties
	-------------------------------------------------------------------------------*/
	//Definition:
	public static String PROPERTIES_NAME_TEXT = pluginId + "wsdlu0100"; //$NON-NLS-1$
	public static String PROPERTIES_DEF_PREFIX_TEXT = pluginId + "wsdlu0101"; //$NON-NLS-1$
	public static String PROPERTIES_DEF_TNS_TEXT = pluginId + "wsdlu0102"; //$NON-NLS-1$

	//Port:
	public static String PROPERTIES_PORT_BINDING_COMBO = pluginId + "wsdlu0103"; //$NON-NLS-1$
	public static String PROPERTIES_PORT_ADDRESS_TEXT = pluginId + "wsdlu0104"; //$NON-NLS-1$
	public static String PROPERTIES_PORT_PROTOCOL_TEXT = pluginId + "wsdlu0105"; //$NON-NLS-1$

	//Binding:
	public static String PROPERTIES_BINDING_PORTTYPE_COMBO = pluginId + "wsdlu0106"; //$NON-NLS-1$
	public static String PROPERTIES_BINDING_PROTOCOL_TEXT = pluginId + "wsdlu0107"; //$NON-NLS-1$
	public static String PROPERTIES_BINDING_GEN_BINDING_BUTTON = pluginId + "wsdlu0108"; //$NON-NLS-1$

	//Input/Output/Fault:
	public static String PROPERTIES_MESSAGE_REF_MESSAGE_COMBO = pluginId + "wsdlu0109"; //$NON-NLS-1$

	//Part:
	public static String PROPERTIES_PART_ELEMENT_COMBO = pluginId + "wsdlu0110"; //$NON-NLS-1$
	public static String PROPERTIES_PART_TYPE_COMBO = pluginId + "wsdlu0111"; //$NON-NLS-1$
	public static String PROPERTIES_PART_TYPE_RADIO = pluginId + "wsdlu0112"; //$NON-NLS-1$
	public static String PROPERTIES_PART_ELEMENT_RADIO = pluginId + "wsdlu0113"; //$NON-NLS-1$
	

	public static String PROPERTIES_DOCUMENTATION_TAB = pluginId + "wsdlu0114"; //$NON-NLS-1$

	public static String WSDL_WIZARD_OPTIONS_PAGE = pluginId + "wsdlu0120";  // New WSDL Wizard - options page 	--> wsdlu0120 //$NON-NLS-1$
	public static String WSDL_DESIGN_VIEW = pluginId + "wsdlu0121"; //  Design View 				--> wsdlu0121 //$NON-NLS-1$
	public static String WSDL_OUTLINE_VIEW = pluginId + "wsdlu0122";  // Outline View 				--> wsdlu0122 //$NON-NLS-1$
	public static String WSDL_PROPERTIES_VIEW = pluginId + "wsdlu0123"; //  Properties View				--> wsdlu0123 //$NON-NLS-1$

//	WSDL Preference Page:
	public static String WSDL_PREF_DEFAULT_TNS = pluginId + "wsdlu0200"; //$NON-NLS-1$
	public static String WSDL_PREF_REGEN_ON_SAVE = pluginId + "wsdlu0201"; //$NON-NLS-1$
	public static String WSDL_PREF_PROMPT_REGEN_ON_SAVE = pluginId + "wsdlu0202"; //$NON-NLS-1$
}

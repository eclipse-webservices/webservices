/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;


public class Messages {
	private static final String BUNDLE_NAME = "org.eclipse.wst.wsdl.ui.internal.asd.messages"; //$NON-NLS-1$

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class); //$NON-NLS-1$
	}
	  
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		// TODO Auto-generated method stub
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static String _UI_TAB_GRAPH;
	public static String _UI_TAB_SOURCE;
	public static String _UI_BUTTON_NEW;
	public static String _UI_LABEL_READ_ONLY;
	public static String _UI_LABEL_NAMESPACE;
	public static String _UI_LABEL_PORTTYPE;
	public static String _UI_LABEL_LOCATION;
	public static String _UI_LABEL_ADDRESS;
	public static String _UI_LABEL_BINDING;
	public static String _UI_LABEL_PREFIX;
	public static String _UI_LABEL_NAME;
	public static String _UI_LABEL_TYPE;
	public static String _UI_ACTION_ADD_SERVICE;
	public static String _UI_ACTION_ADD_MESSAGE;
	public static String _UI_ACTION_ADD_OPERATION;
	public static String _UI_ACTION_ADD_IMPORT;
	public static String _UI_ACTION_ADD_OUTPUT;
	public static String _UI_ACTION_ADD_FAULT;
	public static String _UI_ACTION_ADD_PORT;
	public static String _UI_ACTION_ADD_PORTTYPE;
	public static String _UI_ACTION_DELETE;
	public static String _UI_BUTTON_BROWSE;
	public static String _UI_ACTION_NEW_PORTTYPE;
	public static String _UI_ACTION_NEW_BINDING;
	public static String _UI_ACTION_EXISTING_PORTTYPE;
	public static String _UI_GENERATE_BINDING_CONTENT;
	public static String _UI_ACTION_EXISTING_BINDING;
	public static String _UI_LABEL_BINDING_PROTOCOL;
	public static String _UI_LABEL_TARGET_NAMESPACE;
	public static String _UI_ACTION_ADD_BINDING;
	public static String _UI_ACTION_ADD_SCHEMA;
	public static String _UI_ACTION_ADD_INPUT;
	public static String _UI_SECTION_ADVANCED_ATTRIBUTES;

	public static String _UI_ACTION_EDIT_NAMESPACES;
	public static String _UI_ACTION_OPEN_SCHEMA;
	public static String _UI_TOOLTIP_RENAME_REFACTOR;

	public static String _UI_LABEL_RIGHT_CLICK_TO_INSERT_CONTENT;
	public static String _UI_ACTION_SHOW_PROPERTIES;
}

/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class WSDLPreferenceInitializer extends AbstractPreferenceInitializer {
		  
	public WSDLPreferenceInitializer() {
	}

	public void initializeDefaultPreferences() {
		IEclipsePreferences node = DefaultScope.INSTANCE.getNode(WSDLEditorPlugin.getInstance().getBundle().getSymbolicName());
		// formatting preferences
		node.put(WSDLEditorPlugin.DEFAULT_TARGET_NAMESPACE_ID, WSDLEditorPlugin.DEFAULT_TARGET_NAMESPACE);
		node.put(WSDLEditorPlugin.DEFAULT_TARGET_NAMESPACE_PREFIX_PREFERENCE_ID, WSDLEditorPlugin.DEFAULT_TARGET_NAMESPACE_PREFIX);
		node.putBoolean(WSDLEditorPlugin.GENERATE_SEPARATE_INTERFACE_PREFERENCE_ID, false);
		node.put(WSDLEditorPlugin.INTERFACE_PREFIX_PREFERENCE_ID, WSDLEditorPlugin.INTERFACE_PREFIX_DEFAULT);
		node.put(WSDLEditorPlugin.INTERFACE_DEFAULT_TARGET_NAMESPACE_PREFERENCE_ID, WSDLEditorPlugin.DEFAULT_TARGET_NAMESPACE);
		node.put(WSDLEditorPlugin.INTERFACE_FILE_SUFFIX_PREFERENCE_ID, WSDLEditorPlugin.INTERFACE_FILE_SUFFIX_DEFAULT);
		node.putBoolean(WSDLEditorPlugin.AUTO_REGENERATE_BINDING_ON_SAVE_ID, false);
		node.putBoolean(WSDLEditorPlugin.PROMPT_REGEN_BINDING_ON_SAVE_ID, false);
		node.putBoolean(WSDLEditorPlugin.AUTO_IMPORT_CLEANUP_ID, false);
		node.putBoolean(WSDLEditorPlugin.AUTO_OPEN_IMPORT_DIALOG_ID, false);
	}
}

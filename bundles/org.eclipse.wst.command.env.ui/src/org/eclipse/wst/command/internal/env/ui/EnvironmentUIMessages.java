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
package org.eclipse.wst.command.internal.env.ui;

import org.eclipse.osgi.util.NLS;

public final class EnvironmentUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.wst.command.internal.env.ui.EnvironmentUI";//$NON-NLS-1$

	private EnvironmentUIMessages() {
		// Do not instantiate
	}

	public static String BUTTON_SHOW_ALL_DIALOGS;
	public static String BUTTON_HIDE_ALL_DIALOGS;
	public static String CHECKBOX_DO_NOT_SHOW_DIALOG_AGAIN;
	public static String TOOLTIP_PPAD_PAGE;
	public static String TOOLTIP_DO_NOT_SHOW_DIALOG_AGAIN;
	public static String TOOLTIP_PPAD_BUTTON_SHOW_ALL;
	public static String TOOLTIP_PPAD_BUTTON_HIDE_ALL;
	public static String MSG_ERROR_WIZARD_ID_NOT_FOUND;
	public static String WIZARD_TITLE_ANT;
	public static String WIZARD_PAGE_TITLE_ANT;
	public static String WIZARD_PAGE_DESC_ANT;
	public static String MSG_STATUS_COPYING_ANT_FILES;
	public static String MSG_ERR_COPYING_ANT_FILES;
	public static String MSG_WARNING_FILE_EXISTS;
	public static String DIALOG_TITLE_OVERWRITE;

	static {
		NLS.initializeMessages(BUNDLE_NAME, EnvironmentUIMessages.class);
	}
}
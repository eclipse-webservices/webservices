/*
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *   IBM - Initial API and implementation
 * 
 */
package org.eclipse.wst.wsi.ui.internal.actions;

import org.eclipse.osgi.util.NLS;

/**
 * Strings used by XML Validation
 */
public class WSIValidationUIMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.wsi.ui.internal.actions.wsivalidation"; //$NON-NLS-1$

	private WSIValidationUIMessages() {
		// cannot create new instance
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, WSIValidationUIMessages.class);
	}
	public static String _UI_SAVE_DIRTY_FILE_MESSAGE;
	public static String _UI_SAVE_DIRTY_FILE_TITLE;
}

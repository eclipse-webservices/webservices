/*
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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

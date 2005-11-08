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
package org.eclipse.jst.ws.internal.creation.ui;

import org.eclipse.osgi.util.NLS;

public final class CreationUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.internal.creation.ui.CreationUI";//$NON-NLS-1$

	private CreationUIMessages() {
		// Do not instantiate
	}

	public static String PAGE_TITLE_OBJECT_SELECTION;
	public static String PAGE_DESC_OBJECT_SELECTION;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CreationUIMessages.class);
	}
}
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
package org.eclipse.wst.wsdl.validation.internal.eclipse;

import org.eclipse.osgi.util.NLS;

/**
 * Strings used by WSDL Validation
 */
public class WSDLValidationMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.wsdl.validation.internal.eclipse.wsdlvalidation";//$NON-NLS-1$

	public static String Message_WSDL_validation_message_ui;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, WSDLValidationMessages.class);
	}

	private WSDLValidationMessages() {
		// cannot create new instance
	}
}

/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070213  168766 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  facet to the framework for 168766
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.facet.messages;

import org.eclipse.osgi.util.NLS;

public final class Axis2FacetUIMessages extends NLS {

	private static final String BUNDLE_NAME = 
				"org.eclipse.jst.ws.axis2.facet.messages.Axis2FacetUI";//$NON-NLS-1$

	private Axis2FacetUIMessages() {
		// Do not instantiate
	}

	public static String WEB_INF_LIB;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Axis2FacetUIMessages.class);
	}
}

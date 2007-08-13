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
 * 20070517   187307 sandakith@wso2.com - Lahiru Sandakith, Fix 187307 Property File Fix
 * 20070606   177421 sandakith@wso2.com - fix web.xml wiped out when Axis2 facet
 * 20070730   194786 sandakith@wso2.com - Lahiru Sandakith, adding servletapi jar filter
 * 20070808   194906 sandakith@wso2.com - Lahiru Sandakith, Fixing 194906 Runtime lib issue
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.facet.messages;

import org.eclipse.osgi.util.NLS;

public final class Axis2FacetUIMessages extends NLS {

	private static final String BUNDLE_NAME = 
				"org.eclipse.jst.ws.axis2.facet.messages.Axis2FacetUI";//$NON-NLS-1$
	public static final String DIR_WEB_INF ="WEB-INF";//$NON-NLS-1$
	public static final String DIR_META_INF ="META-INF";//$NON-NLS-1$
	public static final String DIR_AXIS2_WEB="axis2-web";//$NON-NLS-1$
	public static final String FILE_WEB_XML="web.xml";//$NON-NLS-1$
	public static final String FILE_SERVLET_API="servletapi";//$NON-NLS-1$
	
	public static final String[] AXIS2_LIB_PREFIXES = {		//$NON-NLS-1$
        "ant",
        "axiom",
        "axis2",
        "commons",
        "log4j",
        "neethi",
        "stax",
        "wsdl4j",
        "wstx",
        "xbean",
        "XmlSchema",
        "woden",
        "activation",
        "annogen",
        "backport-util"
	};

	private Axis2FacetUIMessages() {
		// Do not instantiate
	}

	static {
		NLS.initializeMessages(BUNDLE_NAME, Axis2FacetUIMessages.class);
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060420   132905 cbrealey@ca.ibm.com - Chris Brealey          
 * 20060607   145604 cbrealey@ca.ibm.com - Chris Brealey          
 *******************************************************************************/
package org.eclipse.jst.ws.internal;

import org.eclipse.osgi.util.NLS;

public final class WSPluginMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.internal.WSPlugin";//$NON-NLS-1$

	private WSPluginMessages() {
		// Do not instantiate
	}

	public static String MSG_ERROR_SERVER;
	public static String ANY_FILTER_NAME;
	public static String ANY_FILTER_DESC;
	public static String ERROR_SELECTION_TRANSFORM;
	public static String MSG_JAXRPC11_NOT_COMPLIANT;
	public static String MSG_JAXRPC11_UNRESOLVED_CLASS;
	public static String MSG_JAXRPC11_UNRESOLVED_TYPE;
	public static String MSG_JAXRPC11_RULE_0001;
	public static String MSG_JAXRPC11_RULE_0002;
	public static String MSG_JAXRPC11_RULE_0003;
	public static String MSG_JAXRPC11_RULE_0004;
	public static String MSG_JAXRPC11_RULE_0005;
	public static String MSG_JAXRPC11_RULE_0006;
	public static String MSG_JAXRPC11_RULE_0007;
	public static String MSG_JAXRPC11_RULE_0008;
	public static String MSG_JAXRPC11_RULE_0009;
	public static String MSG_JAXRPC11_RULE_0010;

	static {
		NLS.initializeMessages(BUNDLE_NAME, WSPluginMessages.class);
	}
}

/*******************************************************************************
 * Copyright (c) 2009, 2010 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin;

import org.eclipse.osgi.util.NLS;

public class JaxWsDomRuntimeMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.messages"; //$NON-NLS-1$

	public static String WsDomStartupParticipant_Startup_Job_Message;
	public static String WorkspaceCUFinder_LOADING_CANCELED;
	public static String JAXWS_DOM_LOADING;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, JaxWsDomRuntimeMessages.class);
	}

	private JaxWsDomRuntimeMessages() {
	}
}

/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.integration.internal.plugin;

import org.eclipse.osgi.util.NLS;

public class DomIntegrationMessages extends NLS
{
	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.jaxws.dom.integration.internal.plugin.DomIntegrationMessages"; //$NON-NLS-1$

	public static String DOMAdapterFactoryLabelProvider_JaxWsWebServicesLabel;

	public static String DOMAdapterFactoryLabelProvider_LoadingDummyLabel;

	public static String DOMAdapterFactoryLabelProvider_SeiLabel;

	public static String DOMAdapterFactoryLabelProvider_WebServicesLabel;

	/** message constant */
	public static String OpenWSResourceAction_Name;

	/** message constant */
	public static String NavigateToImplementationAction_ShowInWebServicesAreaAction;

	/** message constant */
	public static String NavigateToImplementationAction_ShowInEJBAreaAction;

	static
	{
		NLS.initializeMessages(BUNDLE_NAME, DomIntegrationMessages.class);
	}
}

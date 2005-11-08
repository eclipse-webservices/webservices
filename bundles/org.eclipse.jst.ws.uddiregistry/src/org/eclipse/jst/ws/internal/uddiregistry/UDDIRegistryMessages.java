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
package org.eclipse.jst.ws.internal.uddiregistry;

import org.eclipse.osgi.util.NLS;

public final class UDDIRegistryMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.internal.uddiregistry.UDDIRegistry";//$NON-NLS-1$

	private UDDIRegistryMessages() {
		// Do not instantiate
	}

	public static String PAGE_TITLE_PRIVATE_UDDI_CONFIG;
	public static String PAGE_DESC_PRIVATE_UDDI_CONFIG;
	public static String TOOLTIP_PUPR_PRIVATE_UDDI_PAGE;
	public static String TOOLTIP_PUPR_PRIVATE_UDDI_TYPE;
	public static String TOOLTIP_PUPR_DEPLOY_PRIVATE_UDDI;
	public static String TOOLTIP_PUPR_UPDATE_PRIVATE_UDDI;
	public static String TOOLTIP_PUPR_REMOVE_PRIVATE_UDDI;
	public static String LABEL_PRIVATE_UDDI_REGISTRY_TYPES;
	public static String BUTTON_DEPLOY_UDDI_REGISTRY;
	public static String BUTTON_UPDATE_UDDI_REGISTRY;
	public static String BUTTON_REMOVE_UDDI_REGISTRY;
	public static String MSG_ERROR_NO_UDDI_REGISTRY_AVAILABLE;

	static {
		NLS.initializeMessages(BUNDLE_NAME, UDDIRegistryMessages.class);
	}
}
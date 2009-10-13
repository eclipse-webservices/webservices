/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091013   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal;

import org.eclipse.osgi.util.NLS;

/**
 * String resource handler.
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.jaxrs.core.internal.messages"; //$NON-NLS-1$

	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetInstallDelegate_InternalErr;
	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetInstallDelegate_ConfigErr;
	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetUninstallDelegate_ConfigErr;
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}

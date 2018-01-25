/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20091106   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100324   306937 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS Properties page- NPE after pressing OK
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal;

import org.eclipse.jst.ws.jaxrs.ui.internal.JAXRSUIPlugin;

/**
 * JAXRS Core framework constants
 * 
 * <p>
 * <b>Provisional API - subject to change</b>
 * </p>
 * 
 */
public final class IJAXRSUIConstants {
	/**
	 * The root value for saved settings
	 */
	public static final String SETTINGS_ROOT = JAXRSUIPlugin.PLUGIN_ID
			+ ".jaxrsFacetInstall"; //$NON-NLS-1$
	public static final String USER_LIBRARY_ID = "jaxrs-user-library-provider"; //$NON-NLS-1$

	private IJAXRSUIConstants() {
		// no instantiation
	}
}

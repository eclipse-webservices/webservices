/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;

/**
 * Exception for use during JAXRS Facet installation/un-installation
 * 
 */
public class JAXRSFacetException extends CoreException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param msg
	 */
	public JAXRSFacetException(String msg) {
		super(new Status(IStatus.ERROR, JAXRSCorePlugin.PLUGIN_ID, msg));
	}

}

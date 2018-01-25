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
 * 20100420   309846 ericdp@ca.ibm.com - Eric D. Peters, Remove dead code related to e.p. pluginProvidedJaxrsLibraries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.jaxrslibraryregistry.internal;

public class InvalidArchiveFilesCreationException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an instance with the specified message.
	 * 
	 * @param msg
	 *            This instance's message
	 * @deprecated
	 * 
	 * <p>
	 * <b>Provisional API - subject to change - do not use</b>
	 * </p>
	 */
	public InvalidArchiveFilesCreationException(String msg) {
		super(msg);
	}
}

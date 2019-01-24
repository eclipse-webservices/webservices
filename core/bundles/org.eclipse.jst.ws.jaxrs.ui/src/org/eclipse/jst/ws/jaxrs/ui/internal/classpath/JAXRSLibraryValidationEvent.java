/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
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
 * 20091106   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100420   309846 ericdp@ca.ibm.com - Eric D. Peters, Remove dead code related to e.p. pluginProvidedJaxrsLibraries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.classpath;

import org.eclipse.core.runtime.IStatus;

/**
 * Validation event used by JAXRSLibraryControl to notify containers of updates
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public class JAXRSLibraryValidationEvent {
	private String msg;
	private int severity;

	/**
	 * Constructor
	 * 
	 * @param msg
	 * @param severity
	 *            - IStatus int value
	 */
	public JAXRSLibraryValidationEvent(String msg, int severity) {
		this.msg = msg;
		this.severity = severity;
	}

	/**
	 * Constructs event with severity of IStatus.ERROR
	 * 
	 * @param msg
	 */
	public JAXRSLibraryValidationEvent(String msg) {
		this(msg, IStatus.ERROR);
	}

	/**
	 * @return validation message
	 */
	public String getMessage() {
		return msg;
	}

	/**
	 * @return IStatus int value
	 */
	public int getSeverity() {
		return severity;
	}
}

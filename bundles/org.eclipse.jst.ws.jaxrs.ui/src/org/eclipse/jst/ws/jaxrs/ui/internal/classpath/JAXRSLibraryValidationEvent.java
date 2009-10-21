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
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/package org.eclipse.jst.ws.jaxrs.ui.internal.classpath;

import org.eclipse.core.runtime.IStatus;

/**
 * Validation event used by JAXRSLibraryControl to notify containers of updates
 * 
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

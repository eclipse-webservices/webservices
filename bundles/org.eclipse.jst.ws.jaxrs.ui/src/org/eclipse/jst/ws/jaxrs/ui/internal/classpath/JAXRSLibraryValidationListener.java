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

/**
 * Listeners of {@link JAXRSLibraryValidationEvent}s should implement
 * 
 */
public interface JAXRSLibraryValidationListener {
	/**
	 * Callback
	 * 
	 * @param e
	 */
	public void notifyValidation(JAXRSLibraryValidationEvent e);
}

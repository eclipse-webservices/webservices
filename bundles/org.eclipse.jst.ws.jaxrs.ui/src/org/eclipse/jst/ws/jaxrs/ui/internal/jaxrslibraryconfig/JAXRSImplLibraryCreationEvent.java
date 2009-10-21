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
 *******************************************************************************/package org.eclipse.jst.ws.jaxrs.ui.internal.jaxrslibraryconfig;

import java.util.EventObject;

/**
 * JAXRS Implementation library creation event.
 * 
 * 
 */
public class JAXRSImplLibraryCreationEvent extends EventObject {

	private static final long serialVersionUID = 6390319185522362453L;
	private boolean isLibCreated;

	/**
	 * @param source
	 * @param okClicked
	 */
	public JAXRSImplLibraryCreationEvent(Object source, boolean okClicked) {
		super(source);
		this.isLibCreated = okClicked;
	}

	/**
	 * Ok button pressed.
	 * 
	 * @return boolean
	 */
	public boolean isLibraryCreated() {
		return isLibCreated;
	}

}

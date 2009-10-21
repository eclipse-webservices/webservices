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
 * Event that something has changed in the JAXRSLibraryConfigControl
 * 
 */
public class JAXRSLibraryConfigControlChangeEvent {
	private EventObject event;

	/**
	 * Constructor
	 * 
	 * @param ev
	 */
	public JAXRSLibraryConfigControlChangeEvent(EventObject ev) {
		this.event = ev;
	}

	/**
	 * @return java.util.EventObject
	 */
	public EventObject getEvent() {
		return event;
	}
}

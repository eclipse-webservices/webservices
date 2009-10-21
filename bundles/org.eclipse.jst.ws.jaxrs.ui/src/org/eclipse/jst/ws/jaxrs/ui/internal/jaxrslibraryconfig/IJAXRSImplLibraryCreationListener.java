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

/**
 * Listener interface when a new JAXRS implementation library is created.
 * 
 */
public interface IJAXRSImplLibraryCreationListener extends
		java.util.EventListener {
	/**
	 * Ok button is pressed in JAXRS Library dialog.
	 * 
	 * @param event
	 */
	public void okClicked(JAXRSImplLibraryCreationEvent event);
}

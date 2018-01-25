/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.transport;

/**
 * ISOAPTransportProvider is a factory for creating ISOAPTransport objects.
 * Anyone who wishes to extend the org.eclipse.wst.ws.explorer.wseTransportProvider
 * extension-point must provide an implementation for this interface. 
 */
public interface ISOAPTransportProvider {

	/**
	 * Method for obtaining a new ISOAPTransport from this provider.
	 * This method should never return null.
	 * 
	 * @return A new ISOAPTransport object.
	 */
	public ISOAPTransport newTransport();
}

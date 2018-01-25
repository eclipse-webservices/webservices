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
 * The ISerializer is responsible for taking an ISOAPMessage and serializing it
 * into an XML string.
 */
public interface ISerializer {

	/**
	 * Serialize the ISOAPMessage into an XML string.  The part parameter tells the method
	 * which part of the message (the envelope or the contents of the header or body) to
	 * serialize.  Note that "contents" refers to the list of elements inside the header
	 * or body, and not the header or body element itself.
	 * 
	 * @param part One of {@link ISOAPMessage#ENVELOPE}, {@link ISOAPMessage#HEADER_CONTENT}, or
	 * {@link ISOAPMessage#BODY_CONTENT}.
	 * @param message The ISOAPMessage to serialize.
	 * @return An XML string. 
	 * @throws TransportException
	 */
	public String serialize(int part, ISOAPMessage message) throws TransportException;
}

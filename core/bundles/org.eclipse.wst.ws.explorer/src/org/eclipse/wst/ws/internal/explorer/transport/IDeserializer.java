/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.transport;

/**
 * The IDeserializer is responsible for taking an XML string and deserializing it
 * into values to populate an ISOAPMessage.
 */
public interface IDeserializer {
	
	/**
	 * Deserialize the XML string into the ISOAPMessage.  The part parameter tells the method
	 * which part of the message (the envelope or the contents of the header or body) the XML
	 * string represents.  Note that "contents" refers to the list of elements inside the header
	 * or body, and not the header or body element itself.
	 * 
	 * @param part One of {@link ISOAPMessage#ENVELOPE}, {@link ISOAPMessage#HEADER_CONTENT}, or
	 * {@link ISOAPMessage#BODY_CONTENT}.
	 * @param xml The XML string to deserialize.
	 * @param message The ISOAPMessage to deserialize into.
	 * @throws TransportException
	 */
	public void deserialize(int part, String xml, ISOAPMessage message) throws TransportException;		
}

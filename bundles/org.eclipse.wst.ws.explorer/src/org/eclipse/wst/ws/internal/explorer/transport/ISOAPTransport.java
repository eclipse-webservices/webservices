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
 * The ISOAPTransport is the core piece of the Web Services Explorer transport stack.
 * It is responsible for invoking the web service and it also acts as the factory for
 * creating ISerialier, IDeserializer, and ISOAPMessage instances which make up the
 * rest of the WSE transport.
 */
public interface ISOAPTransport {		

	/**
	 * Factory method for ISerializer.
	 * 
	 * @return An instance of ISerializer.
	 */
	public ISerializer newSerializer();
	
	/**
	 * Factory method for IDeserializer.
	 * 
	 * @return An instance of IDeserializer.
	 */
	public IDeserializer newDeserializer();
	
	/**
	 * Factory method for ISOAPMessage.
	 * 
	 * @param context MessageContext encapsulating information about the web service operation.
	 * @return An instance of ISOAPMessage.
	 * @throws TransportException
	 */
	public ISOAPMessage newMessage(MessageContext context) throws TransportException;
	
	/**
	 * Invokes the web service operation by sending the SOAP message, then parsing the results
	 * into a response ISOAPMessage.
	 * 
	 * @param url The endpoint URL.
	 * @param username Username to use for basic auth protected endpoints.  Set to null if not required.  
	 * @param password Password to use for basic auth protected endpoints.  Set to null if not required.
	 * @param message The SOAP request message.
	 * @return An ISOAPMesage representing the response from the web service invocation.
	 * @throws TransportException
	 */
	public ISOAPMessage send(String url, String username, String password, ISOAPMessage message) throws TransportException;
}

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

import java.util.Map;

import org.w3c.dom.Element;

/**
 * The ISOAPMessage represents a SOAP message in a web service invocation made by the
 * Web Services Explorer's transport stack.
 * <br/>
 * <br/>
 * The setters of ISOAPMessage are called by Web Services Explorer to populate the
 * message before sending.  WSE does not guarantee that all message elements it sets
 * will have the same owner document.
 */
public interface ISOAPMessage {

	/**
	 * ISOAPMessage defines this property for storing the SOAP action value in
	 * a web service invocation.  This property will be set by Web Services Explorer
	 * prior to passing the message to {@link ISOAPTransport#send(String, String, String, ISOAPMessage)}
	 */
	String PROP_SOAP_ACTION = "prop_soap_action";
	
	/**
	 * This value is used when serializing and deserializing a message.  It indicates the operation
	 * is working with the entire SOAP envelope element. 
	 */
	int ENVELOPE = 0;
	
	/**
	 * This value is used when serializing and deserializing a message.  It indicates the operation
	 * is working with the elements inside the SOAP header element.
	 */
	int HEADER_CONTENT = 1;
	
	/**
	 * This value is used when serializing and deserializing a message.  It indicates the operation
	 * is working with the elements inside the SOAP body element.
	 */
	int BODY_CONTENT = 2;

	/**
	 * Returns the MessageContext associated with this message.  The MessageContext is created
	 * by the Web Services Explorer and passed to the transport stack during message creation.  
	 * Implementers of ISOAPMessage should store a reference to the MessageContext.
	 * 
	 * @return The MessageContext encasulating information about the web service operation. 
	 * @see ISOAPTransport#newMessage(MessageContext)
	 */
	public MessageContext getMessageContext();

	/**
	 * Sets a property in this ISOAPMessage.
	 * 
	 * @param key The key (name) of the property
	 * @param value The value of the property.
	 */
	public void setProperty(String key, Object value);

	/**
	 * Retrieves a property from this ISOAPMessage.
	 * 
	 * @param key The key (name) of the property to retrieve.
	 * @return The value assoicated with the given key, or null if there's no such property.
	 */
	public Object getProperty(String key);

	/**
	 * The namespace table holds the namespace declaraions that are on the envelope element
	 * of the SOAP message.  The table's keys are the namespace URIs and the values are the
	 * associated namespace prefixes.
	 * <br/>
	 * <br/>
	 * The effect of calling this method is that the declarations in the namespace table
	 * argument should add to or replace the declarations on the envelope.  This API does
	 * not specify what happens to existing declarations on the envelope which are not
	 * in the namespace table passed in.
	 * 
	 * @param namespaceTable The namespace table
	 */
	public void setNamespaceTable(Map namespaceTable);

	/**
	 * Returns a table of the namespace declarations that are on the envelope element of the
	 * SOAP message.  The table's keys are the namespace URIs and the values are the
	 * associated namespace prefixes.
	 * <br/>
	 * <br/>
	 * The namespace table returned by this method may be modified by the Web Services
	 * Explorer and later updated back to the message via the setNamesapceTable() method.
	 * 
	 * @return A table of namespace URIs to prefixes on the SOAP envelope.
	 */
	public Map getNamespaceTable();

	/**
	 * Sets the envelope element of this message.
	 * 
	 * @param envelope The envelope element.
	 */
	public void setEnvelope(Element envelope);

	/**
	 * Returns the envelope element of this message.  The deep parameter dictates whether
	 * the method returns the whole envelope with all its descendants or just the envelope 
	 * element itself.
	 * 
	 * @param deep If true, the envelope and its descendants are returned, otherwise only
	 * the envelope element itself is returned.
	 * @return An element.
	 */
	public Element getEnvelope(boolean deep);

	/**
	 * Sets the header element of this message.
	 * 
	 * @param header The header element.
	 */
	public void setHeader(Element header);

	/**
	 * Returns the header element of this message.  The deep parameter dictates whether
	 * the method returns the whole header with all its descendants or just the header 
	 * element itself.
	 * 
	 * @param deep If true, the header and its descendants are returned, otherwise only
	 * the header element itself is returned.
	 * @return An element.
	 */
	public Element getHeader(boolean deep);

	/**
	 * Sets an array of elements that goes into the message's header.
	 * 
	 * @param headerContent An array of elements.
	 */
	public void setHeaderContent(Element[] headerContent);

	/**
	 * Returns the array of elements that are inside the message's header.
	 * 
	 * @return An array of elements.
	 */
	public Element[] getHeaderContent();

	/**
	 * Sets the body element of this message.  For an RPC style message, the first child
	 * element of the body should be the RPC wrapper element.
	 * 
	 * @param body The body element.
	 */
	public void setBody(Element body);

	/**
	 * Returns the body of this message.  The deep parameter dictates whether
	 * the method returns the whole body with all its descendants elements or just the
	 * body.  For an RPC style message, the first child element of the 
	 * body should be the RPC wrapper element, regardless of the value of the deep
	 * parameter.
	 * 
	 * @param deep If true, the body and its descendants are returned, otherwise only
	 * the body (and the RPC wrapper element if the message is RPC style) is returned.
	 * @return An element.
	 */	
	public Element getBody(boolean deep);

	/**
	 * Sets an array of elements that goes into the message's body.  For an RPC style
	 * message, the body contents are the elements inside the RPC wrapper element.
	 * 
	 * @param bodyContent An array of elements.
	 */
	public void setBodyContent(Element[] bodyContent);

	/**
	 * Returns the array of elements that are inside the message's body.  For an RPC style
	 * message, the body contents are the elements inside the RPC wrapper element.
	 * 
	 * @return An array of elements.
	 */
	public Element[] getBodyContent();

	/**
	 * Set the fault element for this message, if any.
	 * 
	 * @param fault A fault element.
	 */
	public void setFault(Element fault);

	/**
	 * Returns the fault element, if any.
	 * 
	 * @return The fault element or null if no fault has occurred.
	 */
	public Element getFault();

	/**
	 * Returns the XML serialized form of this ISOAPMessage.
	 * 
	 * @return An XML string.
	 * @see ISerializer
	 * @throws TransportException 
	 */
	public String toXML() throws TransportException;

}

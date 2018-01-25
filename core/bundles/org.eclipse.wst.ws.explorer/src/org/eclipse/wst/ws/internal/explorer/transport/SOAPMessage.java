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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This is a default implementation of the ISOAPMessage interface that extenders of the Web
 * Services Explorer transport can use if they do not wish to implement their own message.  
 * This class is  implemented as a collection of loose pieces of the SOAP message, and each 
 * piece must be set individually (for example, setting the envelope element will not 
 * automatically populate the header or body elements).
 * <br/>
 * <br/>
 * This class does not know how to serialize itself.  It depends on an ISerializer provided by
 * the transport extender. 
 */
public final class SOAPMessage implements ISOAPMessage {
	
	private MessageContext context;
	private ISerializer serializer;
		
	private Map properties = new Hashtable();	
	
	private Element envelope = null;
	
	private Element header = null;
	private Element[] headerContent = null;
	
	private Element body = null;
	private Element[] bodyContent = null;	
		
	private Element fault = null;
		
	private String xml = null;
	
	/**
	 * Constructor.
	 * 
	 * @param context The MessageContext.
	 * @param serializer An ISerializer to use to serialize this message.
	 */
	public SOAPMessage(MessageContext context, ISerializer serializer) {
		this.context = context;
		this.serializer = serializer;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#getMessageContext()
	 */
	public MessageContext getMessageContext() {
		return context;
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#setProperty(java.lang.String, java.lang.Object)
	 */
	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#getProperty(java.lang.String)
	 */
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#setNamespaceTable(java.util.Map)
	 */
	public synchronized void setNamespaceTable(Map namespaceTable) {
		if (namespaceTable == null || envelope == null)
			return;
	
		Iterator iter = namespaceTable.entrySet().iterator();
		
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String name = "xmlns:" + entry.getValue();			
			envelope.setAttribute(name, entry.getKey().toString());			
		}
		
		xml = null;
	}
	
	/*
	 * If the attribute is a namespace declaration, the namespace prefix is returned.
	 * Otherwise null is returned.
	 */
	private String getNSPrefix(Attr attribute) {
		String name = attribute.getName();
		if (name.startsWith("xmlns:"))
			return name.substring(6);
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#getNamespaceTable()
	 */
	public Map getNamespaceTable() {
		
		Hashtable namespaceTable = new Hashtable();
		
		if (envelope != null) {
			NamedNodeMap attributes = envelope.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				Attr attribute = (Attr) attributes.item(i);
				String prefix = getNSPrefix(attribute);
				if (prefix != null)
					namespaceTable.put(attribute.getValue(), prefix);
			}			
		}		
		
		return namespaceTable;
	}
	
	/*
	 * Appends a child node to a parent node, takes care of importing
	 * the child node if neccessary.
	 */
	private void appendNode(Node parent, Node child) {
		if (parent == null || child == null)
			return;
		
		Document owner = parent.getOwnerDocument();
		
		if (!owner.equals(child.getOwnerDocument()))
			child = owner.importNode(child, true);
		
		parent.appendChild(child);
	}
	
	/*
	 * Adds an array of elements to the parent element as child elements.
	 */
	private void appendChildren(Element parent, Element[] children) {
		if (parent == null || children == null)
			return;
		
		for (int i = 0; i < children.length; i++) {
			if (children[i] == null)
				continue;
			appendNode(parent, children[i]);
		}
	}		
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#setEnvelope(org.w3c.dom.Element)
	 */
	public synchronized void setEnvelope(Element envelope) {
		if (envelope == null)
			this.envelope = envelope;
		else
			this.envelope = (Element) envelope.cloneNode(false);
					
		xml = null;
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#getEnvelope(boolean)
	 */
	public Element getEnvelope(boolean deep) {
		if (!deep)
			return envelope;
		
		synchronized (this) {
			if (envelope == null)
				return null;
			
			Element clonedEnvelope = (Element) envelope.cloneNode(false);
			
			if (headerContent != null && headerContent.length > 0)
				appendNode(clonedEnvelope, getHeader(true));
				
			appendNode(clonedEnvelope, getBody(true));
			
			return clonedEnvelope;	
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#setHeader(org.w3c.dom.Element)
	 */
	public synchronized void setHeader(Element header) {
		if (header == null)
			this.header = null;			
		else
			this.header = (Element) header.cloneNode(false);

		xml = null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#getHeader(boolean)
	 */
	public Element getHeader(boolean deep) {
		if (!deep)
			return header;
		
		synchronized (this) {
			if (header == null)
				return null;
			
			Element clonedHeader = (Element) header.cloneNode(false);
			appendChildren(clonedHeader, headerContent);				
			return clonedHeader;
		}				
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#setHeaderContent(org.w3c.dom.Element[])
	 */
	public synchronized void setHeaderContent(Element[] headerContent) {
		this.headerContent = headerContent;
		xml = null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#getHeaderContent()
	 */
	public Element[] getHeaderContent() {
		return headerContent;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#setBody(org.w3c.dom.Element)
	 */
	public synchronized void setBody(Element body) {		
		if (body == null)
			this.body = null;
		else {
			this.body = (Element) body.cloneNode(false);
			
			if (!context.isDocumentStyle() && fault == null) {
				NodeList childElements = body.getElementsByTagName("*");
				if (childElements.getLength() > 0)
					this.body.appendChild(childElements.item(0).cloneNode(false));
			}
		}		
		xml = null;
	}
					
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#getBody(boolean)
	 */
	public Element getBody(boolean deep) {
		if (!deep)
			return body;
		
		synchronized (this) {
			if (body == null)
				return null;
		
			// copy the rpc wrapper element as well, if any
			Element clonedBody = (Element) body.cloneNode(true);		
			
			if (fault != null)
				appendNode(clonedBody, fault);
			else {
				Element target = clonedBody;
				
				// check for rpc wrapper
				if (clonedBody.getFirstChild() != null)
					target = (Element) clonedBody.getFirstChild();
				
				appendChildren(target, bodyContent);
			}
				
			return clonedBody;		
		}
	}
				
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#setBodyContent(org.w3c.dom.Element[])
	 */
	public synchronized void setBodyContent(Element[] bodyContent) {
		this.bodyContent = bodyContent;
		if (bodyContent != null)
			fault = null;
		xml = null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#getBodyContent()
	 */
	public Element[] getBodyContent() {
		return bodyContent;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#setFault(org.w3c.dom.Element)
	 */
	public synchronized void setFault(Element fault) {
		this.fault = fault;
		if (fault != null) {
			if (body != null && body.getFirstChild() != null)
				body.removeChild(body.getFirstChild());
			bodyContent = null;
		}
		xml = null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#getFault()
	 */
	public Element getFault() {
		return fault;
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage#toXML()
	 */
	public synchronized String toXML() throws TransportException {
		if (xml == null)
			xml = serializer.serialize(ENVELOPE, this);
		return xml;		
	}		
}

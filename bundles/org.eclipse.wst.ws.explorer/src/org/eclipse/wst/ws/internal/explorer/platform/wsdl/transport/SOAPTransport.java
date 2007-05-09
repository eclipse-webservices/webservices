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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.transport;

import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPBinding;

import org.apache.axis.Constants;
import org.eclipse.wst.ws.internal.explorer.plugin.ExplorerPlugin;
import org.eclipse.wst.ws.internal.explorer.transport.IDeserializer;
import org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage;
import org.eclipse.wst.ws.internal.explorer.transport.ISOAPTransport;
import org.eclipse.wst.ws.internal.explorer.transport.ISerializer;
import org.eclipse.wst.ws.internal.explorer.transport.MessageContext;
import org.eclipse.wst.ws.internal.explorer.transport.SOAPMessage;
import org.eclipse.wst.ws.internal.explorer.transport.TransportException;
import org.eclipse.wst.ws.internal.parser.discovery.NetUtils;

/**
 * WSE's default implementation of ISOAPTransport
 */
public class SOAPTransport implements ISOAPTransport {

	static final String PROP_READ_ONLY = "prop_read_only";
	static final String PROP_RAW_BYTES = "prop_raw_bytes";
	
	/*
	 * Constructor.
	 */
	SOAPTransport() {}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPTransport#newSerializer()
	 */
	public ISerializer newSerializer() {		
		return new SOAPMessageProcessor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPTransport#newDeserializer()
	 */
	public IDeserializer newDeserializer() {
		return new SOAPMessageProcessor();
	}

	/*
	 * Check if the binding is supported
	 */
	private void checkBinding(ExtensibilityElement binding) throws TransportException {
		
		String bindingURI = binding.getElementType().getNamespaceURI();
		
		// looking for SOAP 1.1 binding
		if (!(binding instanceof SOAPBinding) ||
			!Constants.URI_WSDL11_SOAP.equals(bindingURI))
			throw new TransportException(ExplorerPlugin.getMessage("%MSG_ERROR_UNSUPPORTED_BINDING", new String[] { bindingURI }));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPTransport#newMessage(org.eclipse.wst.ws.internal.explorer.transport.MessageContext)
	 */
	public ISOAPMessage newMessage(MessageContext context) throws TransportException {
		
		checkBinding(context.getBindingProtocol());
		
		SOAPMessageProcessor processor = new SOAPMessageProcessor();
		ISOAPMessage message = new SOAPMessage(context, processor);
		processor.initMessage(message);			
		return message;				
	}
	
	/*
	 * Check if the message uses a binding and transport that we support.
	 */
	private void checkMessage(ISOAPMessage message) throws TransportException {
		
		ExtensibilityElement binding = message.getMessageContext().getBindingProtocol(); 
		checkBinding(binding);
		
		SOAPBinding soapBinding = (SOAPBinding) binding; 
			
		if (!Constants.URI_SOAP11_HTTP.equals(soapBinding.getTransportURI()))
			throw new TransportException(ExplorerPlugin.getMessage("%MSG_ERROR_UNSUPPORTED_TRANSPORT", new String[] { soapBinding.getTransportURI() }));
	}
		
	/*
	 * Create an HTTPTransport for internal use.
	 */
	private HTTPTransport createInternalTransport(String username, String password) {

		HTTPTransport internalTransport = new HTTPTransport();
		    
		if (username != null && password != null) {
			internalTransport.setHttpBasicAuthUsername(username);
			internalTransport.setHttpBasicAuthPassword(password);
		}
		
		return internalTransport;
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISOAPTransport#send(java.lang.String, java.lang.String, java.lang.String, org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage)
	 */
	public ISOAPMessage send(String url, String username, String password, ISOAPMessage message) throws TransportException {
		
		checkMessage(message);			
								
		try {
			HTTPTransport internalTransport = createInternalTransport(username, password);
			internalTransport.send(NetUtils.createURL(url), (String) message.getProperty(ISOAPMessage.PROP_SOAP_ACTION), message.toXML());
			
			SOAPMessageProcessor processor = new SOAPMessageProcessor();
			ISOAPMessage reply = new SOAPMessage(message.getMessageContext(), processor);
			reply.setProperty(PROP_READ_ONLY, Boolean.TRUE);
			
			byte[] rawBytes = internalTransport.receiveBytes();
			
			try { 
				processor.deserialize(ISOAPMessage.ENVELOPE, rawBytes, reply);
			}
			catch (Exception e) {
				// if error occurs during deserialization, we want to save a copy of the actual raw bytes
				reply.setProperty(PROP_RAW_BYTES, rawBytes);
			}
					
			return reply;
		}
		catch (TransportException e) {
			throw e;
	    }
	    catch (Exception e) {
	    	throw new TransportException(e);
	    }			
	}		
}

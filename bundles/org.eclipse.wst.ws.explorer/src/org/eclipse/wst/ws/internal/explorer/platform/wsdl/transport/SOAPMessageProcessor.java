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

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.Constants;
import org.eclipse.wst.ws.internal.explorer.platform.util.XMLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.util.SoapHelper;
import org.eclipse.wst.ws.internal.explorer.transport.IDeserializer;
import org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage;
import org.eclipse.wst.ws.internal.explorer.transport.ISerializer;
import org.eclipse.wst.ws.internal.explorer.transport.MessageContext;
import org.eclipse.wst.ws.internal.explorer.transport.TransportException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class provides implementation for WSE default SOAP transport's ISerializer and IDeserializer.
 */
public class SOAPMessageProcessor implements ISerializer, IDeserializer {
	
	private static final String LITERAL = "literal";
	private static final String DEFAULT_SOAP_ENCODING = "UTF-8";	
	private static final String LINE_SEPARATOR = System.getProperties().getProperty("line.separator");
	
	/*
	 * Constructor.
	 */
	SOAPMessageProcessor() {}
	
	/*
	 * Join the encodingStyles into a space separated string
	 */
	private String joinEncodingStyles(List list) {
		
		if (list.isEmpty())
			return null;
		
		StringBuffer sb = new StringBuffer();
		
		Iterator iter = list.iterator();
		while (iter.hasNext())
			sb.append(" ").append(iter.next());
		
		return sb.substring(1).toString();
	}
	
	/*
	 * Retrieves the encoding information from the binding for the selected operation.
	 * This method is called only for RPC style bindings.  Two pieces of information are returned:  
	 * If the use is encoded, this method returns the encoding namespace in element [0] and the 
	 * encoding style(s) in element [1].  If the use is literal, then both values will be null.
	 */
	private String[] getEncodingInfo(MessageContext context) {
		
		String[] info = new String[] { null, null };
		
		Iterator iter = context.getBindingOperation().getBindingInput()
			.getExtensibilityElements().iterator();
				
		// look for the soapbind:body extensilibity element
		while (iter.hasNext()) {
			ExtensibilityElement e = (ExtensibilityElement) iter.next();			
			if (!(e instanceof SOAPBody))
				continue;
			
			SOAPBody soapBody = (SOAPBody) e;
			
			// use="encoded"
			if (!LITERAL.equals(soapBody.getUse())) {
				info[0] = soapBody.getNamespaceURI();
				info[1] = joinEncodingStyles(soapBody.getEncodingStyles());
			}
					
			break;
		}
		
		// namespace not specified on the soapbind:body element, use the definition's
		if (info[0] == null)
			info[0] = context.getDefinition().getTargetNamespace();
		
		return info;
	}
	
	/*
     * WS-I: In a rpc-literal SOAP binding, the serialized child element of the 
     * soap:Body element consists of a wrapper element, whose namespace is the value 
     * of the namespace attribute of the soapbind:body element and whose local name is 
     * either the name of the operation or the name of the operation suffixed 
     * with "Response". The namespace attribute is required, as opposed to being 
     * optional, to ensure that the children of the soap:Body element are namespace-
     * qualified.
     */
	private Element createRPCWrapper(Document document, MessageContext context, Hashtable namespaceTable) {
		
		String encodingNamespaceURI = getEncodingInfo(context)[0];
		
		return SoapHelper.createRPCWrapperElement(
				document, 
				namespaceTable, 
				encodingNamespaceURI, 
				context.getBindingOperation().getOperation().getName(),
				null);
	}
	
	/*
	 * Initializes the contents of new ISOAPMessage.
	 */
	void initMessage(ISOAPMessage message) throws TransportException {
	
		try {
			Document document = XMLUtils.createNewDocument(null);
			
			Hashtable namespaceTable = new Hashtable();
			SoapHelper.addDefaultSoapEnvelopeNamespaces(namespaceTable);
			
			message.setEnvelope(SoapHelper.createSoapEnvelopeElement(document, namespaceTable));
			message.setHeader(SoapHelper.createSoapHeaderElement(document));
			
			Element body = SoapHelper.createSoapBodyElement(document);
			if (!message.getMessageContext().isDocumentStyle()) {
				Element rpcWrapper = createRPCWrapper(document, message.getMessageContext(), namespaceTable);
				body.appendChild(rpcWrapper);
			}			
			message.setBody(body);			
			
			message.setNamespaceTable(namespaceTable);
		}
		catch (ParserConfigurationException e) {
			throw new TransportException(e);
		}		
	}
	
	/*
	 * Returns the first element with the given local name from the soap envelope namespace
	 * This method can be used on elements that are not namespace aware, but we have to give
	 * it a namespace table to look up prefixes.
	 */
	private Element getSOAPElement(Element root, String localName, Map namespaceTable) {
		String prefix = (String) namespaceTable.get(Constants.URI_SOAP11_ENV);
		if (prefix == null)
			return null;
		
		NodeList list = root.getElementsByTagName(prefix + ":" + localName);
		if (list.getLength() == 0)
			return null;
		
		return (Element) list.item(0);
	}
	
	/*
	 * Returns the first element with the given local name from the soap envelope namespace.
	 * This version assume the elements are namespace aware.
	 */
	private Element getSOAPElementNS(Element root, String localName) {
		NodeList list = root.getElementsByTagNameNS(Constants.URI_SOAP11_ENV, localName);
		if (list.getLength() == 0)
			return null;		
	
		return (Element) list.item(0);
	}
	
	/*
	 * Convert a NodeList to a vector of elements, nodes which are not elements are skipped.
	 */
	private Vector toElementVector(NodeList nodes) {
		
		Vector vector = new Vector();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element)
		    	vector.add(node);		    
		}
		
		return vector;	
	}
	
	/*
	 * Convert a NodeList to an array of elements, nodes which are not elements are skipped.
	 */
	private Element[] toElementArray(NodeList nodes) {
		Vector vector = toElementVector(nodes);
		Element[] elements = new Element[vector.size()];
		vector.copyInto(elements);
		return elements;
	}
	
	// Serialize ////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/*
	 * adds the encoding style attribute to the each element in the given array.
	 * noop if encodingStyle is null
	 */
	private void addEncodingStyle(Element[] elements, String encodingStyle) {
				
		// encodingStyle is non-null only if use="encoded"
		if (elements == null || encodingStyle == null)
			return;
		
		for (int i=0; i < elements.length; i++) {
	        if (elements[i] == null)
	        	continue;
	    	elements[i].setAttribute(
	    			Constants.NS_PREFIX_SOAP_ENV + ":" + Constants.ATTR_ENCODING_STYLE, encodingStyle);			
		}
	}
	
	/*
	 * Serialize an array of elements
	 */
	private String serializeElements(Element[] elements) {
	
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < elements.length; i++) {
					
			String serializedFragment = XMLUtils.serialize(elements[i], true);
			if (serializedFragment == null) {
				// On Some JRE's (Sun java 5) elements with an attribute with the xsi
				// prefix do not serialize properly because the namespace can not
				// be found so the string returned comes back as null. To workaround
				// this problem try adding in the namespace declaration attribute
				// and retry the serialization (bug 144824)			 
				elements[i].setAttribute("xmlns:xsi", Constants.URI_2001_SCHEMA_XSI);
				serializedFragment = XMLUtils.serialize(elements[i], true);
			}
			
			buffer.append(serializedFragment);
			buffer.append(LINE_SEPARATOR);
		}
		
		return buffer.toString();
	}

	/*
	 * Determines if the given message is a request message using RPC style
	 */
	private boolean isRPCRequest(ISOAPMessage message) {
		return 
			!message.getMessageContext().isDocumentStyle() &&							// rpc style
			!Boolean.TRUE.equals(message.getProperty(SOAPTransport.PROP_READ_ONLY));	// not read-only
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.ISerializer#serialize(int, org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage)
	 */
	public String serialize(int part, ISOAPMessage message) throws TransportException {

		switch (part) {
			case ISOAPMessage.ENVELOPE:
				Element envelope = message.getEnvelope(true);
				
				if (isRPCRequest(message)) {					
					Element body = getSOAPElementNS(envelope, Constants.ELEM_BODY);
					if (body == null)
						body = getSOAPElement(envelope, Constants.ELEM_BODY, message.getNamespaceTable());
					
					if (body != null) {
						Element[] bodyContent = toElementArray(body.getFirstChild().getChildNodes());
						addEncodingStyle(bodyContent, getEncodingInfo(message.getMessageContext())[1]);
					}
				}
				
				String xml = XMLUtils.serialize(envelope, true);
				if (xml == null)
					xml = new String((byte[]) message.getProperty(SOAPTransport.PROP_RAW_BYTES));
				
				return xml;
				
			case ISOAPMessage.HEADER_CONTENT:
				return serializeElements(message.getHeaderContent());
				
			case ISOAPMessage.BODY_CONTENT:				
				Element[] bodyContent = message.getBodyContent();
				
				if (isRPCRequest(message))
					addEncodingStyle(bodyContent, getEncodingInfo(message.getMessageContext())[1]);
				
				return serializeElements(bodyContent);
		}
		
		return "";
	}
	
	// Deserialize //////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
	/*
	 * Retrieve the soap header from the envelope and populate the given message
	 */
	private void processHeader(Element envelope, ISOAPMessage message) {
		
		Element header = getSOAPElementNS(envelope, Constants.ELEM_HEADER);
		if (header == null)
			return;
		
		message.setHeader(header);
		message.setHeaderContent(toElementArray(header.getChildNodes()));
	}
	
	/*
	 * HACK - The root element tag name of the instance document
	 * is ambiguous.  It lands on a very grey area between the SOAP
	 * spec and the WSDL spec.  The two specs do not explicitly define
	 * that the root element tag name must match the name of the
	 * WSDL part.  The hack is to treat elements with different tag names
	 * as instances of the WSDL part.
	 */
	private Element[] fixSOAPResponse(NodeList instanceList, MessageContext context) {
		
		Vector instanceVector = toElementVector(instanceList);		
		Element[] instanceDocuments = new Element[instanceVector.size()];
		
		Operation oper = context.getBindingOperation().getOperation();		
		Map partsMap = oper.getOutput().getMessage().getParts();
		
		if (partsMap.size() == 1) {
			Iterator it = partsMap.values().iterator();
			Part part = (Part) it.next();
			
			String fragName;
			if (part.getElementName() != null)
				fragName = part.getElementName().getLocalPart();
			else
				fragName = part.getName();
		    
		    for (int i = 0; i < instanceVector.size(); i++) {
		    	Element element = (Element) instanceVector.get(i);
		    	
		    	if (!element.getTagName().equals(fragName)) {
		    		Document doc = element.getOwnerDocument();
		    		NodeList children = element.getChildNodes();
		    		NamedNodeMap attributes = element.getAttributes();
		    		element = doc.createElement(fragName);
		    		
		    		for (int j = 0; j < children.getLength(); j++) {
		    			if (children.item(j) != null) {
		    				element.appendChild(children.item(j));
				            // When you append a node from one element to another,
				            // the original element will lose its reference to this node,
				            // therefore, the size of the node list will decrease by 1.
				            j--;
		    			}
		    		}
		    		
		    		for (int j = 0; j < attributes.getLength(); j++) {
		    			Object attr = attributes.item(j);
		    			if (attr != null && (attr instanceof Attr)) {
		    				Attr attribute = (Attr)attr;
		    				element.setAttribute(attribute.getName(), attribute.getValue());
		    			}
		    		}
		    	}
		    	instanceDocuments[i] = element;
		    }
		}
		else
			instanceVector.copyInto(instanceDocuments);
		
		return instanceDocuments;
	}
	
	/*
	 * Retrieve the soap header from the envelope and populate the given message
	 */
	private void processBody(Element envelope, ISOAPMessage message) {
		
		Element body = getSOAPElementNS(envelope, Constants.ELEM_BODY);
		if (body == null)
			return;				
		
		message.setBody(body);
		
		// check for soap fault
		Element fault = getSOAPElementNS(body, Constants.ELEM_FAULT);
	    if (fault != null) {
	    	message.setFault(fault);
	    	return;
	    }
	        	        
	    NodeList instanceList;
	    
	    if (message.getMessageContext().isDocumentStyle())
	    	instanceList = body.getChildNodes();
	    else {
	        NodeList rpcWrapper = body.getElementsByTagNameNS("*", 
	        		message.getMessageContext().getBindingOperation().getOperation().getOutput().getMessage().getQName().getLocalPart());

	        /*
	        * HACK - Some of the web services out on the internet do not
	        * set their RPC wrapper properly.  It should be set to the output
	        * message name of the selected operation.  The hack is to
	        * assume the first element inside the body element is the
	        * RPC wrapper.
	        */
	        if (rpcWrapper.getLength() <= 0)
	        	rpcWrapper = body.getElementsByTagNameNS("*", "*");

	        if (rpcWrapper.getLength() > 0)
	        	instanceList = rpcWrapper.item(0).getChildNodes();
	        else
	        	return;
	    }
	    
	    message.setBodyContent(fixSOAPResponse(instanceList, message.getMessageContext()));
	}
	
	/*
	 * Deserialize the byte array into the given soap message.  The part parameters tells
	 * the method whether the input is the whole envelop or just the contents of the
	 * header or body. 
	 */
	void deserialize(int part, byte[] xml, ISOAPMessage message) throws		
		ParserConfigurationException,
		SAXException,
		IOException {
		
		Element root = XMLUtils.byteArrayToElement(xml, true);		
		
		switch (part) {
			case ISOAPMessage.ENVELOPE:				 
				message.setEnvelope(root);
				processHeader(root, message);
				processBody(root, message);
				break;
			case ISOAPMessage.HEADER_CONTENT:
				message.setHeaderContent(toElementArray(root.getChildNodes()));
				break;
			case ISOAPMessage.BODY_CONTENT:
				message.setBodyContent(toElementArray(root.getChildNodes()));
				break;
		}
	}
	
	/*
	 * Tack on a root element the string which represents a series of elements.
	 * This is needed to deserialize the string.
	 */
	private String addRootElement(String xml, Map namespaceTable) {
		StringBuffer sb = new StringBuffer();
	    sb.append("<root");
	    
	    Iterator iter = namespaceTable.keySet().iterator();
	    while (iter.hasNext()) {
	    	Object uri = iter.next();
	    	Object prefix = namespaceTable.get(uri);
	    	sb.append(" ").append("xmlns:").append(prefix).append("=\"").append(uri).append("\"");
	    }	    
	    
	    sb.append(">").append(xml).append("</root>");
	    return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.transport.IDeserializer#deserialize(int, java.lang.String, org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage)
	 */
	public void deserialize(int part, String xml, ISOAPMessage message) throws TransportException {
		
		if (part != ISOAPMessage.ENVELOPE)
			xml = addRootElement(xml, message.getNamespaceTable());
		
		try {
			deserialize(part, xml.getBytes(DEFAULT_SOAP_ENCODING), message);
		}
		catch (Exception e) {
			throw new TransportException(e);
		}
	}
}

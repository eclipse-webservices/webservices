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
 * 20070510   186375 makandre@ca.ibm.com - Andrew Mak, Compile errors in wst.ws.explorer
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Part;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeader;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * This class has some common routines used by the Web Services explorer to work with ISOAPMessages.
 */
public class SOAPMessageUtils {
	
	/**
	 * Decodes namespace declarations that have been encoded by WSE into the operation element of the WSDL model.
	 * The decoded declarations will be added to the given namespace table.
	 *  
	 * @param soapEnvelopeNamespaceTable The namespace table to populate.
	 * @param operElement The operation element with the encoded namespace declarations.
	 * @return True if new namespace declarations are found and added to the namespace table, false otherwise.
	 */
	public static boolean decodeNamespaceTable(Hashtable soapEnvelopeNamespaceTable, WSDLOperationElement operElement) {
			
		boolean updated = false;

		String[] nsDeclarations = (String[]) operElement.getPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT_NAMESPACE);
		if (nsDeclarations == null)
			return false;

		// each namespace declaration is decoded into 2 parts: the prefix 
		// in element [0] and the namespace uri in element [1].
		for (int i = 0; i < nsDeclarations.length; i++) {
			String[] prefix_ns = SoapHelper.decodeNamespaceDeclaration(nsDeclarations[i]);
			
			// check if the namespace uri is already a key in the namespace table
			// if it is, don't add it
			if (!soapEnvelopeNamespaceTable.containsKey(prefix_ns[1])) {
			    soapEnvelopeNamespaceTable.put(prefix_ns[1], prefix_ns[0]);
			    updated = true;
			}
		}
		
		return updated;
	}

	/**
	 * Populate the header content of the given ISOAPMessage using the values from WSE's fragment model
	 * 
	 * @param soapEnvelopeNamespaceTable A namespace table to use during this operation.
	 * @param operElement Access WSE's fragments via this operation element. 
	 * @param soapMessage The ISOAPMessage to populate.
	 * @throws ParserConfigurationException
	 */
	public static void setHeaderContentFromModel(Hashtable soapEnvelopeNamespaceTable, WSDLOperationElement operElement, ISOAPMessage soapMessage)
		throws ParserConfigurationException {
	    
		Vector headerEntries = new Vector();
	    
	    Iterator it = operElement.getSOAPHeaders().iterator();
	    while (it.hasNext()) {
	    	SOAPHeader soapHeader = (SOAPHeader) it.next();
	    	boolean isUseLiteral = "literal".equals(soapHeader.getUse());           
	    	IXSDFragment frag = (IXSDFragment) operElement.getHeaderFragment(soapHeader);
	    	Element[] instanceDocuments = frag.genInstanceDocumentsFromParameterValues(
	    			!isUseLiteral, soapEnvelopeNamespaceTable, soapMessage.getEnvelope(false).getOwnerDocument());
	    	for (int i = 0; i < instanceDocuments.length; i++) {
	    		if (instanceDocuments[i] == null)
	    			continue;
	    		headerEntries.addElement(instanceDocuments[i]);
	    	}
	    }

	    Element[] headerContent = new Element[headerEntries.size()];
	    headerEntries.copyInto(headerContent);
	    
	    soapMessage.setHeaderContent(headerContent);
	}

	/**
	 * Populate the body content of the given ISOAPMessage using the values from WSE's fragment model
	 * 
	 * @param soapEnvelopeNamespaceTable A namespace table to use during this operation.
	 * @param operElement Access WSE's fragments via this operation element. 
	 * @param soapMessage The ISOAPMessage to populate.
	 * @throws ParserConfigurationException
	 */
	public static void setBodyContentFromModel(Hashtable soapEnvelopeNamespaceTable, WSDLOperationElement operElement, ISOAPMessage soapMessage)
		throws ParserConfigurationException {
		
	    Vector bodyEntries = new Vector();
	    boolean isUseLiteral = operElement.isUseLiteral();

	    Iterator it = operElement.getOrderedBodyParts().iterator();
	    while (it.hasNext()) {
	    	Part part = (Part)it.next();
	    	IXSDFragment frag = (IXSDFragment) operElement.getFragment(part);
	    	Element[] instanceDocuments = frag.genInstanceDocumentsFromParameterValues(
	    			 !isUseLiteral, soapEnvelopeNamespaceTable, soapMessage.getEnvelope(false).getOwnerDocument());
	    	for (int i = 0; i < instanceDocuments.length; i++) {
	    		if (instanceDocuments[i] == null)
	    			continue;
	    		bodyEntries.addElement(instanceDocuments[i]);
	    	}
	    }

	    Element[] bodyContent = new Element[bodyEntries.size()];
	    bodyEntries.copyInto(bodyContent);
	    
	    soapMessage.setBodyContent(bodyContent);
	}
	
	/*
	 * If the attribute is a namespace declaration, the namespace prefix is returned.
	 * Otherwise null is returned.
	 */
	private static String getNSPrefix(Attr attribute) {
		String name = attribute.getName();
		if (name.startsWith("xmlns:"))
			return name.substring(6);
		return null;
	}
	
	/*
	 * lookup the prefix of the namespaceURI, assuming that it is declared on the element itself
	 */
	private static String lookupPrefix(Element element, String namespaceURI) {
		
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Attr attribute = (Attr) attributes.item(i);
			String prefix = getNSPrefix(attribute);
			if (prefix != null && attribute.getValue().equals(namespaceURI))
				return prefix;
		}		
		
		return null;
	}
	
	/**
	 * Given an array of elements, this method will return the index of the first element that matches
	 * the given Part.  A match is determined by comparing the fully qualified names of the Part and Elements.
	 * 
	 * @param part The Part 
	 * @param elements The array of Elements
	 * @param namespaceTable A namespace table for looking up namespace prefixes.
	 * @param fragName Name of the Part's fragment (essentially the Part's local name).
	 * @param start The index to begin the search.
	 * @return The index of the first matching element, or -1 if no match is found.
	 */
	public static int findFirstMatchingElement(Part part, Element[] elements, Map namespaceTable, String fragName, int start) {
		
		String namespaceURI = null;
		String prefix = null;
		
		// try to get the prefix
		if (part.getElementName() != null) {
			namespaceURI = part.getElementName().getNamespaceURI();
			prefix = (String) namespaceTable.get(namespaceURI);					
		}
		
		for (int i = start; i < elements.length; i++) {			
			Element element = elements[i];
			String name = element.getTagName();
			
			// try to get prefix again, the namespace declaration can be directly on the element
			if (prefix == null && namespaceURI != null && element.hasAttributes())
				prefix = lookupPrefix(element, namespaceURI);
			
			if (prefix == null && name.equals(fragName))
				return i;
			else if (prefix != null && name.equals(prefix + ":" + fragName))
				return i;
		}
				
		return -1;
	}
}

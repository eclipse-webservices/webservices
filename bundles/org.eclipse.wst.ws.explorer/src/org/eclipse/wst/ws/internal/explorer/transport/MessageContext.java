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

import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.extensions.ExtensibilityElement;

/**
 * A MessageContext object holds information about the message that is to be
 * sent on a web service invocation.  This information is passed to the
 * ISOAPTransport so that it can construct an appropriate ISOAPMessage to
 * handle the invocation.
 */
public class MessageContext {
	
	private Definition definition;
	private ExtensibilityElement bindingProtocol;
	private BindingOperation bindingOperation;	
	private boolean documentStyle;			
	
	/**
	 * Sets a reference to the WSDL definition.
	 * 
	 * @param definition The WSDL definition.
	 */
	public void setDefinition(Definition definition) {
		this.definition = definition;
	}
	
	/**
	 * Returns the WSDL definition.
	 * 
	 * @return The WSDL definition.
	 */
	public Definition getDefinition() {
		return definition;
	}
	
	/**
	 * Sets a reference to the binding extensibility element in the WSDL document.
	 * For a web service that uses the SOAP protocol, this will be an instance of
	 * javax.wsdl.extensions.soap.SOAPBinding
	 * 
	 * @param bindingProtocol The binding extensibility element.
	 */
	public void setBindingProtocol(ExtensibilityElement bindingProtocol) {
		this.bindingProtocol = bindingProtocol;		
	}
	
	/**
	 * Returns the binding extensibility element.
	 * 
	 * @return The binding extensibility element.
	 */
	public ExtensibilityElement getBindingProtocol() {
		return bindingProtocol;
	}
	
	/**
	 * Sets a reference to the binding operation element in the WSDL document.
	 * 
	 * @param bindingOperation The binding operation element.
	 */
	public void setBindingOperation(BindingOperation bindingOperation) {
		this.bindingOperation = bindingOperation;
	}
	
	/**
	 * Returns the binding operation element.
	 * 
	 * @return The binding operation element.
	 */
	public BindingOperation getBindingOperation() {
		return bindingOperation;
	}		
		
	/**
	 * Sets the flag on whether the message created from this MessageContext
	 * is document style or not.
	 * 
	 * @param documentStyle True for document style, false otherwise.
	 */
	public void setDocumentStyle(boolean documentStyle) {
		this.documentStyle = documentStyle;
	}
	
	/**
	 * Returns the document style property.
	 * 
	 * @return The document style property.
	 */
	public boolean isDocumentStyle() {
		return documentStyle;
	}
}

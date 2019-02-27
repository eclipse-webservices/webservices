/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.filter;

import org.eclipse.wst.wsdl.binding.mime.internal.util.MIMEConstants;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;

public class SoapExtensiblityElementFilter extends AbstractExtensibilityElementFilter
{   
  public boolean isValidContext(Element parentElement, String localName)
  {
  	boolean result = false;

  	if (parentElement != null)
  	{ 
  	  String parentElementName = parentElement.getLocalName(); 
  	  String parentNamespaceURI = parentElement.getNamespaceURI();

  	  if (WSDLConstants.WSDL_NAMESPACE_URI.equals(parentNamespaceURI))
  	  {
  	    if (WSDLConstants.BINDING_ELEMENT_TAG.equals(parentElementName))
  	    {
  	      result = WSDLConstants.BINDING_ELEMENT_TAG.equals(localName);
  	    }	  
  	    else if (isWSDLBindingOperation(parentElement))
  	    {
  	      result = WSDLConstants.OPERATION_ELEMENT_TAG.equals(localName);
  	    }
  	    else if (isWSDLBindingOperation(parentElement.getParentNode()))
  	    {
  	      if ((WSDLConstants.INPUT_ELEMENT_TAG.equals(parentElementName) ||
  	          WSDLConstants.OUTPUT_ELEMENT_TAG.equals(parentElementName)))
  	      {
  	        result = SOAPConstants.HEADER_ELEMENT_TAG.equals(localName) ||
  	          SOAPConstants.BODY_ELEMENT_TAG.equals(localName);
  	      }	
  	      else if (WSDLConstants.FAULT_ELEMENT_TAG.equals(parentElementName))
  	      {
  	        result = WSDLConstants.FAULT_ELEMENT_TAG.equals(localName);
  	      }
  	    }
  	    else if (WSDLConstants.PORT_ELEMENT_TAG.equals(parentElementName))
  	    {
  	      result = SOAPConstants.ADDRESS_ELEMENT_TAG.equals(localName);
  	    }
  	  }
  	  else if (SOAPConstants.SOAP_NAMESPACE_URI.equals(parentNamespaceURI))
  	  {
  	    if (SOAPConstants.HEADER_ELEMENT_TAG.equals(parentElementName))
  	    {
  	      result = SOAPConstants.HEADER_FAULT_ELEMENT_TAG.equals(localName);
  	    }
  	  }
  	  else if (MIMEConstants.MIME_NAMESPACE_URI.equals(parentNamespaceURI))
  	  {
  	    if (MIMEConstants.PART_ELEMENT_TAG.equals(parentElementName))
  	    {
  	      result = SOAPConstants.BODY_ELEMENT_TAG.equals(localName);
  	    }
  	  }
  	}    
    return result;
  }     
}

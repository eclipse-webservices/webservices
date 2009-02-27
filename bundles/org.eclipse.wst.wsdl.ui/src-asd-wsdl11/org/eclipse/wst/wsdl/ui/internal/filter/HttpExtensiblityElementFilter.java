/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.filter;

import org.eclipse.wst.wsdl.binding.http.internal.util.HTTPConstants;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


public class HttpExtensiblityElementFilter extends AbstractExtensibilityElementFilter
{  
  public boolean isValidContext(Element parentElement, String localName)
  {
  	boolean result = false;   
    
    if (parentElement != null && WSDLConstants.WSDL_NAMESPACE_URI.equals(parentElement.getNamespaceURI()))
    { 
      String parentElementName = parentElement.getLocalName();
      if (WSDLConstants.BINDING_ELEMENT_TAG.equals(parentElementName)) 
      {
        result = HTTPConstants.BINDING_ELEMENT_TAG.equals(localName); 	   
      }	  
      else if (isWSDLBindingOperation(parentElement))
      {
        result = HTTPConstants.OPERATION_ELEMENT_TAG.equals(localName); 
      }
      else if (isWSDLBindingOperation(parentElement.getParentNode())) 
      {
        if (WSDLConstants.INPUT_ELEMENT_TAG.equals(parentElementName))
        {	   	   	  
          result = HTTPConstants.URL_ENCODED_ELEMENT_TAG.equals(localName) ||
          HTTPConstants.URL_REPLACEMENT_ELEMENT_TAG.equals(localName);	  
        }	
      }
      else if (WSDLConstants.PORT_ELEMENT_TAG.equals(parentElementName))
      {
        result = HTTPConstants.ADDRESS_ELEMENT_TAG.equals(localName);
      }
    }
    return result;
  }     
}

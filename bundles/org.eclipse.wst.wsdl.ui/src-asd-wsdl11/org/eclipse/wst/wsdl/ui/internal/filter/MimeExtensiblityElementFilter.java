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

import org.eclipse.wst.wsdl.binding.mime.internal.util.MIMEConstants;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


public class MimeExtensiblityElementFilter extends AbstractExtensibilityElementFilter
{   
  public boolean isValidContext(Element parentElement, String localName)
  {
  	boolean result = false;

  	if (parentElement != null)
  	{
  	  String parentElementNamespace = parentElement.getNamespaceURI();
      String parentElementName = parentElement.getLocalName();
      
  	  if (WSDLConstants.WSDL_NAMESPACE_URI.equals(parentElementNamespace))
  	  {
  	    if (isWSDLBindingOperation(parentElement.getParentNode()))
  	    {
  	      if (WSDLConstants.INPUT_ELEMENT_TAG.equals(parentElementName) ||
  	          WSDLConstants.OUTPUT_ELEMENT_TAG.equals(parentElementName))
  	      {
  	        result = MIMEConstants.CONTENT_ELEMENT_TAG.equals(localName) ||
  	        MIMEConstants.MULTIPART_RELATED_ELEMENT_TAG.equals(localName) ||
  	        MIMEConstants.MIME_XML_ELEMENT_TAG.equals(localName);
  	      }
  	    }
  	  }
  	  else if (MIMEConstants.MIME_NAMESPACE_URI.equals(parentElementNamespace))
  	  {
        if (MIMEConstants.MULTIPART_RELATED_ELEMENT_TAG.equals(parentElementName))
        {
          result = MIMEConstants.PART_ELEMENT_TAG.equals(localName);
        }
        else if (MIMEConstants.PART_ELEMENT_TAG.equals(parentElementName))
        {
          result = MIMEConstants.CONTENT_ELEMENT_TAG.equals(localName);
        }
  	  }
  	}
    return result;
  }     
}

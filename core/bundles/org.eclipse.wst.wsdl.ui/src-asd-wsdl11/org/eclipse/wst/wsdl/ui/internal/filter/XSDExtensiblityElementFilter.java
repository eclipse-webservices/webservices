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

import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Element;

public class XSDExtensiblityElementFilter extends AbstractExtensibilityElementFilter
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
  	    if (WSDLConstants.TYPES_ELEMENT_TAG.equals(parentElementName))
  	    {
  	      result = XSDConstants.SCHEMA_ELEMENT_TAG.equals(localName);
  	    }	
  	  }
  	}
    return result;
  }     
}

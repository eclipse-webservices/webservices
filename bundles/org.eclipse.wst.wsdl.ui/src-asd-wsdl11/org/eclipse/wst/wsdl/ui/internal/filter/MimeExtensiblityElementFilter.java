/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
import org.w3c.dom.Element;


public class MimeExtensiblityElementFilter implements ExtensiblityElementFilter
{   
  private static final String MIME_NAMESPACE_URI="http://schemas.xmlsoap.org/wsdl/mime/"; //$NON-NLS-1$
  public MimeExtensiblityElementFilter()
  {
  }  	
	
  public boolean isValidContext(Element parentElement, String localName)
  {
  	boolean result = false;

    String parentElementName = parentElement.getLocalName();
    String parentElementNamespace = parentElement.getNamespaceURI();

  	if (WSDLConstants.WSDL_NAMESPACE_URI.equals(parentElementNamespace))
  	{
    	if (parentElementName.equals("input") || parentElementName.equals("output")) //$NON-NLS-1$ //$NON-NLS-2$
    	{
    	  	result = localName.equals("content") || //$NON-NLS-1$
				     localName.equals("multipartRelated") || //$NON-NLS-1$
				     localName.equals("mimeXml");    	  	 //$NON-NLS-1$
    	}
  	}
  	else if (MIME_NAMESPACE_URI.equals(parentElementNamespace))
  	{
		result = true;				
  	}
    return result;
  }     
}

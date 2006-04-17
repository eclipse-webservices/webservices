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

import org.w3c.dom.Element;


public class HttpExtensiblityElementFilter implements ExtensiblityElementFilter
{  
  public HttpExtensiblityElementFilter()
  {
  }  	
	
  public boolean isValidContext(Element parentElement, String localName)
  {
  	boolean result = false;
    
    String parentElementName = parentElement.getLocalName();
	if (parentElementName.equals("binding")) //$NON-NLS-1$
	{
	  result = localName.equals("binding"); 	   //$NON-NLS-1$
	}	  
	else if (parentElementName.equals("operation")) //$NON-NLS-1$
	{
	  result = localName.equals("operation");  //$NON-NLS-1$
	}
	else if (parentElementName.equals("input") ||  //$NON-NLS-1$
	         parentElementName.equals("output")) //$NON-NLS-1$
	{
	  result = localName.equals("body") ||  //$NON-NLS-1$
	           localName.equals("header"); 	   //$NON-NLS-1$
	}	
	else if (parentElementName.equals("fault")) //$NON-NLS-1$
	{
	  result = localName.equals("fault"); 	   //$NON-NLS-1$
	}
	else if (parentElementName.equals("port")) //$NON-NLS-1$
	{
	  result = localName.equals("address"); 	   //$NON-NLS-1$
	}	
    
    return result;
  }     
}

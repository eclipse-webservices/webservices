/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
	if (parentElementName.equals("binding"))
	{
	  result = localName.equals("binding"); 	  
	}	  
	else if (parentElementName.equals("operation"))
	{
	  result = localName.equals("operation"); 
	}
	else if (parentElementName.equals("input") || 
	         parentElementName.equals("output"))
	{
	  result = localName.equals("body") || 
	           localName.equals("header"); 	  
	}	
	else if (parentElementName.equals("fault"))
	{
	  result = localName.equals("fault"); 	  
	}
	else if (parentElementName.equals("port"))
	{
	  result = localName.equals("address"); 	  
	}	
    
    return result;
  }     
}

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
package org.eclipse.wst.wsdl.binding.http.internal.util;

import org.w3c.dom.Element;

public final class HTTPConstants 
{
  public static final String ADDRESS_ELEMENT_TAG = "address";
  public static final String BINDING_ELEMENT_TAG = "binding";
  public static final String OPERATION_ELEMENT_TAG = "operation";
  public static final String URL_ENCODED_ELEMENT_TAG = "urlEncoded";
  public static final String URL_REPLACEMENT_ELEMENT_TAG = "urlReplacement";
  
  public static final String HTTP_NAMESPACE_URI = "http://schemas.xmlsoap.org/wsdl/http/";

  public static final String LOCATION_URI_ATTRIBUTE ="location";
  public static final String VERB_ATTRIBUTE ="verb";
  
  public static String getAttribute(Element element, String attributeName)
  {
    return element.hasAttribute(attributeName) ? element.getAttribute(attributeName) : null;
  }
}

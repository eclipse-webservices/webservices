/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap.internal.util;

import org.w3c.dom.Element;

public final class SOAPConstants 
{
  public static final String ADDRESS_ELEMENT_TAG = "address";
  public static final String BINDING_ELEMENT_TAG = "binding";
  public static final String BODY_ELEMENT_TAG = "body";
  public static final String FAULT_ELEMENT_TAG = "fault";
  public static final String HEADER_ELEMENT_TAG = "header";
  public static final String HEADER_FAULT_ELEMENT_TAG = "headerfault";
  public static final String OPERATION_ELEMENT_TAG = "operation";
	  
  public static final String SOAP_NAMESPACE_URI = "http://schemas.xmlsoap.org/wsdl/soap/";
 
  public static final String SOAP_ACTION_ATTRIBUTE ="soapAction";
  public static final String STYLE_ATTRIBUTE ="style";
  public static final String LOCATION_ATTRIBUTE ="location";
  public static final String TRANSPORT_ATTRIBUTE ="transport";
  public static final String USE_ATTRIBUTE ="use";
  public static final String NAMESPACE_ATTRIBUTE ="namespace";
  public static final String NAMESPACE_URI_ATTRIBUTE ="namespaceURI";
  public static final String NAME_ATTRIBUTE ="name";
  public static final String ENCODING_STYLE_ATTRIBUTE ="encodingStyle";
  public static final String MESSAGE_ATTRIBUTE ="message";
  public static final String PART_ATTRIBUTE ="part";
  public static final String PARTS_ATTRIBUTE ="parts";
  
  public static String getAttribute(Element element, String attributeName)
  {
    return element.hasAttribute(attributeName) ? element.getAttribute(attributeName) : null;
  }
}

/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.binding.http.internal.util;


import org.w3c.dom.Element;


public final class HTTPConstants
{
  public static final String ADDRESS_ELEMENT_TAG = "address"; //$NON-NLS-1$

  public static final String BINDING_ELEMENT_TAG = "binding"; //$NON-NLS-1$

  public static final String OPERATION_ELEMENT_TAG = "operation"; //$NON-NLS-1$

  public static final String URL_ENCODED_ELEMENT_TAG = "urlEncoded"; //$NON-NLS-1$

  public static final String URL_REPLACEMENT_ELEMENT_TAG = "urlReplacement"; //$NON-NLS-1$

  public static final String HTTP_NAMESPACE_URI = "http://schemas.xmlsoap.org/wsdl/http/"; //$NON-NLS-1$

  public static final String LOCATION_URI_ATTRIBUTE = "location"; //$NON-NLS-1$

  public static final String VERB_ATTRIBUTE = "verb"; //$NON-NLS-1$

  public static String getAttribute(Element element, String attributeName)
  {
    return element.hasAttribute(attributeName) ? element.getAttribute(attributeName) : null;
  }
}

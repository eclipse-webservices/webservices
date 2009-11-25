/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.tests.internal;


import java.util.List;

import javax.wsdl.extensions.ElementExtensible;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11Validator;


/**
 * An extensions validator used to test that a registered extensions 
 * validator is called by the main WSDL 1.1 validator when validating
 * definitions level extensibility elements.
 */
public class SimpleExtensionsValidator implements IWSDL11Validator
{
  public static final String NS_URI = "http://org.eclipse.wst.wsdl.validation.tests/Extensions"; //$NON-NLS-1$

  public void validate(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    ExtensibilityElement extensibilityElement = (ExtensibilityElement)element;
    QName elementType = extensibilityElement.getElementType();
    String namespaceURI = elementType.getNamespaceURI();
    
    if (parents.isEmpty() || !(parents.get(0) instanceof ElementExtensible))
    {
      valInfo.addWarning("The current parent is expected to have extensibility elements.", element); //$NON-NLS-1$
    }
    
    if (NS_URI.equals(namespaceURI))
    {
      valInfo.addWarning("The test extensions validator got called.", element); //$NON-NLS-1$
    }
    else
    {
      valInfo.addError("Bad extensibility element namespace.", element); //$NON-NLS-1$
    }
  }
}
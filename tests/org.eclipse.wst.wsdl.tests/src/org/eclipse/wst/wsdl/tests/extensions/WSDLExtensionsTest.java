/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.extensions;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.wst.wsdl.WSDLElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import junit.framework.TestCase;


/**
 * Base class for WSDL extension tests.
 */
public abstract class WSDLExtensionsTest extends TestCase
{
  /**
   * Constructs a test with the given name. 
   * @param name the test name.
   */
  public WSDLExtensionsTest(String name)
  {
    super(name);
  }

  /**
   * Tests a String attribute reconciliation. The code changes the attribute value through the DOM and compares it with the value obtained by calling eGet.
   * @param wsdlElement the WSDL element to test
   * @param attributeName the attribute name
   * @param expectedValue the expected value
   * @param feature the EMF feature of the attribute to test. 
   */
  protected void checkStringAttributeReconciliation(
    WSDLElement wsdlElement,
    String attributeName,
    String expectedValue,
    EStructuralFeature feature)
  {
    EObject eObject = (EObject)wsdlElement;

    String initialValue = (String)eObject.eGet(feature);
    
    Element element = wsdlElement.getElement();
    eObject.eUnset(feature);
    Attr attribute = element.getAttributeNode(attributeName);
    assertNull(attribute);

    eObject.eSet(feature, initialValue);
    
    attribute = element.getAttributeNode(attributeName);
    attribute.setValue(expectedValue);

    String actualValue = (String)eObject.eGet(feature);
    assertEquals(expectedValue, actualValue);
    
    element.removeAttribute(attributeName);
    actualValue = (String)eObject.eGet(feature);
    assertNull(actualValue);

    eObject.eSet(feature, initialValue);
  }
}
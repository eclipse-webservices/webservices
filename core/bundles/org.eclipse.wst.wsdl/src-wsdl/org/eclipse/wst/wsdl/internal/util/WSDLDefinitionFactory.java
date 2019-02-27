/*******************************************************************************
 * Copyright (c) 2001, 2010 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.internal.util;


import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLReader;

import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.impl.WSDLFactoryImpl;

import com.ibm.wsdl.xml.WSDLReaderImpl;


public class WSDLDefinitionFactory extends com.ibm.wsdl.factory.WSDLFactoryImpl
{

  private static WSDLDefinitionFactory instance = null;

  public WSDLDefinitionFactory()
  {
    // Make sure the WSDL package is initialized.
    WSDLPackage.eINSTANCE.eClass();
  }

  /**
   * Create a new instance of Definition.
   */
  public javax.wsdl.Definition newDefinition()
  {
    javax.wsdl.Definition definition = WSDLFactoryImpl.eINSTANCE.createDefinition();
    return definition;
  }

  /**
   * Returns a singleton instance of this factory
   */
  public static javax.wsdl.factory.WSDLFactory getInstance() throws WSDLException
  {
    if (instance == null)
      instance = new WSDLDefinitionFactory();
    return instance;
  }

  /**
   * Creates a WSDLReader.
   */
  public WSDLReader newWSDLReader()
  {
    WSDLReader reader = new WSDLReaderImpl();
    reader.setFactoryImplName(getClass().getName());
    return reader;
  }

}

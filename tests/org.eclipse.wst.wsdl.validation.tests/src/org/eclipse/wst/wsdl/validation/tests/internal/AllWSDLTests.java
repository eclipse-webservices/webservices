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
package org.eclipse.wst.wsdl.validation.tests.internal;
import junit.framework.Test;

import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolverTest;
import org.eclipse.wst.wsdl.validation.internal.ui.ant.WSDLValidateTest;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaGeneratorTest;
import org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalogTest;
/**
 * The root test suite that contains all other WSDL validator test suites.
 */
public class AllWSDLTests extends junit.framework.TestSuite
{
  /**
   * Create this test suite.
   * 
   * @return This test suite.
   */
  public static Test suite()
  {
    return new AllWSDLTests();
  }
  
  /**
   * Constructor
   */
  public AllWSDLTests()
  {
    super("AllWSDLTests");
    addTest(XSDTest.suite());
    addTest(WSDLTest.suite());
    addTest(PathsTest.suite());
    addTest(URIResolverTest.suite());
    addTest(WSDLValidateTest.suite());
    addTest(XMLCatalogTest.suite());
    addTest(InlineSchemaGeneratorTest.suite());
  }
}
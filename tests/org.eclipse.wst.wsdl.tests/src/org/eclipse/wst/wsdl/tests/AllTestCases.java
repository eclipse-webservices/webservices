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
package org.eclipse.wst.wsdl.tests;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Kihup Boo
 */
public class AllTestCases extends TestCase
{

  public AllTestCases()
  {
  }

  public static void main(String[] args)
  {
  }
  
  public static Test suite() 
  {
    TestSuite suite = new TestSuite();
    
    // Bug 123547 - comment out this test for now.
    // suite.addTest(InlineSchemaTest.suite());  
    suite.addTest(LoadAndSerializationTest.suite());
    suite.addTest(SemanticTest.suite());
    suite.addTest(WSDLGenerationTest.suite());
    suite.addTest(WSDL4JAPITest.suite());
    suite.addTest(WSDLEMFAPITest.suite());
	suite.addTest(UtilTest.suite());
    
    return suite;
  }

}

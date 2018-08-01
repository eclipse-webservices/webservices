/*******************************************************************************
 * Copyright (c) 2001, 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.tests;


import org.eclipse.wst.wsdl.tests.extensions.HTTPExtensionsTest;
import org.eclipse.wst.wsdl.tests.extensions.MIMEExtensionsTest;
import org.eclipse.wst.wsdl.tests.extensions.SOAPExtensionsTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Kihup Boo
 */
public class AllTestCases extends TestSuite
{

  public AllTestCases()
  {
    super();
    addTest(InlineSchemaTest.suite());
    addTest(new InlineSchemaTest("InlineSchema")
      {
        protected void runTest()
        {
          testInlineSchema();
        }
      }
    );

    addTest(new InlineSchemaTest("InlineSchemaWithWSDL4J")
      {
        protected void runTest()
        {
          testInlineSchemaWithWSDL4J();
        }
      }
    );
    
    
    addTest(LoadAndSerializationTest.suite());
    addTest(SemanticTest.suite());
    addTest(WSDLGenerationTest.suite());
    addTest(WSDL4JAPITest.suite());
    addTest(WSDLEMFAPITest.suite());
    addTest(UtilTest.suite());
    addTest(BugFixesTest.suite());
    addTestSuite(LocationTrackingTest.class);
    addTest(RefactoringTest.suite());
    addTest(SOAPExtensionsTest.suite());
    addTest(HTTPExtensionsTest.suite());
    addTest(MIMEExtensionsTest.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new AllTestCases();
    return suite;
  }
}

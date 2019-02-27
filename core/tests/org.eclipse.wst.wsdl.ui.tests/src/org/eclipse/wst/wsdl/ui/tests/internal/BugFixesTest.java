/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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

package org.eclipse.wst.wsdl.ui.tests.internal;


import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.util.XSDTypeSystemProvider;


/**
 * Contains unit tests for reported bugs.
 */
public class BugFixesTest extends BaseTestCase
{
  public BugFixesTest(String name)
  {
    super(name);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(BugFixesTest.class.getName());

    suite.addTest(new BugFixesTest("BuiltInTypes") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testBuiltInTypes();
        }
      });
    return suite;
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=243800
   */
  public void testBuiltInTypes()
  {
    try
    {
      Definition definition = getDefinition(TC_ROOT_FOLDER + "/BuiltInType/NewWSDLFile1.wsdl");  //$NON-NLS-1$

      XSDTypeSystemProvider provider = new XSDTypeSystemProvider();
      List types = provider.getBuiltInTypeNamesList(definition);
      assertTrue("Unable to find built in schema types", !types.isEmpty());  //$NON-NLS-1$
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
  }
}

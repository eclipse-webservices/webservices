/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.tests;


import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;


public class ParserTest extends TestCase
{

  public ParserTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(new ParserTest("Parser")
      {
        protected void runTest()
        {
          testParser();
        }
      });

    return suite;
  }

  public void testParser()
  {
    EntityResolver myResolver = new MyResolver();
    try
    {
      InputStream is = new FileInputStream("./samples/LoadAndPrintTest.wsdl");
      DocumentBuilder myBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      myBuilder.setEntityResolver(myResolver);
      Document doc = myBuilder.parse(is);
      Assert.assertNotNull("Document is null", doc);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }

  public static void main(String[] args)
  {
  }

}

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

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.wst.wsdl.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.w3c.dom.Element;

/**
 * @author Kihup Boo
 */
public class LoadAndSerializationTest extends TestCase
{    
  /**
   * Executes a stand-alone test.
   * @param objects an array of Strings from the command line.
   * @see #run
   */
  public static void main(String args[]) 
  {
    junit.textui.TestRunner.run(suite());
  }
  
  /**
   * Creates an instance.
   */
  public LoadAndSerializationTest(String name) 
  {
    super(name);
  }
  
  public static Test suite() 
  {
    TestSuite suite = new TestSuite();
    
    suite.addTest
      (new LoadAndSerializationTest("Load") 
         {
           protected void runTest() 
           {
             testLoad();
           }
         }
       );
    
    suite.addTest
      (new LoadAndSerializationTest("Print") 
        {
          protected void runTest() 
          {
            testPrint();
          }
        }
      );
    return suite;
  }
  
  protected void setUp() throws Exception 
  {
    super.setUp();
    
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl());
    WSDLPackage pkg = WSDLPackage.eINSTANCE;
    
    // We need this for XSD <import>.
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl());
    XSDPackage xsdpkg = XSDPackage.eINSTANCE; 
  }

  protected void tearDown() throws Exception 
  {
    super.tearDown();
  }  
  
  static private Definition definition = null;
  
  /**
   * Load the WSDL definitions file and print information about it.
   */
  public void testLoad()
  {
    try
    {
      definition = DefinitionLoader.load("./samples/LoadAndPrintTest.wsdl");
      Assert.assertTrue(definition.eResource() instanceof WSDLResourceImpl);
      WSDLResourceImpl wsdlResource = (WSDLResourceImpl)definition.eResource();
      System.out.println("<!-- ===== Definition Composition =====");
      printDefinitionStart(definition);
      System.out.println("-->");
      Element element = definition.getElement();
      Assert.assertNotNull(definition.getElement());
      if (element != null)
      {
        // Print the serialization of the model.
        //
        WSDLResourceImpl.serialize(System.out, element);
      }
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }  
  }
  
  public void testPrint()
  {
    try
    {
      // This removes the associated DOM element, creates a new associated DOM element, and then prints it.
      // This is a good test for how well serialization works for a model created "bottom up".
      //
      definition.setDocument(null);
      definition.setElement(null);
      ((DefinitionImpl)definition).updateElement();
      System.out.println("<!-- [ definitions: " + definition.getQName() + " ] -->");
      WSDLResourceImpl.serialize(System.out, definition.getDocument());
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }   
  }
  
  /**
   * Prints a header tag for the given WSDL definitions.
   * @param definition a WSDL definition.
   */
  protected void printDefinitionStart(Definition definition)
  {
    System.out.print("<definitions name=\"");
    if (definition.getQName() != null)
    {
      System.out.print(definition.getQName().getLocalPart());
    }
    
    System.out.print("\" targetNamespace=\"");
    if (definition.getTargetNamespace() != null)
    {
      System.out.print(definition.getTargetNamespace());
    }       
    System.out.println("\">");
  }
    
}

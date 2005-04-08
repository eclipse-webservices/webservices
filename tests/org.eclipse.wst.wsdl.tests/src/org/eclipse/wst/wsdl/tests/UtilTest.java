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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.WSDLPlugin;
//import org.eclipse.wst.wsdl.internal.extensibility.ExtensibilityElementFactoryRegistryImpl;
//import org.eclipse.wst.wsdl.internal.extensibility.ExtensibilityElementFactoryRegistryReader;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.wst.wsdl.util.ExtensibilityElementFactory;
// import org.eclipse.wst.wsdl.util.ExtensibilityElementFactoryRegistry;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.wsdl.extensions.soap.SOAPConstants;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UtilTest extends TestCase {

	  public UtilTest(String name) 
	  {
	    super(name);
	  }
	
	  public static void main(String args[]) 
	  {
	    junit.textui.TestRunner.run(suite());
	  }

	  public static Test suite() 
	  {
	    TestSuite suite = new TestSuite();
	    
	    suite.addTest
	      (new UtilTest("WSDLConstants") 
	         {
	           protected void runTest() 
	           {
				 testConstants();
	           }
	         }
	       );
	    
	    suite.addTest
	      (new UtilTest("WSDLResourceFactoryImpl") 
	        {
	          protected void runTest() 
	          {
	            testWSDLResourceFactoryImpl();
	          }
	        }
	      );
		
	    suite.addTest
	      (new UtilTest("WSDLResourceImpl") 
	        {
	          protected void runTest() 
	          {
	            testWSDLResourceImpl();
	          }
	        }
	      );
	    suite.addTest
	      (new UtilTest("ExtensibilityElementFactory") 
	        {
	          protected void runTest() 
	          {
	            testExtensibilityElementFactory();
	          }
	        }
	      );
	    suite.addTest
	      (new UtilTest("ExtensibilityElementFactoryRegistry") 
	        {
	          protected void runTest() 
	          {
	            testExtensibilityElementFactoryRegistry();
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
	  
	  public void testConstants()
	  {
        try
		{
		  WSDLConstants wsdlConstants = new WSDLConstants();
		  int type = WSDLConstants.nodeType(WSDLConstants.PORT_ELEMENT_TAG);
		  Assert.assertTrue("Node type is not that of port", type == 10);
		  
	      definition = DefinitionLoader.load("./samples/LoadAndPrintTest.wsdl");
	      Assert.assertTrue(definition.eResource() instanceof WSDLResourceImpl);

		  int definitionNodeType = WSDLConstants.nodeType(definition.getElement());
		  Assert.assertTrue("Node type is not that of definition", definitionNodeType == 1);
		  
		  boolean isWSDLNamespace = WSDLConstants.isWSDLNamespace(definition.getTargetNamespace());
		  Assert.assertFalse("This should not be the WSDL Namespace", isWSDLNamespace);
		  
		  boolean isMatching = WSDLConstants.isMatchingNamespace("http://www.example.org", "http://www.example.org");  
		  Assert.assertTrue("isMatchingNamespace is incorrect", isMatching);
		  
		  String attr = WSDLConstants.getAttribute(definition.getElement(), "name");
		  Assert.assertTrue("getAttribute is incorrect", attr.equals("LoadAndPrintTest"));
		}
		catch (Exception e)
	    {
	      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
		}
	  }
  
	  public void testWSDLResourceFactoryImpl()
	  {
	    try
	    {
		  WSDLResourceFactoryImpl factoryImpl = new WSDLResourceFactoryImpl();
		  Resource resource = factoryImpl.createResource(URI.createFileURI("./samples/createResourceTest.wsdl"));
          Assert.assertTrue("Resource is not of type WSDLResourceImpl", resource instanceof WSDLResourceImpl);
	    }
	    catch (Exception e)
	    {
	      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
	    }   
	  }
	  
	  public void testWSDLResourceImpl()
	  {
	    try
		{
		  WSDLResourceImpl resourceImpl = new WSDLResourceImpl(URI.createFileURI("./samples/createResourceTest.wsdl"));
          Assert.assertTrue("Resource is not of type WSDLResourceImpl", resourceImpl instanceof WSDLResourceImpl);
		  
	      definition = DefinitionLoader.load("./samples/LoadAndPrintTest.wsdl");
	      Assert.assertTrue(definition.eResource() instanceof WSDLResourceImpl);
		  Element element = definition.getElement();
		  Document document = definition.getDocument();

	      if (element != null)
	      {
	        resourceImpl.serialize(System.out, element, null);
	      }
		  
		  if (document != null)
		  {
			resourceImpl.serialize(System.out, document, null);
		  }
		}
		catch (Exception e)
		{
		  Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
		}   
	  }
	  
	  public void testExtensibilityElementFactory()
	  {
	    try
	    {
		    ExtensibilityElementFactory factory = WSDLPlugin.INSTANCE.getExtensibilityElementFactory(SOAPConstants.NS_URI_SOAP);
		    if (factory != null)
			{
		      ExtensibilityElement ee = factory.createExtensibilityElement(SOAPConstants.NS_URI_SOAP, SOAPConstants.ELEM_BODY);
              Assert.assertTrue("Problem creating SOAP extensibility element", ee != null);
			}
	    }
		catch (Exception e)
		{
		  Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
		}   
	  }
	  
	  public void testExtensibilityElementFactoryRegistry()
	  {
	    try
	    {
	    }
		catch (Exception e)
		{
		  Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
		}   
	  }

}

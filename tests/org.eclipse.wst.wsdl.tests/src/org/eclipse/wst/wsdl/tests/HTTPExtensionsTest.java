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

package org.eclipse.wst.wsdl.tests;


import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.binding.http.HTTPAddress;
import org.eclipse.wst.wsdl.binding.http.HTTPBinding;
import org.eclipse.wst.wsdl.binding.http.HTTPFactory;
import org.eclipse.wst.wsdl.binding.http.HTTPOperation;
import org.eclipse.wst.wsdl.binding.http.HTTPPackage;
import org.eclipse.wst.wsdl.binding.http.HTTPUrlEncoded;
import org.eclipse.wst.wsdl.binding.http.HTTPUrlReplacement;
import org.eclipse.wst.wsdl.binding.http.internal.util.HTTPConstants;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;


/**
 * Tests the SOAP binding extensions. 
 */
public class HTTPExtensionsTest extends TestCase
{
  private static final String ADDRESS_LOCATION_URI = "http://www.example.org/"; //$NON-NLS-1$

  private static final HTTPFactory HTTP_FACTORY = HTTPFactory.eINSTANCE;

  private static final String HTTP_LOCATION_URI = "http://www.example.org/HTTPTest/NewOperation";

  private static final String PLUGIN_ABSOLUTE_PATH = WSDLTestsPlugin.getInstallURL();

  private static final String TARGET_NAMESPACE = "http://www.example.org/HTTPTest/"; //$NON-NLS-1$

  private static final String VERB_GET = "GET"; //$NON-NLS-1$

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(new HTTPExtensionsTest("HTTPExtensionsCreation") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testHTTPExtensionsCreation();
        }
      });

    suite.addTest(new HTTPExtensionsTest("HTTPExtensionsReconciliation") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testHTTPExtensionsReconciliation();
        }
      });

    return suite;
  }

  public HTTPExtensionsTest(String name)
  {
    super(name);
  }

  public void testHTTPExtensionsCreation()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/Extensions/HTTP/HTTPTest.wsdl"); //$NON-NLS-1$

      String bindingName = "HTTPTestHTTP"; //$NON-NLS-1$
      QName bindingQName = new QName(TARGET_NAMESPACE, bindingName);
      Binding binding = (Binding)definition.getBinding(bindingQName);

      addHTTPBinding(binding);

      BindingOperation bindingOperation = (BindingOperation)binding.getBindingOperation("NewOperation", null, null); //$NON-NLS-1$

      addHTTPOperation(bindingOperation);

      BindingInput bindingInput = bindingOperation.getEBindingInput();

      addHTTPBindingInput(bindingInput);
   
      addHTTPAddress(definition);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
  }

  public void testHTTPExtensionsReconciliation()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/Extensions/HTTP/HTTPExample.wsdl", true); //$NON-NLS-1$

      String serviceName = "HTTPExample"; //$NON-NLS-1$
      String targetNamespace = "http://www.example.org/HTTPExample/"; //$NON-NLS-1$
      QName serviceQName = new QName(targetNamespace, serviceName);
      Service service = (Service)definition.getService(serviceQName);
      service.toString();

      Port port = (Port)service.getPort("HTTPExampleHTTP"); //$NON-NLS-1$
      List extensibilityElements = port.getExtensibilityElements();
      assertEquals(1, extensibilityElements.size());
      HTTPAddress httpAddress = (HTTPAddress)extensibilityElements.get(0);
      
      String locationURI = httpAddress.getLocationURI();
      assertEquals("http://www.example.org/", locationURI);
      
      Element httpAddressElement = httpAddress.getElement();
      Attr locationURIAttribute = httpAddressElement.getAttributeNode(HTTPConstants.LOCATION_URI_ATTRIBUTE);

      String expectedLocationURI = "test"; //$NON-NLS-1$ 
      locationURIAttribute.setValue(expectedLocationURI);
      
      locationURI = httpAddress.getLocationURI();
      assertEquals(expectedLocationURI, locationURI);
      
      String bindingName = "HTTPExampleHTTP"; //$NON-NLS-1$
      QName bindingQName = new QName(targetNamespace, bindingName);
      Binding binding = (Binding)definition.getBinding(bindingQName);
      
      extensibilityElements = binding.getExtensibilityElements();
      assertEquals(1, extensibilityElements.size());
      HTTPBinding httpBinding = (HTTPBinding)extensibilityElements.get(0);
      
      String verb = httpBinding.getVerb();
      assertEquals(VERB_GET, verb);

      Element httpBindingElement = httpBinding.getElement();
      Attr verbAttribute = httpBindingElement.getAttributeNode(HTTPConstants.VERB_ATTRIBUTE);
      
      String expectedVerb = VERB_GET; //$NON-NLS-1$ 
      verbAttribute.setValue(expectedVerb);
      
      verb = httpBinding.getVerb();
      assertEquals(expectedVerb, verb);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
  }

  private void addHTTPAddress(Definition definition)
  {
    HTTPAddress httpAddress = HTTP_FACTORY.createHTTPAddress();
    EObject httpAddressEObject = (EObject)httpAddress;
    httpAddress.setLocationURI(ADDRESS_LOCATION_URI);
    String locationURI = (String)httpAddressEObject.eGet(HTTPPackage.Literals.HTTP_ADDRESS__LOCATION_URI);
    assertEquals(ADDRESS_LOCATION_URI, locationURI);
    assertTrue(httpAddressEObject.eIsSet(HTTPPackage.Literals.HTTP_ADDRESS__LOCATION_URI));

    httpAddress.toString();

    String serviceName = "HTTPTest"; //$NON-NLS-1$
    QName serviceQName = new QName(TARGET_NAMESPACE, serviceName);
    Service service = (Service)definition.getService(serviceQName);
    service.toString();

    Port port = (Port)service.getPort("HTTPTestHTTP"); //$NON-NLS-1$
    port.addExtensibilityElement(httpAddress);
    port.toString();
  }

  private void addHTTPBinding(Binding binding)
  {
    binding.toString();

    HTTPBinding httpBinding = HTTP_FACTORY.createHTTPBinding();
    binding.addExtensibilityElement(httpBinding);
    EObject httpBindingEObject = ((EObject)httpBinding);

    httpBinding.setVerb(VERB_GET);
    String style = (String)httpBindingEObject.eGet(HTTPPackage.Literals.HTTP_BINDING__VERB);
    assertEquals(VERB_GET, style);
    assertTrue(httpBindingEObject.eIsSet(HTTPPackage.Literals.HTTP_BINDING__VERB));

    httpBinding.toString();
  }

  private void addHTTPBindingInput(BindingInput bindingInput)
  {
    bindingInput.toString();

    HTTPUrlEncoded httpURLEncoded = HTTP_FACTORY.createHTTPUrlEncoded();
    bindingInput.addExtensibilityElement(httpURLEncoded);

    bindingInput.getExtensibilityElements().clear();

    HTTPUrlReplacement httpURLReplacement = HTTP_FACTORY.createHTTPUrlReplacement();
    bindingInput.addExtensibilityElement(httpURLReplacement);
  }

  private void addHTTPOperation(BindingOperation bindingOperation)
  {
    bindingOperation.toString();

    HTTPOperation httpOperation = HTTP_FACTORY.createHTTPOperation();
    EObject httpOperationEObject = (EObject)httpOperation;

    httpOperation.setLocationURI(HTTP_LOCATION_URI);
    String locationURI = (String)httpOperationEObject.eGet(HTTPPackage.Literals.HTTP_OPERATION__LOCATION_URI);
    assertEquals(HTTP_LOCATION_URI, locationURI);
    assertTrue(httpOperationEObject.eIsSet(HTTPPackage.Literals.HTTP_OPERATION__LOCATION_URI));

    httpOperation.toString();

    bindingOperation.addExtensibilityElement(httpOperation);
  }
}
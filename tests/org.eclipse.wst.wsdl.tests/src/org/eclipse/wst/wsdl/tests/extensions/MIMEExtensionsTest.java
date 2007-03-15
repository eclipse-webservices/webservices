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


import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.binding.mime.MIMEContent;
import org.eclipse.wst.wsdl.binding.mime.MIMEFactory;
import org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml;
import org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated;
import org.eclipse.wst.wsdl.binding.mime.MIMEPart;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.tests.WSDLTestsPlugin;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;


/**
 * Tests the MIME binding extensions. 
 */
public class MIMEExtensionsTest extends TestCase
{
  private static final MIMEFactory MIME_FACTORY = MIMEFactory.eINSTANCE;

  private static final SOAPFactory SOAP_FACTORY = SOAPFactory.eINSTANCE;

  private static final String PLUGIN_ABSOLUTE_PATH = WSDLTestsPlugin.getInstallURL();

  private static final String TARGET_NAMESPACE = "http://www.example.org/MIMETest"; //$NON-NLS-1$

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(new MIMEExtensionsTest("MIMEExtensionsCreation") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testMIMEExtensionsCreation();
        }
      });

    suite.addTest(new MIMEExtensionsTest("EMFSerialization") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testMIMEEMFSerialization();
        }
      });

    suite.addTest(new MIMEExtensionsTest("MIMEExtensionsReconciliation") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testMIMEExtensionsReconciliation();
        }
      });

    return suite;
  }

  public MIMEExtensionsTest(String name)
  {
    super(name);
  }

  public void testMIMEExtensionsCreation()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/Extensions/MIME/MIMETest.wsdl"); //$NON-NLS-1$

      String bindingName = "MIMETestBinding"; //$NON-NLS-1$
      QName bindingQName = new QName(TARGET_NAMESPACE, bindingName);
      Binding binding = (Binding)definition.getBinding(bindingQName);

      BindingOperation bindingOperation = (BindingOperation)binding.getBindingOperation("testOperation", null, null); //$NON-NLS-1$

      BindingOutput bindingOutput = bindingOperation.getEBindingOutput();

      MIMEMultipartRelated multipart = MIME_FACTORY.createMIMEMultipartRelated();
      bindingOutput.addExtensibilityElement(multipart);
      multipart.toString();

      MIMEPart mimePart = MIME_FACTORY.createMIMEPart();
      multipart.addMIMEPart(mimePart);

      SOAPBody soapBody = SOAP_FACTORY.createSOAPBody();
      soapBody.setUse("literal");
      mimePart.addExtensibilityElement(soapBody);
      mimePart.toString();

      mimePart = MIME_FACTORY.createMIMEPart();
      multipart.addMIMEPart(mimePart);

      MIMEContent mimeContent = MIME_FACTORY.createMIMEContent();
      mimeContent.setPart("responseData");
      mimeContent.setType("text/binary");
      mimeContent.toString();
      
      mimePart.addExtensibilityElement(mimeContent);

      List mimeParts = multipart.getMIMEParts();
      assertEquals(2, mimeParts.size());
      
      MIMEMimeXml mimeXML = MIME_FACTORY.createMIMEMimeXml();
      bindingOutput.addExtensibilityElement(mimeXML);
      mimeXML.setPart("xmlData");
      mimeXML.toString();
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
  }

  public void testMIMEExtensionsReconciliation()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/Extensions/MIME/MIMEExample.wsdl", true); //$NON-NLS-1$
      String targetNamespace = "http://www.example.org/MIMEExample"; //$NON-NLS-1$
      String bindingName = "MIMEExampleBinding"; //$NON-NLS-1$
      QName bindingQName = new QName(targetNamespace, bindingName);
      definition.getBinding(bindingQName);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
  }

  /**
   * This method loads a WSDL model then saves it using the default EMF serialization
   * instead of the WSDL XML format, then loads it again. The intent is to exercise
   * the EMF e* methods to aid in identifying real code coverage issues.  
   */
  public void testMIMEEMFSerialization()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/Extensions/MIME/MIMEExample.wsdl", true); //$NON-NLS-1$

      ResourceSet resourceSet = new ResourceSetImpl();
      URI fileURI = URI.createFileURI(PLUGIN_ABSOLUTE_PATH + "samples/generated/MIMEExample.xml");
      Resource resource = resourceSet.createResource(fileURI);
      resource.getContents().add(definition);
      resource.save(null);
      resourceSet = new ResourceSetImpl();
      resource = resourceSet.getResource(fileURI, false);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
  }
}
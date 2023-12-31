/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

package org.eclipse.wst.wsdl.tests;


import java.io.FileInputStream;
import java.util.Iterator;

import javax.wsdl.xml.WSDLReader;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLFactoryImpl;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDParticleContent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.xml.sax.InputSource;


/**
 * @author Kihup Boo
 */
public class InlineSchemaTest extends TestCase
{
  private String PLUGIN_ABSOLUTE_PATH = WSDLTestsPlugin.getInstallURL();

  public InlineSchemaTest(String name)
  {
    super(name);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(new InlineSchemaTest("InlineSchema")
      {
        protected void runTest()
        {
          testInlineSchema();
        }
      });

    suite.addTest(new InlineSchemaTest("InlineSchemaWithWSDL4J")
      {
        protected void runTest()
        {
          testInlineSchemaWithWSDL4J();
        }
      });

    return suite;
  }

  public void testInlineSchema()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/LoadStoreCompare/LoadAndPrintTest.wsdl");
      traverseDefinition(definition);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }

  public void testInlineSchemaWithWSDL4J()
  {
    try
    {
      Definition definition = loadDefinitionForWSDL4J("samples/LoadStoreCompare/LoadAndPrintTest.wsdl");
      traverseDefinition(definition);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }

  public void traverseDefinition(Definition definition) throws Exception
  {
    Assert.assertNotNull(definition);

    Iterator iter = definition.getEImports().iterator();
    while (iter.hasNext())
    {
      Import myImport = (Import)iter.next();
      traverseImport(myImport);
    }

    // Get Inline Schema
    Types types = (org.eclipse.wst.wsdl.Types)definition.getTypes();
    Assert.assertNotNull("<types> is null", types);
    if (types != null)
    {
      Iterator iterator = types.getSchemas().iterator();
      Assert.assertTrue("<types> does not have inline <schema>s", iterator.hasNext());
      while (iterator.hasNext())
      {
        XSDSchema schema = (XSDSchema)iterator.next();
        traverseSchema(schema);
      }
    }
  }

  private void traverseImport(Import myImport) throws Exception
  {
    //    Definition definition = myImport.getEDefinition();
    //    traverseDefinition(definition);
  }

  private void traverseSchema(XSDSchema schema)
  {
    Iterator iterator = schema.getElementDeclarations().iterator();
    XSDElementDeclaration elementDecl = null;
    Assert.assertTrue("No <element>s are found", iterator.hasNext());
    while (iterator.hasNext())
    {
      elementDecl = (XSDElementDeclaration)iterator.next();

      if (elementDecl.getName().equals("NewOperationResponse"))
        traverseElementDecl(elementDecl);
      else if (elementDecl.getName().equals("NewOperationRequest"))
        traverseElementDecl(elementDecl);
    }
  }

  private void traverseElementDecl(XSDElementDeclaration elementDecl)
  {
    XSDTypeDefinition type = elementDecl.getTypeDefinition();
    Assert.assertTrue("<element> does not have <simpleType>", type instanceof XSDSimpleTypeDefinition);
    if (type.getComplexType() == null) // simple type
      return; // TBD - Currently this always returns at this point.
    XSDParticleContent content = type.getComplexType().getContent();
    traverseModelGroup((XSDModelGroup)content);
  }

  private void traverseModelGroup(XSDModelGroup modelGroup)
  {
    Iterator iterator = modelGroup.getContents().iterator();
    XSDParticleContent particleContent = null;
    while (iterator.hasNext())
    {
      particleContent = ((XSDParticle)iterator.next()).getContent();
      if (particleContent instanceof XSDElementDeclaration)
      {
        if (((XSDElementDeclaration)particleContent).isElementDeclarationReference())
          traverseElementDecl(((XSDElementDeclaration)particleContent).getResolvedElementDeclaration());
        else
          traverseElementDecl((XSDElementDeclaration)particleContent);
      }
    }
  }

  private Definition loadDefinitionForWSDL4J(String wsdlFile) throws Exception
  {
    WSDLReader reader = (new WSDLFactoryImpl()).newWSDLReader();
    String s = PLUGIN_ABSOLUTE_PATH + wsdlFile;
    Definition definition = (org.eclipse.wst.wsdl.Definition)reader.readWSDL(s, new InputSource(new FileInputStream(s)));
    return definition;
  }
}

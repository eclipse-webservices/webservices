/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.tests.internal;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLImportManager;
import org.eclipse.wst.wsdl.ui.tests.Activator;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceImpl;

public class BaseTestCase extends TestCase
{
  protected static final String PLUGIN_ABSOLUTE_PATH = Activator.getInstallURL();
  protected static final String RESOURCES_FOLDER = "testresources"; //$NON-NLS-1$
  protected static final String TC_ROOT_FOLDER = PLUGIN_ABSOLUTE_PATH + "/" + RESOURCES_FOLDER + "/WSDL"; //$NON-NLS-1$
  protected WSDLImportManager importManager = new WSDLImportManager();

  public BaseTestCase()
  {
  }

  public BaseTestCase(String name)
  {
    super(name);
  }

  protected XSDSchema getXSDSchema(String path)
  {
    URI uri = URI.createFileURI(path);
    ResourceSet resourceSet = new ResourceSetImpl();
    XSDResourceImpl resource = (XSDResourceImpl) resourceSet.getResource(uri, true);
    XSDSchema schema = resource.getSchema();
    assertNotNull(schema);
    return schema;
  }

  protected Definition getDefinition(String path)
  {
    URI uri = URI.createFileURI(path);
    ResourceSet resourceSet = new ResourceSetImpl();
    WSDLResourceImpl resource = (WSDLResourceImpl) resourceSet.getResource(uri, true);
    Definition definition = resource.getDefinition();
    assertNotNull(definition);
    return definition;
  }

}

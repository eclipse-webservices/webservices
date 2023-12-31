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
package org.eclipse.wst.wsdl.tests.util;


import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;


/**
 * @author Kihup Boo
 */
public final class DefinitionLoader
{
  /**
   * This class provides static methods only.
   */
  private DefinitionLoader()
  {
  }

  static public Definition load(String filename) throws IOException
  {
    return load(filename, false);
  }

  static public Definition load(String filename, boolean useExtensionFactories) throws IOException
  {
    return load(filename, useExtensionFactories, false);
  }

  static public Definition load(String filename, boolean useExtensionFactories, boolean trackLocation) throws IOException
  {
    // filename is an absolute path

    URI uri = null;
    uri = URI.createFileURI(filename);

    ResourceSet resourceSet = new ResourceSetImpl();
    resourceSet.getAdapterFactories().add(new WSDLModelLocatorAdapterFactory());
    resourceSet.getAdapterFactories().add(new XSDSchemaLocationResolverAdapterFactory());

    WSDLResourceImpl wsdlMainResource = (WSDLResourceImpl)resourceSet.createResource(URI.createURI("*.wsdl"));
    wsdlMainResource.setURI(uri);
    java.util.Map map = new Hashtable();
    map.put(WSDLResourceImpl.CONTINUE_ON_LOAD_ERROR, Boolean.valueOf(true));
    map.put(WSDLResourceImpl.USE_EXTENSION_FACTORIES, Boolean.valueOf(useExtensionFactories));
    map.put(WSDLResourceImpl.TRACK_LOCATION, Boolean.valueOf(trackLocation));
    wsdlMainResource.load(map);

    Definition definition = null;
    for (Iterator resources = resourceSet.getResources().iterator(); resources.hasNext();)
    {
      Object resource = resources.next();
      if (resource instanceof WSDLResourceImpl)
      {
        WSDLResourceImpl wsdlResource = (WSDLResourceImpl)resource;
        definition = wsdlResource.getDefinition();
        return definition;
      }
    }

    return null;
  }

  static public void store(Definition definition, String filename) throws IOException
  {
    // filename is an absolute path

    Resource resource = definition.eResource();
    resource.setURI(URI.createFileURI(filename));
    resource.save(null);
  }
}

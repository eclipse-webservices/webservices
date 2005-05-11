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
package org.eclipse.wst.wsdl.ui.internal.util;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.wst.common.uriresolver.URIResolverPlugin;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLModelLocator;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.xml.uriresolver.util.URIHelper;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class WSDLResourceUtil
{
  public static Resource createWSDLResource(ResourceSet resourceSet, Definition definition, String uri)
  {
    WSDLResourceFactoryImpl resourceFactory = new WSDLResourceFactoryImpl();
    Resource resource = resourceFactory.createResource(URI.createURI(uri));
    resourceSet.getResources().add(resource);
    resource.getContents().add(definition);
    resource.setModified(false);
    return resource;
  }
  
  public static Definition lookupAndLoadDefinition(ResourceSet resourceSet, String uri) {
  	Definition definition = null;
  	Resource resource = null;
  	try {
  	 InputStream inputStream = resourceSet.getURIConverter().createInputStream(URI.createURI(uri));
     resource = resourceSet.createResource(URI.createURI(uri));
     resource.load(inputStream, resourceSet.getLoadOptions());
  	}
  	catch(Exception e) {}
    
    if (resource == null)
    {
      try
      {
        definition = parse(resourceSet, uri);
      }
      catch (Exception e)
      {
      }
    }
    else
    {
      Object o = resource.getContents().size() > 0 ? resource.getContents().get(0) : null;
      if (o instanceof Definition)
      {
        definition = (Definition) o;
      }
    }
    return definition;
}

  public static Definition lookupOrCreateDefinition(ResourceSet resourceSet, String uri)
  {
    Definition definition = null;
    Resource resource = resourceSet.getResource(URI.createURI(uri), false);
    if (resource == null)
    {
      try
      {
        definition = parse(resourceSet, uri);
      }
      catch (Exception e)
      {
      }
    }
    else
    {
      Object o = resource.getContents().size() > 0 ? resource.getContents().get(0) : null;
      if (o instanceof Definition)
      {
        definition = (Definition) o;
      }
    }
    return definition;
  }

  public static Definition createDefinition(ResourceSet resourceSet, IFile file, Document document)
  {
    Definition definition = WSDLFactory.eINSTANCE.createDefinition();
    ((DefinitionImpl)definition).setUseExtensionFactories(true);
    String baseURI = URI.createPlatformResourceURI(file.getFullPath().toString()).toString();
    definition.setDocumentBaseURI(baseURI);
    try
    {
      Element element = document.getDocumentElement();
      if (element != null)
      {
        definition.setElement(element);    
      }
    }
    catch (Exception e)
    {
    }
    WSDLResourceUtil.createWSDLResource(resourceSet, definition, baseURI);
    ((DefinitionImpl)definition).reconcileReferences(true);    
    return definition;
  }

  public static Definition parse(ResourceSet resourceSet, String uri) throws Exception
  {
    Definition definition = null;
    Document document = null;
    ClassLoader prevClassLoader = Thread.currentThread().getContextClassLoader();

    try
    {
      Thread.currentThread().setContextClassLoader(WSDLResourceUtil.class.getClassLoader());
//      DOMParser builder = new DOMParser();
//      builder.parse(uri);
//      document = builder.getDocument();
      
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.parse(uri);
      
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(prevClassLoader);
    }

    definition = ((org.eclipse.wst.wsdl.WSDLPackage) EPackage.Registry.INSTANCE.getEPackage(org.eclipse.wst.wsdl.WSDLPackage.eNS_URI)).getWSDLFactory().createDefinition();
    definition.setDocumentBaseURI(uri);
    if (document != null)
    {
      Element element = document.getDocumentElement();
      if (element != null)
      {
        definition.setElement(element);
      }
    }
    WSDLResourceUtil.createWSDLResource(resourceSet, definition, uri);
    ((DefinitionImpl)definition).reconcileReferences(true);
    return definition;
  }

  public static void reloadDirectives(Definition definition)
  {
    Resource rootResource = definition.eResource();
    ResourceSet resourceSet = rootResource.getResourceSet();

    for (Iterator i = resourceSet.getResources().iterator(); i.hasNext();)
    {
      Resource resource = (Resource) i.next();
      if (resource != rootResource)
      {
        //  TODO... consider unloading the resources  	
        //  resource.unload();		      
        i.remove();
      }
    }
    ReloadDirectiveVisitor visitor = new ReloadDirectiveVisitor();
    visitor.visitDefinition(definition);
  }

  static class ReloadDirectiveVisitor
  {

    public void visitImport(Import theImport)
    {
      // force the import to reload
      Element element = WSDLEditorUtil.getInstance().getElementForObject(theImport);
      if (element != null)
      {
        ((WSDLElementImpl)theImport).elementChanged(element);
      }
    }

    public void visitXSDSchemaDirective(XSDSchemaDirective directive)
    {
      // force the schema directive to reload
      Element element = directive.getElement();
      if (element != null)
      {
        directive.elementAttributesChanged(element);
      }
    }

    public void visitDefinition(Definition definition)
    {
      if (definition != null)
      {
        for (Iterator i = definition.getEImports().iterator(); i.hasNext();)
        {
          visitImport((Import) i.next());
        }
        Types types = definition.getETypes();
        if (types != null)
        {

          for (Iterator i = types.getEExtensibilityElements().iterator(); i.hasNext();)
          {
            Object o = i.next();
            if (o instanceof XSDSchemaExtensibilityElement)
            {
              XSDSchemaExtensibilityElement e = (XSDSchemaExtensibilityElement) o;
              if (e.getSchema() != null)
              {
                visitSchema(e.getSchema());
              }
            }
          }
        }
      }
    }

    public void visitSchema(XSDSchema schema)
    {
      for (Iterator i = schema.getContents().iterator(); i.hasNext();)
      {
        Object o = i.next();
        if (o instanceof XSDSchemaDirective)
        {
          visitXSDSchemaDirective((XSDSchemaDirective) o);
        }
      }
    }
  }


  public static class InternalURIResolver implements WSDLModelLocator
  {
    //IdResolver idResolver = new IdResolverImpl(null, true);
    org.eclipse.wst.common.uriresolver.URIResolver resolver = URIResolverPlugin.createResolver();

    InternalURIResolver()
    {  
    }

    public String resolveURI(String baseLocation, String namespace, String location)
    {
      String resolvedLocation = resolver.resolve(baseLocation, namespace, location);

      // here's an ugly hack... Platform.resolve() returns Windows files with the pattern "file:D:/hello.xsd"
      // but EMF's URI class expects a pattern like this "file:///D:/hello.xsd"
      //        
      if (resolvedLocation != null)
      {
		String fileProtocol = "file:";      	
        if (resolvedLocation.startsWith(fileProtocol) && !resolvedLocation.startsWith(fileProtocol + "/"))
        {
          resolvedLocation = resolvedLocation.substring(fileProtocol.length());
        }
        resolvedLocation = URIHelper.addImpliedFileProtocol(resolvedLocation);
      }
      // end ugly hack
      return resolvedLocation;
    }
  }
}
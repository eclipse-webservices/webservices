/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.util;

import java.util.Iterator;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin;
import org.eclipse.wst.common.uriresolver.internal.util.URIHelper;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLModelLocator;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.w3c.dom.Element;


public class WSDLResourceUtil
{
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
    org.eclipse.wst.common.uriresolver.internal.provisional.URIResolver resolver = URIResolverPlugin.createResolver();

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
		String fileProtocol = "file:";      	 //$NON-NLS-1$
        if (resolvedLocation.startsWith(fileProtocol) && !resolvedLocation.startsWith(fileProtocol + "/")) //$NON-NLS-1$
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
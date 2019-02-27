/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.extensions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.util.XSDTypeSystemProvider;
import org.eclipse.xsd.XSDSchema;

public class ExtensibleTypeSystemProvider implements ITypeSystemProvider
{
  protected WSDLEditorExtension[] extensions;
  protected ITypeSystemProvider[] typeSystemProviders;

  protected final static Object[] EMPTY_ARRAY = {};

  public ExtensibleTypeSystemProvider()
  {
    typeSystemProviders = new ITypeSystemProvider[1]; 
    typeSystemProviders[0] = new XSDTypeSystemProvider();
  }          
        
  public List getAvailableTypeNames(Definition definition, int typeNameCategory)
  {
    List list = new ArrayList();
    for (int i = 0; i < typeSystemProviders.length; i++)
    {
      list.addAll(typeSystemProviders[i].getAvailableTypeNames(definition, typeNameCategory));
    }   
    return list;
  }
  
  public List getAvailableTypes(Definition definition, XSDSchema schema, int typeNameCategory)
  {
    List list = new ArrayList();
    for (int i = 0; i < typeSystemProviders.length; i++)
    {
      list.addAll(typeSystemProviders[i].getAvailableTypes(definition, schema, typeNameCategory));
    }   
    return list;
  }

  public List getAvailableElementNames(Definition definition)
  {
    List list = new ArrayList();
    for (int i = 0; i < typeSystemProviders.length; i++)
    {
      list.addAll(typeSystemProviders[i].getAvailableElementNames(definition));
    }     
    return list;
  }

  public int getCategoryForTypeName(Definition definition, String typeName)
  {
    int result = UNKNOWN_TYPE;
    for (int i = 0; i < typeSystemProviders.length; i++)
    {
      result = typeSystemProviders[i].getCategoryForTypeName(definition, typeName);
      if (result != UNKNOWN_TYPE)
      {
        break;
      }
    }                                                                                 
    return result;
  }
  
  public List getPrefixedNames(Definition definition, String namespace, String localName) {
  	 List list = new ArrayList();
     for (int i = 0; i < typeSystemProviders.length; i++)
     {
       list.addAll(typeSystemProviders[i].getPrefixedNames(definition, namespace, localName));
     }     
     return list;
  }
}
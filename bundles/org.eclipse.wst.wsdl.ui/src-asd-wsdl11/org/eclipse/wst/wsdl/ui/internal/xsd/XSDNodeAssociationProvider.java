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
package org.eclipse.wst.wsdl.ui.internal.xsd;
                                                                    
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.extensions.INodeAssociationProvider;


import org.eclipse.wst.xsd.ui.internal.adapters.CategoryAdapter;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XSDNodeAssociationProvider implements INodeAssociationProvider
{                    
  public Object getModelObject(Object parentModelObject, Element[] elementChain, int start, int[] end)
  {   
    Object result = null;
    if (parentModelObject instanceof XSDSchemaExtensibilityElement)
    {
      XSDSchemaExtensibilityElement extensibilityElement = (XSDSchemaExtensibilityElement)parentModelObject;
      XSDSchema schema = extensibilityElement.getSchema();
      if (schema != null)
      {          
        Element targetElement = elementChain[elementChain.length - 1];
        result = schema.getCorrespondingComponent(targetElement);
      }
    }
    else if (parentModelObject instanceof CategoryAdapter)
    {
      result = ((CategoryAdapter)parentModelObject).getXSDSchema();
    }
//    else if (parentModelObject instanceof Category)
//    {
//      result = ((Category)parentModelObject).getXSDSchema();
//    }

    return result;
  }

  public Node getNode(Object modelObject)
  {        
    Node node = null;
    if (modelObject instanceof XSDSchemaExtensibilityElement)
    {           
      XSDSchemaExtensibilityElement extensibilityElement = (XSDSchemaExtensibilityElement)modelObject;
      XSDSchema schema = extensibilityElement.getSchema();
      if (schema != null)
      {
        node = schema.getElement();
      }
    }        
    else if (modelObject instanceof XSDConcreteComponent)
    {
      node = ((XSDConcreteComponent)modelObject).getElement();
    }
    else if (modelObject instanceof CategoryAdapter)
    {
      node = ((CategoryAdapter)modelObject).getXSDSchema().getElement();
    }
//    else if (modelObject instanceof Category)
//    {
//      node = ((Category)modelObject).getXSDSchema().getElement();
//    }
    
    return node;
  }      
} 

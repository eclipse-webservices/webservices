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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.xsd.XSDNodeAssociationProvider;
import org.eclipse.wst.wsdl.ui.internal.extensions.INodeAssociationProvider;
import org.eclipse.wst.wsdl.ui.internal.extensions.WSDLNodeAssociationProvider;
import org.eclipse.wst.xsd.ui.internal.adapters.CategoryAdapter;
import org.eclipse.xsd.XSDConcreteComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class NodeAssociationManager
{                   
  protected INodeAssociationProvider wsdlProvider;
  protected INodeAssociationProvider xsdProvider;

  public NodeAssociationManager()
  {             
	  wsdlProvider = new WSDLNodeAssociationProvider();
	  xsdProvider = new XSDNodeAssociationProvider();
  }        

  protected INodeAssociationProvider getAppicableProvider(Object object)
  {
	  if (object instanceof XSDSchemaExtensibilityElement || 
	            object instanceof XSDConcreteComponent ||
	            object instanceof CategoryAdapter) {// ||
//	            object instanceof Category) {
		  return xsdProvider;
	  }
	  
	  return wsdlProvider;
  }
        

  public Object getModelObjectForNode(Object rootObject, Element targetNode)
  {                                                                       
    int currentIndex = 0;

    Element[] elementChain = getParentElementChain((Element)targetNode);

    INodeAssociationProvider p = getAppicableProvider(rootObject);
    if (p != null)
    {
      Node rootObjectNode = p.getNode(rootObject);

      while (currentIndex < elementChain.length)
      {
        Element e = elementChain[currentIndex];
        if (e == rootObjectNode)
        {                
          currentIndex++;
          break;
        }
        currentIndex++;
      }
    }
             
    Object currentObject = rootObject;                        
    int end[] = new int[1];

    while (currentIndex < elementChain.length)
    {               
      p = getAppicableProvider(currentObject);                                                                                             
      if (p != null)
      {
        end[0] = elementChain.length;
        currentObject = p.getModelObject(currentObject, elementChain, currentIndex, end);
        
        if (currentObject == null)
        {                      
          // failure
          break;
        }                              
        else
        {
          currentIndex = Math.max(end[0], currentIndex + 1);
        }
      }
      else
      {          
        break;
      } 
    }     

    return (currentIndex == elementChain.length) ? currentObject : null;
  }  
           

  public Node getNode(Object modelObject)
  {          
    Node result = null;                                                                 
    if (modelObject instanceof Node)
    {
      result = (Node)modelObject;
    }
    else
    {
      INodeAssociationProvider p = getAppicableProvider(modelObject);               
      if (p != null)
      {
        result = p.getNode(modelObject);
      }
    }       
    return result;
  }     

  protected Element[] getParentElementChain(Element element)
  {
    List list = new ArrayList();
    while (element != null)
    {
      list.add(0, element);
      Node node = element.getParentNode();
      element = (node != null && node.getNodeType() == Node.ELEMENT_NODE) ? (Element)node : null;    
    }                

    int listSize = list.size();
    Element[] result = new Element[listSize];
    for (int i = 0; i < listSize; i++)
    {
      result[i] = (Element)list.get(i);
    }             
    return result;
  }
}
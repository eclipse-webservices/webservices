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
package org.eclipse.wst.wsdl.ui.internal.extensions;      

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.eclipse.xsd.impl.XSDComponentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WSDLNodeAssociationProvider implements INodeAssociationProvider
{                    
  public Object getModelObject(Object parentModelObject, Element[] elementChain, int start, int[] end)
  {                 
    Object currentObject = parentModelObject;
    for (int i = start; i < elementChain.length; i++)
    {
      Object o = getModelObjectHelper(currentObject, elementChain[i]);
      if (o == null)
      {              
        end[0] = i;
        break;
      }            
      else
      {              
        currentObject = o;
      }
    }                                                               
    return currentObject != parentModelObject ? currentObject : null;                                                  
  }

  public Node getNode(Object modelObject)
  {
    if (modelObject instanceof WSDLElement)
      return ((WSDLElementImpl)modelObject).getElement();
    else
      return null;
  }
      
  protected Object getModelObjectHelper(Object parentObject, Element elementNode)
  {          
		Object result = null;
		Collection childComponentList = ((WSDLElementImpl) parentObject).getWSDLContents();
		for (Iterator i = childComponentList.iterator(); i.hasNext();)
		{
			Object o = i.next();
			Element element = null;

      if (o != null)
      {
        if (o instanceof WSDLElementImpl)
        {
          element = ((WSDLElementImpl)o).getElement();
        }
        else if (o instanceof XSDComponentImpl)
        {
          element = ((XSDComponentImpl)o).getElement();
        }
      }
      
			if (element == elementNode)
			{
				result = o;
				break;
			}
		}
		return result;
  }
}    

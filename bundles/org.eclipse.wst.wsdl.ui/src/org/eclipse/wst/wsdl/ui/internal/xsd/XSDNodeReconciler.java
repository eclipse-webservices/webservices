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
                                           
import org.eclipse.wst.sse.core.INodeNotifier;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.extension.INodeReconciler;
import org.eclipse.xsd.XSDConcreteComponent;
import org.w3c.dom.Element;


public class XSDNodeReconciler implements INodeReconciler
{                    
  public void notifyChanged(Object modelObject, Element element, int eventType, Object feature, Object oldValue, Object newValue, int index)
  {                         
    XSDConcreteComponent component = null;
    if (modelObject instanceof XSDConcreteComponent)
    {
      component = (XSDConcreteComponent)modelObject;
    }
    else if (modelObject instanceof XSDSchemaExtensibilityElement)
    {           
      XSDSchemaExtensibilityElement extensibilityElement = (XSDSchemaExtensibilityElement)modelObject;
      component = extensibilityElement.getSchema();
    }   

    if (component != null)
    {
      switch (eventType)
      {
        case INodeNotifier.CHANGE:
        {                           
          component.elementAttributesChanged(element); 
          break;
        }
        case INodeNotifier.STRUCTURE_CHANGED:
        {                              
          component.elementContentsChanged(element);
          break;
        }
        case INodeNotifier.CONTENT_CHANGED:
        {                              
          component.elementChanged(element);
          break;
        }
      }                   
    }
  }
}                                                                   
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
package org.eclipse.wst.wsdl.ui.internal.reconciler;

import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.INodeReconciler;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtensionRegistry;
import org.w3c.dom.Element;

                                    

// TODO.. we should probably be keying off of the node's qname (perhaps supporting 'null' as a wild card for local names) 
// in order to compute an applicable reconciler
//
public class ExtensibleNodeReconciler
{  
  protected WSDLEditorExtension[] extensions;
  protected INodeReconciler[] reconcilers;

  public ExtensibleNodeReconciler()
  {                                  
    WSDLEditorExtensionRegistry registry = WSDLEditorPlugin.getInstance().getWSDLEditorExtensionRegistry();
    extensions = registry.getRegisteredExtensions(WSDLEditorExtension.NODE_RECONCILER); 
    reconcilers = new INodeReconciler[extensions.length]; 
    for (int i = 0; i < extensions.length; i++)
    {
      reconcilers[i] = (INodeReconciler)extensions[i].createExtensionObject(WSDLEditorExtension.NODE_RECONCILER, null);
    }
  }         

  protected INodeReconciler getApplicableNodeReconciler(Object object)
  {
    INodeReconciler reconciler = null;
    for (int i = 0; i < extensions.length; i++)
    {
      if (extensions[i].isApplicable(object))
      {
        reconciler = reconcilers[i];
        if (reconciler != null)
        {
          break;
        }
      }
    }
    return reconciler;
  }

  public void notifyChanged(Object modelObject, Element element, int eventType, Object feature, Object oldValue, Object newValue, int index)             
  {
    INodeReconciler reconciler = getApplicableNodeReconciler(modelObject);
    if (reconciler != null)
    {                                                    
      reconciler.notifyChanged(modelObject, element, eventType, feature, oldValue, newValue, index);
    }
  }                                         
}
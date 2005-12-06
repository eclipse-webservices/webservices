/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.text;

import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.eclipse.wst.wsdl.internal.impl.XSDSchemaExtensibilityElementImpl;
import org.eclipse.wst.wsdl.ui.internal.reconciler.ExtensibleNodeReconciler;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


class WSDLModelReconcileAdapter extends DocumentAdapter
{                             
  protected Definition definition;   
  protected ExtensibleNodeReconciler extensibleNodeReconciler;

  public WSDLModelReconcileAdapter(Document document, Definition definition)
  {           
    super(document);
    this.definition = definition;
    this.extensibleNodeReconciler = new ExtensibleNodeReconciler();
  } 
   
     
  public void notifyChanged(INodeNotifier notifier, int eventType, Object feature, Object oldValue, Object newValue, int index) 
  {                      
    if (eventType == INodeNotifier.ADD)
    {
      if (newValue instanceof Element)
      {
        adapt((Element)newValue);

        // See Bug 5366
        // We need to sync up the Model and the DOM
        Element newDocumentElement = (Element)newValue;
        String wsdlPrefix = newDocumentElement.getPrefix();
        if (wsdlPrefix == null) wsdlPrefix = "";
        String ns = definition.getNamespace(wsdlPrefix);
        if (ns != null && ns.equals(WSDLConstants.WSDL_NAMESPACE_URI)
           && newDocumentElement.getLocalName().equals(WSDLConstants.DEFINITION_ELEMENT_TAG)) // &&
           // !isValidDefinition)
        {
//          System.out.println("****** Setting new definition");
          definition.setElement(newDocumentElement);
        }
      }
    }   

    switch (eventType)
    {                  
      // we make the assumption that reconciling will only be triggered by one of these notifications 
      // (ADD and REMOVE notifications are omitted)
      //
      case INodeNotifier.CHANGE: 
      case INodeNotifier.STRUCTURE_CHANGED:
      case INodeNotifier.CONTENT_CHANGED:
      {                      
        if (notifier instanceof Element)
        {
          reconcileModelObjectForElement((Element)notifier, eventType, feature, oldValue, newValue, index);
        }
        else if (notifier instanceof Document)
        {
          Document document = (Document)notifier;
          Element definitionElement = null;          

          for (Node node = document.getFirstChild(); node != null; node = node.getNextSibling())
          {
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
              Element element = (Element)node;
              if (WSDLEditorUtil.getInstance().getWSDLType(element) == WSDLConstants.DEFINITION)
              {
                definitionElement = element;
                break;
              }
            }
          }
         
          // TODO... revisit definition.removeAllContent() and who should call this?
          //
          if (definitionElement != null)
          {
//            isValidDefinition = true;
//            System.out.println("VALID DEFINITION ELEMENT");
            ((DefinitionImpl)definition).elementChanged(definitionElement);
          }
          else
          {
//            System.out.println("INVALID DEFINITION ELEMENT");
//            isValidDefinition = false;
            ((DefinitionImpl)definition).removeAll();
          }
        }
        break;
      }
    }
  }

  protected void reconcileModelObjectForElement(Element element, int eventType, Object feature, Object oldValue, Object newValue, int index)
  {                                                                  
    Object modelObject = WSDLEditorUtil.getInstance().findModelObjectForElement(definition, element);  
    if (modelObject != null)
    {
      if (modelObject instanceof XSDSchemaExtensibilityElementImpl)
      {
        XSDSchemaExtensibilityElementImpl ee = (XSDSchemaExtensibilityElementImpl)modelObject;
        ((XSDSchemaImpl)ee.getSchema()).elementChanged(element);
        ee.elementChanged(element);            
      }   
      else if (modelObject instanceof WSDLElementImpl)
      {
        ((WSDLElementImpl)modelObject).elementChanged(element);
      }
      else if (modelObject instanceof XSDConcreteComponent)
      {
        ((XSDConcreteComponent)modelObject).elementChanged(element);
        extensibleNodeReconciler.notifyChanged(modelObject, element, eventType, feature, oldValue, newValue, index);     
      }
    }     
  }   
}


abstract class DocumentAdapter implements INodeAdapter
{
  Document document;
  
  public DocumentAdapter(Document document)
  {
    this.document = document;
    ((INodeNotifier)document).addAdapter(this);
    adapt(document.getDocumentElement());
  }

  public void adapt(Element element)
  {
    if (((INodeNotifier)element).getExistingAdapter(this) == null)
    {
      ((INodeNotifier)element).addAdapter(this);

      for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
      {
        if (child.getNodeType() == Node.ELEMENT_NODE)
        {
          adapt((Element)child);
        }
      }
    }
  }

  public boolean isAdapterForType(Object type)
  {
    return type == this;
  }

  abstract public void notifyChanged
    (INodeNotifier notifier, int eventType, Object feature, Object oldValue, Object newValue, int index);
}
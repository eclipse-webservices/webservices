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
package org.eclipse.wst.wsdl.ui.internal.text;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.eclipse.wst.wsdl.internal.impl.XSDSchemaExtensibilityElementImpl;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xsd.ui.internal.util.ModelReconcileAdapter;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


class WSDLModelReconcileAdapter extends ModelReconcileAdapter
{                             
  protected Definition definition;

  public WSDLModelReconcileAdapter(Document document, Definition definition)
  {           
    super(document);
    this.definition = definition;
  } 

  // This method is clever enough to deal with 'bad' documents that happen 
  // to have more than one root element.  It picks of the first 'matching' element.
  //
  // TODO (cs) why aren't we calling this from the WSDLModelAdapter when the model is initialized?
  //
  private Element getDefinitionElement(Document document)
  {
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
    return definitionElement;
  }
  
  protected void handleNodeChanged(Node node)
  {
    if (node instanceof Element)
    {
      reconcileModelObjectForElement((Element)node);      
    }
    else if (node instanceof Document)
    {
      // The document changed so we may need to fix up the 
      // definition's root element
      Document document = (Document)node;    
      Element definitionElement = getDefinitionElement(document);
      if (definitionElement != null && definitionElement != definition.getElement())
      {   
        // here we handle the case where a new 'definition' element was added
        //(e.g. the file was totally blank and then we type in the root element)        
        // See Bug 5366
        //
        if (definitionElement.getLocalName().equals(WSDLConstants.DEFINITION_ELEMENT_TAG))         
        {  
          //System.out.println("****** Setting new definition");
          definition.setElement(definitionElement);
        }
      }      
      else if (definitionElement != null)
      {       
        // handle the case where the definition element's content has changed
        // 
        ((DefinitionImpl)definition).elementChanged(definitionElement);
      }      
      else if (definitionElement == null)
      {
        // if there's no definition element clear out the WSDL
        //
        ((DefinitionImpl)definition).removeAll();
        
        // The removeAll() call does not remove namespaces as well and the model
        // does not reconcile well in this case. Also reset the definition name and target
        // namespace.
        
        definition.getNamespaces().clear();
        definition.setQName(null);
        definition.setTargetNamespace(null);
        
        // Reset the document because removeAll() sets the document to null as well.
        definition.setDocument(document);
      }
    }         
  }
       
  private void reconcileModelObjectForElement(Element element)
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
      }  
    }     
  }  

  /**
   * @deprecated
   */
  protected void reconcileModelObjectForElement(Element element, int eventType, Object feature, Object oldValue, Object newValue, int index)
  {                                          
    reconcileModelObjectForElement(element);
  }  
}

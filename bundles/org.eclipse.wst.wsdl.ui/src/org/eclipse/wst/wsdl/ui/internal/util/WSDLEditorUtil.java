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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.ui.internal.extension.ITypeSystemProvider;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WSDLEditorUtil extends WSDLConstants
{
  protected static WSDLEditorUtil instance;
  protected NodeAssociationManager nodeAssociationManager = new NodeAssociationManager();

  protected HashMap elementNameToTypeMap = new HashMap();

  public static WSDLEditorUtil getInstance()
  {
    if (instance == null)
    {
      instance = new WSDLEditorUtil();
    }
    return instance;
  }

  public WSDLEditorUtil()
  {
    elementNameToTypeMap.put(BINDING_ELEMENT_TAG, new Integer(BINDING));
    elementNameToTypeMap.put(DEFINITION_ELEMENT_TAG, new Integer(DEFINITION));
    elementNameToTypeMap.put(DOCUMENTATION_ELEMENT_TAG, new Integer(DOCUMENTATION));
    elementNameToTypeMap.put(FAULT_ELEMENT_TAG, new Integer(FAULT));
    elementNameToTypeMap.put(IMPORT_ELEMENT_TAG, new Integer(IMPORT));
    elementNameToTypeMap.put(INPUT_ELEMENT_TAG, new Integer(INPUT));
    elementNameToTypeMap.put(MESSAGE_ELEMENT_TAG, new Integer(MESSAGE));
    elementNameToTypeMap.put(OPERATION_ELEMENT_TAG, new Integer(OPERATION));
    elementNameToTypeMap.put(OUTPUT_ELEMENT_TAG, new Integer(OUTPUT));
    elementNameToTypeMap.put(PART_ELEMENT_TAG, new Integer(PART));
    elementNameToTypeMap.put(PORT_ELEMENT_TAG, new Integer(PORT));
    elementNameToTypeMap.put(PORT_TYPE_ELEMENT_TAG, new Integer(PORT_TYPE));
    elementNameToTypeMap.put(SERVICE_ELEMENT_TAG, new Integer(SERVICE));
    elementNameToTypeMap.put(TYPES_ELEMENT_TAG, new Integer(TYPES));
  }

  public int getWSDLType(Element element)
  {
    int result = -1;

    Integer integer = (Integer)elementNameToTypeMap.get(element.getLocalName());
    if (integer != null)
    {
      result = integer.intValue();
    }
    return result;
  }

  protected List getParentElementChain(Element element)
  {
    List list = new ArrayList();
    while (element != null)
    {
      list.add(0, element);
      Node node = element.getParentNode();
      element = (node != null && node.getNodeType() == Node.ELEMENT_NODE) ? (Element)node : null;
    }
    return list;
  }

  public Object findModelObjectForElement(Definition definition, Element targetElement)
  {
    Object o = nodeAssociationManager.getModelObjectForNode(definition, targetElement);
    return o;
  }

  public Element getElementForObject(Object o)
  {
    return ((org.eclipse.wst.wsdl.WSDLElement)o).getElement();
  }

  public Node getNodeForObject(Object o)
  {
    return nodeAssociationManager.getNode(o);
  }

  // Provide a mapping between Definitions and ITypeSystemProviders
  private Hashtable typeSystemProviders = new Hashtable();
  
  public ITypeSystemProvider getTypeSystemProvider(Definition definition)
  {
    return (ITypeSystemProvider)typeSystemProviders.get(definition);
  }

  public void setTypeSystemProvider(Definition definition, ITypeSystemProvider typeSystemProvider)
  {
    typeSystemProviders.put(definition,typeSystemProvider);
  }

  public static QName createQName(Definition definition, String prefixedName)
  {
    QName qname = null;
    if (prefixedName != null)
    {
      int index = prefixedName.indexOf(":");
      String prefix = (index == -1) ? null : prefixedName.substring(0, index);
      if (prefix != null)
      {
        String namespace = definition.getNamespace(prefix);
        if (namespace != null)
        {
          String localPart = prefixedName.substring(index + 1);
          qname = new QName(namespace, localPart);
        }
      }
    }
    return qname;
  }

  public List getExtensibilityElementNodes(ExtensibleElement extensibleElement)
  {
    // For Types, I need to get all the schemas
    if (extensibleElement instanceof Types)
    {
      Types xsdEE = (Types)extensibleElement;
      return xsdEE.getSchemas();
    }
    /*
    List childList = new ArrayList();

    Element parentElement = getElementForObject(extensibleElement);
    if (parentElement != null)
    {

      NodeList nodeList = parentElement.getChildNodes();
      int nodeListLength = nodeList.getLength();
      for (int i = 0; i < nodeListLength; i++)
      {
        childList.add(nodeList.item(i));
      }
      HashMap extensibleElementTable = new HashMap();
      for (Iterator i = extensibleElement.getEExtensibilityElements().iterator(); i.hasNext();)
      {
        ExtensibilityElement extensibilityElement = (ExtensibilityElement)i.next();
        if (extensibilityElement != null)
        {
          Element element = getElementForObject(extensibilityElement);
          if (element != null)
          {
            extensibleElementTable.put(element, element);
          }
        }
      }

      for (Iterator i = childList.iterator(); i.hasNext();)
      {
        Object o = i.next();
        if (extensibleElementTable.get(o) == null)
        {
          i.remove();
        }
      }
    }
    return childList;
    */
    return extensibleElement.getEExtensibilityElements();
  }

  /*
   * Returns a list of 'children' of the given object model (WSDLElement).
   */
  public static List getModelGraphViewChildren(Object object) {
  	List childList = new ArrayList();
  	
  	if (object instanceof PortType) {
  		PortType portType = (PortType) object;
        childList.addAll(portType.getOperations());
  	}
  	else if (object instanceof Operation) {
  		Operation operation = (Operation) object;
  		
  		if (operation.getEInput() != null) {
  			childList.add(operation.getEInput());
  		}
  		if (operation.getEOutput() != null) {
  			childList.add(operation.getEOutput());
  		}
  		childList.addAll(operation.getFaults().values());
  	}
  	else if (object instanceof MessageReference) {
  		MessageReference messageReference = (MessageReference) object;
  		childList.add(messageReference.getEMessage());
  	}  	
  	
  	return childList;
  }
}
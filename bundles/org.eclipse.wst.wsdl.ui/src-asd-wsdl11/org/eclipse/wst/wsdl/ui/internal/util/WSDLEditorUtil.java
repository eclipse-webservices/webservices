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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerFactory;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.ui.internal.extensions.ExtensibleTypeSystemProvider;
import org.eclipse.wst.wsdl.ui.internal.extensions.ITypeSystemProvider;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class WSDLEditorUtil extends WSDLConstants
{
  protected static WSDLEditorUtil instance;
  protected NodeAssociationManager nodeAssociationManager = new NodeAssociationManager();

  protected HashMap elementNameToTypeMap = new HashMap();
  
  public static final int ELEMENT = 14;
  public static final int TYPE = 15;

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
  private ITypeSystemProvider typeSystemProvider;
  
  public ITypeSystemProvider getTypeSystemProvider(Definition definition)
  {
	  if (typeSystemProvider == null) {
		  typeSystemProvider = new ExtensibleTypeSystemProvider();
	  }
	  return typeSystemProvider;
  }

  public void setTypeSystemProvider(Definition definition, ITypeSystemProvider typeSystemProvider)
  {
 //   typeSystemProviders.put(definition,typeSystemProvider);
  }

  public static QName createQName(Definition definition, String prefixedName)
  {
    QName qname = null;
    if (prefixedName != null)
    {
      int index = prefixedName.indexOf(":"); //$NON-NLS-1$
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
  
  public static String getTargetNamespaceURIForSchema(String uri)
  {
    String result = null;
    try
    {             
      URL url = new URL(uri);
      InputStream inputStream = url.openStream();
      result = WSDLEditorUtil.getTargetNamespaceURIForSchema(inputStream); 
    }
    catch (Exception e)
    {      
    }  
    return result;
  }

  public static String getTargetNamespaceURIForSchema(InputStream input)
  {  
    TargetNamespaceURIContentHandler handler = new TargetNamespaceURIContentHandler();                                                                  
    ClassLoader prevClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(WSDLEditorUtil.class.getClassLoader());
    // Line below is a hack to get XMLReader working
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    try
    {
    	XMLReader reader = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
    	reader.setContentHandler(handler);
    	reader.parse(new InputSource(input));
    }
    catch (Exception e)
    {      
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(prevClassLoader);
    }
    return handler.targetNamespaceURI;
  }  

  protected static class TargetNamespaceURIContentHandler extends DefaultHandler
  {       
    public String targetNamespaceURI;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {            
      if (localName.equals("schema") || localName.equals("definitions")) //$NON-NLS-1$ //$NON-NLS-2$
      {               
        int nAttributes = attributes.getLength();
        for (int i = 0; i < nAttributes; i++)
        {
          if (attributes.getLocalName(i).equals("targetNamespace")) //$NON-NLS-1$
          {
            targetNamespaceURI = attributes.getValue(i);
            break;
          }
        }
      }                                    
      // todo there's a ice way to do this I'm sure    
      // here I intentially cause an exception... 
      String x = null;
      x.length();
    }
  }
}
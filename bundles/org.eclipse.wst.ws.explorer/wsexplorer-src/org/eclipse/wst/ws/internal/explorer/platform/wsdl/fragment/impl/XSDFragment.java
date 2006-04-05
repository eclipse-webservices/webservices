/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;

import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDForm;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XSDFragment extends Fragment implements IXSDFragment
{
  private XSDToFragmentConfiguration config_;
  private XSDTypeDefinition typeDef_;
  private int seed_;

  public XSDFragment(String id, String name, XSDToFragmentConfiguration config)
  {
    super(id, name);
    config_ = config;
    if (config_ != null)
    {
      XSDComponent component = config.getXSDComponent();
      if (component instanceof XSDTypeDefinition)
        typeDef_ = (XSDTypeDefinition)component;
      else
        typeDef_ = null;
    }
    seed_ = 0;
  }

  public void setXSDToFragmentConfiguration(XSDToFragmentConfiguration config)
  {
    config_ = config;
    if (config_ != null)
    {
      XSDComponent component = config.getXSDComponent();
      if (component instanceof XSDTypeDefinition)
        setXSDTypeDefinition((XSDTypeDefinition)component);
    }
  }

  public XSDToFragmentConfiguration getXSDToFragmentConfiguration()
  {
    return config_;
  }

  public void setXSDTypeDefinition(XSDTypeDefinition typeDef)
  {
    typeDef_ = typeDef;
  }

  public XSDTypeDefinition getXSDTypeDefinition()
  {
    return typeDef_;
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments)
  {
    Element[] instanceDocumentsCopy = getInstanceDocumentsByTagName(instanceDocuments, getName());
    String[] params = new String[instanceDocumentsCopy.length];
    boolean paramsValid = internalEquals(instanceDocumentsCopy, instanceDocuments);
    for (int i = 0; i < instanceDocumentsCopy.length; i++)
    {
      params[i] = "";
      NodeList nodeList = instanceDocumentsCopy[i].getChildNodes();
      if (nodeList.getLength() > 0)
      {
        Node node = nodeList.item(0);
        if (nodeList.getLength() > 1)
          paramsValid = false;
        if (node.getNodeType() != Node.TEXT_NODE)
          paramsValid = false;
        else
          params[i] = node.getNodeValue();
      }
      else
        paramsValid = false;
    }
    setParameterValues(getID(), params);
    return paramsValid && validateAllParameterValues();
  }

  protected Element[] getInstanceDocumentsByTagName(Element[] instanceDocuments, String tagName)
  {
    if (instanceDocuments == null)
      return new Element[0];
    Vector instancesCopy = new Vector();
    for (int i = 0; i < instanceDocuments.length; i++)
    {
      if (instanceDocuments[i] == null)
        continue;
      String instanceTagName = trimPrefix(instanceDocuments[i].getTagName());
      if (instanceTagName.equals(tagName))
        instancesCopy.add(instanceDocuments[i]);
    }
    Element[] instances = new Element[instancesCopy.size()];
    instancesCopy.copyInto(instances);
    return instances;
  }

  protected String trimPrefix(String s)
  {
    String sCopy = new String(s);
    int colonIndex = sCopy.indexOf(FragmentConstants.COLON);
    if (colonIndex != -1 && colonIndex + 1 < sCopy.length() && !((String.valueOf(sCopy.charAt(colonIndex + 1))).equals(FragmentConstants.COLON)))
      sCopy = sCopy.substring(colonIndex + 1, sCopy.length());
    return sCopy;
  }

  public Element[] genInstanceDocumentsForNil(boolean genXSIType, Hashtable namespaceTable,Document doc){
  	Element[] instanceDocuments = new Element[1];
    String tagName = getInstanceDocumentTagName(namespaceTable);
      for (int i = 0; i < instanceDocuments.length; i++) {
        Element instanceDocument = doc.createElement(tagName);
        instanceDocuments[i] = instanceDocument;
      }
    return (genXSIType ? addXSIType(instanceDocuments, namespaceTable) : instanceDocuments);
  	
  }
  
  public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable,Document doc) {
    String[] params = getParameterValues(getID());
    if (params == null)
      return new Element[0];
    Element[] instanceDocuments = new Element[params.length];
    String tagName = getInstanceDocumentTagName(namespaceTable);
    for (int i = 0; i < instanceDocuments.length; i++)
    {
      Element instanceDocument = doc.createElement(tagName);
      Node textNode = doc.createTextNode(params[i]);
      instanceDocument.appendChild(textNode);
      instanceDocuments[i] = instanceDocument;
    }
    return (genXSIType ? addXSIType(instanceDocuments, namespaceTable) : instanceDocuments);
  }

  public Element setAttributesOnInstanceDocuments(Element instanceDocument,String attName){
  	String[] params = getParameterValues(getID());
    if (params == null)
      return instanceDocument;
    if (isAttributeInstanceNamespaceQualified()){
      instanceDocument.setAttributeNS(config_.getXSDComponent().getSchema().getTargetNamespace(),attName,params[0]);
      
    }
    else instanceDocument.setAttribute(attName,params[0]);
    
    return instanceDocument;
  }
  
  public boolean setAttributeParamsFromInstanceDocuments(Node attribute){
  	String[] params = new String[1];
  	params[0] = attribute.getNodeValue(); 
  	setParameterValues(getID(), params);
    return validateAllParameterValues();
  }
  
  public String genID()
  {
    StringBuffer newID = new StringBuffer();
    newID.append(getID());
    newID.append(FragmentConstants.ID_SEPERATOR);
    newID.append(seed_++);
    return newID.toString();
  }

  protected boolean isInstanceNamespaceQualified()
  {
    XSDSchema xsdSchema = config_.getXSDComponent().getSchema();
    return (!config_.getIsWSDLPart() && config_.getPartEncoding() == FragmentConstants.ENCODING_LITERAL && xsdSchema.getElementFormDefault().getValue() == XSDForm.QUALIFIED);
  }

  protected boolean isAttributeInstanceNamespaceQualified() {
  	XSDSchema xsdSchema = config_.getXSDComponent().getSchema();
    
    return (!config_.getIsWSDLPart() &&
                config_.getPartEncoding() == FragmentConstants.ENCODING_LITERAL &&
                xsdSchema.getAttributeFormDefault().getValue() == XSDForm.QUALIFIED);

  }

 
  
  protected String getInstanceDocumentTagName(Hashtable namespaceTable) {
    StringBuffer tagName = new StringBuffer();
    if (isInstanceNamespaceQualified())
    {
      String nsURI = config_.getXSDComponent().getSchema().getTargetNamespace();
      String prefix = getPrefixFromNamespaceURI(nsURI, namespaceTable);
      tagName.append(prefix);
      tagName.append(FragmentConstants.COLON);
    }
    tagName.append(getName());
    return tagName.toString();
  }

  protected Element[] addXSIType(Element[] instanceDocuments, Hashtable namespaceTable)
  {
    XSDComponent xsdComponent = config_.getXSDComponent();
    if (instanceDocuments != null && (xsdComponent instanceof XSDNamedComponent))
    {
      String xsdComponentName = ((XSDNamedComponent)xsdComponent).getName();
      if (xsdComponentName != null && xsdComponentName.length() > 0)
      {
        for (int i = 0; i < instanceDocuments.length; i++)
        {
          if (instanceDocuments[i] == null)
            continue;
          String xsiURI = FragmentConstants.URI_XSI;
          String xsiPrefix = getPrefixFromNamespaceURI(xsiURI, namespaceTable);
          String xsiTypeURI = xsdComponent.getSchema().getTargetNamespace();
          String xsiTypePrefix = getPrefixFromNamespaceURI(xsiTypeURI, namespaceTable);
          ;
          StringBuffer attrName = new StringBuffer();
          attrName.append(xsiPrefix);
          attrName.append(FragmentConstants.COLON);
          attrName.append(FragmentConstants.XSI_TYPE);
          StringBuffer attrValue = new StringBuffer();
          attrValue.append(xsiTypePrefix);
          attrValue.append(FragmentConstants.COLON);
          attrValue.append(xsdComponentName);
          instanceDocuments[i].setAttribute(attrName.toString(), attrValue.toString());
        }
      }
    }
    return instanceDocuments;
  }

  public boolean validateAllParameterValues()
  {
    if (!super.validateAllParameterValues())
      return false;
    String[] params = getParameterValues(getID());
    int length = (params != null) ? params.length : 0;
    int min = config_.getMinOccurs();
    int max = config_.getMaxOccurs();
    return (length >= min && (max == FragmentConstants.UNBOUNDED || length <= max));
  }

  protected String getPrefixFromNamespaceURI(String nsURI, Hashtable namespaceTable)
  {
    String prefix = (String)namespaceTable.get(nsURI);
    int i = 0;
    while (prefix == null || prefix.length() <= 0)
    {
      StringBuffer prefixCopy = new StringBuffer();
      prefixCopy.append(FragmentConstants.QNAME_PREFIX);
      prefixCopy.append(String.valueOf(i));
      i++;
      if (!namespaceTable.contains(prefixCopy.toString()))
      {
        namespaceTable.put(nsURI, prefixCopy.toString());
        prefix = prefixCopy.toString();
      }
    }
    return prefix;
  }

  protected Element setElementTagName(Element e, String tagName)
  {
    if (!e.getTagName().equals(tagName))
    {
      try
      {
        Document doc = e.getOwnerDocument();
        NodeList children = e.getChildNodes();
        NamedNodeMap attributes = e.getAttributes();
        Element eCopy = doc.createElement(tagName);
        for (int i = 0; i < children.getLength(); i++)
        {
          Node child = children.item(i);
          if (child != null)
          {
            eCopy.appendChild(child);
            // When you append a node from one element to another,
            // the original element will lose its reference to this node,
            // therefore, the size of the node list will decrease by 1.
            i--;
          }
        }
        for (int j = 0; j < attributes.getLength(); j++)
        {
          Node attr = attributes.item(j);
          if (attr != null && (attr instanceof Attr))
          {
            Attr attribute = (Attr)attr;
            eCopy.setAttribute(attribute.getName(), attribute.getValue());
          }
        }
        return eCopy;
      }
      catch (Exception exception)
      {
      }
    }
    return e;
  }

  protected Element[] setElementsTagName(Element[] e, String tagName)
  {
    Element[] eCopy = new Element[e.length];
    for (int i = 0; i < eCopy.length; i++)
    {
      eCopy[i] = setElementTagName(e[i], tagName);
    }
    return eCopy;
  }

  protected boolean internalEquals(Element[] instanceDocumentsCopy, Element[] instanceDocuments)
  {
    if (!config_.getIsWSDLPart())
    {
      int length = (instanceDocuments != null) ? instanceDocuments.length : 0;
      int lengthCopy = (instanceDocumentsCopy != null) ? instanceDocumentsCopy.length : 0;
      return (length == lengthCopy);
    }
    return true;
  }
}

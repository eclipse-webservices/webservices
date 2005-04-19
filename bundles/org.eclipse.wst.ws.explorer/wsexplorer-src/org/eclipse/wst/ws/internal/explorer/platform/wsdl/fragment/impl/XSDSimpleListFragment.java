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

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDSimpleListFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDLengthFacet;
import org.eclipse.xsd.XSDMinLengthFacet;
import org.eclipse.xsd.XSDMaxLengthFacet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.StringTokenizer;
import java.util.Hashtable;

public abstract class XSDSimpleListFragment extends XSDMapFragment implements IXSDSimpleListFragment
{
  public XSDSimpleListFragment(String id, String name, XSDToFragmentConfiguration config, XSDToFragmentController controller)
  {
    super(id, name, config, controller);
  }

  public void setName(String name)
  {
    super.setName(name);
    IXSDFragment[] fragments = getAllFragments();
    for (int i = 0; i < fragments.length; i++)
    {
      fragments[i].setName(name);
    }
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
    removeAllFragments();
    for (int i = 0; i < params.length; i++)
    {
      IXSDFragment childFrag = getFragment(createListInstance());
      StringTokenizer st = new StringTokenizer(params[i].trim());
      String[] childParams = new String[st.countTokens()];
      for (int j = 0; j < childParams.length; j++)
      {
        childParams[j] = st.nextToken();
      }
      childFrag.setParameterValues(childFrag.getID(), childParams);
      paramsValid = paramsValid && childFrag.validateAllParameterValues();
    }
    return paramsValid;
  }

  public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable, Document doc)
  {
    IXSDFragment[] fragments = getAllFragments();
    Element[] instanceDocuments = new Element[fragments.length];
    String tagName = getInstanceDocumentTagName(namespaceTable);
    for (int i = 0; i < fragments.length; i++)
    {
      Element instanceDocument = doc.createElement(tagName);
      StringBuffer listValue = new StringBuffer();
      String[] params = fragments[i].getParameterValues(fragments[i].getID());
      for (int j = 0; params != null && j < params.length; j++)
      {
        if (j != 0)
          listValue.append(FragmentConstants.LIST_SEPERATOR);
        listValue.append(params[j]);
      }
      Node textNode = doc.createTextNode(listValue.toString());
      instanceDocument.appendChild(textNode);
      instanceDocuments[i] = instanceDocument;
    }
    return (genXSIType ? addXSIType(instanceDocuments, namespaceTable) : instanceDocuments);
  }

  public String createListInstance()
  {
    String newID = genID();
    XSDSimpleTypeDefinition simpleType = (XSDSimpleTypeDefinition)getXSDTypeDefinition();
    int min = 0;
    int max = FragmentConstants.UNBOUNDED;
    XSDLengthFacet xsdLengthFacet = simpleType.getLengthFacet();
    // port to org.eclipse.xsd
    if (xsdLengthFacet != null)
    {
      min = xsdLengthFacet.getValue();
      max = xsdLengthFacet.getValue();
    }
    else
    {
      XSDMinLengthFacet xsdMinLengthFacet = simpleType.getMinLengthFacet();
      XSDMaxLengthFacet xsdMaxLengthFacet = simpleType.getMaxLengthFacet();
      // port to org.eclipse.xsd
      if (xsdMinLengthFacet != null)
        min = xsdMinLengthFacet.getValue();
      if (xsdMaxLengthFacet != null)
        max = xsdMaxLengthFacet.getValue();
    }
    XSDToFragmentConfiguration thisConfig = getXSDToFragmentConfiguration();
    XSDToFragmentConfiguration xsdConfig = new XSDToFragmentConfiguration();
    xsdConfig.setXSDComponent(simpleType.getItemTypeDefinition());
    xsdConfig.setStyle(thisConfig.getStyle());
    xsdConfig.setPartEncoding(thisConfig.getPartEncoding());
    xsdConfig.setWSDLPartName(thisConfig.getWSDLPartName());
    xsdConfig.setMinOccurs(min);
    xsdConfig.setMaxOccurs(max);
    IXSDFragment frag = getXSDToFragmentController().getFragment(xsdConfig, newID, getName());
    frag.setID(newID);
    frag.setName(getName());
    addFragment(newID, frag);
    return newID;
  }

  public String createInstance()
  {
    return createListInstance();
  }
}

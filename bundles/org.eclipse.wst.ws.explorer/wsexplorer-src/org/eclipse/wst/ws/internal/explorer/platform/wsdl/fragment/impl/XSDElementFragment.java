/*******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060119   123539 jesper@selskabet.org - Jesper Møller
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;

import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDElementFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDForm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XSDElementFragment extends XSDDelegationFragment implements IXSDElementFragment
{
  private int minOccurs_;
  private int maxOccurs_;

  public XSDElementFragment(String id, String name, XSDToFragmentConfiguration config)
  {
    super(id, name, config);
    if (config != null)
    {
      minOccurs_ = config.getMinOccurs();
      maxOccurs_ = config.getMaxOccurs();
    }
    else
    {
      minOccurs_ = FragmentConstants.DEFAULT_MIN_OCCURS;
      maxOccurs_ = FragmentConstants.DEFAULT_MAX_OCCURS;
    }
  }

  public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    IXSDFragment elementTypeFragment = getXSDDelegationFragment();
    if (!elementTypeFragment.processParameterValues(parser))
      return false;
    String[] params = getParameterValues(elementTypeFragment.getID());
    if (params != null && !withinRange(params.length))
      return false;
    return true;
  }

  public boolean validateAllParameterValues()
  {
    IXSDFragment elementTypeFragment = getXSDDelegationFragment();
    String[] params = getParameterValues(elementTypeFragment.getID());
    if (params != null && !withinRange(params.length))
      return false;
    if (!elementTypeFragment.validateAllParameterValues())
      return false;
    return true;
  }

  public boolean validateParameterValues(String paramKey)
  {
    String[] params = getParameterValues(paramKey);
    if (params != null && !withinRange(params.length))
      return false;
    if (!getXSDDelegationFragment().validateParameterValues(paramKey))
      return false;
    return true;
  }

  public boolean validateParameterValue(String paramKey, int paramIndex)
  {
    if (!withinRange(paramIndex + 1))
      return false;
    XSDElementDeclaration xsdElement = (XSDElementDeclaration)getXSDToFragmentConfiguration().getXSDComponent();
    String param = getParameterValue(paramKey, paramIndex);
    if (param != null && param.length() <= 0 && !xsdElement.isNillable())
      return false;
    if (!getXSDDelegationFragment().validateParameterValue(paramKey, paramIndex))
      return false;
    return true;
  }

  public void setXSDToFragmentConfiguration(XSDToFragmentConfiguration config)
  {
    super.setXSDToFragmentConfiguration(config);
    minOccurs_ = config.getMinOccurs();
    maxOccurs_ = config.getMaxOccurs();
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments)
  {
    Element[] instanceDocumentsCopy = instanceDocuments;
    XSDToFragmentConfiguration config = getXSDToFragmentConfiguration();
    String wsdlPartName = config.getWSDLPartName();
    if (config.getIsWSDLPart() && config.getStyle() == FragmentConstants.STYLE_RPC && wsdlPartName != null && wsdlPartName.length() > 0 && instanceDocumentsCopy.length == 1 && wsdlPartName.equals(instanceDocumentsCopy[0].getTagName()))
    {
      NodeList nl = instanceDocumentsCopy[0].getChildNodes();
      Vector childElements = new Vector();
      for (int i = 0; i < nl.getLength(); i++)
      {
        Node node = nl.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE)
          childElements.add(node);
      }
      instanceDocumentsCopy = (Element[])childElements.toArray(new Element[0]);
    }
    IXSDFragment elementTypeFragment = getXSDDelegationFragment();
    instanceDocumentsCopy = getInstanceDocumentsByTagName(instanceDocumentsCopy, getName());
    return elementTypeFragment.setParameterValuesFromInstanceDocuments(setElementsTagName(instanceDocumentsCopy, elementTypeFragment.getName()));
  }

  public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable, Document doc)
  {
    Element[] instanceDocuments = getXSDDelegationFragment().genInstanceDocumentsFromParameterValues(genXSIType, namespaceTable, doc);
    instanceDocuments = setElementsTagName(instanceDocuments, getInstanceDocumentTagName(namespaceTable));
    XSDToFragmentConfiguration config = getXSDToFragmentConfiguration();
    String wsdlPartName = config.getWSDLPartName();
    if (config.getIsWSDLPart() && config.getStyle() == FragmentConstants.STYLE_RPC && wsdlPartName != null && wsdlPartName.length() > 0)
    {
      Element[] instanceDocumentsCopy = instanceDocuments;
      instanceDocuments = new Element[1];
      instanceDocuments[0] = doc.createElement(wsdlPartName);
      for (int i = 0; i < instanceDocumentsCopy.length; i++)
      {
        if (instanceDocumentsCopy[i] != null)
          instanceDocuments[0].appendChild(instanceDocumentsCopy[i]);
      }
    }
    return instanceDocuments;
  }

  protected boolean isInstanceNamespaceQualified()
  {
    XSDToFragmentConfiguration config = getXSDToFragmentConfiguration();
    if (config.getPartEncoding() == FragmentConstants.ENCODING_LITERAL)
    {
      XSDElementDeclaration xsdElement = (XSDElementDeclaration)config.getXSDComponent();
      // Try to resolve the element, since an element with a ref can never be global
      if (xsdElement.getResolvedElementDeclaration() != null)
        xsdElement = xsdElement.getResolvedElementDeclaration();
      
      if (xsdElement.isGlobal())
        return true;
      else
      {
        if (xsdElement.isSetForm())
          return (xsdElement.getForm().getValue() == XSDForm.QUALIFIED);
        else
          return (xsdElement.getSchema().getElementFormDefault().getValue() == XSDForm.QUALIFIED);
      }
    }
    else
      return false;
  }

  protected String getInstanceDocumentTagName(Hashtable namespaceTable)
  {
    XSDToFragmentConfiguration config = getXSDToFragmentConfiguration();
    XSDElementDeclaration xsdElement = (XSDElementDeclaration)config.getXSDComponent();
    StringBuffer tagName = new StringBuffer();
    if (isInstanceNamespaceQualified())
    {
      String nsURI = null;
      // port to org.eclipse.xsd
      if (xsdElement.getResolvedElementDeclaration() != null)
        nsURI = xsdElement.getResolvedElementDeclaration().getTargetNamespace();
      else
        nsURI = xsdElement.getTargetNamespace();
      if (nsURI != null && nsURI.length() > 0)
      {
        String prefix = getPrefixFromNamespaceURI(nsURI, namespaceTable);
        tagName.append(prefix);
        tagName.append(FragmentConstants.COLON);
      }
    }
    String xsdElementName = xsdElement.getName();
    if (xsdElementName == null || xsdElementName.length() <= 0)
    {
      // port to org.eclipse.xsd
      if (xsdElement.getResolvedElementDeclaration() != null)
        xsdElementName = xsdElement.getResolvedElementDeclaration().getName();
      else
        xsdElementName = getName();
    }
    tagName.append(xsdElementName);
    return tagName.toString();
  }

  private boolean withinRange(int size)
  {
    if (size < minOccurs_)
      return false;
    if (maxOccurs_ != FragmentConstants.UNBOUNDED && size > maxOccurs_)
      return false;
    return true;
  }

  public void setMinOccurs(int minOccurs)
  {
    minOccurs_ = minOccurs;
  }

  public int getMinOccurs()
  {
    return minOccurs_;
  }

  public void setMaxOccurs(int maxOccurs)
  {
    maxOccurs_ = maxOccurs;
  }

  public int getMaxOccurs()
  {
    return maxOccurs_;
  }
}

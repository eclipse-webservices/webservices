/**
 * <copyright>
 * 
 * Licensed Material - Property of IBM (C) Copyright IBM Corp. 2002 - All Rights
 * Reserved. US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 * 
 * </copyright>
 * 
 * File
 * plugins/com.ibm.etools.webservice.explorer/wsexplorer/src/com/ibm/etools/webservice/explorer/wsdl/fragment/Impl/XSDElementFragment.java,
 * wsa.etools.ws.explorer, lunar-5.1.2, 20031231a 5 Version 1.5 03/09/24
 * 14:30:38
 */
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;

import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDAttributeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDForm;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XSDAttributeFragment extends XSDDelegationFragment implements IXSDAttributeFragment
{
   
  
  public XSDAttributeFragment(String id, String name, XSDToFragmentConfiguration config)
  {
    super(id, name, config);
    
  }

  public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    IXSDFragment attributeTypeFragment = getXSDDelegationFragment();
    	
    if (!attributeTypeFragment.processParameterValues(parser))
      return false;
    String[] params = getParameterValues(attributeTypeFragment.getID());
    return true;
  }
  
  public boolean validateAllParameterValues()
  {
    IXSDFragment attributeTypeFragment = getXSDDelegationFragment();
    String[] params = getParameterValues(attributeTypeFragment.getID());
    if (!attributeTypeFragment.validateAllParameterValues())
      return false;
    return true;
  }

  public boolean validateParameterValues(String paramKey)
  {
    String[] params = getParameterValues(paramKey);
    if (params != null)
      return false;
    if (!getXSDDelegationFragment().validateParameterValues(paramKey))
      return false;
    return true;
  }

  public boolean validateParameterValue(String paramKey, int paramIndex)
  {
    XSDAttributeUse xsdAttribute = (XSDAttributeUse)getXSDToFragmentConfiguration().getXSDComponent();
    String param = getParameterValue(paramKey, paramIndex);
    if (!getXSDDelegationFragment().validateParameterValue(paramKey, paramIndex))
      return false;
    return true;
  }

  public void setXSDToFragmentConfiguration(XSDToFragmentConfiguration config)
  {
    super.setXSDToFragmentConfiguration(config);
    
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments)
  {
    Element[] instanceDocumentsCopy = instanceDocuments;
    XSDToFragmentConfiguration config = getXSDToFragmentConfiguration();
    String wsdlPartName = config.getWSDLPartName();
    NodeList nl = instanceDocumentsCopy[0].getChildNodes();
     
    
    
    if (config.getIsWSDLPart() && config.getStyle() == FragmentConstants.STYLE_RPC && wsdlPartName != null && wsdlPartName.length() > 0 && instanceDocumentsCopy.length == 1 && wsdlPartName.equals(instanceDocumentsCopy[0].getTagName()))
    {
      Vector childElements = new Vector();
      for (int i = 0; i < nl.getLength(); i++)
      {
        Node node = nl.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE){
          childElements.add(node);
        }
      }
      instanceDocumentsCopy = (Element[])childElements.toArray(new Element[0]);
    }
    IXSDFragment attributeTypeFragment = getXSDDelegationFragment();
    instanceDocumentsCopy = getInstanceDocumentsByTagName(instanceDocumentsCopy, getName());
    return attributeTypeFragment.setParameterValuesFromInstanceDocuments(setElementsTagName(instanceDocumentsCopy, attributeTypeFragment.getName()));
  }

  protected boolean isInstanceNamespaceQualified()
  {
    XSDToFragmentConfiguration config = getXSDToFragmentConfiguration();
    if (config.getPartEncoding() == FragmentConstants.ENCODING_LITERAL)
    {
      XSDAttributeUse xsdAttributeUse = (XSDAttributeUse)config.getXSDComponent();
      XSDAttributeDeclaration xsdAttribute = (XSDAttributeDeclaration)xsdAttributeUse.getAttributeDeclaration();
      if (xsdAttribute.isGlobal())
        return true;
      else
      {
        if (xsdAttribute.isSetForm())
          return (xsdAttribute.getForm().getValue() == XSDForm.QUALIFIED);
        else
          return (xsdAttribute.getSchema().getAttributeFormDefault().getValue() == XSDForm.QUALIFIED);
      }
    }
    else
      return false;
  }

  protected String getInstanceDocumentTagName(Hashtable namespaceTable)
  {
    XSDToFragmentConfiguration config = getXSDToFragmentConfiguration();
    XSDAttributeUse xsdAttributeUse = (XSDAttributeUse)config.getXSDComponent();
    XSDAttributeDeclaration xsdAttribute = xsdAttributeUse.getAttributeDeclaration();
    
    StringBuffer tagName = new StringBuffer();
    if (isInstanceNamespaceQualified())
    {
      String nsURI = null;
      // port to org.eclipse.xsd
      if (xsdAttribute.getResolvedAttributeDeclaration() != null)
        nsURI = xsdAttribute.getResolvedAttributeDeclaration().getTargetNamespace();
      else
        nsURI = xsdAttribute.getTargetNamespace();
      if (nsURI != null && nsURI.length() > 0)
      {
        String prefix = getPrefixFromNamespaceURI(nsURI, namespaceTable);
        tagName.append(prefix);
        tagName.append(FragmentConstants.COLON);
      }
    }
    String xsdAttributeName = xsdAttribute.getName();
    if (xsdAttributeName == null || xsdAttributeName.length() <= 0)
    {
      // port to org.eclipse.xsd
      if (xsdAttribute.getResolvedAttributeDeclaration() != null)
      	xsdAttributeName = xsdAttribute.getResolvedAttributeDeclaration().getName();
      else
      	xsdAttributeName = getName();
    }
    tagName.append(xsdAttributeName);
    return tagName.toString();
  }

  

  
  public String getInformationFragment()
  {
    return "/wsdl/fragment/XSDDefaultInfoFragmentJSP.jsp";
  }

  public String getReadFragment()
  {
    return "/wsdl/fragment/XSDDelegateFragmentJSP.jsp";
  }

  public String getWriteFragment()
  {
    return "/wsdl/fragment/XSDDelegateFragmentJSP.jsp";
  }

}

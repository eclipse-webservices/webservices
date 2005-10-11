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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment;

import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.SOAPEncArrayFixFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.SOAPEncArrayRangeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.SOAPEncodingWrapperFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.XSDTypeDefinitionUtil;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.WSDLPartsToXSDTypeMapper;
import org.eclipse.xsd.XSDAttributeGroupContent;
import org.eclipse.xsd.XSDComplexTypeContent;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDParticleContent;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Element;

public class XSDSOAPEncodingToFragmentMapper extends XSDToFragmentMapper {
  private XSDLiteralXMLEncodingToFragmentMapper xsdLiteralMapper_;
  private XSDComplexTypeToFragmentMapper xsdComplexMapper_;

  public XSDSOAPEncodingToFragmentMapper(XSDToFragmentController controller, WSDLPartsToXSDTypeMapper wsdlToXSDMapper) {
    super(controller, wsdlToXSDMapper);
    xsdLiteralMapper_ = null;
    xsdComplexMapper_ = null;
  }

  private XSDLiteralXMLEncodingToFragmentMapper getXSDLiteralMapper() {
    if (xsdLiteralMapper_ == null)
      xsdLiteralMapper_ = new XSDLiteralXMLEncodingToFragmentMapper(getController(), getWSDLPartsToXSDTypeMapper());
    return xsdLiteralMapper_;
  }

  private XSDComplexTypeToFragmentMapper getXSDComplexMapper() {
    if (xsdComplexMapper_ == null)
      xsdComplexMapper_ = new XSDComplexTypeToFragmentMapper(getController(), getWSDLPartsToXSDTypeMapper());
     return xsdComplexMapper_;
  }

  public IXSDFragment getFragment(XSDToFragmentConfiguration config, String id, String name) {
    if (config.getIsWSDLPart()) {
      ISOAPEncodingWrapperFragment wrapperFragment = new SOAPEncodingWrapperFragment(id, name, null);
      String delegationFragId = wrapperFragment.genID();
      IXSDFragment delegationFragment = getFragmentDelegate(config, delegationFragId, name);
      wrapperFragment.setXSDDelegationFragment(delegationFragment);
      getController().addToCache(delegationFragId, delegationFragment);
      return wrapperFragment;
    }
    else
      return getFragmentDelegate(config, id, name);
  }

  public IXSDFragment getFragmentDelegate(XSDToFragmentConfiguration config, String id, String name) {
    config = resolveXSDComponent(config);
    XSDComponent component = config.getXSDComponent();
    if (!isComponentResolvable(component))
      return getXSDDefaultFragment(config, id, name);
    else if (component instanceof XSDComplexTypeDefinition) {
      XSDComplexTypeDefinition complexType = (XSDComplexTypeDefinition)component;
      if (XSDTypeDefinitionUtil.isSoapEncArray(complexType))
        return getSOAPEncArrayFragment(config, id, name, complexType);
      else
        return getXSDComplexMapper().getFragment(config, id, name);
    }
    else
      return getXSDLiteralMapper().getFragment(config, id, name);
  }

  private IXSDFragment getSOAPEncArrayFragment(XSDToFragmentConfiguration config, String id, String name, XSDComplexTypeDefinition complexType) {
    EList xsdAttrContents = complexType.getAttributeContents();
    for (int i = 0; i < xsdAttrContents.size(); i++) {
      String soapEncArrayTypeNamespaceURI = null;
      String soapEncArrayTypeLocalName = null;
      int soapEncArrayDimension = 0;
      XSDAttributeGroupContent xsdAttrContent = (XSDAttributeGroupContent)xsdAttrContents.get(i);
      Element element = xsdAttrContent.getElement();
      String arrayTypeAttr = element.getAttributeNS(FragmentConstants.URI_WSDL, FragmentConstants.QNAME_LOCAL_NAME_ARRAY_TYPE);
      if (arrayTypeAttr != null && arrayTypeAttr.length() > 0) {
        int colon = arrayTypeAttr.indexOf(FragmentConstants.COLON);
        String nsPrefix = (colon > 0) ? arrayTypeAttr.substring(0, colon) : null;
        soapEncArrayTypeNamespaceURI = (String)xsdAttrContent.getSchema().getQNamePrefixToNamespaceMap().get(nsPrefix);
        int dimensionIndex = arrayTypeAttr.indexOf(FragmentConstants.LEFT_SQUARE_BRACKET);
        soapEncArrayTypeLocalName = arrayTypeAttr.substring(colon + 1, dimensionIndex);
        String dimensionString = arrayTypeAttr.substring(dimensionIndex, arrayTypeAttr.length());
        for (int j = 0; j < dimensionString.length() - 1; j++) {
          if (dimensionString.charAt(j) == FragmentConstants.LEFT_SQUARE_BRACKET.charAt(0) && dimensionString.charAt(j + 1) == FragmentConstants.RIGHT_SQUARE_BRACKET.charAt(0)) {
            soapEncArrayDimension++;
            j++;
          }
          else {
            soapEncArrayDimension = 0;
            break;
          }
        }
      }
      if (soapEncArrayTypeNamespaceURI != null && soapEncArrayTypeLocalName != null && soapEncArrayDimension == 1) {
        XSDComponent soapEncArrayType = getWSDLPartsToXSDTypeMapper().getXSDTypeFromSchema(soapEncArrayTypeNamespaceURI, soapEncArrayTypeLocalName, false);
        if (soapEncArrayType != null && soapEncArrayType instanceof XSDTypeDefinition) {
          XSDModelGroup sequence = null;
          XSDComplexTypeContent complexTypeContent = XSDTypeDefinitionUtil.getXSDComplexTypeContent(complexType);
          if (complexTypeContent != null && (complexTypeContent instanceof XSDParticle)) {
            XSDParticleContent xsdParticleContent = ((XSDParticle)complexTypeContent).getContent();
            if (xsdParticleContent instanceof XSDModelGroup)
              sequence = (XSDModelGroup)xsdParticleContent;
          }
          ISOAPEncArrayFragment frag;
          if (config.getMinOccurs() == config.getMaxOccurs())
            frag = new SOAPEncArrayFixFragment(id, name, config, getController(), sequence);
          else
            frag = new SOAPEncArrayRangeFragment(id, name, config, getController(), sequence);
          frag.setXSDTypeDefinition((XSDTypeDefinition)soapEncArrayType);
          return frag;
        }
      }
    }
    return getXSDComplexMapper().getFragment(config, id, name);
  }
}

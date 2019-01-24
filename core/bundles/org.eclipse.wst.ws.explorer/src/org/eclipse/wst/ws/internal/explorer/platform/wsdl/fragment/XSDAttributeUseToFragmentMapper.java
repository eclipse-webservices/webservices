/**
* <copyright>
*
 * Copyright (c) 2002, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
*
* </copyright>
*
* File plugins/com.ibm.etools.webservice.explorer/wsexplorer/src/com/ibm/etools/webservice/explorer/wsdl/fragment/XSDElementDeclarationToFragmentMapper.java, wsa.etools.ws.explorer, lunar-5.1.2, 20031231a 2
* Version 1.2 03/06/05 14:17:44
*/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment;

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDAttributeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.WSDLPartsToXSDTypeMapper;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDTypeDefinition;

public class XSDAttributeUseToFragmentMapper extends XSDToFragmentMapper {
  public XSDAttributeUseToFragmentMapper(XSDToFragmentController controller, WSDLPartsToXSDTypeMapper wsdlToXSDMapper) {
    super(controller, wsdlToXSDMapper);
  }

  public IXSDFragment getFragment(XSDToFragmentConfiguration config, String id, String name) {
   	XSDAttributeUse attribute = (XSDAttributeUse)config.getXSDComponent();
    if (attribute != null && attribute.getAttributeDeclaration() != null) {
   	  XSDAttributeDeclaration resolvedAttribute = resolveXSDAttributeDeclaration(attribute.getAttributeDeclaration());
      XSDTypeDefinition typeDef = getXSDTypeDefinition(resolvedAttribute);
      IXSDAttributeFragment attributeFrag = new XSDAttributeFragment(id, resolvedAttribute.getName(), config);
      attributeFrag.setXSDTypeDefinition(typeDef);
      XSDToFragmentConfiguration attributeTypeConfig = new XSDToFragmentConfiguration();
      attributeTypeConfig.setXSDComponent(typeDef);
      attributeTypeConfig.setStyle(config.getStyle());
      attributeTypeConfig.setPartEncoding(config.getPartEncoding());
      attributeTypeConfig.setWSDLPartName(config.getWSDLPartName());
      IXSDFragment xsdFragment = getController().getFragment(attributeTypeConfig, attributeFrag.genID(), resolvedAttribute.getName());
      attributeFrag.setXSDDelegationFragment(xsdFragment);
      return attributeFrag;
    }
    return getXSDDefaultFragment(config, id, name);
  }

  private XSDAttributeDeclaration resolveXSDAttributeDeclaration(XSDAttributeDeclaration attribute) {
    // port to org.eclipse.xsd
    if (attribute.getResolvedAttributeDeclaration() != null)
    {
      XSDAttributeDeclaration resolvedAttribute = attribute.getResolvedAttributeDeclaration();
      if (!isComponentResolvable(resolvedAttribute))
      {
        XSDComponent resolvedComponent = getWSDLPartsToXSDTypeMapper().resolveXSDNamedComponent(resolvedAttribute);
        if (resolvedComponent != null && (resolvedComponent instanceof XSDAttributeDeclaration))
          resolvedAttribute = (XSDAttributeDeclaration)resolvedComponent;
      }
      return resolvedAttribute;
    }
    else
      return attribute;
  }

  private XSDTypeDefinition getXSDTypeDefinition(XSDAttributeDeclaration attribute) {
    // port to org.eclipse.xsd
    if (attribute.getTypeDefinition() != null)
      return attribute.getTypeDefinition();
    // port to org.eclipse.xsd
    else if (attribute.getAnonymousTypeDefinition() != null)
      return attribute.getAnonymousTypeDefinition();
    else
      return null;
    }

}

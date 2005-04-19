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

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.*;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;


public class XSDElementDeclarationToFragmentMapper extends XSDToFragmentMapper {
  public XSDElementDeclarationToFragmentMapper(XSDToFragmentController controller, WSDLPartsToXSDTypeMapper wsdlToXSDMapper) {
    super(controller, wsdlToXSDMapper);
  }

  public IXSDFragment getFragment(XSDToFragmentConfiguration config, String id, String name) {
    XSDElementDeclaration element = (XSDElementDeclaration)config.getXSDComponent();
    if (element != null) {
      XSDElementDeclaration resolvedElement = resolveXSDElementDeclaration(element);
      XSDTypeDefinition typeDef = getXSDTypeDefinition(resolvedElement);
      if (typeDef != null) {
        int minOccurs = FragmentConstants.DEFAULT_MIN_OCCURS;
        int maxOccurs = FragmentConstants.DEFAULT_MAX_OCCURS;
        XSDConcreteComponent concreteComponent = element.getContainer();
        if (concreteComponent != null && concreteComponent instanceof XSDParticle) {
          XSDParticle particle = (XSDParticle)concreteComponent;
          if (particle.isSetMinOccurs())
            minOccurs = particle.getMinOccurs();
          if (particle.isSetMaxOccurs())
            maxOccurs = particle.getMaxOccurs();
        }
        config.setMinOccurs(minOccurs);
        config.setMaxOccurs(maxOccurs);
        IXSDElementFragment elementFrag = new XSDElementFragment(id, resolvedElement.getName(), config);
        elementFrag.setXSDTypeDefinition(typeDef);
        XSDToFragmentConfiguration elementTypeConfig = new XSDToFragmentConfiguration();
        elementTypeConfig.setXSDComponent(typeDef);
        elementTypeConfig.setMinOccurs(minOccurs);
        elementTypeConfig.setMaxOccurs(maxOccurs);
        elementTypeConfig.setStyle(config.getStyle());
        elementTypeConfig.setPartEncoding(config.getPartEncoding());
        elementTypeConfig.setWSDLPartName(config.getWSDLPartName());
        IXSDFragment xsdFragment = getController().getFragment(elementTypeConfig, elementFrag.genID(), resolvedElement.getName());
        elementFrag.setXSDDelegationFragment(xsdFragment);
        return elementFrag;
      }
    }
    return getXSDDefaultFragment(config, id, name);
  }

  private XSDElementDeclaration resolveXSDElementDeclaration(XSDElementDeclaration element) {
    // port to org.eclipse.xsd
    if (element.getResolvedElementDeclaration() != null)
    {
      XSDElementDeclaration resolvedElement = element.getResolvedElementDeclaration();
      if (!isComponentResolvable(resolvedElement))
      {
        XSDComponent resolvedComponent = getWSDLPartsToXSDTypeMapper().resolveXSDNamedComponent(resolvedElement);
        if (resolvedComponent != null && (resolvedComponent instanceof XSDElementDeclaration))
          resolvedElement = (XSDElementDeclaration)resolvedComponent;
      }
      return resolvedElement;
    }
    else
      return element;
  }

  private XSDTypeDefinition getXSDTypeDefinition(XSDElementDeclaration element) {
    // port to org.eclipse.xsd
    if (element.getTypeDefinition() != null)
      return element.getTypeDefinition();
    // port to org.eclipse.xsd
    else if (element.getAnonymousTypeDefinition() != null)
      return element.getAnonymousTypeDefinition();
    else
      return null;
    }

}

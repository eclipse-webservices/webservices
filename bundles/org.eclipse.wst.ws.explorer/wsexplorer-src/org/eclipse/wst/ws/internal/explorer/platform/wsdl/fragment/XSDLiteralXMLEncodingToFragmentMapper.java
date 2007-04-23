/*******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment;

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.WSDLPartsToXSDTypeMapper;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDParticleContent;
import org.eclipse.xsd.XSDSimpleTypeDefinition;

public class XSDLiteralXMLEncodingToFragmentMapper extends XSDToFragmentMapper {
  private XSDSimpleTypeToFragmentMapper xsdSimpleMapper_;
  private XSDComplexTypeToFragmentMapper xsdComplexMapper_;
  private XSDElementDeclarationToFragmentMapper xsdElementMapper_;
  private XSDAttributeUseToFragmentMapper xsdAttributeMapper_;
  private XSDParticleToFragmentMapper xsdParticleMapper_;

  public XSDLiteralXMLEncodingToFragmentMapper(XSDToFragmentController controller, WSDLPartsToXSDTypeMapper wsdlToXSDMapper) {
    super(controller, wsdlToXSDMapper);
    xsdSimpleMapper_ = null;
    xsdComplexMapper_ = null;
    xsdElementMapper_ = null;
    xsdAttributeMapper_ = null;
    xsdParticleMapper_= null;
  }

  private XSDSimpleTypeToFragmentMapper getXSDSimpleMapper() {
    if (xsdSimpleMapper_ == null)
      xsdSimpleMapper_ = new XSDSimpleTypeToFragmentMapper(getController(), getWSDLPartsToXSDTypeMapper());
    return xsdSimpleMapper_;
  }

  private XSDComplexTypeToFragmentMapper getXSDComplexMapper() {
    if (xsdComplexMapper_ == null)
      xsdComplexMapper_ = new XSDComplexTypeToFragmentMapper(getController(), getWSDLPartsToXSDTypeMapper());
    return xsdComplexMapper_;
  }

  private XSDElementDeclarationToFragmentMapper getXSDElementMapper() {
    if (xsdElementMapper_ == null)
      xsdElementMapper_ = new XSDElementDeclarationToFragmentMapper(getController(), getWSDLPartsToXSDTypeMapper());
    return xsdElementMapper_;
  }

  private XSDAttributeUseToFragmentMapper getXSDAttributeMapper() {
    if (xsdAttributeMapper_ == null)
      xsdAttributeMapper_ = new XSDAttributeUseToFragmentMapper(getController(), getWSDLPartsToXSDTypeMapper());
    return xsdAttributeMapper_;
  }
  
  private XSDParticleToFragmentMapper getXSDParticleMapper() {
    if (xsdParticleMapper_ == null)
      xsdParticleMapper_ = new XSDParticleToFragmentMapper(getController(), getWSDLPartsToXSDTypeMapper());
    return xsdParticleMapper_;
  }

  public IXSDFragment getFragment(XSDToFragmentConfiguration config, String id, String name) {
    config = resolveXSDComponent(config);
    XSDComponent component = config.getXSDComponent();
    if (!isComponentResolvable(component))
      return getXSDDefaultFragment(config, id, name);
    else if (component instanceof XSDElementDeclaration)
      return getXSDElementMapper().getFragment(config, id, name);
    else if (component instanceof XSDSimpleTypeDefinition)
      return getXSDSimpleMapper().getFragment(config, id, name);
    else if (component instanceof XSDComplexTypeDefinition)
      return getXSDComplexMapper().getFragment(config, id, name);
    else if (component instanceof XSDParticle)
      return getXSDParticleMapper().getFragment(config, id, name);
    else if (component instanceof XSDParticleContent)
      return getXSDParticleMapper().getFragment(config, id, name);
    else if (component instanceof XSDAttributeUse)
      return getXSDAttributeMapper().getFragment(config, id, name);	
    else
      return getXSDDefaultFragment(config, id, name);
  }
}

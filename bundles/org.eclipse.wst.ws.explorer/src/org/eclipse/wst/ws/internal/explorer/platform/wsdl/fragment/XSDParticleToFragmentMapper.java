/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
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
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDGroupAllFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDGroupChoiceFixFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDGroupChoiceRangeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDGroupSeqFixFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDGroupSeqRangeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.WSDLPartsToXSDTypeMapper;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDCompositor;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDModelGroupDefinition;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDParticleContent;
import org.eclipse.xsd.XSDWildcard;


public class XSDParticleToFragmentMapper extends XSDToFragmentMapper {
  public XSDParticleToFragmentMapper(XSDToFragmentController controller, WSDLPartsToXSDTypeMapper wsdlToXSDMapper) {
    super(controller, wsdlToXSDMapper);
  }

  public IXSDFragment getFragment(XSDToFragmentConfiguration config, String id, String name) {
    XSDComponent component = config.getXSDComponent();
    XSDParticleContent xsdParticleContent;
    if (component instanceof XSDParticle)
      xsdParticleContent = ((XSDParticle)component).getContent();
    else if (component instanceof XSDParticleContent)
      xsdParticleContent = (XSDParticleContent)component;
    else
      xsdParticleContent = null;
    if (xsdParticleContent != null) {
      if (xsdParticleContent instanceof XSDModelGroupDefinition)
        return getXSDModelGroupDefFragment(config, id, name, (XSDModelGroupDefinition)xsdParticleContent);
      else if (xsdParticleContent instanceof XSDModelGroup)
        return getXSDModelGroupFragment(config, id, name, (XSDModelGroup)xsdParticleContent);
      else if (xsdParticleContent instanceof XSDWildcard)
        return getXSDWildcardFragment(config, id, name, (XSDWildcard)xsdParticleContent);
      else
        return getXSDDefaultFragment(config, id, name);
    }
    else
      return getXSDDefaultFragment(config, id, name);
  }

  private IXSDFragment getXSDModelGroupDefFragment(XSDToFragmentConfiguration config, String id, String name, XSDModelGroupDefinition xsdModelGroupDef) {
    XSDModelGroupDefinition resolvedXSDModelGroupDef = xsdModelGroupDef;
    if (xsdModelGroupDef.isModelGroupDefinitionReference())
      resolvedXSDModelGroupDef = xsdModelGroupDef.getResolvedModelGroupDefinition();
    XSDModelGroup xsdModelGroup = resolvedXSDModelGroupDef.getModelGroup();
    if (xsdModelGroup == null)
      return getXSDDefaultFragment(config, id, name);
    else
      return getXSDModelGroupFragment(config, id, name, xsdModelGroup);
  }

  private IXSDFragment getXSDModelGroupFragment(XSDToFragmentConfiguration config, String id, String name, XSDModelGroup xsdModelGroup) {
    int minOccurs = FragmentConstants.DEFAULT_MIN_OCCURS;
    int maxOccurs = FragmentConstants.DEFAULT_MAX_OCCURS;
    XSDConcreteComponent concreteComponent = xsdModelGroup.getContainer();
    if (concreteComponent != null && concreteComponent instanceof XSDParticle) {
      XSDParticle particle = (XSDParticle)concreteComponent;
      if (particle.isSetMinOccurs())
        minOccurs = particle.getMinOccurs();
      if (particle.isSetMaxOccurs())
        maxOccurs = particle.getMaxOccurs();
    }
    config.setMinOccurs(minOccurs);
    config.setMaxOccurs(maxOccurs);
    
    switch (xsdModelGroup.getCompositor().getValue()) {
      case XSDCompositor.SEQUENCE: 
        if (minOccurs == maxOccurs)
          return new XSDGroupSeqFixFragment(id, name, config, getController(), xsdModelGroup);
        else
          return new XSDGroupSeqRangeFragment(id, name, config, getController(), xsdModelGroup);
      case XSDCompositor.CHOICE:
        if (minOccurs == maxOccurs)
          return new XSDGroupChoiceFixFragment(id, name, config, getController(), xsdModelGroup);
        else
          return new XSDGroupChoiceRangeFragment(id, name, config, getController(), xsdModelGroup);
      case XSDCompositor.ALL:
        return new XSDGroupAllFragment(id, name, config, getController(), xsdModelGroup);
      default:
        return getXSDDefaultFragment(config, id, name);
    }
  }

  private IXSDFragment getXSDWildcardFragment(XSDToFragmentConfiguration config, String id, String name, XSDWildcard xsdParticleContent) {
    return getXSDDefaultFragment(config, id, name);
  }
}

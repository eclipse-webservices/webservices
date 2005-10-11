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

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDComplexFixFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDComplexRangeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.XSDTypeDefinitionUtil;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.WSDLPartsToXSDTypeMapper;
import org.eclipse.xsd.XSDComplexTypeContent;
import org.eclipse.xsd.XSDComplexTypeDefinition;

public class XSDComplexTypeToFragmentMapper extends XSDToFragmentMapper {
  public XSDComplexTypeToFragmentMapper(XSDToFragmentController controller, WSDLPartsToXSDTypeMapper wsdlToXSDMapper) {
    super(controller, wsdlToXSDMapper);
  }

  public IXSDFragment getFragment(XSDToFragmentConfiguration config, String id, String name) {
    XSDComplexTypeDefinition complexType = (XSDComplexTypeDefinition)config.getXSDComponent();
    if (complexType != null && complexType.isMixed())
      return getXSDDefaultFragment(config, id, name);
    XSDComplexTypeContent complexTypeContent = XSDTypeDefinitionUtil.getXSDComplexTypeContent(complexType);
    if (complexTypeContent != null)
      return getXSDComplexFragment(config, id, name);
    else
      return getXSDEmptyFragment(config, id, name);
  }

  private IXSDFragment getXSDComplexFragment(XSDToFragmentConfiguration config, String id, String name) {
    int minOccurs = config.getMinOccurs();
    int maxOccurs = config.getMaxOccurs();
    
    if (minOccurs == maxOccurs)
      return new XSDComplexFixFragment(id, name, config, getController());
    else
      return new XSDComplexRangeFragment(id, name, config, getController());
  }
}

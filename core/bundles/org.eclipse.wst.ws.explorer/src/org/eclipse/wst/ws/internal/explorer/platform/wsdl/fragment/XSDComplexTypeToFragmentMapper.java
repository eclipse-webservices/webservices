/*******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment;

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDComplexFixFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDComplexRangeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDComplexSimpleContentFixFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDComplexSimpleContentRangeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.XSDTypeDefinitionUtil;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.WSDLPartsToXSDTypeMapper;
import org.eclipse.xsd.XSDComplexTypeContent;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDContentTypeCategory;

public class XSDComplexTypeToFragmentMapper extends XSDToFragmentMapper {
  public XSDComplexTypeToFragmentMapper(XSDToFragmentController controller, WSDLPartsToXSDTypeMapper wsdlToXSDMapper) {
    super(controller, wsdlToXSDMapper);
  }

  public IXSDFragment getFragment(XSDToFragmentConfiguration config, String id, String name) {
    XSDComplexTypeDefinition complexType = (XSDComplexTypeDefinition)config.getXSDComponent();
    int category = complexType.getContentTypeCategory().getValue();
    if (complexType != null && complexType.isMixed())
      return getXSDDefaultFragment(config, id, name);
    XSDComplexTypeContent complexTypeContent = XSDTypeDefinitionUtil.getXSDComplexTypeContent(complexType);
    if (complexTypeContent != null)
      return getXSDComplexFragment(category,config, id, name);
    else
      return getXSDEmptyFragment(config, id, name);
  }

  private IXSDFragment getXSDComplexFragment(int category,XSDToFragmentConfiguration config, String id, String name) {
    int minOccurs = config.getMinOccurs();
    int maxOccurs = config.getMaxOccurs();
    if (minOccurs == maxOccurs)
      if(category == XSDContentTypeCategory.SIMPLE){
      	return new XSDComplexSimpleContentFixFragment(id, name, config, getController());
        
      }
      else	
      	return new XSDComplexFixFragment(id, name, config, getController());
    else 
    	if(category == XSDContentTypeCategory.SIMPLE)
    	  return new XSDComplexSimpleContentRangeFragment(id, name, config, getController());
    	else
          return new XSDComplexRangeFragment(id, name, config, getController());
  }
}

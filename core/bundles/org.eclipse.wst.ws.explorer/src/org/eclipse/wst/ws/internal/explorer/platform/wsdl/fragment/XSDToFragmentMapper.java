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

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDDefaultFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDEmptyFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.xsd.WSDLPartsToXSDTypeMapper;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;

public abstract class XSDToFragmentMapper {
  private XSDToFragmentController controller_;
  private WSDLPartsToXSDTypeMapper wsdlToXSDMapper_;

  public XSDToFragmentMapper(XSDToFragmentController controller, WSDLPartsToXSDTypeMapper wsdlToXSDMapper) {
    controller_ = controller;
    wsdlToXSDMapper_ = wsdlToXSDMapper;
  }

  public void setController(XSDToFragmentController controller) {
    controller_ = controller;
  }

  public XSDToFragmentController getController() {
    return controller_;
  }

  public void setWSDLPartsToXSDTypeMapper(WSDLPartsToXSDTypeMapper wsdlToXSDMapper) {
    wsdlToXSDMapper_ = wsdlToXSDMapper;
  }

  public WSDLPartsToXSDTypeMapper getWSDLPartsToXSDTypeMapper() {
    return wsdlToXSDMapper_;
  }

  protected IXSDFragment getXSDDefaultFragment(XSDToFragmentConfiguration config, String id, String name) {
    return new XSDDefaultFragment(id, name, config);
  }

  protected IXSDFragment getXSDEmptyFragment(XSDToFragmentConfiguration config, String id, String name) {
    return new XSDEmptyFragment(id, name, config);
  }

  public abstract IXSDFragment getFragment(XSDToFragmentConfiguration config, String id, String name);

  protected XSDToFragmentConfiguration resolveXSDComponent(XSDToFragmentConfiguration config)
  {
    XSDComponent component = config.getXSDComponent();
    if (!isComponentResolvable(component) && (component instanceof XSDNamedComponent))
    {
      XSDComponent resolvedComponent = getWSDLPartsToXSDTypeMapper().resolveXSDNamedComponent((XSDNamedComponent)component);
      if (resolvedComponent != null)
        config.setXSDComponent(resolvedComponent);
    }
    return config;
  }

  protected boolean isComponentResolvable(XSDComponent component)
  {
    if (component == null)
      return false;
    XSDSchema schema = component.getSchema();
    if (schema == null)
      return false;
    if (schema.getTargetNamespace() == null)
      return false;
    return true;
  }
}

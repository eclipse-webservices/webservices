/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.visitor;

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.w3c.dom.Element;

public class PortTypeRenamer extends BaseRenamer
{
  /**
   * Constructor for PortTypeRenamer.
   * @param globalComponent
   * @param newName
   */
  public PortTypeRenamer(WSDLElement globalComponent, String newName)
  {
    super(globalComponent, newName);
  }

  public void visitBinding(Binding binding)
  {
    super.visitBinding(binding);
    PortType portType = binding.getEPortType();

    if (globalComponent.equals(portType))
    {
      Element element = WSDLEditorUtil.getInstance().getElementForObject(binding);
      element.setAttribute("type", getNewQName());
    }
  }
}

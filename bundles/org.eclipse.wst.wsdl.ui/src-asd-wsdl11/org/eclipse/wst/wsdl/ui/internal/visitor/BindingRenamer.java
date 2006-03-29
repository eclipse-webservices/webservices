/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.w3c.dom.Element;

public class BindingRenamer extends BaseRenamer
{
  /**
   * Constructor for BindingRenamer.
   * @param globalComponent
   * @param newName
   */
  public BindingRenamer(WSDLElement globalComponent, String newName)
  {
    super(globalComponent, newName);
  }
  
  public void visitPort(Port port)
  {
    super.visitPort(port);
    Binding binding = port.getEBinding();
    if (globalComponent.equals(binding))
    {
      Element element = WSDLEditorUtil.getInstance().getElementForObject(port);
      element.setAttribute("binding", getNewQName());
    }
  }
  
}

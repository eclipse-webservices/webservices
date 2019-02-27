/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.visitor;

import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.w3c.dom.Element;

public class MessageRenamer extends BaseRenamer
{
  /**
   * Constructor for MessageRenamer.
   * @param globalComponent
   * @param newName
   */
  public MessageRenamer(WSDLElement globalComponent, String newName)
  {
    super(globalComponent, newName);
  }

  public void visitInput(Input input)
  {
    super.visitInput(input);
    Message message = input.getEMessage();
    if (globalComponent.equals(message))
    {
      Element element = WSDLEditorUtil.getInstance().getElementForObject(input);
      element.setAttribute("message", getNewQName()); //$NON-NLS-1$
    }
  }

  public void visitOutput(Output output)
  {
    super.visitOutput(output);
    if (globalComponent.equals(output))
    {
      Element element = WSDLEditorUtil.getInstance().getElementForObject(output);
      element.setAttribute("message", getNewQName()); //$NON-NLS-1$
    }
  }

  public void visitFault(Fault fault)
  {
    super.visitFault(fault);
    if (globalComponent.equals(fault))
    {
      Element element = WSDLEditorUtil.getInstance().getElementForObject(fault);
      element.setAttribute("message", getNewQName()); //$NON-NLS-1$
    }
  }
}

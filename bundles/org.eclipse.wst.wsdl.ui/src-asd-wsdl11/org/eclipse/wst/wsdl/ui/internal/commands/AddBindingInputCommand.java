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
package org.eclipse.wst.wsdl.ui.internal.commands;

import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;

public final class AddBindingInputCommand extends WSDLElementCommand
{
  BindingInput bindingInput;
  BindingOperation bindingOperation;
  String name;
  
  public AddBindingInputCommand(BindingOperation bindingOperation, String name)
  {
    this.bindingOperation = bindingOperation;
    this.name = name;
  }

  public void run()
  {
    bindingInput = WSDLFactory.eINSTANCE.createBindingInput();
    bindingInput.setName(name); 
    bindingInput.setEnclosingDefinition(bindingOperation.getEnclosingDefinition());
    bindingOperation.setBindingInput((BindingInput)bindingInput);
  }
  
  public WSDLElement getWSDLElement()
  {
    return bindingInput;
  }
}

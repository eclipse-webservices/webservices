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

import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;

public final class AddBindingOutputCommand extends WSDLElementCommand
{
  BindingOutput bindingOutput;
  BindingOperation bindingOperation;
  String name;
  
  public AddBindingOutputCommand(BindingOperation bindingOperation, String name)
  {
    this.bindingOperation = bindingOperation;
    this.name = name;
  }

  public void run()
  {
    bindingOutput = WSDLFactory.eINSTANCE.createBindingOutput();
    bindingOutput.setName(name); 
    bindingOutput.setEnclosingDefinition(bindingOperation.getEnclosingDefinition());
    bindingOperation.setBindingOutput((BindingOutput)bindingOutput);
  }
  
  public WSDLElement getWSDLElement()
  {
    return bindingOutput;
  }
}


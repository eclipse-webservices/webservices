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
package org.eclipse.wst.wsdl.ui.internal.commands;

import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;

public final class AddBindingFaultCommand extends WSDLElementCommand
{
  BindingFault bindingFault;
  BindingOperation bindingOperation;
  String name;
  
  public AddBindingFaultCommand(BindingOperation bindingOperation, String name)
  {
    this.bindingOperation = bindingOperation;
    this.name = name;
  }

  public void run()
  {
    bindingFault = WSDLFactory.eINSTANCE.createBindingFault();
    bindingFault.setName(name); 
    bindingFault.setEnclosingDefinition(bindingOperation.getEnclosingDefinition());
    bindingOperation.addBindingFault((BindingFault)bindingFault);
  }
  
  public WSDLElement getWSDLElement()
  {
    return bindingFault;
  }
}


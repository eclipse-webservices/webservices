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

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;

public final class AddBindingOperationCommand extends WSDLElementCommand
{
  private Binding binding;
  
  private String name;
  private String bindingInputName = "";
  private String bindingOutputName = "";
  private String bindingFaultName ="";
  
  private BindingOperation bindingOperation;
  
  private boolean createBindingInput = false;
  private boolean createBindingOutput = false;
  private boolean createBindingFault = false;
  
  public AddBindingOperationCommand(Binding binding,String name)
  {
    this.binding = binding;
    this.name = name;
  }
  
  public AddBindingOperationCommand
    (Binding binding,  
     String name,
     boolean createBindingInput,
     boolean createBindingOutput,
     boolean createBindingFault)
  {
    this.binding = binding;
    this.name = name;
    this.createBindingInput = createBindingInput;
    this.createBindingOutput = createBindingOutput;
    this.createBindingFault = createBindingFault;
  }
    
  public void run()
  {
    bindingOperation = WSDLFactory.eINSTANCE.createBindingOperation();
    bindingOperation.setName(name);
    bindingOperation.setEnclosingDefinition(binding.getEnclosingDefinition());
    binding.addBindingOperation(bindingOperation);

    WSDLElementCommand command = null;
    if (createBindingInput)
    {
      command = new AddBindingInputCommand(bindingOperation,bindingInputName);
      command.run();
    }

    if (createBindingOutput)
    {
      command = new AddBindingOutputCommand(bindingOperation,bindingOutputName);
      command.run();
    }

    if (createBindingFault)
    {
      command = new AddBindingFaultCommand(bindingOperation,bindingFaultName);
      command.run();
    }
  }
  
  public WSDLElement getWSDLElement()
  {
    return bindingOperation;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public void setBindingInputName(String name)
  {
    this.bindingInputName = name;
  }
  
  public void setBindingOutputName(String name)
  {
    this.bindingOutputName = name;
  }
  
  public void setBindingFaultName(String name)
  {
    this.bindingFaultName = name;
  }
}

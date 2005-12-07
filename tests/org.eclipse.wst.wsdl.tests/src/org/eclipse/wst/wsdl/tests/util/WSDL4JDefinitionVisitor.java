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
package org.eclipse.wst.wsdl.tests.util;

import java.util.Iterator;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Import;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;

import junit.framework.TestCase;

/**
 * @author Kihup Boo
 */
public abstract class WSDL4JDefinitionVisitor extends TestCase
{
  protected Definition definition;
  
  /**
   * Use DefinitionVisitor(Definition definition)
   */
  private WSDL4JDefinitionVisitor()
  {
  }
  
  // Added for JUnit
  public WSDL4JDefinitionVisitor(String name)
  {
    super(name);
  }
  
  protected WSDL4JDefinitionVisitor(Definition definition)
  {
    this.definition = definition;
  }

  // main entry
  public void visit()
  {
    visitDefinition(definition);
  }
  
  protected void visitDefinition(Definition def)
  {
    java.util.Map imports = def.getImports();
    Iterator iterator = imports.keySet().iterator();
    Import myImport = null;
    while (iterator.hasNext())
    {
      myImport = (Import)((java.util.ArrayList)imports.get(iterator.next())).get(0);
      visitImport(myImport);
    }
    
    Types types = def.getTypes();
    if (types != null)
      visitTypes(types);

    iterator = def.getMessages().values().iterator();
    while (iterator.hasNext())
      visitMessage((Message)iterator.next());
    
    iterator = def.getPortTypes().values().iterator();
    while (iterator.hasNext())
      visitPortType((PortType)iterator.next());

    iterator = def.getBindings().values().iterator();
    while (iterator.hasNext())
      visitBinding((Binding)iterator.next());
    
    iterator = def.getServices().values().iterator();
    while (iterator.hasNext())
      visitService((Service)iterator.next());

    iterator = def.getExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement((ExtensibilityElement)iterator.next());

  }
  
  abstract protected void visitImport(Import wsdlImport);
  
  abstract protected void visitTypes(Types types);

  protected void visitMessage(Message message)
  {
    Iterator iterator = message.getParts().values().iterator();
    while (iterator.hasNext())
      visitPart((Part)iterator.next()); 
  }

  abstract protected void visitPart(Part part);
  
  protected void visitPortType(PortType portType)
  {
    Iterator iterator = portType.getOperations().iterator();
    while (iterator.hasNext())
      visitOperation((Operation)iterator.next());
  }
  
  protected void visitOperation(Operation operation)
  {
    Input input = operation.getInput();
    visitInput((Input)input);  
    
    Output output = operation.getOutput();
    visitOutput((Output)output); 

    java.util.Map faults = operation.getFaults();
    Iterator iterator = faults.keySet().iterator();
    Fault fault = null;
    while (iterator.hasNext())
    {
      fault = (Fault)faults.get(iterator.next());
      visitFault(fault);
    }
  }
  
  protected abstract void visitInput(Input input);
  
  protected abstract void visitOutput(Output output);
  
  protected abstract void visitFault(Fault fault);

  protected void visitBinding(Binding binding)
  {
    Iterator iterator = binding.getBindingOperations().iterator();
    while (iterator.hasNext())
      visitBindingOperation((BindingOperation)iterator.next());

    iterator = binding.getExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement((ExtensibilityElement)iterator.next());
  }
  
  protected void visitBindingOperation(BindingOperation operation)
  {
    BindingInput input = operation.getBindingInput();
    visitBindingInput((BindingInput)input);  
    
    BindingOutput output = operation.getBindingOutput();
    visitBindingOutput((BindingOutput)output); 
 
    java.util.Map bindingFaults = operation.getBindingFaults();
    Iterator iterator = bindingFaults.keySet().iterator();
    BindingFault bindingFault = null;
    while (iterator.hasNext())
    {
      bindingFault = (BindingFault)bindingFaults.get(iterator.next());
      visitBindingFault(bindingFault);
    }
    
    iterator = operation.getExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement((ExtensibilityElement)iterator.next());

  }
 
  protected void visitBindingInput(BindingInput input)
  {
    Iterator iterator = input.getExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement((ExtensibilityElement)iterator.next());
  }
  
  protected void visitBindingOutput(BindingOutput output)
  {
    Iterator iterator = output.getExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement((ExtensibilityElement)iterator.next());
  }
  
  protected void visitBindingFault(BindingFault fault)
  {
    Iterator iterator = fault.getExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement((ExtensibilityElement)iterator.next());
  }

  protected void visitService(Service service)
  {
    Iterator iterator = service.getPorts().values().iterator();
    while (iterator.hasNext())
      visitPort((Port)iterator.next());

    iterator = service.getExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement((ExtensibilityElement)iterator.next());
  }
  
  protected void visitPort(Port port)
  {
    Iterator iterator = port.getExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement((ExtensibilityElement)iterator.next());
  }

  abstract protected void visitExtensibilityElement(ExtensibilityElement extensibilityElement);

}

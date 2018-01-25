/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

import junit.framework.TestCase;

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.Types;


/**
 * @author Kihup Boo
 */
public abstract class DefinitionVisitor extends TestCase
{
  protected Definition definition;

  // Added for JUnit
  public DefinitionVisitor(String name)
  {
    super(name);
  }

  protected DefinitionVisitor(Definition definition)
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
    Iterator iterator = def.getEImports().iterator();
    while (iterator.hasNext())
      visitImport((Import)iterator.next());

    Types types = def.getETypes();
    if (types != null)
      visitTypes(types);

    iterator = def.getEMessages().iterator();
    while (iterator.hasNext())
      visitMessage((Message)iterator.next());

    iterator = def.getEPortTypes().iterator();
    while (iterator.hasNext())
      visitPortType((PortType)iterator.next());

    iterator = def.getEBindings().iterator();
    while (iterator.hasNext())
      visitBinding((Binding)iterator.next());

    iterator = def.getEServices().iterator();
    while (iterator.hasNext())
      visitService((Service)iterator.next());

    iterator = def.getEExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement(def, (ExtensibilityElement)iterator.next());

  }

  abstract protected void visitImport(Import wsdlImport);

  abstract protected void visitTypes(Types types);

  protected void visitMessage(Message message)
  {
    Iterator iterator = message.getEParts().iterator();
    while (iterator.hasNext())
      visitPart((Part)iterator.next());
  }

  abstract protected void visitPart(Part part);

  protected void visitPortType(PortType portType)
  {
    Iterator iterator = portType.getEOperations().iterator();
    while (iterator.hasNext())
      visitOperation((Operation)iterator.next());
  }

  protected void visitOperation(Operation operation)
  {
    Input input = operation.getEInput();
    visitInput((Input)input);

    Output output = operation.getEOutput();
    visitOutput((Output)output);

    Iterator iterator = operation.getEFaults().iterator();
    while (iterator.hasNext())
      visitFault((Fault)iterator.next());
  }

  protected abstract void visitInput(Input input);

  protected abstract void visitOutput(Output output);

  protected abstract void visitFault(Fault fault);

  protected void visitBinding(Binding binding)
  {
    Iterator iterator = binding.getEBindingOperations().iterator();
    while (iterator.hasNext())
      visitBindingOperation((BindingOperation)iterator.next());

    iterator = binding.getEExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement(binding, (ExtensibilityElement)iterator.next());
  }

  protected void visitBindingOperation(BindingOperation operation)
  {
    BindingInput input = operation.getEBindingInput();
    visitBindingInput((BindingInput)input);

    BindingOutput output = operation.getEBindingOutput();
    visitBindingOutput((BindingOutput)output);

    Iterator iterator = operation.getEBindingFaults().iterator();
    while (iterator.hasNext())
      visitBindingFault((BindingFault)iterator.next());

    iterator = operation.getEExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement(operation, (ExtensibilityElement)iterator.next());

  }

  protected void visitBindingInput(BindingInput input)
  {
    Iterator iterator = input.getEExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement(input, (ExtensibilityElement)iterator.next());
  }

  protected void visitBindingOutput(BindingOutput output)
  {
    Iterator iterator = output.getEExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement(output, (ExtensibilityElement)iterator.next());
  }

  protected void visitBindingFault(BindingFault fault)
  {
    Iterator iterator = fault.getEExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement(fault, (ExtensibilityElement)iterator.next());
  }

  protected void visitService(Service service)
  {
    Iterator iterator = service.getEPorts().iterator();
    while (iterator.hasNext())
      visitPort((Port)iterator.next());

    iterator = service.getEExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement(service, (ExtensibilityElement)iterator.next());
  }

  protected void visitPort(Port port)
  {
    Iterator iterator = port.getEExtensibilityElements().iterator();
    while (iterator.hasNext())
      visitExtensibilityElement(port, (ExtensibilityElement)iterator.next());
  }

  abstract protected void visitExtensibilityElement(ExtensibleElement owner, ExtensibilityElement extensibilityElement);

}

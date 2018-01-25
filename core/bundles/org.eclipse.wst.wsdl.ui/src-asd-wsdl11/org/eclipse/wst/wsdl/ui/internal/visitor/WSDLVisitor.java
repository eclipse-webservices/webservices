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

import java.util.Iterator;

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;

public class WSDLVisitor
{
  protected Definition definition;


  public WSDLVisitor(Definition definition)
  {
    this.definition = definition;
  }
  
  
  public void visitDefinition()
  {
    visitBindings();
    visitMessages();
    visitPortTypes();
    visitServices();
  }

  public void visitBindings()
  {
    for (Iterator iterator = definition.getEBindings().iterator(); iterator.hasNext(); )
    {
      Binding binding = (Binding)iterator.next();
      visitBinding(binding);
    }
  }

  public void visitMessages()
  {
    for (Iterator iterator = definition.getEMessages().iterator(); iterator.hasNext(); )
    {
      Message message = (Message)iterator.next();
      visitMessage(message); 
    }
  }
  
  public void visitPortTypes()
  {
    for (Iterator iterator = definition.getEPortTypes().iterator(); iterator.hasNext(); )
    {
      PortType portType = (PortType)iterator.next();
      visitPortType(portType);
    }
  }
  
  public void visitServices()
  {
    for (Iterator iterator = definition.getEServices().iterator(); iterator.hasNext(); )
    {
      Service service = (Service)iterator.next();
      visitService(service);
    }
  }

  public void visitBinding(Binding binding)
  {
    PortType portType = binding.getEPortType();
    if (portType != null)
    {
      visitPortType(portType);
    }

    for (Iterator iterator = binding.getEBindingOperations().iterator(); iterator.hasNext(); )
    {
      BindingOperation bindingOperation = (BindingOperation)iterator.next();
      visitBindingOperation(bindingOperation); 
    }
  }

  public void visitPortType(PortType portType)
  {
    for (Iterator iterator = portType.getEOperations().iterator(); iterator.hasNext(); )
    {
      Operation operation = (Operation)iterator.next();
      visitOperation(operation); 
    }
  }

  public void visitBindingOperation(BindingOperation bindingOperation)
  {
//    BindingInput bindingInput = bindingOperation.getEBindingInput();
//    BindingOutput bindingOutput = bindingOperation.getEBindingOutput();         
  }

  public void visitOperation(Operation operation)
  {
    Input input = operation.getEInput();
    Output output = operation.getEOutput();
    
    if (input != null)
    {
      visitInput(input);
    }
    if (output != null)
    {
      visitOutput(output);
    }
    
    for (Iterator iterator = operation.getEFaults().iterator(); iterator.hasNext(); )
    {
      Fault fault = (Fault)iterator.next();
      if (fault != null)
      {
        visitFault(fault);
      }
    }

  }

  public void visitPart(Part part)
  {
//    XSDTypeDefinition type = part.getTypeDefinition();
  }

  public void visitMessage(Message message)
  {
    for (Iterator iterator = message.getEParts().iterator(); iterator.hasNext(); )
    {
      Part part = (Part)iterator.next();
      visitPart(part);
    }
  }  

  public void visitInput(Input input)
  {
    Message message = input.getEMessage();
    if (message != null)
    {
      visitMessage(message);
    }
  }

  public void visitOutput(Output output)
  {
    Message message = output.getEMessage();
    if (message != null)
    {
      visitMessage(message);
    }
  }

  public void visitFault(Fault fault)
  {
    Message message = fault.getEMessage();
    if (message != null)
    {
      visitMessage(message);
    }
  }

  public void visitPort(Port port)
  {
    Binding binding = port.getEBinding();
    if (binding != null)
    {
      visitBinding(binding);
    }
  }

  public void visitService(Service service)
  {
    for (Iterator iterator = service.getEPorts().iterator(); iterator.hasNext(); )
    {
      Port port = (Port)iterator.next();
      visitPort(port);
    }
    
  }  
}

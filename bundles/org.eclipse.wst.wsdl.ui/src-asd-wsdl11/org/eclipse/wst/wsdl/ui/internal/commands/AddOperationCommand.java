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

import java.util.Iterator;

import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;


public final class AddOperationCommand extends WSDLElementCommand
{
  private PortType portType;
  private String name;
  private Operation operation;
  private Operation originalOperation;
  private boolean copyInOutFault;
  
  private boolean createInput = false;
  private boolean createOutput = false;
  private boolean createFault = false;
  
  private final String DEFAULT_INPUT_NAME = "";
  private final String DEFAULT_OUTPUT_NAME = "";
  private final String DEFAULT_FAULT_NAME = "";
  
  public AddOperationCommand
		(PortType portType,  
		 String name)
	{
	  this.portType = portType;
	  this.name = name;
	}
  
  public AddOperationCommand
		(PortType portType,  
		 String name,
		 boolean createInput,
		 boolean createOutput,
		 boolean createFault)
	{
	  this.portType = portType;
	  this.name = name;
	  this.createInput = createInput;
	  this.createOutput = createOutput;
	  this.createFault = createFault;
	}
  
  /*
   * Constructor used to create an Operation based on the given Operation.  In essence, this will
   * create a copy of the given Operation.
   */
  public AddOperationCommand(PortType portType, Operation originalOperation, String name, boolean copyInOutFault) {
  	this.portType = portType;
  	this.originalOperation = originalOperation;
  	this.name = name;
  	this.copyInOutFault = copyInOutFault;
  }
  
  public void run()
  {
    operation = WSDLFactory.eINSTANCE.createOperation();
    operation.setName(name);
    operation.setEnclosingDefinition(portType.getEnclosingDefinition());
    portType.addOperation(operation);

    if (originalOperation == null) {    
    	WSDLElementCommand command = null;
    	if (createInput)
    	{
    		command = new AddInputCommand(operation,DEFAULT_INPUT_NAME,true);
    		command.run();
    	}
    
    	if (createOutput)
    	{
    		command = new AddOutputCommand(operation,DEFAULT_OUTPUT_NAME,true);
    		command.run();
    	}
    
    	if (createFault)
    	{
    		command = new AddFaultCommand(operation,DEFAULT_FAULT_NAME,true);
    		command.run();
    	}
    }
    else {
    	// Do necessary copying of data from original operation to new operation
    	// Paramater ordering? Documentation?  This part still needs to be worked on    	
    	// Copy 'kids' if necessary
    	if (copyInOutFault) {
    		WSDLElementCommand command = null;
        
        	if (originalOperation.getEOutput() != null)
        	{
        		Output output = originalOperation.getEOutput();
        		command = new AddOutputCommand(operation, output, NameUtil.buildUniqueOutputName(portType, operation.getName(), ""));
        		command.run();
        	}
        	
        	if (originalOperation.getEInput() != null)
        	{
        		Input input = originalOperation.getEInput();
        		command = new AddInputCommand(operation, input, NameUtil.buildUniqueInputName(portType, operation.getName(), ""));
        		command.run();
        	}
        
        	if (originalOperation.getEFaults() != null)
        	{
        		Iterator it = originalOperation.getEFaults().iterator();
        		while (it.hasNext()) {
        			Fault fault = (Fault) it.next();
        			command = new AddFaultCommand(operation, fault, NameUtil.buildUniqueFaultName(originalOperation, fault.getName()));
        			command.run();
        		}
        	}
    		
    	}
    }
  }
  
  public WSDLElement getWSDLElement()
  {
    return operation;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
}

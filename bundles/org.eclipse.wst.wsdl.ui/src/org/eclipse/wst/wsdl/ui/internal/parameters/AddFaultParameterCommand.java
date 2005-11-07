/*
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package org.eclipse.wst.wsdl.ui.internal.parameters;

import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.commands.AddFaultCommand;

public class AddFaultParameterCommand extends AddBaseParameterCommand {
	protected Fault fault;
	private String faultName;
	
	public AddFaultParameterCommand(Operation operation, Fault fault) {
		super(operation, AddBaseParameterCommand.PART_ELEMENT);
		this.fault = fault;
	}
	
	/*
	 * 
	 */
	public void run() {
		if (fault == null) {
			AddFaultCommand command = new AddFaultCommand(operation, getFaultName());
			command.run();
			fault = (Fault) command.getWSDLElement();
		}
		
		Part part = createWSDLComponents(fault);
		
		// Create necessary XSD Objects starting with the Part reference
		newXSDElement = createXSDObjects(part);
	}

	protected String getAnonymousXSDElementBaseName() {
		if (newAnonymousXSDElementName == null) {
			newAnonymousXSDElementName = operation.getName() + "_" + getFaultName(); 
		}
		
		return newAnonymousXSDElementName;
	}
	
	protected String getNewXSDElementBaseName() {
		if (newXSDElementName == null) {
			newXSDElementName = operation.getName() + "_" + getFaultName();
		}
		
		return newXSDElementName;
	}
	
	protected String getWSDLMessageName() {
		if (newWSDLMessageName == null) {
			newWSDLMessageName = operation.getName() + "_" + getFaultName() + "Msg";
		}
		
		return newWSDLMessageName;
	}

	protected String getWSDLPartName() {
		if (newWSDLPartName == null) {
			newWSDLPartName = getFaultName();
		}
		
		return newWSDLPartName;
	}
	
	private String getFaultName() {
		if (faultName == null) {
			if (fault != null) {
				faultName = fault.getName();
			}
			else {
				faultName = WNameHelperUtil.getFaultName(operation);
			}
		}

		return faultName;
	}
}

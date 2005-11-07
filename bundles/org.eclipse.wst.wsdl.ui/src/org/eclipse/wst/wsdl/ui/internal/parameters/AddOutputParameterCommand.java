/*
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package org.eclipse.wst.wsdl.ui.internal.parameters;

import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOutputCommand;

public class AddOutputParameterCommand extends AddBaseParameterCommand {
	public AddOutputParameterCommand(Operation operation, int style) {
		super(operation, style);
	}
	
	/*
	 * 
	 */
	public void run() {
		Part part = null;
		if (operation.getEOutput() != null) {
			part = createWSDLComponents(operation.getEOutput());
		}
		else {
			AddOutputCommand command = new AddOutputCommand(operation, null);
			command.run();
			Output output = (Output) command.getWSDLElement();
			part = createWSDLComponents(output);
		}
		
		// Create necessary XSD Objects starting with the Part reference
		newXSDElement = createXSDObjects(part);
	}
	
	protected String getAnonymousXSDElementBaseName() {
		if (newAnonymousXSDElementName == null) {
			newAnonymousXSDElementName = operation.getName() + "Response"; 
		}
		
		return newAnonymousXSDElementName;
	}
	
	protected String getNewXSDElementBaseName() {
		if (newXSDElementName == null) {
			newXSDElementName = "output";
		}
		
		return newXSDElementName;
	}
	
	protected String getWSDLMessageName() {
		if (newWSDLMessageName == null) {
			if (operation.getEOutput() != null) {
				newWSDLMessageName= WNameHelperUtil.getMessageName(operation.getEOutput());
			}
		}
		
		return newWSDLMessageName;
	}

	protected String getWSDLPartName() {
		if (newWSDLPartName == null) {
			newWSDLPartName  = WNameHelperUtil.getPartName(operation.getEOutput());			
		}
		
		return newWSDLPartName;
	}
}

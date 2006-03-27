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

import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class AddInputParameterCommand extends AddBaseParameterCommand {	
	public AddInputParameterCommand(Operation operation, int style) {
		super(operation, style);
	}
	
	/*
	 * 
	 */
	public void run() {
		Part part = null;
		if (operation.getEInput() != null) {
			part = createWSDLComponents(operation.getEInput());
		}
		else {
			AddInputCommand command = new AddInputCommand(operation, null);
			command.run();
			Input input = (Input) command.getWSDLElement();
			part = createWSDLComponents(input);
		}
		
		// Create necessary XSD Objects starting with the Part reference
		newXSDElement = createXSDObjects(part);
	}
	
	protected String getAnonymousXSDElementBaseName() {
		if (newAnonymousXSDElementName == null) {
			newAnonymousXSDElementName = operation.getName() + "Request"; 
		}
		
		return newAnonymousXSDElementName;
	}
	
	protected String getNewXSDElementBaseName() {
		if (newXSDElementName == null) {
			newXSDElementName = "input";
		}
		
		return newXSDElementName;
	}
	
	protected String getWSDLMessageName() {
		if (newWSDLMessageName == null) {
			if (operation.getEInput() != null) {
				newWSDLMessageName= NameUtil.getMessageName(operation.getEInput());
			}
		}
		
		return newWSDLMessageName;
	}

	protected String getWSDLPartName() {
		if (newWSDLPartName == null) {
			newWSDLPartName  = NameUtil.getPartName(operation.getEInput());			
		}
		
		return newWSDLPartName;
	}
}

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

import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class AddOutputParameterCommand extends AddBaseParameterCommand {
	private Output output;
	
	public AddOutputParameterCommand(Operation operation, int style) {
		super(operation, style);
	}
	
	/*
	 * 
	 */
	public void run() {
		Part part = null;
		if (operation.getEOutput() != null) {
			output = operation.getEOutput();
			part = createWSDLComponents(operation.getEOutput());
		}
		else {
			AddOutputCommand command = new AddOutputCommand(operation, null);
			command.run();
			output = (Output) command.getWSDLElement();
			part = createWSDLComponents(output);
		}
		
		// Create necessary XSD Objects starting with the Part reference
		newXSDElement = createXSDObjects(part);
	}
	
	protected String getAnonymousXSDElementBaseName() {
		if (newAnonymousXSDElementName == null) {
			newAnonymousXSDElementName = getWSDLPartName();
		}
		
		return newAnonymousXSDElementName;
	}
	
	protected String getNewXSDElementBaseName() {
		if (newXSDElementName == null) {
//			newXSDElementName = "output"; //$NON-NLS-1$
			newXSDElementName = getWSDLPartName();
		}
		
		return newXSDElementName;
	}
	
	protected String getWSDLMessageName() {
		if (newWSDLMessageName == null) {
			if (operation.getEOutput() != null) {
				newWSDLMessageName= NameUtil.getMessageName(operation.getEOutput());
			}
		}
		
		return newWSDLMessageName;
	}

	protected String getWSDLPartName() {
		if (newWSDLPartName == null) {
			newWSDLPartName  = NameUtil.getPartName(operation.getEOutput());			
		}
		
		return newWSDLPartName;
	}
	
	public Output getOutput() {
		return output;
	}
}

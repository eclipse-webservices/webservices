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

import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

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
			newAnonymousXSDElementName = operation.getName() + "_" + getFaultName();  //$NON-NLS-1$
		}
		
		return newAnonymousXSDElementName;
	}
	
	protected String getNewXSDElementBaseName() {
		if (newXSDElementName == null) {
			newXSDElementName = operation.getName() + "_" + getFaultName(); //$NON-NLS-1$
		}
		
		return newXSDElementName;
	}
	
	protected String getWSDLMessageName() {
		if (newWSDLMessageName == null) {
			newWSDLMessageName = operation.getName() + "_" + getFaultName() + "Msg"; //$NON-NLS-1$ //$NON-NLS-2$
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
				faultName = NameUtil.getFaultName(operation);
			}
		}

		return faultName;
	}
	
	public Fault getFault() {
		return fault;
	}
}

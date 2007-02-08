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
import java.util.List;

import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;

public class AddFaultParameterCommand extends AddBaseParameterCommand {
	protected Fault fault;
	private String faultName;
	
    public static int getParameterPatternForFault(Operation operation, Fault fault) {
    	int pattern = -1;
    	if (fault != null) {
    		pattern = getPattern(fault.getEMessage());
    	}
		if (pattern == -1) {
			pattern = AddBaseParameterCommand.getParameterPattern(operation);
		}
		
		return pattern;
    }
    
    private static int getPattern(Message message) {
    	int pattern = -1;
		if (message != null) {
			Iterator parts = message.getEParts().iterator();
			while (parts.hasNext()) {
				Part part = (Part) parts.next();
				if (part.getElementDeclaration() != null) {
					pattern = AddBaseParameterCommand.PART_ELEMENT;

					XSDElementDeclaration xsdElement = part.getElementDeclaration();
					if (isSequencePattern(xsdElement.getTypeDefinition())) {
						pattern = AddBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT;	
					}					
					break;
				}
				else if (part.getTypeDefinition() != null) {
					pattern = AddBaseParameterCommand.PART_SIMPLETYPE;
					
					if (part.getTypeDefinition() instanceof XSDComplexTypeDefinition) {
						pattern = AddBaseParameterCommand.PART_COMPLEXTYPE;
						
						XSDComplexTypeDefinition xsdType = (XSDComplexTypeDefinition) part.getTypeDefinition();
						if (isSequencePattern(xsdType)) {
							pattern = AddBaseParameterCommand.PART_COMPLEXTYPE_SEQ_ELEMENT; 
						}
					}
					break;
				}
			}
		}
		
		return pattern;
    }
    
    private static boolean isSequencePattern(XSDTypeDefinition type) {
    	boolean isSequencePattern = false;
    	
    	if (type instanceof XSDComplexTypeDefinition) {
    		XSDComplexTypeDefinition complexType = (XSDComplexTypeDefinition) type;

	    	if (complexType.getContent() instanceof XSDParticle) {
				XSDParticle particle = (XSDParticle) complexType.getContent();
				if (particle.getContent() instanceof XSDModelGroup) {
					isSequencePattern = true;
				}
			}
    	}
    	
    	return isSequencePattern;
    }
	
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
		newPart = part;
		
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
			List usedNames = NameUtil.getUsedMessageNames(operation.getEnclosingDefinition());
			newWSDLMessageName = NameUtil.getUniqueNameHelper(newWSDLMessageName, usedNames);
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
	
	// TODO: remove this method and use getMessageReference() instead
	public Fault getFault() {
		return fault;
	}
	
	public MessageReference getMessageReference() {
		return fault;
	}
}

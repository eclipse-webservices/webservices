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
package org.eclipse.wst.wsdl.ui.internal.adapters.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11MessageReference;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForPart;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;


public class W11DeleteParameterCommand extends Command {
	private IParameter parameter;
	
	public W11DeleteParameterCommand(IParameter paramter) {
        super(Messages.getString("_UI_ACTION_DELETE"));
		this.parameter = paramter;
	}
	
	public void execute() {
	    delete();
	}
	
	private void delete() {
		Object object = ((WSDLBaseAdapter) parameter).getTarget();
		
		if (object instanceof XSDElementDeclaration) {
			XSDConcreteComponent xsdComponent = ((XSDElementDeclaration) object).getContainer();
			
			if (xsdComponent instanceof XSDParticle) {
				XSDParticle particle = (XSDParticle) xsdComponent;
				if (particle.getContainer() instanceof XSDModelGroup) {
					XSDModelGroup modelGroup = (XSDModelGroup) particle.getContainer();
					if (modelGroup.getContents().size() >= 2) {
						modelGroup.getContents().remove(particle);
					}
					else {
						modelGroup.getContents().remove(particle);
						// TODO: We need the line below to delete the Message and Part
//						removeMessageAndPart(wParameter.getPart());
						// TODO: Remove the XSD components as well.....
					}
				}
			}
			else if (xsdComponent instanceof XSDSchema){
				// TODO: We need the line below to delete the Message and Part
//				removeMessageAndPart(wParameter.getPart()); // Delete the part as well
				XSDSchema xsdSchema = (XSDSchema) xsdComponent;
				xsdSchema.getContents().remove(object);
			}
		}
		else if (object instanceof Part) {
			removeMessageAndPart((Part) object);
		}
		else if (object instanceof Message) {
			removeMessageAndPart((Part) ((Message) object).getEParts().get(0));
		}
		
		// Do we want to remove the Input/Output/Fault MessageReference as well if
		// there are no more "inputs"?....
	}
	
	protected void removeMessageAndPart(Part part) {
		W11MessageReference  wMessage = null;
		/*
		if (parameter instanceof W11ParameterForElement) {
			wMessage = (W11MessageReference) ((W11ParameterForElement) parameter).getOwner();	
		}
		else */if (parameter instanceof W11ParameterForPart) {
			wMessage = (W11MessageReference) ((W11ParameterForPart) parameter).getOwner();
		}
		
		Message message = wMessage.getMessageReference().getEMessage(); 
		message.getEParts().remove(part);
		
		((Definition) part.getEnclosingDefinition()).getEMessages().remove(message);
	}
}

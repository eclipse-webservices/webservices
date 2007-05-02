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
package org.eclipse.wst.wsdl.ui.internal.commands;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.adapters.visitor.W11FindInnerElementVisitor;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.XSDComponentHelper;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;

public class AddInputParameterCommand extends AddBaseParameterCommand {
	protected boolean createXSDObjects = true;
	protected boolean reuseExistingMessage = false;
	private Input input;
	public AddInputParameterCommand(Operation operation, int style) {
		super(operation, style);
	}

	public AddInputParameterCommand(Operation operation, int style, boolean reuseMessage) {
		super(operation, style);
		reuseExistingMessage = reuseMessage;
	}
	
	/*
	 * 
	 */
	public void run() {
		Part part = null;
		if (operation.getEInput() != null) {
			input = operation.getEInput();
		}
		else {
			AddInputCommand command = new AddInputCommand(operation, null);
			command.run();
			input = (Input) command.getWSDLElement();
		}
		
		if (style == AddBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT) {
			part = createDocLitWrappedWSDLComponents(input);
		}
		else {
			part = createWSDLComponents(input);
		}
		
		newPart = part;
		// Create necessary XSD Objects starting with the Part reference
		if (createXSDObjects)
			newXSDElement = createXSDObjects(part);
	}
	
	protected Part createDocLitWrappedWSDLComponents(MessageReference messageRef) {
		Message message = messageRef.getEMessage();
		Part part = null;

		if (reuseExistingMessage && message == null) {
			// See if we can use an existing Message
			Message existingMessage = null;
			String messageName = getIdealMessageName(messageRef);
			Iterator messages = messageRef.getEnclosingDefinition().getEMessages().iterator();
			while (messages.hasNext()) {
				Message item = (Message) messages.next();
				QName itemName = item.getQName();
				if (itemName != null && itemName.getLocalPart() != null && messageName.equals(itemName.getLocalPart())) {
					existingMessage = item;
					break;
				}
			}

			if (existingMessage != null) {
				if (existingMessage.getEParts().size() > 0) {
					// See if existing Message has a Part referencing an XSD Element with the proper name
					Part existingPart = (Part) existingMessage.getEParts().get(0);
					if (!(existingPart.getName().equals(getDocLitWrappedPartName()))) {
						message = null;
					}
					else {
						XSDElementDeclaration existingElement = existingPart.getElementDeclaration();
						if (existingElement != null && existingElement.getName().equals(operation.getName())) {
							// There is an existing XSD Element with the proper name
							// See if the XSD Element has an anonymous type with a sequence and an XSD Element
							W11FindInnerElementVisitor visitor = new W11FindInnerElementVisitor();
							XSDElementDeclaration innerElement = visitor.getInnerXSDElement(existingElement);
							if (!innerElement.equals(existingElement)) {
								// Found an existing inner XSD Element
								createXSDObjects = false;

								// set the MessageReference --> Message reference
								messageRef.setEMessage(existingMessage);
								message = existingMessage;
							}
						}
					}
				}
			}
		}

		if (message == null || message.eContainer() == null) {
			// Create Message
			AddMessageCommand command = new AddMessageCommand(messageRef.getEnclosingDefinition(), getWSDLMessageName());
			command.run();
			message = (Message) command.getWSDLElement();
			messageRef.setEMessage(message);
		}


		if (message.getEParts().size() == 0) {
			// Create Part
			String partName = getDocLitWrappedPartName();
			AddPartCommand command = new AddPartCommand(message, partName);
			command.run();
			part = (Part) command.getWSDLElement();
		}
		else {
			part = (Part) message.getEParts().get(0);
		}
		formatChild(message.getElement());

		return part;
	}
	
	protected String getAnonymousXSDElementBaseName() {
		if (newAnonymousXSDElementName == null) {
			if (this.style == AddBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT) {
				newAnonymousXSDElementName = operation.getName();
			}
			else {
				newAnonymousXSDElementName = getWSDLPartName();
			}
		}
		
		return newAnonymousXSDElementName;
	}
	
	protected String getNewXSDElementBaseName() {
		if (newXSDElementName == null) {
			newXSDElementName = "in"; //$NON-NLS-1$
//			newXSDElementName = getWSDLPartName();
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

	protected XSDElementDeclaration createPartElementSeqElementPattern(Part part, XSDElementDeclaration partElement) {
		XSDElementDeclaration returnedXSDElement = null;

		XSDElementDeclaration originalElement = null;
		XSDElementDeclaration anonXSDElement = null;

		// Create the XSDElement (anonymous) referenced by the Part if necessary
		if (partElement == null || partElement.getAnonymousTypeDefinition() == null) {
			//Find an existing XSDElement to use if possible
			XSDElementDeclaration existingElement = null;
			String idealElementName = getAnonymousXSDElementBaseName();
			XSDSchema schema = XSDComponentHelper.getXSDSchema(part.getEnclosingDefinition());
			if (schema != null) {
				Iterator it = schema.getElementDeclarations().iterator();
				while (existingElement == null && it.hasNext()) {
					XSDElementDeclaration item = (XSDElementDeclaration) it.next();
					if (item.getName().equals(idealElementName)) {
						// found an element we can use.  Confirm it has a doc-lit-wrapped pattern
						// See if the XSD Element has an anonymous type with a sequence and an XSD Element
						existingElement = item;

						XSDTypeDefinition xsdType = item.getAnonymousTypeDefinition();
						if (xsdType != null) {
							List contents = xsdType.eContents();
							if (contents.size() > 0 && contents.get(0) instanceof XSDParticle) {
								XSDParticle particle = (XSDParticle) contents.get(0);
								List particleContents = particle.eContents();
								if (particleContents.size() > 0 && particleContents.get(0) instanceof XSDModelGroup) {
									XSDModelGroup modelGroup = (XSDModelGroup) particleContents.get(0);
									List modelContents = modelGroup.eContents();
									if (modelContents.size() > 0 && modelContents.get(0) instanceof XSDParticle) {
										XSDParticle innerParticle = (XSDParticle) modelContents.get(0);
										List innerContents = innerParticle.eContents();
										if (innerContents.size() > 0 && innerContents.get(0) instanceof XSDElementDeclaration) {
											// inner XSD Element exists
											// reuse existing element
											String prefixedName = getPrefixedComponentName(part.getEnclosingDefinition(), existingElement);
											ComponentReferenceUtil.setComponentReference(part, false, prefixedName);
											return existingElement;
										}
									}
								}
							}
						}
					}
				}
			}

			if (existingElement == null) {
				anonXSDElement = XSDComponentHelper.createAnonymousXSDElementDefinition(getAnonymousXSDElementBaseName(), part);
//				part.setElementDeclaration(anonXSDElement);
				String prefixedName = getPrefixedComponentName(part.getEnclosingDefinition(), anonXSDElement);
				ComponentReferenceUtil.setComponentReference(part, false, prefixedName);
				part.setTypeDefinition(null);

				if (partElement != null && partElement.getSchema() != null) {
					originalElement = partElement;
					// Remove the 'original' XSDElement as a Global Element
					partElement.getSchema().getContents().remove(partElement);
				}
			}
			else {
				anonXSDElement = existingElement;
				// reuse existing element
				String prefixedName = getPrefixedComponentName(part.getEnclosingDefinition(), existingElement);
				ComponentReferenceUtil.setComponentReference(part, false, prefixedName);
			}
		}
		else {
			anonXSDElement = partElement;
		}

		// Create a new XSDElement
		XSDModelGroup modelGroup = XSDComponentHelper.getXSDModelGroup(anonXSDElement, part.getEnclosingDefinition());
		returnedXSDElement = XSDComponentHelper.createXSDElementDeclarationCommand(null, getNewXSDElementBaseName(), modelGroup);

		// Add the newly created XSDElement to the ModelGroup
		XSDComponentHelper.addXSDElementToModelGroup(anonXSDElement, returnedXSDElement);

		// Add the 'original' XSDElement if it's type wasn't anonymous
		if (originalElement != null) {
			XSDComponentHelper.addXSDElementToModelGroup(anonXSDElement, originalElement);
		}
		formatChild(anonXSDElement.getElement());
		return returnedXSDElement;
	}
	
	private String getIdealMessageName(MessageReference messageRef) {	    
		String messageName = "NewMessage"; //$NON-NLS-1$
		messageName = operation.getName() + "Request"; //$NON-NLS-1$
		
		return messageName;
	}
	
	public MessageReference getMessageReference() {
		return input;
	}
}

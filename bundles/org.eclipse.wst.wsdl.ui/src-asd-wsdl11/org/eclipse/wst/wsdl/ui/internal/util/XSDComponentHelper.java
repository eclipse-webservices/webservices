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
package org.eclipse.wst.wsdl.ui.internal.util;

import java.util.Iterator;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDElementDeclarationCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDSchemaCommand;
import org.eclipse.wst.xsd.ui.internal.common.commands.AddXSDElementCommand;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDCompositor;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;

/**
 * Input createInput(Operation operation, String style) --- create default message/part/etc.
 * Object createInputParameter(Operation operation, Input input, Style style) --
 * 
 * Command getCreateInputParameterCommand(Operation operation, Input input, Style style)
 * 
 * Message/part/Element
 * Message/part/Element/sequence/Element
 * (x) Message/part
 */
public class XSDComponentHelper {
	/*
	 * Create an XSDElement with type anonymous.  Set the given part to reference the newly created
	 * XSDElement.
	 */
	public static XSDElementDeclaration createAnonymousXSDElementDefinition(String baseName, Part part) {
		XSDElementDeclaration anonElement = createXSDElementDeclarationCommand(part.getEnclosingDefinition(), baseName, part);
		XSDComplexTypeDefinition newAnon = XSDFactory.eINSTANCE.createXSDComplexTypeDefinition();
		XSDParticle newXSDParticle = XSDFactory.eINSTANCE.createXSDParticle();
		XSDModelGroup newXSDModelGroup = XSDFactory.eINSTANCE.createXSDModelGroup();
		newXSDModelGroup.setCompositor(XSDCompositor.SEQUENCE_LITERAL);
		
		anonElement.setAnonymousTypeDefinition(newAnon);
		newXSDParticle.setContent(newXSDModelGroup);
		newAnon.setContent(newXSDParticle);
		
		return anonElement;
	}
	
	public static XSDElementDeclaration createXSDElementDeclarationCommand(Definition definition, String baseName, Object context) {
		XSDElementDeclaration xsdElement = null;
		
		if (context instanceof Part) {
			AddXSDElementDeclarationCommand command = new AddXSDElementDeclarationCommand(definition, ""); //$NON-NLS-1$
			
			// Find a unique name
			XSDSchema xsdSchema = command.getSchema();
			String name = NameUtil.getXSDElementName(baseName, xsdSchema);
			command.run(name);
			// The 'workaround' below is causing me to hang.....  The latest WTP is needed.
//			xsdElement = xsdSchema.resolveElementDeclaration(name);
			xsdElement = command.getXSDElement();
		}
		else if (context instanceof XSDModelGroup) {
			XSDModelGroup modelGroup = (XSDModelGroup) context;
			XSDParticle newParticle = XSDFactory.eINSTANCE.createXSDParticle();
			String name = NameUtil.getXSDElementName(baseName, modelGroup);
			
			AddXSDElementCommand command = new AddXSDElementCommand("Add XML Schema Element", modelGroup);
			command.execute();
			xsdElement = (XSDElementDeclaration) command.getAddedComponent();
			xsdElement.setName(name);
			xsdElement.setTypeDefinition(getXSDStringType(modelGroup));
			
			newParticle.setContent(xsdElement);
			modelGroup.getContents().add(newParticle);
		}
		else {
			AddXSDElementCommand command = new AddXSDElementCommand();
			command.execute();
			xsdElement = (XSDElementDeclaration) command.getAddedComponent();
			xsdElement.setName(baseName);
		}
		
		return xsdElement;
	}
	
	public static void addXSDElementToModelGroup(XSDElementDeclaration parent, XSDElementDeclaration child) {
		if (parent.getAnonymousTypeDefinition() instanceof XSDComplexTypeDefinition) {
			XSDComplexTypeDefinition anonType = (XSDComplexTypeDefinition) parent.getAnonymousTypeDefinition();
			if (anonType.getContent() instanceof XSDParticle) {
				XSDParticle particle = (XSDParticle) anonType.getContent();
				if (particle.getContent() instanceof XSDModelGroup) {
					// Create XSDParticle
					XSDModelGroup modelGroup = (XSDModelGroup) particle.getContent();
					addXSDElementToModelGroup(modelGroup, child);
				}
				else {
					// Create XSDModelGroup -- XSDParticle
					addXSDElementToModelGroup(particle, child);
				}				  
			}
			else {
				// Create XSDParticle --> XSDModelGroup --> XSDParticle
				XSDParticle newParticle = XSDFactory.eINSTANCE.createXSDParticle();
				anonType.setContent(newParticle);
				addXSDElementToModelGroup(newParticle, child);
			}			  
		}
		else {
			// Create Anonymous Type --> XSDParticle --> XSDModelGroup --> XSDParticle
			XSDComplexTypeDefinition newAnonType = XSDFactory.eINSTANCE.createXSDComplexTypeDefinition();
			parent.setAnonymousTypeDefinition(newAnonType);
			
			XSDParticle newParticle = XSDFactory.eINSTANCE.createXSDParticle();
			newAnonType.setContent(newParticle);
			addXSDElementToModelGroup(newParticle, child);
		}
	}
	
	public static void addXSDElementToModelGroup(XSDParticle particle, XSDElementDeclaration child) {
		XSDModelGroup newXSDModelGroup = XSDFactory.eINSTANCE.createXSDModelGroup();
		newXSDModelGroup.setCompositor(XSDCompositor.SEQUENCE_LITERAL);					  
		particle.setContent(newXSDModelGroup);
		addXSDElementToModelGroup(newXSDModelGroup, child);
	}
	
	public static void addXSDElementToModelGroup(XSDModelGroup modelGroup, XSDElementDeclaration child) {
		// Create XSDParticle
		XSDParticle newParticle = XSDFactory.eINSTANCE.createXSDParticle();
		newParticle.setContent(child);
		modelGroup.getContents().add(newParticle);
	}
	
	/*
	 * Return it's XSDModelGroup.  If one does not exist, create one
	 */
	public static XSDModelGroup getXSDModelGroup(XSDElementDeclaration xsdElement, Definition definition) {
		XSDModelGroup modelGroup = null;
		XSDComplexTypeDefinition anonType = null;
		
		if (xsdElement.getAnonymousTypeDefinition() instanceof XSDComplexTypeDefinition) {
			anonType = (XSDComplexTypeDefinition) xsdElement.getAnonymousTypeDefinition();
		}		  
		else {
			// Create Anonymous Type
			anonType = XSDFactory.eINSTANCE.createXSDComplexTypeDefinition();
			anonType.setName("NewComplexTypeName"); //$NON-NLS-1$
			getXSDSchema(definition).getContents().add(anonType);
			xsdElement.setAnonymousTypeDefinition(anonType);
		}
		
		if (anonType != null) {
			modelGroup = getXSDModelGroup(anonType);
		}
		
		return modelGroup;
	}
	
	/*
	 * Return it's XSDModelGroup.  If one does not exist, create one
	 */
	public static XSDModelGroup getXSDModelGroup(XSDComplexTypeDefinition xsdComplexType) {
		XSDModelGroup modelGroup = null;
		XSDParticle particle = null;
		// Check for XSDParticle
		if (xsdComplexType.getContent() instanceof XSDParticle) {
			particle = (XSDParticle) xsdComplexType.getContent();
		}
		else {
			particle = XSDFactory.eINSTANCE.createXSDParticle();
			modelGroup = XSDFactory.eINSTANCE.createXSDModelGroup();
			modelGroup.setCompositor(XSDCompositor.SEQUENCE_LITERAL);			  
			particle.setContent(modelGroup);
			xsdComplexType.setContent(particle);
		}
		
		// Check for XSDModelGroup
		if (particle.getContent() instanceof XSDModelGroup) {
			modelGroup = (XSDModelGroup) particle.getContent();
		}
		else {
			modelGroup = XSDFactory.eINSTANCE.createXSDModelGroup();
			modelGroup.setCompositor(XSDCompositor.SEQUENCE_LITERAL);			  
			particle.setContent(modelGroup);
		}
		
		return modelGroup;
	}
	
	/*
	 * 
	 */
	public static XSDComplexTypeDefinition createXSDComplexTypeDefiniion(String complexTypeName, Part part) {
		XSDComplexTypeDefinition newComplexType = XSDFactory.eINSTANCE.createXSDComplexTypeDefinition();
		XSDParticle newXSDParticle = XSDFactory.eINSTANCE.createXSDParticle();
		XSDModelGroup newXSDModelGroup = XSDFactory.eINSTANCE.createXSDModelGroup();
		newXSDModelGroup.setCompositor(XSDCompositor.SEQUENCE_LITERAL);
		
		newComplexType.setName(complexTypeName);
		newComplexType.setContent(newXSDParticle);
		newXSDParticle.setContent(newXSDModelGroup);
		getXSDSchema(part.getEnclosingDefinition()).getContents().add(newComplexType);
		
		return newComplexType;
	}
	
	public static void addXSDElementToModelGroup(XSDComplexTypeDefinition parent, XSDElementDeclaration child) {
		if (parent.getContent() instanceof XSDParticle) {
			XSDParticle particle = (XSDParticle) parent.getContent();
			if (particle.getContent() instanceof XSDModelGroup) {
				// Create XSDParticle
				XSDModelGroup modelGroup = (XSDModelGroup) particle.getContent();
				addXSDElementToModelGroup(modelGroup, child);
			}
			else {
				// Create XSDModelGroup -- XSDParticle
				addXSDElementToModelGroup(particle, child);
			}				  
		}
		else {
			// Create XSDParticle --> XSDModelGroup --> XSDParticle
			XSDParticle newParticle = XSDFactory.eINSTANCE.createXSDParticle();
			parent.setContent(newParticle);
			addXSDElementToModelGroup(newParticle, child);
		}
	}
	
	/*
	 * Return the inline XSDSchema associated with this WSDL.
	 * Look for the inline XSDSchema with the same namespace as the WSDL.
	 * If this inline XSDSchema does not exists, create it.
	 */
	public static XSDSchema getXSDSchema(Definition definition) {
		XSDSchema schema = null;
		String wsdlTargetNamespace = definition.getTargetNamespace();
		
		Types types = (Types) definition.getTypes();
		if (types != null) {
			
			Iterator eeIt = types.getExtensibilityElements().iterator();
			while (eeIt.hasNext()) {
				Object eeElement = eeIt.next();
				if (eeElement instanceof XSDSchemaExtensibilityElement) {
					XSDSchemaExtensibilityElement ee = (XSDSchemaExtensibilityElement) eeElement;
					if (ee.getSchema().getTargetNamespace().equals(wsdlTargetNamespace)) {
						schema = ee.getSchema();
						break;
					}
				}
			}
		}
		
		if (schema == null) {
			// We need to create the schema
			AddXSDSchemaCommand command = new AddXSDSchemaCommand(definition);
			command.run();
			XSDSchemaExtensibilityElement eeElement = (XSDSchemaExtensibilityElement) command.getWSDLElement();
			schema = eeElement.getSchema();
		}
		
		return schema;
	}
	
	private static XSDSimpleTypeDefinition getXSDStringType(XSDComponent component) {
		XSDSchema schema = component.getSchema();
		return schema.getSchemaForSchema().resolveSimpleTypeDefinition("string"); //$NON-NLS-1$
	}
}

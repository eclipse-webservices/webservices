/*
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package org.eclipse.wst.wsdl.ui.internal.parameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;

public class WNameHelperUtil {
	public static String getMessageName(MessageReference messageRef) {	    
		String messageName = "NewMessage";
		List messageNames = new ArrayList();
		Operation operation = (Operation) messageRef.getContainer();
		Iterator messageIt = operation.getEnclosingDefinition().getEMessages().iterator();
		while (messageIt.hasNext()) {
			messageNames.add(((Message) messageIt.next()).getQName().getLocalPart());
		}
		
		String requestResponseString = getRequestOrResponse(messageRef) + "Msg";
		messageName = getUniqueName(operation.getName() + requestResponseString, messageNames);
		
		return messageName;
	}
	
	public static String getPartName(MessageReference messageRef) {
		String partName = "NewPart";
		Message message = messageRef.getEMessage();
		
		Operation operation = (Operation) messageRef.getContainer();
//		Operation operation = (Operation) ((WOperation) ownerMessage.getOwnerOperation()).getModel();
		String operationName = operation.getName();
		String appendString = "";    	  
		if (messageRef instanceof Input) {
			appendString = "Parameters";
		}
		else if (messageRef instanceof Output) {
			appendString = "Result";
		}
		else if (messageRef instanceof Fault) {
			appendString = "Fault";
		}
		partName = operationName + appendString;
		
		List usedPartNames = new ArrayList();
		if (message != null) {
			Iterator partIt = message.getEParts().iterator();
			while (partIt.hasNext()) {
				usedPartNames.add(((Part) partIt.next()).getName());
			}
		}
		
		partName = getUniqueName(partName, usedPartNames);
		
		return partName;
	}
	
	public static String getOperationName(PortType portType) {
		String operationName = "NewOperation";
		Iterator operationIt = portType.getEOperations().iterator();
		List usedNames = new ArrayList();
		while (operationIt.hasNext()) {
			usedNames.add(((Operation) operationIt.next()).getName());
		}
		
		operationName = getUniqueName("NewOperation", usedNames);
		
		return operationName;
	}
	
	public static String getRequestOrResponse(MessageReference messageRef) {
		if (messageRef instanceof Input)
		{
			return "Request";
		}
		else if (messageRef instanceof Output)
		{
			return "Response";
		}
		else if (messageRef instanceof Fault)
		{
			return "_Fault";
		}
		
		return "";
	}
	
	public static String getFaultName(Operation operation) {
		String faultName = "fault";
		List nameList = new ArrayList();
		Iterator faultIt = operation.getEFaults().iterator();
		while (faultIt.hasNext()) {
			nameList.add(((Fault) faultIt.next()).getName());
		}
		
		faultName = getUniqueName(faultName, nameList);
		
		return faultName;
	}
	
	public static String getXSDElementName(String baseName, Object parent) {
		String elementName = "";
		
		if (parent instanceof XSDSchema) {
			elementName = getUniqueName(baseName, getUsedElementNames((XSDSchema) parent));
		}
		else if (parent instanceof XSDModelGroup) {
			List existingNames = new ArrayList();
			XSDModelGroup modelGroup = (XSDModelGroup) parent;
			Iterator modelGroupIt = modelGroup.getContents().iterator();
			while (modelGroupIt.hasNext()) {
				Object item = modelGroupIt.next();
				if (item instanceof XSDParticle) {
					XSDParticle existingParticle = (XSDParticle) item;
					if (existingParticle.getContent() instanceof XSDElementDeclaration) {
						existingNames.add(((XSDElementDeclaration) existingParticle.getContent()).getName());
					}
				}
			}
			
			elementName = getUniqueName(baseName, existingNames);			
		}
		
		return elementName;
	}
	
	public static String getXSDComplexTypeName(String baseName, XSDSchema schema) {
		String typeName = "";
		List existingNames = new ArrayList();
		
		Iterator it = schema.getTypeDefinitions().iterator();
		while (it.hasNext()) {
			Object item = it.next();
			if (item instanceof XSDComplexTypeDefinition) {
				existingNames.add(((XSDComplexTypeDefinition) item).getName());
			}
		}
		
		typeName = getUniqueName(baseName, existingNames);
		return typeName;
	}
	
	private static List getUsedElementNames(XSDSchema xsdSchema) {
		List usedNames = new ArrayList();
		Iterator schemaIt = xsdSchema.getContents().iterator();
		while (schemaIt.hasNext()) {
			Object item = schemaIt.next();
			if (item instanceof XSDElementDeclaration) {
				usedNames.add(((XSDElementDeclaration) item).getName());
			}
		}
		
		return usedNames;
	}
	
	private static String getUniqueName(String baseName, List existingNames) {
		int count = 1;
		
		if (!existingNames.contains(baseName)) {
			return baseName;
		}
		
		Iterator namesIt = existingNames.iterator();
		boolean foundMatch = true;
		while (foundMatch) {
			foundMatch = false;
			while (namesIt.hasNext()) {
				String name = (String) namesIt.next();
				if (name.equals(baseName + String.valueOf(count))) {
					count++;
					foundMatch = true;
					break;
				}
			}
		}
		
		return baseName + String.valueOf(count);
	}
}

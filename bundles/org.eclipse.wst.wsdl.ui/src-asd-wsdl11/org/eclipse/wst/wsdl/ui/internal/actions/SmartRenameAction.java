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
package org.eclipse.wst.wsdl.ui.internal.actions;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.xsd.XSDElementDeclaration;
import org.w3c.dom.Node;


/*
 * This class renames 'related' WSDLElements.  The scenario is as follows:
 * The user renames a WSDLElement in the editor, say a Message.  This class
 * will find the 'related' elements to this Message and rename them as well.
 * It will only rename a 'related' element if the element name (or part of it)
 * is generated.
 * 
 * See NameUtil.java
 */
public class SmartRenameAction extends BaseNodeAction implements Runnable {
	protected Object element;
	protected String newName;
	private List messageReferences; // This variable should be accessed by method getAllMessageReferences()
	protected Node node;   
	
	public SmartRenameAction(Object element, String newName) {
		setText("Smart Rename Action"); // Do not translate //$NON-NLS-1$
		this.element = element;
		this.newName = newName;
	    this.node = WSDLEditorUtil.getInstance().getNodeForObject(element);
	}
	
	public Node getNode() {
	   return node;
	}

	public String getUndoDescription() {
	  return Messages.getString("_UI_ACTION_RENAME");  //$NON-NLS-1$
	}

	public void run() {
		RenameAction renamer;
		String oldName;
		
		beginRecording();
		if (element instanceof Operation) {
			Operation operation = (Operation) element;
			oldName = operation.getName();

			if (oldName.equals(newName)) {
				return;
			}
			
			// Rename Operation 
			renamer = new RenameAction(operation, newName);
			renamer.run();
			
			// Rename Input
			Input input = operation.getEInput();
			/*
			if (input != null && isInputOutputNameGenerated(oldName, input.getName())) {
				renamer = new RenameAction(input, newName);
				renamer.run();
			}
			*/
			
			// Rename Output
			Output output = operation.getEOutput();
			/*
			if (output != null && isInputOutputNameGenerated(oldName, output.getName())) {
				renamer = new RenameAction(output, newName);
				renamer.run();
			}
			*/
			
			// Rename Messages and Parts
			Message msg;

			// Input
			if (input != null && input.getEMessage() != null) {
				msg = input.getEMessage();
				String oldMessageName = ""; //$NON-NLS-1$

				if (msg != null) {
					oldMessageName = msg.getQName().getLocalPart();
					
					if (isMessageNameGenerated(oldMessageName, oldName, "Request")) { //$NON-NLS-1$
						renameMessageHelper(msg, computeNewMessageName(msg, oldName, newName));
						
						if (msg.getEParts() != null)
							renamePartsHelper(msg.getEParts(), oldMessageName, msg.getQName().getLocalPart(), true);	
					}
				}
			}
			
			// Output
			if (output != null && output.getMessage() != null) {
				msg = output.getEMessage();
				String oldMessageName = ""; //$NON-NLS-1$
				
				if (msg != null) {
					oldMessageName = msg.getQName().getLocalPart();
					
					if (isMessageNameGenerated(oldMessageName, oldName, "Response")) { //$NON-NLS-1$
						renameMessageHelper(msg, computeNewMessageName(msg, oldName, newName));
						
						if (msg.getEParts() != null)
							renamePartsHelper(msg.getEParts(), oldMessageName, msg.getQName().getLocalPart(), true);
					}
					
				}
			}
			
			// Faults
			List faults = operation.getEFaults();
			if (faults != null) {
				Iterator it = faults.iterator();
				while (it.hasNext()) {
					Fault fault = (Fault) it.next();
					msg = fault.getEMessage();
					String oldMessageName = ""; //$NON-NLS-1$

					if (msg != null) {
						oldMessageName = msg.getQName().getLocalPart();
						
						if (isMessageNameGenerated(oldMessageName, oldName, fault.getName())) {
							renameMessageHelper(msg, computeNewMessageName(msg, oldName, newName));
							
							if (msg.getEParts() != null)
								renamePartsHelper(msg.getEParts(), oldMessageName, msg.getQName().getLocalPart(), true);
						}
					}
				}
			}
		}
		else if (element instanceof Input) {
			Input input = (Input) element;
			oldName = input.getName();
			if (oldName == null) {
				oldName = ""; //$NON-NLS-1$
			}
			
			if (oldName.equals(newName)) {
				return;
			}
			
			input.setName(newName);

//			 Rename Messages and Parts
			Message msg;
			
			// Input
			if (input != null && input.getEMessage() != null) {
				msg = input.getEMessage();
				String oldMessageName = ""; //$NON-NLS-1$
				
				if (msg != null) {
					oldMessageName = msg.getQName().getLocalPart();
					
					if (isMessageNameGenerated(oldMessageName, oldName, "Request")) { //$NON-NLS-1$
						renameMessageHelper(msg, computeNewMessageName(msg, oldName, newName));
						
						if (msg.getEParts() != null)
							renamePartsHelper(msg.getEParts(), oldMessageName, msg.getQName().getLocalPart(), true);	
					}
				}
			}
		}
		else if (element instanceof Output) {
			Output output = (Output) element;
			oldName = output.getName();
			if (oldName == null) {
				oldName = ""; //$NON-NLS-1$
			}
			
			if (oldName.equals(newName)) {
				return;
			}
			
			output.setName(newName);

//			 Rename Messages and Parts
			Message msg;
			
			// Output
			if (output != null && output.getMessage() != null) {
				msg = output.getEMessage();
				String oldMessageName = ""; //$NON-NLS-1$
				if (msg != null) {
					oldMessageName = msg.getQName().getLocalPart();
					if (isMessageNameGenerated(oldMessageName, oldName, "Response")) { //$NON-NLS-1$
						renameMessageHelper(msg, computeNewMessageName(msg, oldName, newName));
						
						if (msg.getEParts() != null)
							renamePartsHelper(msg.getEParts(), oldMessageName, msg.getQName().getLocalPart(), true);
					}
				}
			}
		}
		else if (element instanceof Fault) {
			Fault fault = (Fault) element;
			Message msg = fault.getEMessage();
			String oldMessageName = ""; //$NON-NLS-1$
			oldName = fault.getName();
			
			if (oldName.equals(newName)) {
				return;
			}
			
			// Rename the Fault
			renamer = new RenameAction(fault, newName);
			renamer.run();

			// Rename the Message and Parts
			if (msg != null) {
				oldMessageName = msg.getQName().getLocalPart();
				
				Operation op = (Operation) fault.eContainer();
				if (isMessageNameGenerated(oldMessageName, op.getName(), oldName)) {
					renameMessageHelper(msg, computeNewMessageName(msg, oldName, newName));
					
					if (msg.getEParts() != null)
						renameFaultParts(msg.getEParts(), op.getName(), oldName, newName);
				}
			}
		}
		else if (element instanceof Message) {
			Message msg = (Message) element;
			oldName = msg.getQName().getLocalPart();
			String oldMessageName = msg.getQName().getLocalPart();
			
			if (oldName.equals(newName)) {
				return;
			}

			renameMessageHelper(msg, computeNewMessageName(msg, oldName, newName));
					
			if (msg.getEParts() != null)
				renamePartsHelper(msg.getEParts(), oldMessageName, msg.getQName().getLocalPart(), true);
		}
		else if (element instanceof Part) {
			Part part = (Part) element;
			String oldPartName = part.getName();
			
			if (oldPartName.equals(newName)) {
				return;
			}
			
			renamer = new RenameAction(element, newName);
			renamer.run();
			
//			Rename Elements
			renameXSDElement(part, oldPartName, newName);
		}
		else if (element instanceof Port) {
			Port port = (Port) element;
			String oldPortName = port.getName();
			
			renamer = new RenameAction(element, newName);
			renamer.run();

//			Rename Binding
			Binding binding = port.getEBinding();
			if (binding != null && binding.getQName().getLocalPart().equals(oldPortName)) {
				renamer = new RenameAction(binding, newName);	
				renamer.run();
			}
		}
		endRecording();
	}
	
	// boolean isInputOutput should be set to true if the part is an Input or Output.
	// Set false if the part is a Fault.
	private void renamePartsHelper(List partsList, String oldSubString, String newSubString, boolean isInputOutput) {
		RenameAction renamer;
		
		if (partsList != null) {
			Iterator pIt = partsList.iterator();
			while (pIt.hasNext()) {
				Part part = (Part) pIt.next();
				String oldPartName = part.getName();
				
				if (isPartNameGenerated(oldPartName, oldSubString)) {
					String newPartName;
					if (isInputOutput) {
						newPartName = computeNewPartName(part, oldSubString, newSubString);
					}
					else {
						newPartName = computeNewFaultPartName(part, oldSubString, newSubString);
					}
					
					renamer = new RenameAction(part, newPartName);
					renamer.run();
					
					// Rename Elements
					renameXSDElement(part, oldPartName, newPartName);
				}
			}
		}
	}
	
	// This method is used to update the Part name when the Fault name is changed.  Only
	// change the Part name if the part name is a generated name.
	// Compare to renamePartsHelper().  The renamePartsHelper() method is called when a Fault
	// has NOT been renamed, rather, it is triggered by some other naming (ex. Operation
	// renaming).  It will updat Input/Output, AND Fault Parts.
	private void renameFaultParts(List partsList, String baseName, String oldSubString, String newSubString) {
		RenameAction renamer;
		
		if (partsList != null) {
			Iterator pIt = partsList.iterator();
			while (pIt.hasNext()) {
				Part part = (Part) pIt.next();
				String oldPartName = part.getName();
				
				if (isPartNameGenerated(oldPartName, baseName, oldSubString)) {
					String newPartName;
					newPartName = computeNewFaultPartName(part, oldSubString, newSubString);
					renamer = new RenameAction(part, newPartName);
					renamer.run();
					
					// Rename Elements
					renameXSDElement(part, oldPartName, newPartName);
				}
			}
		}
	}
	
	private String computeNewMessageName(Message message, String oldSubString, String newSubString) {
		String string = message.getQName().getLocalPart();
		return replaceSectionWithSubString(string, oldSubString, newSubString, 0);		
	}
	
//	private String computeNewFaultMessageName(Message message, String oldSubString, String newSubString) {
//		String string = message.getQName().getLocalPart();
//		return replaceSectionWithSubString(string, oldSubString, newSubString, 1);		
//	}
	
	// Method for Input, Output, and Fault Parts
	// See computeNewFaultPartName() for comparison
	private String computeNewPartName(Part part, String oldSubString, String newSubString) {
		String string = part.getName();
		return replaceSectionWithSubString(string, oldSubString, newSubString, 0);		
	}
	
	// Method for Fault Parts
	// This method behaves in the exact same way as computeNewPartName() except it starts searching for a match
	// after the first character.  This method is intended for users who wish to rename a generated (Fault) part
	// where the Fault name has changed.  We start our search after the first character because the generated name
	// of the part is built by appending the Fault name after the Operation name.
	private String computeNewFaultPartName(Part part, String oldSubString, String newSubString) {
		String string = part.getName();
		return replaceSectionWithSubString(string, oldSubString, newSubString, 1);		
	}
	
	private String replaceSectionWithSubString(String fullString, String oldSubString, String newSubString, int startSearchIndex) {
		StringBuffer fullSB = new StringBuffer(fullString);
		int index = fullSB.indexOf(oldSubString, startSearchIndex);
		if (index >= 0) {
			return fullSB.replace(index, index + oldSubString.length(), newSubString).toString();
		}
		
		return ""; //$NON-NLS-1$
	}

	////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean commonNameGeneratorCheck(String targetName, String baseName, String appendName) {
		// First criteria is targetName must start with the baseName
		if (!targetName.startsWith(baseName))
			return false;
			
		if (appendName.trim().length() > 0) {
			// Second criteria.  The appended name must be in the targetName and begin right
			// after the baseName location
			if (!targetName.startsWith(appendName, baseName.length()))
				return false;
		}
			
		// Third criteria (if necessary).  If baseName + appendName is shorter than targetName,
		// then there must be 'extra' characters at the end of targetName.  These characters must
		// make up an integer.  If not, it is not a generated string.
		int subLength = baseName.length() + appendName.length();
		if (targetName.length() > subLength) {
			// We have 'extra' characters
			String extras = targetName.substring(subLength);
			
			if(!isDigit(extras))
				return false;
		}
		
		return true;
	}
	
	public static boolean isOperationNameGenerated(String opName, String name) {
		return opName.equals(name);
	}
	
	public static boolean isMessageNameGenerated(String msgName, String baseName, String appendName) {
		return commonNameGeneratorCheck(msgName, baseName, appendName);
	}
	
	public static boolean isInputOutputNameGenerated(String inOutName, String name) {
		return inOutName.equals(name);
	}
	
	public static boolean isFaultNameGenerated(String faultName, String name) {
		return faultName.equals(name);
	}
	
	public static boolean isPartNameGenerated(String partName, String baseName) {
		return commonNameGeneratorCheck(partName, baseName, ""); //$NON-NLS-1$
	}
	
	private boolean isPartNameGenerated(String partName, String baseName, String appendName) {
		return commonNameGeneratorCheck(partName, baseName, appendName);
	}
	
	private static boolean isDigit(String string) {
		boolean rValue = true;
		char[] chars = string.toCharArray();
	
		for (int index = 0; index < chars.length; index++) {
			if (!Character.isDigit(chars[index])) {
				rValue = false;
				break;
			}
		}
		
		return rValue;
	}
	
	/*
	 * The following classes aid in renaming a message
	 */
	private void renameMessageHelper(Message msg, String newName) {
		List messageRefs = getReferencingMessageReferences(msg, getAllMessageReferences());
		
		RenameAction renamer = new RenameAction(msg, newName);
		renamer.run();
		
		Iterator iterator = messageRefs.iterator();
		while (iterator.hasNext()) {
			MessageReference messageReference = (MessageReference) iterator.next();
			messageReference.setEMessage(msg);
		}
	}
	
	private List getReferencingMessageReferences(Message msg, List messageRefs) {
		Vector referencesVector = new Vector();
		QName messageQName = msg.getQName();
		Iterator iterator = messageRefs.iterator();
		
		while (iterator.hasNext()) {
			MessageReference messageReference = (MessageReference) iterator.next();
			if (messageReference.getEMessage() != null && messageQName.equals(messageReference.getEMessage().getQName())) {
				referencesVector.addElement(messageReference);
			}
		}
		
		return referencesVector;
	}
	
	private List getAllMessageReferences() {
		if (messageReferences == null) {
			messageReferences = new Vector();
			Definition definition = ((WSDLElement) element).getEnclosingDefinition();
			List portTypes = definition.getEPortTypes();
		
			if (portTypes != null) {
				Iterator portTypeIterator = portTypes.iterator();
				while (portTypeIterator.hasNext()) {
					List operationsList = ((PortType) portTypeIterator.next()).getEOperations();
					
					if (operationsList != null) {
						Iterator operationsIterator = operationsList.iterator();
						while (operationsIterator.hasNext()) {
							messageReferences.addAll(getAllMessageReferences((Operation) operationsIterator.next()));
						}
					}
				}
			}
		}
		
		return messageReferences;
	}
	
	private Vector getAllMessageReferences(Operation operation) {
		Vector iofs = new Vector();
		Iterator it = operation.getEFaults().iterator();
		while (it.hasNext()) {
			iofs.addElement(it.next());
		}
		
		if (iofs == null)
			iofs = new Vector();
		
		if (operation.getEInput() != null)
			iofs.addElement(operation.getEInput());
		
		if (operation.getEOutput() != null)
			iofs.addElement(operation.getEOutput());
		
		return iofs;
	}
	
	private void renameXSDElement(Part part, String oldXSDName, String newXSDName) {
		XSDElementDeclaration elementDeclaration = part.getElementDeclaration();
		if (elementDeclaration != null && oldXSDName.equals(elementDeclaration.getName())) {
			renameElementDeclarationHelper(elementDeclaration, oldXSDName, newXSDName);
			
			// Here we rename the element reference.
			//
			QName qname = new QName(part.getElementName().getNamespaceURI(), newXSDName);
			part.setElementName(qname);			
		}
	}
	
	private void renameElementDeclarationHelper(XSDElementDeclaration elementDeclaration, String oldXSDName, String newXSDName) {
		if (elementDeclaration != null && elementDeclaration.getName().equals(oldXSDName)) {
			elementDeclaration.setName(newXSDName);
		}
	}
	
}

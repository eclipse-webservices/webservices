/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.actions;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DeleteAction extends BaseNodeAction {
	 protected List list;
	 protected String deleteString = WSDLEditorPlugin.getWSDLString("_UI_ACTION_DELETE") + " "; //$NON-NLS-1$
//	 private Object object;
//	 private Node node;
	
	public DeleteAction(Object object, Node node) {
		setText(WSDLEditorPlugin.getWSDLString("_UI_ACTION_DELETE"));  //$NON-NLS-1$
	    list = new Vector();
	    list.add(object);
	}
	
	public DeleteAction(List deleteList) {
		setText(WSDLEditorPlugin.getWSDLString("_UI_ACTION_DELETE"));  //$NON-NLS-1$
	    list = deleteList;
	}

	private Vector getReferencedMessages(Operation operation) {
		Vector messages = new Vector();
		messages.addAll(getMessages(operation.getEFaults()));
		
		if (operation.getEInput() != null && operation.getEInput().getEMessage() != null)
			messages.addElement(operation.getEInput().getEMessage());
		if (operation.getEOutput() != null && operation.getEOutput().getEMessage() != null)
			messages.addElement(operation.getEOutput().getEMessage());
		
		return messages;
	}
	
	public void run() {
		// We want to delete Operations first.  So we need to sort the list
		List sortedList = new ArrayList(list.size());
		Iterator listIterator = list.iterator();
		
		while (listIterator.hasNext()) {
			Object unsortedObject = listIterator.next();
			if (unsortedObject instanceof Operation) {
				sortedList.add(0, unsortedObject);
			}
			else {
				sortedList.add(unsortedObject);
			}
		}
		
		Node recordingNode = null;
		if (sortedList.size() > 1) {
			recordingNode = getElement(sortedList.get(0));
			beginRecording(recordingNode, WSDLEditorPlugin.getWSDLString("_UI_ACTION_MULTIPLE_DELETE"));
		}
		
		Iterator iterator = sortedList.iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			
			if (object instanceof Operation) {
				deleteOperation((Operation) object);
			}
			else if (object instanceof Part) {
				deletePart((Part) object);
			}
			else {
				DeleteNodeAction deleteNodeAction = new DeleteNodeAction(getElement(object));
				deleteNodeAction.run();
			}
		}
		
		if (recordingNode != null) {
			endRecording(recordingNode);
		}
	}
	
	private Vector getMessages(List faults) {
		Vector v = new Vector();
		Iterator it = faults.iterator();
		
		while (it.hasNext()) {
			Fault fault = (Fault) it.next();
			if (fault.getEMessage() != null) {
				v.addElement(fault.getEMessage());
			}
		}
		
		return v;
	}
	
	private Vector getParts(Vector messages) {
		Vector parts = new Vector();
		Iterator it = messages.iterator();
		
		while (it.hasNext()) {
			Message message = (Message) it.next();
			
			if (message.getEParts() != null) {
				parts.addAll(message.getEParts());
			}
		}
		
		return parts;
	}
	
	public Node getNode()
	{
	  return list.size() > 0 ? (Node) getElement(list.get(0)) : null;
	} 

	public String getUndoDescription()
	{
	  return WSDLEditorPlugin.getWSDLString("_UI_ACTION_DELETE"); //$NON-NLS-1$
	}
	
	private void deleteOperation(Operation operation) {
		DeleteOperationDialog dialog = new DeleteOperationDialog(WSDLEditorPlugin.getShell(), operation.getName());
	    int returnCode = dialog.createAndOpen();
	    
	    if (returnCode == IDialogConstants.OK_ID) {
	    	if (dialog.deleteMessagesAndParts()) {
	    		// Get all associated messages
	    		Vector messages = getReferencedMessages(operation);
	    		
	    		// Determine which Messages are referenced from 'outside' this operation
	    		Vector referencedMessages = new Vector();
	    		if (operation.getEnclosingDefinition().getEPortTypes() != null) {
	    			Iterator portTypeIterator = operation.getEnclosingDefinition().getEPortTypes().iterator();
	    			Vector operations = new Vector();
	    			
	    			while (portTypeIterator.hasNext()) {
	    				PortType portType = (PortType) portTypeIterator.next();
	    				
	    				if (portType.getEOperations() != null) {
	    					Iterator operationIterator = portType.getEOperations().iterator();
	    					while (operationIterator.hasNext()) {
	    						Operation nextOperation = (Operation) operationIterator.next();
	    						if (!nextOperation.equals(operation))
	    							operations.add(nextOperation);
	    					}
	    				}
	    			}
	    			
	    			for (int index = 0; index < operations.size(); index++) {
	    				referencedMessages.addAll(getReferencedMessages((Operation) operations.elementAt(index)));
	    			}
	    		
	    			// Filter which Messages need to be deleted
	    			for (int index = 0; index < messages.size(); index++) {
	    				int foundIndex = referencedMessages.indexOf(messages.elementAt(index));
	    				
	    				if (foundIndex != -1) {
	    					// Message is referenced elsewhere.  Do not delete
	    					messages.remove(index);
	    				}
	    			}
	    		}
	    		
	    		// Get all associated parts
	    		Vector parts = getParts(messages);
	    		
	    		// Remove our list of Messages and Parts
	    		PortType portType = (PortType) operation.eContainer();
	    		Node recordingNode = portType.getEnclosingDefinition().getElement();
	    		beginRecording(recordingNode, deleteString + WSDLEditorPlugin.getWSDLString("_UI_LABEL_OPERATION"));
	    		
	    		for (int index = 0; index < messages.size(); index++) {
	    			Message message = (Message) messages.elementAt(index);
	    			Definition definition = (Definition) message.eContainer();
	    			definition.getEMessages().remove(message);
	    		}

	    		for (int index = 0; index < parts.size(); index++) {
	    			Part part = (Part) parts.elementAt(index);
	    			Message message = (Message) part.eContainer();
	    			message.getEParts().remove(part);
	    		}
	    		
	    		// Delete the Operation
	    		portType.getEOperations().remove(operation);

	    		endRecording(recordingNode);
	    	}
	    	else {
	    		// Only delete the Operation
	    		PortType portType = (PortType) operation.eContainer();
	    		Node recordingNode = portType.getElement();
	    		
	    		beginRecording(recordingNode, deleteString + WSDLEditorPlugin.getWSDLString("_UI_LABEL_OPERATION")); // Translate this!!!
	    		portType.getEOperations().remove(operation);
	    		endRecording(recordingNode);
	    	}
	    }
	}
	
	private void deletePart(Part part) {
		Message message = (Message) part.eContainer();
		if (message != null) {
			beginRecording(message.getElement(), deleteString + WSDLEditorPlugin.getWSDLString("_UI_LABEL_PART"));
			message.getEParts().remove(part);
			endRecording(message.getElement());
		}
	}
	
	private Element getElement(Object object) {
		Element element = null;
		if (object instanceof WSDLElement) {
			element = ((WSDLElement) object).getElement();
		}
		else if (object instanceof XSDSchema) {
			element = ((XSDSchema) object).getElement();
		}
		
		return element;
	}
	
	/*
	 * The following method should be used to begin recording changes.
	 * This should eventually replace BaseNodeAction.beginRecording()
	 * when all deletes are model driven.
	 */
	  private void beginRecording(Node node, String undoDescription)
	  {    
	    if (node instanceof IDOMNode)
	    {
	      ((IDOMNode)node).getModel().beginRecording(this, undoDescription);  
	    }
	  }

	/*
	 * The following method should be used to begin recording changes.
	 * This should eventually replace BaseNodeAction.beginRecording()
	 * when all deletes are model driven.
	 */
	  public void endRecording(Node node)
	  {
	    if (node instanceof IDOMNode)
	    {
	      ((IDOMNode)node).getModel().endRecording(this);  
	    }
	  }
	
	private class DeleteOperationDialog extends Dialog implements SelectionListener{
		private Button checkButton = null;
		private boolean deleteMessagesAndParts = true;
		private String operationName;
		 
		public DeleteOperationDialog(Shell shell, String name) {
			super(shell);
			operationName = name;
		}
		
		protected Control createDialogArea(Composite parent) 
		  {
		    Composite dialogArea = (Composite)super.createDialogArea(parent);

		    Composite composite = new Composite(dialogArea, SWT.NONE);
		    GridLayout layout = new GridLayout();
		    layout.numColumns = 2;
		    layout.marginWidth = 0;
		    layout.marginHeight = 0;
		    layout.verticalSpacing = 0;

		    composite.setLayout(layout);

		    GridData gdFill= new GridData();
		    gdFill.horizontalAlignment= GridData.FILL;
		    gdFill.grabExcessHorizontalSpace= true;
		    gdFill.verticalAlignment= GridData.FILL;
		    gdFill.grabExcessVerticalSpace= true;
		    composite.setLayoutData(gdFill);

		    Label label = new Label(composite, SWT.NONE);
		    label.setText(operationName);
//		    label.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_OPERATION") + " " + operationName);
		    GridData labelData = new GridData();
		    labelData.horizontalSpan = 2;
		    
		    label.setLayoutData(labelData);
		    
		    Label emptyLabel = new Label(composite, SWT.NONE);
		    GridData emptyData = new GridData();
		    emptyData.horizontalSpan = 2;
		    emptyLabel.setLayoutData(emptyData);

		    checkButton = new Button(composite, SWT.CHECK);
		    checkButton.setSelection(deleteMessagesAndParts);
		    checkButton.addSelectionListener(this);
		    Label nameLabel = new Label(composite, SWT.NONE);
		    nameLabel.setText(WSDLEditorPlugin.getWSDLString("_UI_DELETE_ASSOCIATED_MSG_AND_PARTS"));
		    
		    return dialogArea;
		  }
		
		public boolean deleteMessagesAndParts() {
			return deleteMessagesAndParts;
		}
		
		public void widgetDefaultSelected(SelectionEvent e)  {}

		public void widgetSelected(SelectionEvent e)  {
			if (e.widget == checkButton) {
				if (checkButton.getSelection()) {
					deleteMessagesAndParts = true;
				}
				else {
					deleteMessagesAndParts = false;
				}
			}
		}

		public int createAndOpen() {
			create();
		    getShell().setText(WSDLEditorPlugin.getWSDLString("_UI_DELETE_OPERATION_TITLE"));
		    setBlockOnOpen(true);
		    return open();
		}
		
	}
}

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

import java.util.Iterator;
import java.util.Map;


import org.eclipse.gef.EditPartViewer;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOperationCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.WSDLElementCommand;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.WSDLTreeNodeEditPart;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;



import org.w3c.dom.Node;

/*
 * Class which copies a WSDLElement and it's 'children' elements.
 * For example, a copy command on a Operation will result in a copy
 * of the operation and it's input/output/faults, and it's message
 * and parts.
 */
public class CopyWSDLElementAction extends WSDLDragAction {
	WSDLElement element, parent;
	Node parentNode;
	WSDLElementCommand wsdlElementCommand;
	EditPartViewer editPartViewer;
	
	/*
	 *  element = The WSDLElement being copied
	 *  parent = The Parent of the 'new' WSDLElement
	 *  parentNode = ElementImpl (for undo)
	 */	
	public CopyWSDLElementAction(EditPartViewer editPartViewer, WSDLElement element, WSDLElement parent, Node parentNode) {
		setText("Copy WSDLElement");  // Do not Translate This
		this.element = element;	
		this.parent = parent;
		this.parentNode = parentNode;
		this.editPartViewer = editPartViewer;
	}
	
	public boolean canExecute() {
		if (element instanceof Operation){
			if(parent instanceof PortType) {
				PortType pt = (PortType) parent;
				Operation op = (Operation) element;
				wsdlElementCommand  = new AddOperationCommand(pt, op, NameUtil.buildUniqueOperationName(pt, op.getName()), true);		
			}
		}
		/*
		else if (element instanceof MessageReference) {
	
		}
		else if (element instanceof Input) {

		}
		else if (element instanceof Output) {
			
		}
		else if (element instanceof Fault) {
			
		}
		else if (element instanceof Message) {
			
		}
		*/
		
		if (wsdlElementCommand != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void run() {
		if (wsdlElementCommand == null) {
			if (canExecute()) {
				runCommand();
			}
		}
		 else {
		 	runCommand();
		 }
	}
	
	private void runCommand() {
	 	this.beginRecording();
		wsdlElementCommand.run();
		selectWSDLElement(wsdlElementCommand.getWSDLElement());
		this.endRecording();
	}
	
	private void selectWSDLElement(WSDLElement element) {
		// Select the newly created element
	    // Expand all the associated elements 'below' the given element
	    Map editPartMap = editPartViewer.getEditPartRegistry();
	    WSDLTreeNodeEditPart wsdlEditPart = (WSDLTreeNodeEditPart) editPartMap.get(element);
	    if (wsdlEditPart != null) {
	    	editPartViewer.select(wsdlEditPart);
		    expandEditParts(element, true);
	    }
	}
	
	private void expandEditParts(Object element, boolean expandChildren) {
	    if (element != null) {
	    	Map editPartMap = editPartViewer.getEditPartRegistry();
	    	WSDLTreeNodeEditPart wsdlEditPart = (WSDLTreeNodeEditPart) editPartMap.get(element);
			wsdlEditPart.setExpanded(true);
			
			if (expandChildren) {
				Iterator iterator = WSDLEditorUtil.getModelGraphViewChildren(element).iterator();
				
				while (iterator.hasNext()) {
					expandEditParts(iterator.next(), expandChildren);
				}
			}
	    }
	}
	
	// Inherited classes from BaseNodeAction
	  public Node getNode() {
	  	return parentNode;
	  }
	  
	  public String getUndoDescription() {
	  	return WSDLEditorPlugin.getWSDLString("_UI_ACTION_COPY");
	  }
}
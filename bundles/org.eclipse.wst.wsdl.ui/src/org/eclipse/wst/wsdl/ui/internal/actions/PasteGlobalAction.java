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

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.w3c.dom.Node;

public class PasteGlobalAction extends Action {
	private IEditorPart editor;
	private WSDLElement selection;
	private Node selectionNode;
	
	public PasteGlobalAction(WSDLElement selection, IEditorPart editor) {
		setText(WSDLEditorPlugin.getWSDLString("_UI_ACTION_PASTE"));  // Translate This
		this.selection = selection;
		this.editor = editor;
		selectionNode = selection.getElement();
		setEnabledState();
	}
	
	public void run() {
		if (editor instanceof WSDLEditor) {
			WSDLEditor wsdlEditor = (WSDLEditor) editor;
			CopyWSDLElementAction copyAction = new CopyWSDLElementAction(wsdlEditor.getGraphViewer().getComponentViewer(), wsdlEditor.getClipboardContents(), selection, selectionNode);
			copyAction.run();
		}
	}
	
  	public void setSelection(WSDLElement newSelection) {
  		selection = newSelection;
  		setEnabledState();
  	}
  	
  	protected void setEnabledState() {
  		WSDLElement parentElement = null;
  		if ((parentElement = showPasteAction((WSDLElement) selection, editor)) != null) {
  			selection = parentElement;
  			setEnabled(true);
  		}
  		else {
  			setEnabled(false);
  		}
  	}
  	
  	 private WSDLElement showPasteAction(WSDLElement element, IEditorPart ePart) {
  	  	WSDLElement parentElement = null;
  	  	
  	  	if (ePart instanceof WSDLEditor) {
  	  		WSDLElement clipboardElement = ((WSDLEditor) ePart).getClipboardContents();
  	  		
  	  		if (element instanceof PortType) {
  	  			if (clipboardElement instanceof Operation) {
  	  				parentElement = element;
  	  			}
  	  		}
  	  		if (element instanceof Operation) {
  	  			if (clipboardElement instanceof Operation) {
  	  				parentElement = (WSDLElement) ((Operation) element).eContainer();
  	  			}
  	  		}
  	  	}
  	  	
  	  	return parentElement;
  	  }
}

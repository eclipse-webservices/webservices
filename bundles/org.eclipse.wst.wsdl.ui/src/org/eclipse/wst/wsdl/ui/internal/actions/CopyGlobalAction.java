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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;

public class CopyGlobalAction extends Action {
  	private IEditorPart editor;
  	private WSDLElement selection;
  	
  	public CopyGlobalAction(WSDLElement selection, IEditorPart editor) {
  		setText(WSDLEditorPlugin.getWSDLString("_UI_ACTION_COPY"));
  		this.selection = selection;
  		this.editor = editor;
  		setEnabledState();
  	}
  	
  	public void run() {
  		if (editor instanceof WSDLEditor) {
  			((WSDLEditor) editor).setClipboardContents(selection);
  			
  			((WSDLEditor) editor).getSelectionManager().setSelection(new StructuredSelection(selection));
  		}
  	}
  	
  	public void setSelection(WSDLElement newSelection) {
  		selection = newSelection;
  		setEnabledState();
  	}
  	
  	protected void setEnabledState() {
  		if (selection instanceof Operation) {
  			setEnabled(true);
  		}
  		else {
  	  		setEnabled(false);
  		}
  	}
}
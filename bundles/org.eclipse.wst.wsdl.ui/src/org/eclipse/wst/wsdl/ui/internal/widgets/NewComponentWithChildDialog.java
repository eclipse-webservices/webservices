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
package org.eclipse.wst.wsdl.ui.internal.widgets;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;


/*
 * This class extends NewComponentDialog to allow additional widgets.  More
 * specifically, the 'create sub-components' checkbox
 */
public class NewComponentWithChildDialog extends NewComponentDialog {
	protected Button createMessageCheckBox;
	private boolean createMessageBoolean;

	public NewComponentWithChildDialog(Shell parentShell, String title, String defaultName) {
		super(parentShell, title, defaultName);
	}
	
	public NewComponentWithChildDialog(Shell parentShell, String title, String defaultName, List usedNames) {
		super(parentShell, title, defaultName, usedNames);
	}
	
	protected void createExtendedContent(Composite parent) {
		Composite child = new Composite (parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
	    layout.numColumns = 1;
	    layout.marginWidth = 0;
	    layout.marginHeight = 0;
	    child.setLayout(layout);
		
	    createMessageCheckBox = new Button(child, SWT.CHECK);
	    createMessageCheckBox.setSelection(true);
	    createMessageCheckBox.setText(WSDLEditorPlugin.getWSDLString("_UI_CREATE_MESSAGE_CHECKBOX_LABEL"));
	}

	public boolean createSubComponents() {
		return createMessageBoolean;
	}
	
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			createMessageBoolean = createMessageCheckBox.getSelection();
		}
		super.buttonPressed(buttonId);
	}
}

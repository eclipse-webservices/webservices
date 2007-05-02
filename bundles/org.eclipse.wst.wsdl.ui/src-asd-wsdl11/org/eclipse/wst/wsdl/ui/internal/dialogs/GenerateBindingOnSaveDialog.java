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
package org.eclipse.wst.wsdl.ui.internal.dialogs;

import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;

public class GenerateBindingOnSaveDialog extends MessageDialog implements SelectionListener {
	private static String dialogTitle = Messages._UI_REGEN_BINDINDS_ON_SAVE; //$NON-NLS-1$
	private static String dialogMessage = Messages._UI_REGEN_BINDINGS_ON_SAVE_QUESTIONMARK; //$NON-NLS-1$
	private static String[] buttons = new String[]{Messages._UI_YES_LABEL, Messages._UI_NO_LABEL, Messages._UI_CANCEL_LABEL}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	private Button rememberDecisionBox;
	private boolean rememberDecision = false;
	
	public GenerateBindingOnSaveDialog(Shell shell) {
		super(shell, dialogTitle, null, dialogMessage, MessageDialog.QUESTION, buttons, 0);
	}
	
	
	protected Control createCustomArea(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);
		comp.setData(new GridData());
		
		// Spacer
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		rememberDecisionBox = new Button(comp, SWT.CHECK);
		rememberDecisionBox.setText(Messages._UI_REMEMBER_MY_DECISION_LABEL);
		rememberDecisionBox.addSelectionListener(this);

		return parent;
	}
	
	private void storePromptPreference(int swtValue) {
		// Store the new preference for displaying this dialog.
		if (rememberDecision) {
			WSDLEditorPlugin.getInstance().getPluginPreferences().setValue(Messages._UI_PREF_PAGE_PROMPT_REGEN_BINDING_ON_SAVE, false); //$NON-NLS-1$
		
			String generateID = Messages._UI_PREF_PAGE_AUTO_REGENERATE_BINDING; //$NON-NLS-1$
			if (swtValue == SWT.YES) {
				WSDLEditorPlugin.getInstance().getPluginPreferences().setValue(generateID, true);
			}
			else if (swtValue == SWT.NO) {
				WSDLEditorPlugin.getInstance().getPluginPreferences().setValue(generateID, false);
			}
		}
	}
	
	public int open() {
		int rValue = super.open();
		
		if (rValue ==0) {
			storePromptPreference(SWT.YES);
			return SWT.YES;
		}
		else if (rValue == 1) {
			storePromptPreference(SWT.NO);
			return SWT.NO;
		}
		else if (rValue == 2) {
			storePromptPreference(SWT.CANCEL);
			return SWT.CANCEL;
		}
		
		return rValue;
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.widget == rememberDecisionBox) {
			rememberDecision = rememberDecisionBox.getSelection();
		}
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {}
}

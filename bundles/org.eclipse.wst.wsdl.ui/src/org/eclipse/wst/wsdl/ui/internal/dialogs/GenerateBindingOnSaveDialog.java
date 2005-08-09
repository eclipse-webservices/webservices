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
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;

public class GenerateBindingOnSaveDialog extends MessageDialog implements SelectionListener {
	private static String dialogTitle = "Regenerate Bindings On Save";		// TODO: Translations needed....
	private static String dialogMessage = "Regenerate Bindings On Save?";
	private static String[] buttons = new String[]{"Yes", "No", "Cancel"};
	
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
		rememberDecisionBox.setText("Remember my decision");	// TODO: Externalize...
		rememberDecisionBox.addSelectionListener(this);

		return parent;
	}
	
	private void storePromptPreference(int swtValue) {
		// Store the new preference for displaying this dialog.
		if (rememberDecision) {
			WSDLEditorPlugin.getInstance().getPluginPreferences().setValue("Prompt Regenerate Binding on save", false);
		
			String generateID = WSDLEditorPlugin.getWSDLString("_UI_PREF_PAGE_AUTO_REGENERATE_BINDING");
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
/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071107 196997   ericdp@ca.ibm.com - Eric Peters
 * 20071108 196997   ericdp@ca.ibm.com - Eric Peters
 * 20071120   209858 ericdp@ca.ibm.com - Eric Peters, Enhancing service policy framework and UI
 *******************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ControlEnableState;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.wst.ws.internal.service.policy.ui.ServicePoliciesComposite;
import org.eclipse.wst.ws.internal.service.policy.ui.WstSPUIPluginMessages;
import org.eclipse.wst.ws.internal.service.policy.ui.preferences.ServicePoliciesPreferencePage;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;
import org.eclipse.wst.ws.service.policy.ui.ServicePolicyActivatorUI;

public class ServicePoliciesPropertyPage extends PropertyPage implements
		SelectionListener {

	private ServicePoliciesComposite propertyPage;
	private IProject project;
	public static final String PAGE_ID = "org.eclipse.wst.ws.internal.ui.wsi.properties.WSICompliancePropertyPage"; //$NON-NLS-1$
	private Button fUseProjectSettings;
	private Link fChangeWorkspaceSettings;
	private ControlEnableState fBlockEnableState;
	private Control fConfigurationBlockControl;
	private ServicePolicyPlatform platform = ServicePolicyPlatform
			.getInstance();

	public ServicePoliciesPropertyPage() {
		fBlockEnableState = null;
	}

	protected Control createContents(Composite superparent) {
		project = (IProject) getElement().getAdapter(IProject.class);
		boolean projectPrefsEnabled = platform
				.isProjectPreferencesEnabled(project);
		if (!projectPrefsEnabled) {
			//enable them so that service policies composite UI reflects 
			//currently set project preferences, otherwise shows workspace 
			//preferences and would need to redraw tree when user enables them 
			platform.setProjectPreferencesEnabled(project, true);
		}
		propertyPage = new ServicePoliciesComposite(superparent, project, this);
		platform.setProjectPreferencesEnabled(project, projectPrefsEnabled);
		fConfigurationBlockControl = propertyPage;
		enablePreferenceContent(projectPrefsEnabled);
		fUseProjectSettings.setSelection(projectPrefsEnabled);
		updateLinkVisibility();

		org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);
		return propertyPage;
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		propertyPage.widgetDefaultSelected(e);
	}

	public void dispose() {
		super.dispose();
		propertyPage.dispose();
	}

	public boolean okToLeave() {
		return propertyPage.getError() == null;

	}

	protected void performApply() {
		if (!fUseProjectSettings.getSelection()) {
			platform.setProjectPreferencesEnabled(project, false);
		}
		storeValues();
	}

	/**
	 * Do anything necessary because the Cancel button has been pressed.
	 *  @return whether it is okay to close the preference page
	 */
	public boolean performCancel() {
		platform.discardChanges(project);
		return true;
	}

	/**
	 * Does anything necessary because the default button has been pressed.
	 */
	protected void performDefaults() {
		enableProjectSpecificSettings(false);
		platform.setProjectPreferencesEnabled(project, false);
		propertyPage.performDefaults();
		IStatus error = propertyPage.getError();
		if (error == null) {
			super.setValid(true);
			super.setErrorMessage(null);
		} else {
			super.setErrorMessage(error.getMessage());
			super.setValid(false);
		}

	}

	/**
	 * Do anything necessary because the OK button has been pressed.
	 *  @return whether it is okay to close the preference page
	 */
	public boolean performOk() {
		if (!fUseProjectSettings.getSelection()) {
			platform.setProjectPreferencesEnabled(project, false);
		}
		storeValues();
		return true;
	}

	/**
	 * Stores the values of the controls back to the preference store.
	 */
	private void storeValues() {
		platform.commitChanges(project);
	}

	public void widgetSelected(SelectionEvent e) {
		IStatus error = propertyPage.getError();
		if (error == null) {
			super.setValid(true);
			super.setErrorMessage(null);
		} else {
			super.setErrorMessage(error.getMessage());
			super.setValid(false);
		}

	}

	protected Label createDescriptionLabel(Composite parent) {
		//			fParentComposite= parent;
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		SelectionListener listener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent se) {
			}

			public void widgetSelected(SelectionEvent se) {
				enableProjectSpecificSettings(fUseProjectSettings
						.getSelection());
				platform.setProjectPreferencesEnabled(project,
						fUseProjectSettings.getSelection());
			}
		};

		fUseProjectSettings = new Button(composite, SWT.CHECK);
		fUseProjectSettings.addSelectionListener(listener);
		fUseProjectSettings
				.setText(WstSPUIPluginMessages.LABEL_ENABLE_PROJECT_SETTINGS);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		gd.horizontalAlignment = GridData.FILL;
		fUseProjectSettings.setLayoutData(gd);
		fChangeWorkspaceSettings = createLink(composite,
				WstSPUIPluginMessages.LINK_CONFIGWORKSPACE_SETTINGS);
		fChangeWorkspaceSettings.setLayoutData(new GridData(SWT.END,
				SWT.CENTER, false, false));

		Label horizontalLine = new Label(composite, SWT.SEPARATOR
				| SWT.HORIZONTAL);
		horizontalLine.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, false, 2, 1));
		horizontalLine.setFont(composite.getFont());

		return super.createDescriptionLabel(parent);
	}

	private Link createLink(Composite composite, String text) {
		Link link = new Link(composite, SWT.NONE);
		link.setFont(composite.getFont());
		link.setText("<A>" + text + "</A>"); //$NON-NLS-1$//$NON-NLS-2$
		link.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				openWorkspacePreferences((Link) e.widget);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				openWorkspacePreferences((Link) e.widget);
			}
		});
		return link;
	}

	private void updateLinkVisibility() {
		if (fChangeWorkspaceSettings == null
				|| fChangeWorkspaceSettings.isDisposed()) {
			return;
		}

		fChangeWorkspaceSettings.setEnabled(!useProjectSettings());
	}

	protected boolean useProjectSettings() {
		return fUseProjectSettings.getSelection();
	}

	protected void enablePreferenceContent(boolean enable) {
		if (enable) {
			if (fBlockEnableState != null) {
				fBlockEnableState.restore();
				fBlockEnableState = null;
			}
		} else {
			if (fBlockEnableState == null) {
				fBlockEnableState = ControlEnableState
						.disable(fConfigurationBlockControl);
			}
		}
	}

	protected void enableProjectSpecificSettings(
			boolean useProjectSpecificSettings) {
		fUseProjectSettings.setSelection(useProjectSpecificSettings);
		enablePreferenceContent(useProjectSpecificSettings);
		updateLinkVisibility();
		doStatusChanged();
	}

	protected void doStatusChanged() {
		if (useProjectSettings()) {
			IStatus status;
			status = (propertyPage.getError() == null) ? new Status(IStatus.OK,
					ServicePolicyActivatorUI.PLUGIN_ID, "") : propertyPage
					.getError();
			updateStatus(status);
		} else {
			updateStatus(new Status(IStatus.OK,
					ServicePolicyActivatorUI.PLUGIN_ID, ""));
		}
	}

	private void updateStatus(IStatus status) {
		boolean valid = (status == null || !status.matches(IStatus.ERROR));
		setValid(valid);
		applyToStatusLine(this, status);
	}

	/**
	 * Applies the status to the status line of a dialog page.
	 * @param page the dialog page
	 * @param status the status to apply
	 */
	public static void applyToStatusLine(DialogPage page, IStatus status) {
		String message = status.getMessage();
		if (message != null && message.length() == 0) {
			message = null;
		}
		switch (status.getSeverity()) {
		case IStatus.OK:
			page.setMessage(message, IMessageProvider.NONE);
			page.setErrorMessage(null);
			break;
		case IStatus.WARNING:
			page.setMessage(message, IMessageProvider.WARNING);
			page.setErrorMessage(null);
			break;
		case IStatus.INFO:
			page.setMessage(message, IMessageProvider.INFORMATION);
			page.setErrorMessage(null);
			break;
		default:
			page.setMessage(null);
			page.setErrorMessage(message);
			break;
		}
	}

	protected final void openWorkspacePreferences(Object data) {
		String id = ServicePoliciesPreferencePage.PAGE_ID;
		PreferencesUtil.createPreferenceDialogOn(getShell(), id,
				new String[] { id }, data).open();
	}

}

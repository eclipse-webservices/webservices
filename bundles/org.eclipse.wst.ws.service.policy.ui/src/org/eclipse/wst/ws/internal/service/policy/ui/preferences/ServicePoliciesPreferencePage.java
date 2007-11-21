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
 * 20071025 196997   ericdp@ca.ibm.com - Eric Peters
 * 20071107 196997   ericdp@ca.ibm.com - Eric Peters
 * 20071120   209858 ericdp@ca.ibm.com - Eric Peters, Enhancing service policy framework and UI
 *******************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui.preferences;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wst.ws.internal.service.policy.ui.ServicePoliciesComposite;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;

public class ServicePoliciesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage, SelectionListener

{
	ServicePoliciesComposite preferencePage;
	Composite superParent;
	public static final String PAGE_ID = "org.eclipse.wst.ws.internal.ui.wsi.preferences.WSICompliancePreferencePage";

	/**
	 * Creates preference page controls on demand.
	 * 
	 * @param parentComposite
	 *            the parent for the preference page
	 */
	protected Control createContents(Composite superparent) {
		preferencePage = new ServicePoliciesComposite(superparent, null, this);
		org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);
		this.superParent = superparent;
		return preferencePage;

	}

	/**
	 * Does anything necessary because the default button has been pressed.
	 */
	protected void performDefaults() {
		preferencePage.performDefaults();
		IStatus error = preferencePage.getError();
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
	 * 
	 * @return whether it is okay to close the preference page
	 */
	public boolean performOk() {
		storeValues();
		return true;
	}

	/**
	 * Stores the values of the controls back to the preference store.
	 */
	private void storeValues() {
		ServicePolicyPlatform.getInstance().commitChanges();
	}

	/**
	 * Do anything necessary because the Cancel button has been pressed.
	 * 
	 * @return whether it is okay to close the preference page
	 */
	public boolean performCancel() {
		ServicePolicyPlatform.getInstance().discardChanges();
		return true;
	}

	/**
	 * Do anything necessary because the OK button has been pressed.
	 * 
	 */
	protected void performApply() {
		storeValues();
	}

	/**
	 * @see IWorkbenchPreferencePage
	 */
	public void init(IWorkbench workbench) {
	}

	/**
	 * @return whether it is okay to close the preference page
	 */
	public boolean okToLeave() {
		return preferencePage.getError() == null;

	}

	public void dispose() {
		super.dispose();
		preferencePage.dispose();
	}

	public void widgetDefaultSelected(SelectionEvent e) {

	}

	public void widgetSelected(SelectionEvent e) {
		IStatus error = preferencePage.getError();
		if (error == null) {
			super.setValid(true);
			super.setErrorMessage(null);
		} else {
			super.setErrorMessage(error.getMessage());
			super.setValid(false);
		}

	}

}

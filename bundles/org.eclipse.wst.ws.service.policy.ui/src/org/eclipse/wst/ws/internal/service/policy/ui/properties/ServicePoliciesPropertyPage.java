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
 * 20071107          ericdp@ca.ibm.com - Eric Peters
 *******************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.wst.ws.internal.service.policy.ui.ServicePoliciesComposite;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;



public class ServicePoliciesPropertyPage extends PropertyPage implements
		SelectionListener {
	ServicePoliciesComposite propertyPage;

	protected Control createContents(Composite superparent) {
		IProject p = (IProject) getElement();
		ServicePolicyPlatform.getInstance().setProjectPreferencesEnabled(p, true);
		propertyPage = new ServicePoliciesComposite(superparent, p, this);
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
		storeValues();
	}

	/**
	 * Do anything necessary because the Cancel button has been pressed.
	 *  @return whether it is okay to close the preference page
	 */
	public boolean performCancel() {
		ServicePolicyPlatform.getInstance().discardChanges();
		return true;
	}

	/**
	 * Does anything necessary because the default button has been pressed.
	 */
	protected void performDefaults() {
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
		storeValues();
		return true;
	}

	/**
	 * Stores the values of the controls back to the preference store.
	 */
	private void storeValues() {
		ServicePolicyPlatform.getInstance().commitChanges();
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
	
}


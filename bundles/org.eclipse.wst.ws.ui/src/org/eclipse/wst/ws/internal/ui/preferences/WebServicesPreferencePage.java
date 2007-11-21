/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070424 182376 pmoogk@ca.ibm.com - Peter Moogk
 * 20071120   209858 ericdp@ca.ibm.com - Eric Peters, Enhancing service policy framework and UI
 *******************************************************************************/
package org.eclipse.wst.ws.internal.ui.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.wst.ws.internal.ui.WstWSUIPluginMessages;

/**
 * The WebServicesPreferencePage
 */
public class WebServicesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private Composite parentComposite = null;
/**
 * Creates and returns the SWT control for the customized body 
 * of this preference page under the given parent composite.
 * <p>
 * This framework method must be implemented by concrete
 * subclasses.
 * </p>
 *
 * @param parent the parent composite
 * @return the new control
 */
	protected Control createContents(org.eclipse.swt.widgets.Composite parent) {
		parentComposite = parent;
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		Label message = new Label(composite, SWT.WRAP);
		message.setText(WstWSUIPluginMessages.WEBSERVICE_CATEGORY_PREF);
		Link link = new Link(composite, SWT.WRAP);
		link.setText(WstWSUIPluginMessages.WEBSERVICE_CATEGORY_PREF_LINK);
		link.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				PreferencesUtil.createPreferenceDialogOn(parentComposite
						.getShell(), e.text, null, null);
				parentComposite.layout();
			}
		});

		noDefaultAndApplyButton();
		Dialog.applyDialogFont(composite);
		return parent;
	}

	/**
	 * Do any initialization required by the desktop. By default do nothing.
	 */
public void init(IWorkbench desktop) {}
}


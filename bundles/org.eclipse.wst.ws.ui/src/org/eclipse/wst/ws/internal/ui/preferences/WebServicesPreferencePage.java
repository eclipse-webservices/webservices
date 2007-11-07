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
 * 20070424   182376 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.internal.ui.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wst.ws.internal.ui.WstWSUIPluginMessages;

/**
 * The WebServicesPreferencePage
 */
public class WebServicesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

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
protected Control createContents(org.eclipse.swt.widgets.Composite parent) 
{
  Composite composite = new Composite( parent, SWT.NONE );
  Text      message   = new Text( composite, SWT.READ_ONLY | SWT.WRAP );
  Text      linkMessage   = new Text( composite, SWT.READ_ONLY | SWT.WRAP );
  composite.setLayout( new GridLayout() ); 
  message.setText( WstWSUIPluginMessages.WEBSERVICE_CATEGORY_PREF );
  linkMessage.setText(WstWSUIPluginMessages.WEBSERVICE_CATEGORY_PREF_LINK);
  noDefaultAndApplyButton();
  Dialog.applyDialogFont( composite );    
	return parent;
}
/**
 * Do any initialization required by the desktop. By default do nothing.
 */
public void init(IWorkbench desktop) {}
}


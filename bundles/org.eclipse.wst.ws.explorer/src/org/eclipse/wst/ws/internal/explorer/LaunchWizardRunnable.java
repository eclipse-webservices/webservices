/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.command.internal.env.ui.widgets.DynamicWizard;

public class LaunchWizardRunnable extends Thread implements Runnable {
	private String id;

	private String wsdlURL;

	private boolean finish;

	public LaunchWizardRunnable(String id, String wsdlURL) {
		this.id = id;
		this.wsdlURL = (wsdlURL != null) ? wsdlURL : "";
		finish = false;
	}

	public boolean isFinish() {
		return finish;
	}

	public void run() {
		try {
			DynamicWizard wizard = new DynamicWizard();
			wizard.setInitialData(id);
			IStructuredSelection sel = new StructuredSelection(wsdlURL);
			wizard.init(null, sel);
			WizardDialog wd = new WizardDialog(new Shell(Display.getDefault(),
					SWT.APPLICATION_MODAL), wizard);
			wd.open();
		} catch (Exception e) {
		} finally {
			finish = true;
		}
	}
}

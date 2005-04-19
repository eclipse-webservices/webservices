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

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class LaunchWizardTask {
	// singleton
	private static LaunchWizardTask task_;

	// the shell for the wizard to be launched
	private Shell shell_;

	// the LaunchWizardRunnable that is currently running
	private LaunchWizardRunnable runnable_;

	private LaunchWizardTask(Shell shell) {
		shell_ = shell;
		runnable_ = null;
	}

	public static LaunchWizardTask getInstance() {
		if (task_ == null)
			task_ = new LaunchWizardTask(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell());
		return task_;
	}

	public void asyncExec(Runnable runnable) throws SWTException {
		shell_.getDisplay().asyncExec(runnable);
	}

	public boolean checkAndAsyncExec(LaunchWizardRunnable runnable) {
		try {
			if (!getIsExecuting()) {
				asyncExec(runnable);
				runnable_ = runnable;
				return true;
			} else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getIsExecuting() {
		if (runnable_ != null) {
			boolean isFinish = runnable_.isFinish();
			if (isFinish)
				runnable_ = null;
			return !isFinish;
		} else
			return false;
	}
}
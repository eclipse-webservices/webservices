/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20090302   242462 ericdp@ca.ibm.com - Eric D. Peters, Save Web services wizard settings
 *******************************************************************************/
package org.eclipse.jst.ws.internal.ui.common;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public final class ComboWithHistory extends Combo implements WidgetWithHistory {
	private IDialogSettings settings_;

	public ComboWithHistory(Composite parent, int style,
			IDialogSettings settings) {
		super(parent, style);
		settings_ = settings;
	}

	public void restoreWidgetHistory(String restoreKey) {
		if (restoreKey == null || settings_ == null)
			return;
		String[] historyValues = settings_.getArray(restoreKey);
		if (historyValues == null || historyValues.length == 0) {
			return; // ie.- no history stored
		}
		String[] existingItems = this.getItems();
		if (existingItems.length == 0) {
			for (int i = 0; i < historyValues.length; i++) {
				if (historyValues[i] != null)
					add(historyValues[i]);
			}
		} else {
			// only restore from history if widget doesn't already contain value
			for (int i = 0; i < historyValues.length; i++) {
				boolean foundInExistingItems = false;
				for (int j = 0; j < existingItems.length; j++) {

					if (existingItems[j].equals(historyValues[i])) {
						foundInExistingItems = true;
						break;
					}
				}
				if (!foundInExistingItems)
					// doesn't contain value already
					if (historyValues[i] != null)
						add(historyValues[i]);

			}
		}

	}

	protected void checkSubclass() {
		// do nothing, required to over-ride this method if extending SWT
		// widgets,
		// not extending SWT here per-se here, but just allowing for a wrapper
		// in order to
		// add history via org.eclipse.jface.dialogs.IDialogSettings
	}

	public void storeWidgetHistory(String storeKey) {
		if (storeKey == null || settings_ == null)
			return;
		String currentlySelectedValue = getText();
		String[] oldHistoryValues = settings_.getArray(storeKey);
		if (oldHistoryValues == null) {
			oldHistoryValues = new String[0];
		}
		// rip out any empty history, don't add selected value as processed
		// separately,
		// and trim length to 5
		ArrayList newHistoryValues = new ArrayList();
		for (int i = 0; i < oldHistoryValues.length && i < 5; i++) {
			if (oldHistoryValues[i] != null
					&& oldHistoryValues[i].trim().length() > 0
					&& !oldHistoryValues[i].equals(currentlySelectedValue)) {
				newHistoryValues.add(oldHistoryValues[i]);
			}
		}
		// add currently selected value to top of list if not empty string
		if (currentlySelectedValue.trim().length() > 0)
			newHistoryValues.add(0, currentlySelectedValue);
		// done processing old history values, reallocate the var with same size
		// as new values we are about to store
		oldHistoryValues = new String[newHistoryValues.size()];
		newHistoryValues.toArray(oldHistoryValues);
		settings_.put(storeKey, oldHistoryValues);
	}

}

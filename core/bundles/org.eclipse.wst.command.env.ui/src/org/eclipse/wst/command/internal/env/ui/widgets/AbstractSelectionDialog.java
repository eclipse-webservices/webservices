/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractSelectionDialog extends SimpleDialog {
	
	public AbstractSelectionDialog( Shell shell, PageInfo pageInfo)
	{
		super(shell, pageInfo);
	}

	protected void callSetters() {
		// extenders should override this method

	}
	
	public String getDisplayableSelectionString()
	{
		return "";
	}
	
	public abstract IStructuredSelection getObjectSelection();

}

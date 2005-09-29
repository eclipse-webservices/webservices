/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.provisional.ui.wsrt;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.internal.provisional.wsrt.ISelection;

public interface IObjectSelector {
	
	public void setInitialSelection(ISelection sel);
	public ISelection getSelection();
	public String getProject();
	public IStatus validateSelection(ISelection sel);
	public WidgetDataEvents addControls(Composite parent, Listener statusListener);

}

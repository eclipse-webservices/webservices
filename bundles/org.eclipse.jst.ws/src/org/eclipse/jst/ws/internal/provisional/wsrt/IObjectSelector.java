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

package org.eclipse.jst.ws.internal.provisional.wsrt;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;

public interface IObjectSelector {
	
	public void setInitialSelection(ISelection sel);
	public ISelection getSelection();
	public String getProject();
	public Status validateSelection(ISelection sel);
	public WidgetDataEvents addControls(Composite parent, Listener statusListener);

}

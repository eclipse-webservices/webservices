/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;

public class ReloadDependenciesActionDelegate extends ActionDelegate implements IEditorActionDelegate
{
	private WSDLEditor wsdlEditor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor)
	{
		wsdlEditor = (targetEditor instanceof WSDLEditor) ? (WSDLEditor)targetEditor : null;
	}

	public void run(IAction action)
	{
		if (wsdlEditor != null)
		{
			try
			{
				wsdlEditor.reloadDependencies();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}

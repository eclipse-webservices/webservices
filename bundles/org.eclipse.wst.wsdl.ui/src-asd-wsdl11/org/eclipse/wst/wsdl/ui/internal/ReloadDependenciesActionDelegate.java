/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;

public class ReloadDependenciesActionDelegate extends ActionDelegate implements IEditorActionDelegate
{
	private InternalWSDLMultiPageEditor wsdlEditor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor)
	{
		wsdlEditor = (targetEditor instanceof InternalWSDLMultiPageEditor) ? (InternalWSDLMultiPageEditor)targetEditor : null;
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

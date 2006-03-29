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
package org.eclipse.wst.wsdl.ui.internal.refactor.actions;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.xsd.ui.internal.refactor.actions.SelectionDispatchAction;


public class WSDLSelectionDispatchAction extends SelectionDispatchAction
{
	public static final String RENAME_ELEMENT = "org.eclipse.wst.wsdl.ui.refactor.rename.element"; //$NON-NLS-1$

	public WSDLSelectionDispatchAction(ISelection selection)
	{
		super(selection);
	}
	
	protected Definition getDefinition(){
		Object model = getModel();
		if(model instanceof Definition)
		{
			return (Definition) model;
		}
	
		return null;
	}

	

}

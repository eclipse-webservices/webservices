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

package org.eclipse.jst.ws.internal.wsrt;

import org.eclipse.jst.ws.internal.provisional.wsrt.ISelection;

public class Selection implements ISelection {

	private String[] selection;
	
	public String[] getSelection() {
		return selection;
	}
	
	public void setSelection(String[] selection)
	{
		this.selection = selection;
	}

}

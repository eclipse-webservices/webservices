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

package org.eclipse.jst.ws.internal.consumption.ui.selection;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.Selection;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

public class SelectionTransformer implements Transformer 
{
  public Object transform(Object value) 
  {
	IStructuredSelection inSelection  = (IStructuredSelection)value;
	Selection            outSelection = new Selection();
	
	if (inSelection != null)
	{
		outSelection.setSelection( inSelection.toArray() );	
	}
	
	return outSelection;
  }
}

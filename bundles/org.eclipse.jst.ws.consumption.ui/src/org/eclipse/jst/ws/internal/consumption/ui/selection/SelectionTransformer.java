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

package org.eclipse.jst.ws.internal.consumption.ui.selection;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.Selection;
import org.eclipse.wst.command.env.core.data.Transformer;

public class SelectionTransformer implements Transformer 
{
  public Object transform(Object value) 
  {
	IStructuredSelection inSelection  = (IStructuredSelection)value;
	Selection            outSelection = new Selection();
	
	outSelection.setSelection( inSelection.toArray() );
	
	return outSelection;
  }
}

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
package org.eclipse.wst.wsdl.ui.internal.graph.editpolicies;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.wsdl.ui.internal.actions.SmartRenameAction;
import org.eclipse.wst.wsdl.ui.internal.gef.util.editpolicies.TextCellEditorManager;

public class NameDirectEditManager extends TextCellEditorManager
{
  protected Object model;

  public NameDirectEditManager(GraphicalEditPart source, Label label, Object model)
  {
    super(source, label);  
    this.model = model;
  }

  public void performModify(final String value)
  {          
    Display.getCurrent().asyncExec(new SmartRenameAction(model, value));
  }      
}
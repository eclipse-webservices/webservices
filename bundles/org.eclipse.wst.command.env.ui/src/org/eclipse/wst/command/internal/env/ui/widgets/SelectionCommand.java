/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;


/**
 * This SelectionCommand is execute at the very beginning of the
 * dynamic wizard.  Commands executed in the wizard that need
 * the initial selection can get it through a data mapping from 
 * this command to the command that needs it.
 *
 */
public class SelectionCommand extends EnvironmentalOperation
{
  private IStructuredSelection selection_;
  
  public SelectionCommand( IStructuredSelection selection )
  {
    selection_ = selection;  
  }
  
  public IStructuredSelection getInitialSelection()
  {
    return selection_;
  }
}

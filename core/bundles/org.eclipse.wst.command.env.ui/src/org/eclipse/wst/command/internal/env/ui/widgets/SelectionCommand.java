/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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

package org.eclipse.wst.command.internal.env.ui.widgets;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


/**
 * This SelectionCommand is execute at the very beginning of the
 * dynamic wizard.  Commands executed in the wizard that need
 * the initial selection can get it through a data mapping from 
 * this command to the command that needs it.
 *
 */
public class SelectionCommand extends AbstractDataModelOperation
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

  public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
  {
    return Status.OK_STATUS;
  }
}

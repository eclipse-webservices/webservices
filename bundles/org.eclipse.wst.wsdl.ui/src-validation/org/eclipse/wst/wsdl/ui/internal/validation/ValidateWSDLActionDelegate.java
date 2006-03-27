/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.ui.internal.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;

/**
 * An action delegate for the WSDL validator.
 */
public class ValidateWSDLActionDelegate implements IActionDelegate
{
  protected ISelection selection;
  protected void validate(IFile file)
  {
    ValidateWSDLAction validateAction = new ValidateWSDLAction(file, true);
    validateAction.setValidator(new Validator());
    validateAction.run();	
  }
 
  /**
   * @see org.eclipse.ui.IActionDelegate#run(IAction)
   */
  public void run(IAction action)
  {
  	IFile file = null;
    if (!selection.isEmpty() && selection instanceof IStructuredSelection)
    {
      IStructuredSelection structuredSelection = (IStructuredSelection) selection;
      Object element = structuredSelection.getFirstElement();

      if (element instanceof IFile)
      {
        file = (IFile) element;
      }
      else
      {
        return;
      }
    }
          
    if (file != null)
    {          
      validate(file);
    }
  }

  /**
   * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection)
  {
  	this.selection = selection;
  }

}

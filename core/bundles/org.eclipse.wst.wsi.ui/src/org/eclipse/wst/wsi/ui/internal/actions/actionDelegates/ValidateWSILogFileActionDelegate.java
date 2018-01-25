/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.ui.internal.actions.actionDelegates;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;

import org.eclipse.wst.wsi.ui.internal.WSIMessageValidator;
import org.eclipse.wst.wsi.ui.internal.actions.WSIValidateAction;


/**
 * Action delegate for validating a WS-I log file.
 * 
 * @author David Lauzon, IBM
 * @author Lawrence Mandel, IBM
 */
public class ValidateWSILogFileActionDelegate implements IActionDelegate
{
  /**
   * The current selection, or null if there is no selection.
   */
  ISelection selection;

  /**
   * Constructor.
   */
  public ValidateWSILogFileActionDelegate()
  {
  }

  /**
   * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
   */
  public void run(IAction action)
  {
    try
    {
      if (selection instanceof IStructuredSelection)
      {
        Object  obj = ((IStructuredSelection)selection).getFirstElement();
        if (obj instanceof IFile)
        {
          IFile file = (IFile)obj;
          WSIMessageValidator messageValidator = new WSIMessageValidator();
          WSIValidateAction validateAction = new WSIValidateAction(file, true);
          validateAction.setValidator(messageValidator);
          validateAction.run();
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection)
  {
    this.selection = selection;
  }
}

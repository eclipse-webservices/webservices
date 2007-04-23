/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.ui.action;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.ui.widgets.DynamicWizard;


/**
 * This class launches the import wizard.
 */
public class LaunchImportWizardAction implements IActionDelegate
{
    private IStructuredSelection selection_;
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) 
    {
      DynamicWizard wizard = new DynamicWizard();
      
      try
      {
        wizard.setInitialData( "org.eclipse.jst.ws.consumption.ui.wsimport" );
        wizard.init( PlatformUI.getWorkbench(), selection_ );
      }
      catch( CoreException exc )
      {
        // Do nothing.
        return;
      }
      
  	  WizardDialog dialog= new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
	  dialog.setPageSize( 500, 400 );
	  dialog.create();
	  dialog.open();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) 
    {
      if( selection instanceof IStructuredSelection )
      {
        selection_ = (IStructuredSelection)selection;
      }
    }
}

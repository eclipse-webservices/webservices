/*******************************************************************************
 * Copyright (c) 2007, 2019 IBM Corporation and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.test.popup;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.ui.widgets.DynamicWizard;
import org.osgi.framework.Bundle;

public class NewAction implements IObjectActionDelegate {

  IStructuredSelection selection;
  
	/**
	 * Constructor for Action1.
	 */
	public NewAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) 
	{
	  DynamicWizard wizard = new DynamicWizard();
	  WizardDialog  dialog = new WizardDialog( null, wizard );
	  
	  try
	  {
	    Bundle bundle = Platform.getBundle( "org.eclipse.wst.ws.tests" );
	    wizard.setInitialData( "org.eclipse.jst.ws.creation.ui.wizard.serverwizard", bundle, "icons/full/wizban/webservices_wiz.png", "Peter title" );
	    wizard.init( PlatformUI.getWorkbench(), selection );
	    
	    dialog.open();
	  }
	  catch( CoreException exc )
	  {
	    exc.printStackTrace();
	  }
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) 
	{
	  if( selection instanceof IStructuredSelection )
    {
      this.selection = (IStructuredSelection ) selection;
    }
	}

}

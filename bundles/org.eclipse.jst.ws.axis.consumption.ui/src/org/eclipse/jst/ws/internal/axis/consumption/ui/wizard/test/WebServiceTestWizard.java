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
package org.eclipse.jst.ws.internal.axis.consumption.ui.wizard.test;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class WebServiceTestWizard extends Wizard implements INewWizard
{

  public WebServiceTestWizard()
  {
    super();

    TestWizardPage page = new TestWizardPage("Test page");
  }



  /**
  * Initializes this "New" wizard.
  * @param workbench The workbench that launched this wizard.
  * @param selection The selection context of this wizard.
  * @return This wizard.
  */
  public void init ( IWorkbench workbench, IStructuredSelection selection )
  {
    //Do nothing
  }


  public void addPages()
  {

    TestWizardPage page = new TestWizardPage("Test page");
    addPage(page);
  }

  public boolean performFinish()
  {
    //Always allow finish
    return true;
  }
  
}

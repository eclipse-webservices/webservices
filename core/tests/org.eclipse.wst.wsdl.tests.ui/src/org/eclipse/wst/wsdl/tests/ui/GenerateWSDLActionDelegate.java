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
package org.eclipse.wst.wsdl.tests.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.wst.wsdl.tests.WSDLGenerationTest;

public class GenerateWSDLActionDelegate implements IActionDelegate
{
  private ISelection selection = null;

  public void run(IAction action)
  {
    IFile ifile = null;
    if (!selection.isEmpty() && selection instanceof IStructuredSelection)
    {
      IStructuredSelection structuredSelection = (IStructuredSelection) selection;
      Object element = structuredSelection.getFirstElement();

      if (element instanceof IFile)
      {
      	ifile = (IFile) element;
        IPath path = ifile.getLocation();
        path = path.removeLastSegments(1);
        String output = path.toString() + "/MySample.wsdl";
        
        try
		{
          (new WSDLGenerationTest()).generateTemperatureService(output);
		}
        catch (Exception e)
		{
          e.printStackTrace();
		}

      }
      else
        return;
    }
        
  }
  
  public void selectionChanged(IAction action, ISelection selection)
  {
    this.selection = selection;
  }

}

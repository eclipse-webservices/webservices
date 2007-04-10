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
package org.eclipse.wst.wsdl.tests.ui;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.wst.wsdl.tests.util.WSDLConverter;



public class ConvertWSDL20ActionDelegate implements IActionDelegate
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
        IPath path2 = path.removeLastSegments(1);
        String output = path2.toString() + "/" + getNewName(ifile);
        
        try
		    {  
            Definition def = DefinitionLoader.load("file:/" + path.toString());
            WSDLConverter converter = new WSDLConverter(def);
            converter.generate20(output);
            IContainer folder = ifile.getParent();
            folder.refreshLocal(IResource.DEPTH_ONE,null);
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
  
  private String getNewName(IFile ifile)
  {
    // Assert currentName has a file extension, e.g. foo.wsdl.
    // This will return fooV20.wsdl.
    String currentName = ifile.getName();
    String name = currentName.substring(0,currentName.indexOf("."));
    String extension = currentName.substring(currentName.indexOf("."));
    String newName = name + "V20" + extension;
    return newName;
  }
  
  public void selectionChanged(IAction action, ISelection selection)
  {
    this.selection = selection;
  }

}

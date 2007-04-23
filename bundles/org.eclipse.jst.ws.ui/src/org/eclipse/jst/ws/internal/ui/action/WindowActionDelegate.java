/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.action;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import org.eclipse.jem.util.emf.workbench.ProjectUtilities;


public abstract class WindowActionDelegate implements IWorkbenchWindowActionDelegate {
protected IStructuredSelection fSelection;

public WindowActionDelegate() {
    super();
 }

public void dispose() 
 {
 }

public void init(org.eclipse.ui.IWorkbenchWindow window) { }

public void selectionChanged(org.eclipse.jface.action.IAction action, 
                                    org.eclipse.jface.viewers.ISelection selection) {
   if (selection instanceof IStructuredSelection)                                 
     fSelection = (IStructuredSelection) selection;
 }

public void setSelection(IStructuredSelection selection) {
	fSelection = selection;
}
public IStructuredSelection getSelection() {
	return fSelection;
}

public IProject getSelectedResourceProject() {
 IProject project = null;
 IResource resource_ = null;
   try {
   		if (fSelection != null && fSelection.size() == 1)    {
      		Object obj = fSelection.getFirstElement();

      		if (obj != null)    {
      			if ( obj instanceof IResource) 
      				{
      					resource_ = (IResource) obj;
      				}
      			else if (obj instanceof ICompilationUnit)
      			{
        			ICompilationUnit compUnit = (ICompilationUnit)obj;
        			resource_= compUnit.getCorrespondingResource();
      			}
				else if (obj instanceof IJavaProject) {
					project = ((IJavaProject) obj).getProject();
				}

				else if (obj instanceof EObject) {
					project = ProjectUtilities.getProject((EObject) obj);
				}

      			if ( resource_ != null )
   					project = resource_.getProject();
         		
    		}
   		}
   	}	
   catch (Exception e) {}
   return project;
 }

}

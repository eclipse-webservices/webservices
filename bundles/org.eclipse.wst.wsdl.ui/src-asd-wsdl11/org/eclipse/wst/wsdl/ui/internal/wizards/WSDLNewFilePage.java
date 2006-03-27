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
package org.eclipse.wst.wsdl.ui.internal.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;

public class WSDLNewFilePage extends WizardNewFileCreationPage
{
  public WSDLNewFilePage(IStructuredSelection selection) 
  {
    super(WSDLEditorPlugin.getWSDLString("_UI_TITLE_NEW_WSDL_FILE"), selection); //$NON-NLS-1$
    setTitle(WSDLEditorPlugin.getWSDLString("_UI_TITLE_NEW_WSDL_FILE")); //$NON-NLS-1$
    setDescription(WSDLEditorPlugin.getWSDLString("_UI_LABEL_CREATE_NEW_WSDL_FILE")); //$NON-NLS-1$
  }

  public void createControl(Composite parent) 
  {
    // inherit default container and name specification widgets
    super.createControl(parent);
    this.setFileName(computeDefaultFileName());

    setPageComplete(validatePage());
  }
  
  protected boolean validatePage()
  {
    Path newName = new Path(getFileName());
    String fullFileName = getFileName();
    String extension = newName.getFileExtension();
    if (extension == null || !extension.equalsIgnoreCase("wsdl"))  //$NON-NLS-1$
    {
      setErrorMessage(WSDLEditorPlugin.getWSDLString("_UI_ERROR_FILE_MUST_END_WITH_WSDL")); //$NON-NLS-1$
      return false;
    }
    else 
    {
      setErrorMessage(null);
    }

    // check for file should be case insensitive
    String sameName = existsFileAnyCase(fullFileName);
    if (sameName != null) 
    {
//       String qualifiedFileName = getContainerFullPath().toString() + '/' + fullFileName;
       setErrorMessage(WSDLEditorPlugin.getWSDLString("_UI_ERROR_FILE_ALREADY_EXISTS",  sameName)); //$NON-NLS-1$
       return false;
    }

    
    return super.validatePage();
  }

  public String defaultName = "NewWSDLFile"; //$NON-NLS-1$
  public String defaultFileExtension = ".wsdl"; //$NON-NLS-1$
  public String[] filterExtensions = { "*.wsdl"}; //$NON-NLS-1$


  protected String computeDefaultFileName()
  {
    int count = 0;
    String fileName = defaultName + defaultFileExtension;
    IPath containerFullPath = getContainerFullPath();
    if (containerFullPath != null)
    {
      while (true)
      {
        IPath path = containerFullPath.append(fileName);
        // if (WorkbenchUtility.getWorkspace().getRoot().exists(path))
        if (ResourcesPlugin.getWorkspace().getRoot().exists(path))
        {
          count++;
          fileName = defaultName + count + defaultFileExtension;
        }
        else
        {
          break;
        }
      }
    }
    return fileName;
  }

  // returns true if file of specified name exists in any case for selected container
  protected String existsFileAnyCase(String fileName)
  {
    if ( (getContainerFullPath() != null) && (getContainerFullPath().isEmpty() == false)
          && (fileName.compareTo("") != 0))
    {
      //look through all resources at the specified container - compare in upper case
      IResource parent = ResourcesPlugin.getWorkspace().getRoot().findMember(getContainerFullPath());
      if (parent instanceof IContainer)
      {
        IContainer container = (IContainer) parent;
        try
        {
          IResource[] members = container.members();
          String enteredFileUpper = fileName.toUpperCase();
          for (int i=0; i<members.length; i++)
          {
            String resourceUpperName = members[i].getName().toUpperCase();
            if (resourceUpperName.equals(enteredFileUpper))
            {  
              return members[i].getName();    
            }
          }
        }
        catch (CoreException e)
        {
        }
      }
    }
    return null;
  }

}

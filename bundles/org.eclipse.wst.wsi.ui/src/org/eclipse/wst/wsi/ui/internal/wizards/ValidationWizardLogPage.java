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
package org.eclipse.wst.wsi.ui.internal.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import org.eclipse.wst.wsi.ui.internal.SoapMonitorPlugin;

/**
 * Wizard page that allows the user to specify the location of the 
 * WS-I Message Log file for purposes of validation.
 * 
 * @author David Lauzon, IBM
 */

public class ValidationWizardLogPage extends WizardNewFileCreationPage
{
  /**
   * Recommended extension for a WS-I Message Log file.
   */
  public static final String WSI_LOG_EXTENSION = "wsimsg";
 
  /**
   * Default WS-I Message filename passed in the Constructor.
   */
  protected String filename;
  
  /**
   * Constructor.
   * 
   * @param selection: selection in the Resource Navigator view.
   * @param filename: default message log file name.
   */
  public ValidationWizardLogPage(IStructuredSelection selection, String filename)
  {
    super("ValidationWizardLogPage", selection);
    this.filename = filename;
    this.setTitle(SoapMonitorPlugin.getResourceString("_UI_WIZARD_V_SELECT_LOG_FILENAME_HEADING"));
    this.setDescription(SoapMonitorPlugin.getResourceString("_UI_WIZARD_V_SELECT_LOG_FILENAME_EXPL"));
  }


  /** 
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite parent) 
  {
    super.createControl(parent);
    super.setFileName(filename);
    setPageComplete(validatePage());
  }

  /**
   * Returns the current selected file.
   * 
   * @return the selected IFile.  
   */
  public IFile getFile()
  {
    String fileName = getFileName();
    String fileExtension = (new Path(fileName)).getFileExtension();
    
    if (fileExtension == null)
      fileName = fileName.concat("." + WSI_LOG_EXTENSION);
      
    return ResourcesPlugin.getWorkspace().getRoot().getFile(getContainerFullPath().append(fileName));
  }

  /**
   * Returns true if selected file is a valid WS-I Message Log filename.
   * 
   * Since the WizardNewFileCreationPage does not provide an interface to
   * allow the file specified to already exist (it gives an error that the
   * the file already exists, an no way of allowing it to pass), and we want
   * to allow the file to exist, so that we can overwrite it, we have to
   * override this method, and do some magic to allow this case to pass.
   *
   * @return true if selected file is a valid WS-I Message Log filename.
   * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
   */
  protected boolean validatePage() 
  {
    String fileExtension = (new Path(getFileName())).getFileExtension();
    if (fileExtension != null) 
    {
      if (fileExtension.compareTo(WSI_LOG_EXTENSION) != 0)
      {        
        setErrorMessage(SoapMonitorPlugin.getResourceString("_ERROR_INVALID_LOG_FILE_EXTENSION"));
        return false;
      }
    }
    
    // Essentially, we take the container and concat the file to it, and
    // check if its in the workspace, if it is, then it exists and we can 
    // proceed. Otherwise, continue with the default validating.
    if ( (getContainerFullPath() != null) && (getContainerFullPath().isEmpty() == false)
           && (getFileName().compareTo("") != 0))
    {
      Path fullPath;
            
      if (fileExtension != null)
        fullPath = new Path(getContainerFullPath().toString() + '/' + getFileName());
      else
        fullPath = new Path(getContainerFullPath().toString() + '/' + getFileName() + "." + WSI_LOG_EXTENSION);
      
      IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(fullPath);
      
      setErrorMessage(null);
      if (resource != null)
        setMessage(SoapMonitorPlugin.getResourceString("_WARNING_FILE_ALREADY_EXISTS"));
      else
        setMessage(null);

      return true;
    }
    return super.validatePage();
  }
}


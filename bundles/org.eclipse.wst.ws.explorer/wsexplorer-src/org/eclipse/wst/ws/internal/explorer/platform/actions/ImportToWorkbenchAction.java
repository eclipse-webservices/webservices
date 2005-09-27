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
package org.eclipse.wst.ws.internal.explorer.platform.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.wsil.AddWSDLToWSILCommand;

public abstract class ImportToWorkbenchAction extends FormAction {
  private IWorkspaceRoot iWorkspaceRoot_;
  private IProject iProject_;
  private IResource targetFileResource_;

    public ImportToWorkbenchAction(Controller controller) {
        super(controller);
        iWorkspaceRoot_ = ResourcesPlugin.getWorkspace().getRoot();
    }

    protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException {
        getSelectedFormTool();
        MessageQueue msgQueue = controller_.getCurrentPerspective().getMessageQueue();
        boolean inputsValid = true;
        String workbenchProjectName = parser.getParameter(ActionInputs.WORKBENCH_PROJECT_NAME);
        if (workbenchProjectName == null || workbenchProjectName.length() <= 0) {
            msgQueue.addMessage(controller_.getMessage("MSG_ERROR_INVALID_WORKBENCH_PROJECT"));
            inputsValid = false;
        }
        else
        {
            iProject_ = iWorkspaceRoot_.getProject(workbenchProjectName);
            propertyTable_.put(ActionInputs.WORKBENCH_PROJECT_NAME, workbenchProjectName);
        }
        String importFile = parser.getParameter(ActionInputs.IMPORT_FILE);
        if (importFile != null)
        {
            propertyTable_.put(ActionInputs.IMPORT_FILE, ActionInputs.IMPORT_FILE);
            String importedFileName = parser.getParameter(ActionInputs.IMPORTED_FILE_NAME);
            if (importedFileName == null || importedFileName.length() <= 0)
            {
                msgQueue.addMessage(controller_.getMessage("MSG_ERROR_INVALID_FILE_NAME"));
                inputsValid = false;
            }
            else
                propertyTable_.put(ActionInputs.IMPORTED_FILE_NAME, importedFileName);
        }
        String importToWSIL = parser.getParameter(ActionInputs.IMPORT_TO_WSIL);
        if (importToWSIL != null)
        {
            propertyTable_.put(ActionInputs.IMPORT_TO_WSIL, ActionInputs.IMPORT_TO_WSIL);
            propertyTable_.put(ActionInputs.IMPORTED_WSDL_URL, parser.getParameter(ActionInputs.IMPORTED_WSDL_URL));
            String wsilFileName = parser.getParameter(ActionInputs.WSIL_FILE_NAME);
            if (wsilFileName == null || wsilFileName.length() <= 0)
            {
                msgQueue.addMessage(controller_.getMessage("MSG_ERROR_INVALID_WSIL_FILE_NAME"));
                inputsValid = false;
            }
            else
                propertyTable_.put(ActionInputs.WSIL_FILE_NAME, wsilFileName);
        }
        if (importFile == null && importToWSIL == null)
        {
            msgQueue.addMessage(controller_.getMessage("MSG_ERROR_NO_IMPORT_OPTION_SELECTED"));
            inputsValid = false;
        }
        return inputsValid;
    }

    private OutputStream getOutputStream(String defaultFileName) throws FileNotFoundException {
        File file = new File(controller_.getServletEngineStateLocation() + defaultFileName);
        FileOutputStream fos = new FileOutputStream(file);
        return fos;
    }

    private boolean importTempFileToWebProject(String defaultFileName) throws FileNotFoundException, CoreException
    {
      if (targetFileResource_ != null && targetFileResource_.getType() == IResource.FILE)
        targetFileResource_.delete(true,new NullProgressMonitor());

      String importedFileName = (String)propertyTable_.get(ActionInputs.IMPORTED_FILE_NAME);
      File file = new File(controller_.getServletEngineStateLocation()+defaultFileName);
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

      IFile iFile = iProject_.getFile("/"+importedFileName);
      iFile.create(bis,true,new NullProgressMonitor());

      // Remove the temporary file
      file.delete();
      return true;
    }

    public boolean fileExists()
    {
      String importedFileName = (String)propertyTable_.get(ActionInputs.IMPORTED_FILE_NAME);
      if (importedFileName != null)
      {
        targetFileResource_ = iProject_.findMember(importedFileName);
        return (targetFileResource_ != null && targetFileResource_.getType() == IResource.FILE);
      }
      else
        return false;
    }

    public boolean isTargetFileResourceReadOnly()
    {
      return targetFileResource_.getResourceAttributes().isReadOnly();
    }

    // TODO: ResourceContext need to move to org.eclipse.wst.ws.
    public boolean isCheckoutFilesEnabled()
    {
    	return true;
//      return WebServicePlugin.getInstance().getResourceContext().isCheckoutFilesEnabled();
    }

    public String getWebServicePluginFileMessage(String messageId)
    {
      String importedFileName = (String)propertyTable_.get(ActionInputs.IMPORTED_FILE_NAME);
      return controller_.getMessage(messageId,new String[]{iProject_.getFullPath().toString(),importedFileName});
    }

    // TODO: ResourceContext need to move to org.eclipse.wst.ws. 
    public boolean isOverwriteFilesEnabled()
    {
    	return true;
//      return WebServicePlugin.getInstance().getResourceContext().isOverwriteFilesEnabled();
    }

    public boolean validateEdit()
    {
      IFile[] files = new IFile[1];
      files[0] = (IFile)targetFileResource_;
      IStatus status = ResourcesPlugin.getWorkspace().validateEdit(files,null);
      MessageQueue messageQueue = controller_.getCurrentPerspective().getMessageQueue();
      messageQueue.addMessage(status.getMessage());
      return status.isOK();
    }

    public boolean run() {
        MessageQueue msgQueue = controller_.getCurrentPerspective().getMessageQueue();
        if (propertyTable_.get(ActionInputs.IMPORT_FILE) != null)
        {
            try {
                ImportToFileSystemAction action = newImportToFileSystemAction();
                action.run();
                String defaultFileName = action.getDefaultFileName();
                if (!action.write(getOutputStream(defaultFileName))) {
                    msgQueue.addMessage(controller_.getMessage("MSG_ERROR_WRITING_TEMP_FILE_TO_FS"));
                    return false;
                }
                importTempFileToWebProject(defaultFileName);
                String importedFileName = (String)propertyTable_.get(ActionInputs.IMPORTED_FILE_NAME);
                msgQueue.addMessage(controller_.getMessage("MSG_INFO_IMPORT_TO_WORKBENCH_SUCCESSFUL", importedFileName));
            }
            catch (FileNotFoundException fnfe) {
                msgQueue.addMessage(fnfe.getMessage());
                return false;
            }
            catch (CoreException ce) {
                msgQueue.addMessage(ce.getMessage());
                return false;
            }
        }
        if (propertyTable_.get(ActionInputs.IMPORT_TO_WSIL) != null)
        {
            String projectName = (String)propertyTable_.get(ActionInputs.WORKBENCH_PROJECT_NAME);
            String importedWSILFileName = (String)propertyTable_.get(ActionInputs.WSIL_FILE_NAME);
            String wsdlURL = (String)propertyTable_.get(ActionInputs.IMPORTED_WSDL_URL);
            StringBuffer wsilPlatformURL = new StringBuffer("platform:/resource/");
            wsilPlatformURL.append(projectName);
            wsilPlatformURL.append('/');
            wsilPlatformURL.append(importedWSILFileName);
            AddWSDLToWSILCommand command = new AddWSDLToWSILCommand();
            String[] args = new String[5];
            args[0] = AddWSDLToWSILCommand.ARG_WSIL;
            args[1] = wsilPlatformURL.toString();
            args[2] = AddWSDLToWSILCommand.ARG_WSDL;
            args[3] = wsdlURL;
            args[4] = AddWSDLToWSILCommand.ARG_RESOLVE_WSDL;
            command.setArguments(args);
            Environment env = new EclipseEnvironment(null, null, null, null);
            command.setEnvironment( env );
            command.execute( null, null );
            msgQueue.addMessage(controller_.getMessage("MSG_INFO_IMPORT_SERVICE_REF_TO_WSIL_SUCCESSFUL", importedWSILFileName));
        }
        return true;
    }

    public abstract String getStatusContentVar();
    public abstract String getStatusContentPage();
    public abstract ImportToFileSystemAction newImportToFileSystemAction();
}

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
package org.eclipse.wst.wsdl.ui.internal.actions;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewFolderMainPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;


public class CreateDocActionDelegate extends ActionDelegate implements IEditorActionDelegate
{
  protected IFile iFile;
  protected IEditorPart editorPart;

  protected final static String WSDLEDITOR_DOCGEN_PLUGIN_ID = "org.eclipse.wst.wsdl.ui.internal.docgen"; //$NON-NLS-1$
  protected final static String VALIDATE_WSDL_PLUGIN_ID = "org.eclipse.wst.validate.wsdl"; //$NON-NLS-1$

  protected IWorkspaceRoot workspaceRoot = null;

  public CreateDocActionDelegate()
  {
  }

  public void setFile(IFile iFile)
  {
    this.iFile = iFile;
  }

  public void setEditorPart(IEditorPart editorPart)
  {
    this.editorPart = editorPart;
  }

  public void run(IAction action)
  {
// TODO: Uncomment when validation is checked into corona    
//    ISharedService validateSharedService = ExtensionPlugin.getSharedService("ValidateWSDLSharedService"); //$NON-NLS-1$
//    System.out.println(validateSharedService);
//    if (validateSharedService != null)
//    {
//      try
//      {
//        Boolean rc = (Boolean) validateSharedService.run(iFile);
//        if (rc.booleanValue())
//        {
          NewWSDLDocFolderWizard wiz = new NewWSDLDocFolderWizard();
          NewWSDLDocDialog dlg = new NewWSDLDocDialog(WSDLEditorPlugin.getShell(), wiz);
          dlg.open();
//        }
//        else
//        {
//          MessageDialog.openError(Display.getCurrent().getActiveShell(), WSDLEditorPlugin.getWSDLString("_UI_ERROR_INVALID_WSDL"), //$NON-NLS-1$
//          WSDLEditorPlugin.getWSDLString("_UI_ERROR_INVALID_WSDL_DESC")); //$NON-NLS-1$
//        }
//      }
//      catch (Exception e)
//      {
//      }
//    }
//    else
//    {
//      MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", //$NON-NLS-1$
//      "Cannot find validator"); //$NON-NLS-1$
//    }
  }

  class GenHTMLOperation extends WorkspaceModifyOperation
  {
    IFile iFile;
    String outputLocation;
    public GenHTMLOperation(IFile iFile)
    {
      super();
      this.iFile = iFile;
    }

    public void setOutputLocation(String outputLocation)
    {
      this.outputLocation = outputLocation;
    }

    protected void execute(IProgressMonitor monitor) throws CoreException
    {
    	/*
      monitor.beginTask(WSDLEditorPlugin.getWSDLString("_UI_LABEL_CREATING_HTML_FILES"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
      WSDLDoc doc = new WSDLDoc(new WSDLDocFileWriter());

      try
      {
        Plugin plugin = Platform.getPlugin("org.eclipse.wst.wsdl.ui.internal.docgen"); //$NON-NLS-1$
        IPath installPath = new Path(plugin.getDescriptor().getInstallURL().toExternalForm()).removeTrailingSeparator();
        String installStr = Platform.asLocalURL(new URL(installPath.toString())).getFile();
        Path docgenPath = new Path(installStr);

        doc.setTemplateLocation(docgenPath.toOSString() + "template" + File.separator); //$NON-NLS-1$
        doc.setOptions(iFile.getRawLocation().removeLastSegments(1).toOSString(), outputLocation, "");

        doc.addFile(iFile.getName());
        doc.doBuildStep();
      }
      catch (Exception e)
      {
        // e.printStackTrace(); 
      }
      finally
      {
        monitor.done();
      }
      */
    }
  }

  class WSDLDocNewFolderMainPage extends WizardNewFolderMainPage
  {
    public WSDLDocNewFolderMainPage(String pageName)
    {
      super(pageName, new StructuredSelection(iFile));
      setDescription(WSDLEditorPlugin.getWSDLString("_UI_LABEL_CREATE_FOLDER_FOR_DOCS")); //$NON-NLS-1$
    }
  }

  class NewWSDLDocFolderWizard extends Wizard
  {
    IFolder folder;

    private WSDLDocNewFolderMainPage newFolderPage;

    public NewWSDLDocFolderWizard()
    {
      super();
      setNeedsProgressMonitor(true);
    }

    public boolean performFinish()
    {
      folder = newFolderPage.createNewFolder();
      if (folder == null)
      {
        MessageDialog.openError(WSDLEditorPlugin.getShell(), WSDLEditorPlugin.getWSDLString("_UI_ERROR_ERROR"), //$NON-NLS-1$
        WSDLEditorPlugin.getWSDLString("_UI_ERROR_FOLDER_NOT_CREATED")); //$NON-NLS-1$
        return false;
      }

      GenHTMLOperation op = new GenHTMLOperation(iFile);
      op.setOutputLocation(folder.getLocation().toOSString());

      try
      {
        op.execute(new NullProgressMonitor());

        // refresh folder and open index.html in web browser
        folder.refreshLocal(IResource.DEPTH_INFINITE, null);
        String indexHtmlFile = folder.getLocation().toOSString() + File.separator + "index.html"; //$NON-NLS-1$
        IFile indexHtmlIFile = getWorkspaceFileFromLocalLocation(indexHtmlFile);
        if (indexHtmlIFile != null && indexHtmlIFile.exists())
        {
          revealSelection(new StructuredSelection(indexHtmlIFile));
          
          openEditor(indexHtmlIFile, "org.eclipse.webbrowser"); //$NON-NLS-1$
        }
        else
        {
          MessageDialog.openError(Display.getCurrent().getActiveShell(), WSDLEditorPlugin.getWSDLString("_UI_ERROR_ERROR"), //$NON-NLS-1$
          WSDLEditorPlugin.getWSDLString("_UI_ERROR_CREATING_HTML_FILES")); //$NON-NLS-1$
        }
      }
      catch (CoreException ce)
      {
        //        ce.printStackTrace();
      }
      catch (Exception e)
      {
        //        e.printStackTrace();
      }
      return true;
    }

    protected void revealSelection(final ISelection selection)
    {
      if (selection != null)
      {
        IWorkbench workbench = WSDLEditorPlugin.getInstance().getWorkbench();
        final IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
        final IWorkbenchPart focusPart = workbenchWindow.getActivePage().getActivePart();
        if (focusPart instanceof ISetSelectionTarget)
        {
          Display.getCurrent().asyncExec
          (new Runnable()
              {
            public void run()
            {
              ((ISetSelectionTarget)focusPart).selectReveal(selection);
            }
          });
        }
      }
    }
    
    protected void openEditor(final IFile iFile, final String editorId)
    {
      if (iFile != null)
      {
        IWorkbench workbench = WSDLEditorPlugin.getInstance().getWorkbench();
        final IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();

        Display.getDefault().asyncExec
        (new Runnable()
            {
          public void run()
          {
            try
            {
              workbenchWindow.getActivePage().openEditor(new FileEditorInput(iFile), editorId);
            }
            catch (PartInitException ex)
            {
//              B2BGUIPlugin.getPlugin().getMsgLogger().write("Exception encountered when attempting to open file: " + iFile + "\n\n" + ex);
            }
          }
        });
      }
    }
    
    protected IFile getWorkspaceFileFromLocalLocation(String fileName)
    {
      IWorkspace workspace = ResourcesPlugin.getWorkspace();
      if (workspace != null)
      {
        IFile iFile = workspace.getRoot().getFileForLocation(new Path(fileName));
        if (iFile != null)
        {
          return iFile;
        }
      }
      return null;
    }
    
    public void addPages()
    {
      newFolderPage = new WSDLDocNewFolderMainPage(WSDLEditorPlugin.getWSDLString("_UI_LABEL_NEW_FOLDER")); //$NON-NLS-1$
      addPage(newFolderPage);
    }

    public boolean canFinish()
    {
      if (newFolderPage.isPageComplete())
      {
        return true;
      }
      return false;
    }

    public IFolder getFolder()
    {
      return folder;
    }
  }

  class NewWSDLDocDialog extends WizardDialog
  {
    public NewWSDLDocDialog(Shell parentShell, IWizard newWizard)
    {
      super(parentShell, newWizard);
    }
  }

  public void setActiveEditor(IAction action, IEditorPart targetEditor)
  {
    editorPart = targetEditor;
    iFile = null;
    if (editorPart != null)
    {
      IEditorInput input = targetEditor.getEditorInput();
      if (input instanceof IFileEditorInput)
      {
        iFile = ((IFileEditorInput) input).getFile();
      }
    }
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
  }
}

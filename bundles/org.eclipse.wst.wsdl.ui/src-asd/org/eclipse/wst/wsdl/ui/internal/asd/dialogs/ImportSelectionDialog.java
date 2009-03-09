/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.ui.internal.asd.dialogs;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.ui.internal.dialogs.SelectSingleFileDialog;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;


/**
 * A dialog used to select a wsdl or xsd file location. Provides the computed
 * relative location of the imported file to the currently edited file, as well as 
 * the imported definition's target namespace.
 */
public class ImportSelectionDialog extends SelectSingleFileDialog
{
  private IFile currentWSDLFile;

  public ImportSelectionDialog(Shell parentShell, IStructuredSelection selection, boolean isFileMandatory)
  {
    super(parentShell, selection, isFileMandatory);

    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
    IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
    IEditorPart editor = activePage.getActiveEditor();
    IEditorInput editorInput = editor.getEditorInput();

    // TODO vb Use the extensions declared for the WSDL and XSD content types 
    // instead of hardcoded file extensions.

    String[] filters = { "xsd", "wsdl" }; //$NON-NLS-1$ //$NON-NLS-2$

    if (editorInput instanceof IFileEditorInput)
    {
      currentWSDLFile = ((IFileEditorInput)editorInput).getFile();
      IFile[] excludedFiles = { currentWSDLFile };
      addFilterExtensions(filters, excludedFiles);
    }
    else
    {
      IFile[] excludedFiles = {};
      addFilterExtensions(filters, excludedFiles);
    }
  }

  public void create()
  {
    super.create();
    getShell().setText(Messages._UI_TITLE_SELECT);
    setTitle(Messages._UI_TITLE_SELECT_FILE);
    setMessage(Messages._UI_DESCRIPTION_SELECT_WSDL_OR_XSD);
  }

  /**
   * Provides the import location, relative to the file currently edited in the editor.
   * @return a String with the relative location URI of the imported definition.
   */
  public String getImportLocation()
  {
    IFile selectedFile = getFile();
    String location = ComponentReferenceUtil.computeRelativeURI(selectedFile, currentWSDLFile, true);
    return location;
  }

  /**
   * Provides the imported definition's target namespace.
   * @return a String with the targe namespace of the imported definition.
   */
  public String getImportNamespace()
  {
    IFile selectedFile = getFile();
    IPath fullPath = selectedFile.getFullPath();
    URI uri = URI.createPlatformResourceURI(fullPath.toString(), false);

    // TODO rm Note that the getTargetNamespaceURIForSchema works for both schema and wsdl files
    // I should change the name of this convenience method
    String importTargetNamespace = WSDLEditorUtil.getTargetNamespaceURIForSchema(uri.toString());

    return importTargetNamespace;
  }
}

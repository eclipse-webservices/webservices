/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.actions;

import java.net.URI;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.xsd.ui.internal.adt.editor.ADTReadOnlyFileEditorInput;

/**
 * 
 * Convenience Class to open a WSDL resource
 *
 */
public class OpenWSDLEditorUtil
{
  public OpenWSDLEditorUtil()
  {
  }

  /**
   * Opens the WSDL Editor on a WSDL resource from an HTTP reference
   * 
   * @param httpURI : URI in the form of "http://www..."
   * @return IEditorPart of the opened WSDL editor
   */
  public static IEditorPart openHttpFileInEditor(URI httpURI) throws Exception
  {
    return openHttpFileInEditor(httpURI.toString()); 
  }
  
  private static IEditorPart openHttpFileInEditor(String urlString) throws PartInitException
  {
    try {
      IEditorPart editorPart = null;
      IWorkbench workbench = PlatformUI.getWorkbench();
      if (workbench != null) {
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window != null) {
          IWorkbenchPage page = window.getActivePage();
          if (page != null) {
            ADTReadOnlyFileEditorInput readOnlyFile = new ADTReadOnlyFileEditorInput(urlString);
            readOnlyFile.setEditorID(WSDLEditorPlugin.WSDL_EDITOR_ID);
            editorPart = page.openEditor(readOnlyFile, WSDLEditorPlugin.WSDL_EDITOR_ID); 
          }
        }
      }
      return editorPart;
    }
    catch (PartInitException pie)
    {
      throw pie;
    }
  }
  
  /**
   * Opens the WSDL Editor on a WSDL resource that is in the local file system
   * 
   * @param fileURI: The URI file in the form of file:// 
   * @return IEditorPart of the opened WSDL editor
   * @throws Exception
   */
  public static IEditorPart openFileInEditor(URI fileURI) throws PartInitException
  {
    try {
      IEditorPart editorPart = null;
      IWorkbench workbench = PlatformUI.getWorkbench();
      if (workbench != null) {
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window != null) {
          IWorkbenchPage page = window.getActivePage();
          if (page != null) {
            IPath path = new Path(fileURI.getPath());
            IFileStore fileStore = EFS.getLocalFileSystem().getStore(path);
            if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
              IDE.openEditorOnFileStore(page, fileStore);
            }
          }
        }
      }
      return editorPart;
    }
    catch (PartInitException pie)
    {
      throw pie;
    }
  }

}

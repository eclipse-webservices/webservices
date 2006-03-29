/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.text;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * WSDLHyperlink knows how to open links from wsdl files.
 * 
 * @see WSDLHyperlinkDetector
 */
public class WSDLHyperlink implements IHyperlink
{
  private IRegion fRegion;
  private String fResource;
  private String fSpec;

  public WSDLHyperlink(IRegion region, String resource, String spec)
  {
    fRegion = region;
    fResource = resource;
    fSpec = spec;
  }

  public IRegion getHyperlinkRegion()
  {
    return fRegion;
  }

  public String getTypeLabel()
  {
    return null;
  }

  public String getHyperlinkText()
  {
    return null;
  }

  public void open()
  {
    /*
     * ISSUE: There are cleaner ways to find the right file based on a URI
     * string and cleaner ways to find which editor to open for the file. See
     * other IHyperlink and IHyperlinkDetector implementors for examples.
     */
    String pattern = "platform:/resource";
    if (fResource != null && fResource.startsWith(pattern))
    {
      Path path = new Path(fResource.substring(pattern.length()));
      IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

      IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

      IEditorPart editorPart = workbenchPage.getActiveEditor();

      // When using the cursor or the mouse to move around in the text
      // editor, the location history is not updated, so we make sure we keep
      // a reference to where we are. If we use other navigation means, like the
      // outline view, this call will not add a new location history entry, as
      // it is the same as the last location set through the outline driven
      // navigation.

      boolean sameEditor = editorPart.getEditorInput() instanceof IFileEditorInput && ((IFileEditorInput) editorPart.getEditorInput()).getFile().equals(file);

      workbenchPage.getNavigationHistory().markLocation(editorPart);

      if (!sameEditor)
      {
        try
        {
          // The target is in a different file, so open the target's enclosing
          // resource in it's own editor. Let the workbench decide what editor
          // to open based on the file's content type.

          editorPart = IDE.openEditor(workbenchPage, file, true);
        }
        catch (PartInitException e)
        {
//          Logger.log(Logger.WARNING_DEBUG, e.getMessage(), e);
          return;
        }
      }

      // Attempt to retrieve the target editor's selection manager and try to
      // change the selection to the thing (schema component, wsdl element, etc)
      // pointed to by fSpec.

      ISelectionProvider selectionProvider = (ISelectionProvider) editorPart.getAdapter(ISelectionProvider.class);

      if (selectionProvider != null)
      {
        selectionProvider.setSelection(new StructuredSelection(fSpec));
      }
    }
  }
}

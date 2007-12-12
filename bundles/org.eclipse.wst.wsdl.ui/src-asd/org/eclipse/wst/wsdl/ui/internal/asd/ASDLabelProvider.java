/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.xsd.ui.internal.adt.editor.EditorModeManager;

public class ASDLabelProvider extends LabelProvider {
	/**
	 * 
	 */
	public ASDLabelProvider() {
		super();
	}
	
    private ILabelProvider getDelegate()
    {
      ILabelProvider labelProvider = null;   
      IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
      EditorModeManager manager = (EditorModeManager) editor.getAdapter(EditorModeManager.class);
      if (manager != null)
      {
        labelProvider = (ILabelProvider) manager.getCurrentMode().getAdapter(ILabelProvider.class);
      }
      return labelProvider;
    }
	/**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object object) {
		if (object == null || object.equals(StructuredSelection.EMPTY)) {
			return null;
		}
		Image result = null;           
		if (object instanceof StructuredSelection) {
			Object selected = ((StructuredSelection)object).getFirstElement();
		    ILabelProvider delegate = getDelegate();
            if (delegate != null) {
              result = delegate.getImage(selected);
            }
            else if (selected instanceof ITreeElement) {
				result = ((ITreeElement) selected).getImage();
			}
		}
		
		return result;
	}
	
	/**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object object) {
		if (object == null || object.equals(StructuredSelection.EMPTY)) {
			return "No items selected";//$NON-NLS-1$
		}
		String result = null;
		Object selected = null;
		if (object instanceof StructuredSelection) {
			selected = ((StructuredSelection) object).getFirstElement();
			
      ILabelProvider delegate = getDelegate();
      if (delegate != null) {
        result = delegate.getText(selected);
      }
      else if (selected instanceof ITreeElement) {
          result = ((ITreeElement) selected).getText();
      }

      boolean isFileReadOnly = false;
      IWorkbench workbench = PlatformUI.getWorkbench();
      if (workbench != null) {
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window != null) {
          IWorkbenchPage page = window.getActivePage();
          if (page != null) {
            IEditorPart editor = page.getActiveEditor();
            if (editor != null) {
              IEditorInput editorInput = editor.getEditorInput();
              if (!(editorInput instanceof IFileEditorInput || editorInput instanceof FileStoreEditorInput)) {
                isFileReadOnly = true;
              }
            }
          }
        }
      }

			if (selected instanceof IASDObject && ((IASDObject) selected).isReadOnly() || isFileReadOnly) {
				result  = result + " (" + Messages._UI_LABEL_READ_ONLY + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		
		return result;
	}
}

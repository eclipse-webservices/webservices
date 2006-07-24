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
package org.eclipse.wst.wsdl.ui.internal.asd.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;

public class ASDDirectEditAction extends DirectEditAction {
	protected ISelectionProvider provider;
	
	/**
	 * Same as {@link #DirectEditAction(IWorkbenchPart)}.
	 * @param editor the editor
	 */
	public ASDDirectEditAction(IEditorPart editor) {
		super((IWorkbenchPart)editor);
	}

	/**
	 * Constructs a DirectEditAction using the specified part.
	 * @param part the workbench part
	 */
	public ASDDirectEditAction(IWorkbenchPart part) {
		super(part);
	}
	
	  /* (non-Javadoc)
	   * @see org.eclipse.gef.ui.actions.SelectionAction#getSelection()
	   */
	  protected ISelection getSelection()
	  {
		  // If we're in this action, we assume the action was initiated via the graphical viewer.
		  // We use the graphical view to obtain the selected edit part because for us, one model
		  // may drive more than one edit part.  So we need to find the exact edit part we wish to
		  // direct edit.  The preferred method of determining the edit part would have been to go
		  // through the selection manager, then through the edit part registry.  However, the selection
		  // manager only returns the model object (not the edit part).... and since the model object
		  // may drive more than one edit part, the edit part registry may return the "wrong" edit
		  // part for direct editing.  Thus we look call GraphicalViewer.getFocusEditPart().
		  IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		  Object object = editor.getAdapter(GraphicalViewer.class);
		  if (object instanceof GraphicalViewer) {
			  Object selection = ((GraphicalViewer) object).getFocusEditPart();
			  return new StructuredSelection(selection);
		  }
	    
	    return super.getSelection();
	  }
	  
	  /* (non-Javadoc)
	   * @see org.eclipse.gef.ui.actions.SelectionAction#setSelectionProvider(org.eclipse.jface.viewers.ISelectionProvider)
	   */
	  public void setSelectionProvider(ISelectionProvider provider)
	  {
	    super.setSelectionProvider(provider);
	    this.provider = provider;
	  }
	  
	  protected boolean calculateEnabled() {
		  Object selection = ((IStructuredSelection) getSelection()).getFirstElement();
		  
		  if (selection instanceof IASDObject) {
			  return  !((IASDObject) selection).isReadOnly();
		  }
		  
		  return true;
	  }
}

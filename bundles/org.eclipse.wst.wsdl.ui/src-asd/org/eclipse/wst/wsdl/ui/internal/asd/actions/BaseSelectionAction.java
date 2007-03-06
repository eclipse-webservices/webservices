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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.AbstractEditPartViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.AbstractModelCollection;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;

public abstract class BaseSelectionAction extends SelectionAction
{
  public static final String SUBMENU_START_ID = "SUBMENU_START_ID: "; //$NON-NLS-1$
  public static final String SUBMENU_END_ID = "SUBMENU_END_ID: "; //$NON-NLS-1$
  
  protected ISelectionProvider provider;
  
  public BaseSelectionAction(IWorkbenchPart part)
  {
    super(part);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.gef.ui.actions.SelectionAction#getSelection()
   */
  protected ISelection getSelection()
  {
    // always get selection from selection provider first
    if (provider!=null)
      return provider.getSelection();
    
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
  
  public List getSelectedObjects()
  {
	  List processedObjects = new ArrayList();
	  List objects = super.getSelectedObjects();
	  Iterator it = objects.iterator();
	  while (it.hasNext()) {
		  Object item = it.next();
		  if (item instanceof AbstractModelCollection) {
			  processedObjects.add(((AbstractModelCollection) item).getModel());
		  }
		  else {
			  processedObjects.add(item);
		  }
	  }
	  
	  return processedObjects;
  }
  
  protected boolean calculateEnabled() {
	  Object selection = ((IStructuredSelection) getSelection()).getFirstElement();
	  
	  if (selection instanceof IASDObject) {
		  return  !((IASDObject) selection).isReadOnly();
	  }
	  
	  return true;
  }
  
  protected void selectAndDirectEdit(final Object o) {
	  Runnable runnable = new Runnable() {
		  public void run() {
			  if (o instanceof Notifier) {
				  performSelection(o);
				  activateDirectEdit();
			  }
		  }};
		  Display.getCurrent().asyncExec(runnable);
  }

	protected void activateDirectEdit() {
		IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (!(part instanceof ContentOutline)) {
			IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			Object graphicalViewer = editor.getAdapter(GraphicalViewer.class);
			if (graphicalViewer instanceof AbstractEditPartViewer) {
				AbstractEditPartViewer viewer = (AbstractEditPartViewer) graphicalViewer;
				Object obj = viewer.getSelectedEditParts().get(0);
				// todo: rmah: we need to completely remove all implementations of doDirectEdit().
				// Use performRequest() instead.
//				doDirectEdit((EditPart) obj);
				Request request = new Request();
				request.setType(RequestConstants.REQ_DIRECT_EDIT);
				((EditPart) obj).performRequest(request);
			}
		}
	}
	
	protected void doDirectEdit(EditPart ep) {
		Request request = new Request();
		request.setType(RequestConstants.REQ_DIRECT_EDIT);
		ep.performRequest(request);
	}
	
    protected void performSelection(Object object) {
		if (!(object instanceof Notifier)) {
			return;
		}
		
		Notifier element = (Notifier) object;
		
    	try {
			// TODO: We shouldn't know about WSDLAdapterFactoryHelper here....
	    	Object adapted = WSDLAdapterFactoryHelper.getInstance().adapt(element);
	        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	        if (editor != null && editor.getAdapter(ISelectionProvider.class) != null) {
	        	ISelectionProvider provider = (ISelectionProvider) editor.getAdapter(ISelectionProvider.class);
	        	if (provider != null) {
	        		provider.setSelection(new StructuredSelection(adapted));
	        	}
	        }
    	}
    	catch (Exception e) {}
    }
}
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

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.AbstractModelCollection;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;

public abstract class BaseSelectionAction extends SelectionAction
{
  public static final String SUBMENU_START_ID = "SUBMENU_START_ID: ";
  public static final String SUBMENU_END_ID = "SUBMENU_END_ID: ";
  
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
}
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

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.w3c.dom.Node;


public class WSDLMenuListener implements IMenuListener
{                      
  protected ISelectionProvider selectionProvider;
  protected ExtensibleMenuActionContributor extensibleMenuActionContributor;

  public WSDLMenuListener(WSDLEditor editor, ISelectionProvider selectionProvider)
  {
    super();                                                               
    this.extensibleMenuActionContributor = new ExtensibleMenuActionContributor(editor);
    this.selectionProvider = selectionProvider;              
  }
      
  protected Object getSelectedObject()
  {      
    ISelection selection = selectionProvider.getSelection();    
    return selection.isEmpty() ? null : ((IStructuredSelection)selection).getFirstElement();   
  }

  public void menuAboutToShow(IMenuManager manager)
  {                                                                                         
    Object object = getSelectedObject();
    Node node = null;
    if (object instanceof Node)
    {
      node = (Node)object;
    }
    else if (object instanceof WSDLElement)
    {
      node = ((WSDLElement)object).getElement();   
    }                                                                            
    else if (object instanceof WSDLGroupObject)
    {
      node =  ((WSDLGroupObject)object).getDefinition().getElement();
    }
    extensibleMenuActionContributor.contributeMenuActions(manager, node, object);
  }
}
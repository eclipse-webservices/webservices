/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.outline;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDMultiPageEditor;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;

public class ASDContentOutlinePage extends ContentOutlinePage {
	protected ASDMultiPageEditor wsdlEditor;
	protected IDescription model;
	protected ITreeContentProvider contentProvider;
	protected ILabelProvider labelProvider;
	protected IMenuListener menuListener;
	
	public ASDContentOutlinePage(ASDMultiPageEditor editor, IMenuListener menuListener) {
		wsdlEditor = editor;
		this.menuListener = menuListener;
	}
	
	public TreeViewer getTreeViewer() {
		return super.getTreeViewer();
	}
	
	public void setContentProvider(ITreeContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}	
	
	public void setLabelProvider(ILabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}
	
	public void setModel(IDescription model) {
		this.model = model;
	}
	
	public void createControl(Composite parent) {                                                 
		super.createControl(parent);                 
		
		getTreeViewer().setContentProvider(contentProvider);
		getTreeViewer().setLabelProvider(labelProvider);
		getTreeViewer().setInput(model);
		getTreeViewer().addSelectionChangedListener(this);
		
		MenuManager menuManager = new MenuManager("#popup");//$NON-NLS-1$
		menuManager.setRemoveAllWhenShown(true);
		Menu menu = menuManager.createContextMenu(getTreeViewer().getControl());
		getTreeViewer().getControl().setMenu(menu);
		menuManager.addMenuListener(menuListener);
		
	    getSite().registerContextMenu("org.eclipse.wst.wsdl.ui.popup.outline", menuManager, wsdlEditor.getSelectionManager()); //$NON-NLS-1$
		  PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),ASDEditorCSHelpIds.WSDL_OUTLINE_VIEW); 
	}
	
	protected boolean processingSelectionChange = false;
	
	public void selectionChanged(SelectionChangedEvent event) {
		if (!processingSelectionChange) {
			processingSelectionChange = true;
			super.selectionChanged(event);
			Object selection = null;
			
			if (((IStructuredSelection) event.getSelection()).getFirstElement() instanceof EditPart){
				EditPart newEditPart = (EditPart) ((IStructuredSelection) event.getSelection()).getFirstElement();
				selection = newEditPart.getModel();
			}
			else {
				selection = ((IStructuredSelection) event.getSelection()).getFirstElement();
			}
	
			if (selection != null) {
				setSelection(new StructuredSelection(selection));
			}
			processingSelectionChange = false;
		}
	}
}
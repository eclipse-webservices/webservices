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
package org.eclipse.wst.wsdl.ui.internal.asd.outline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDMultiPageEditor;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObjectListener;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.INamedObject;

public class ASDContentOutlineProvider implements ITreeContentProvider, ILabelProvider, IASDObjectListener {
	protected Viewer viewer;
	protected ASDMultiPageEditor editor;
	protected List listeners = new ArrayList();
	protected IDescription description;
	
	public ASDContentOutlineProvider(ASDMultiPageEditor editor, IDescription description) {
		this.editor = editor;
		this.description = description;
	}
	
	public Object[] getChildren(Object parentElement) {
		attachListener(parentElement);
		if (parentElement instanceof ITreeElement) {
			return ((ITreeElement) parentElement).getChildren();
		}

		return new Object[0]; 
	}

	public Object getParent(Object element) {
		if (element instanceof ITreeElement) {
			return ((ITreeElement) element).getParent();
		}
		
		return null;
	}

	public boolean hasChildren(Object element) {
		if (getChildren(element).length > 0) {
			return true;
		}

		return false;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {
		unattachAllListeners();
		viewer = null;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
		unattachAllListeners();
	}

	public Image getImage(Object element) {
		if (element instanceof ITreeElement) {
			return ((ITreeElement) element).getImage();
		}

		return null;
	}
    
	public String getText(Object element) {
		String text = null;
		if (element instanceof INamedObject) {
			text =  ((INamedObject) element).getName();
		}
		else if (element instanceof ITreeElement) {
			text = ((ITreeElement) element).getText();
		}
		else {
			text = element.toString();
		}
		
		if (text == null) {
			text = ""; //$NON-NLS-1$
		}
		
		return text;
	}

	public void addListener(ILabelProviderListener listener) {

	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {

	}

	protected void attachListener(Object object) {
		if (object instanceof IASDObject && !listeners.contains(object)) {
			((IASDObject) object).registerListener(this);
			listeners.add(object);
		}
	}
	
	protected void unattachAllListeners() {
		Iterator it = listeners.iterator();
		while (it.hasNext()) {
			Object item = it.next();
			((IASDObject) item).unregisterListener(this);
		}
	}

	public void propertyChanged(Object object, String property) {             
		if (viewer != null) {
			viewer.refresh();
		}
	}
}
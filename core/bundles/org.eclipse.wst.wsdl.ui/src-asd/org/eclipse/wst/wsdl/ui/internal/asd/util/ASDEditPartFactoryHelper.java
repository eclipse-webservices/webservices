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
package org.eclipse.wst.wsdl.ui.internal.asd.util;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicalViewer;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.ASDEditPartFactory;

/**
 * @depracated
 */
public class ASDEditPartFactoryHelper {
	private static ASDEditPartFactoryHelper instance;
	
	private EditPartFactory editPartFactory;
	
	public static ASDEditPartFactoryHelper getInstance() {
		if (instance == null) {
			instance = new ASDEditPartFactoryHelper();
		}
		
		return instance;
	}
	
	public EditPartFactory getEditPartFactory() {
		if (editPartFactory == null) {
			editPartFactory = new ASDEditPartFactory();
		}
		return editPartFactory;
	}
	
	public void setEditPartFactory(EditPartFactory factory) {
		editPartFactory = factory;
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Object adapter = editor.getAdapter(GraphicalViewer.class);
		if (adapter instanceof DesignViewGraphicalViewer) {
			((DesignViewGraphicalViewer) adapter).setEditPartFactory(factory);
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - Initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.ui.internal.refactor.actions;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xsd.ui.internal.refactor.wizard.RefactorActionGroup;
import org.eclipse.wst.xsd.ui.internal.refactor.wizard.RefactorGroupActionDelegate;
import org.eclipse.wst.xsd.ui.internal.refactor.wizard.RefactorGroupSubMenu;

public class WSDLRefactorGroupActionDelegate extends RefactorGroupActionDelegate {

	

    public WSDLRefactorGroupActionDelegate() {
		super();
	}

	/**
     * Fills the menu with applicable refactor sub-menues
     * @param menu The menu to fill
     */
	protected void fillMenu(Menu menu) {
		if (fSelection == null) {
			return;
		}
		if (workbenchPart != null) {
			IWorkbenchPartSite site = workbenchPart.getSite();
			if (site == null)
				return;

			IEditorPart editor = site.getPage().getActiveEditor();
			if (editor != null) {
				IEditorInput editorInput = editor.getEditorInput();
				if(editorInput instanceof IFileEditorInput){
					IFileEditorInput fileInput = (IFileEditorInput)editorInput;
					Definition definition = createDefinition(fileInput.getFile(), resourceSet);
					RefactorActionGroup refactorMenuGroup = new WSDLRefactorActionGroup(fSelection, definition);
					RefactorGroupSubMenu subMenu = new RefactorGroupSubMenu(refactorMenuGroup);
					subMenu.fill(menu, -1);
				}
				
			}
		
		}

	}
	
	
public static Definition createDefinition(IFile file, ResourceSet resourceSet) {
		
		URI uri = URI.createFileURI(file.getLocation().toString());
		Definition definition = WSDLFactory.eINSTANCE.createDefinition();		
		// we need this model to be able to get locations
		try {
			definition.setDocumentBaseURI(uri.toString());
			IStructuredModel structuredModel = StructuredModelManager
					.getModelManager().getModelForRead(file);
			IDOMModel domModel = (IDOMModel) structuredModel;
			WSDLResourceFactoryImpl resourceFactory = new WSDLResourceFactoryImpl();
			Resource wsdlResource = resourceFactory.createResource(uri);
			wsdlResource.setURI(uri);
			if(resourceSet != null){
				resourceSet.getResources().add(wsdlResource);
			}
			definition.setElement(domModel.getDocument()
					.getDocumentElement());
		} catch (IOException e) {
			// do nothing
		} catch (CoreException e) {
			// do nothing
		}
		return definition;
	}


	
}

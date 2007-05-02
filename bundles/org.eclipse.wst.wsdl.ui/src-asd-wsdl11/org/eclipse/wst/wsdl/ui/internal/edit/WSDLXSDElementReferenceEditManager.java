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
package org.eclipse.wst.wsdl.ui.internal.edit;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Type;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDElementDeclarationCommand;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLSetComponentHelper;
import org.eclipse.wst.xsd.ui.internal.editor.XSDElementReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.search.IXSDSearchConstants;
import org.eclipse.xsd.XSDSchema;

public class WSDLXSDElementReferenceEditManager extends	XSDElementReferenceEditManager {
	public WSDLXSDElementReferenceEditManager(IFile currentFile, XSDSchema[] schemas) {
		super(currentFile, schemas);
	}
	
	public WSDLXSDElementReferenceEditManager(IFile currentFile, XSDSchema[] schemas, IDescription description) {
		super(currentFile, null);
		if (schemas == null || schemas.length == 0) {
			setSchemas(getInlineSchemas(description));
		}
	}

	public void modifyComponentReference(Object referencingObject, ComponentSpecification component) {
		if (referencingObject instanceof Adapter) {
			Adapter adapter = (Adapter) referencingObject;
			referencingObject = adapter.getTarget();
		}
		
		if (referencingObject instanceof Part) {
			Part part = (Part) referencingObject;
			IFile file = null;
			if (ASDEditorPlugin.getActiveEditor().getEditorInput() instanceof IFileEditorInput) {
				file = ((IFileEditorInput) ASDEditorPlugin.getActiveEditor().getEditorInput()).getFile();
			}
			
			if (component.isNew()) {  
				if (component.getMetaName() == IXSDSearchConstants.ELEMENT_META_NAME) {
					AddXSDElementDeclarationCommand command = new AddXSDElementDeclarationCommand(part.getEnclosingDefinition(), component.getName());
					command.run();
					String tns = command.getXSDElement().getTargetNamespace();
					component.setQualifier(tns);
				}
			}
			
			WSDLSetComponentHelper helper = new WSDLSetComponentHelper(file, part.getEnclosingDefinition());
			helper.setXSDElementComponent(part, component);
		}
	}

	public ComponentSpecification[] getQuickPicks() {
//		ElementComponentList list = new ElementComponentList();
//		
//		if (ASDEditorPlugin.getActiveEditor().getEditorInput() instanceof IFileEditorInput) {
//			IFile file = ((IFileEditorInput) ASDEditorPlugin.getActiveEditor().getEditorInput()).getFile();
//			XSDElementsSearchListProvider provider = new XSDElementsSearchListProvider(file, schemas);
//			provider.populateComponentList(list, null, null);
//		}
//
//		ComponentSpecification specs[] = new ComponentSpecification[list.size()];
//		for (int index = 0; index < list.size(); index++) {
//			specs[index] = (ComponentSpecification) list.get(index);
//		}
		
		return new ComponentSpecification[0];
	}
	
	public void setSchemas(XSDSchema[] schemas) {
		this.schemas = schemas;
	}
	
	private XSDSchema[] getInlineSchemas(IDescription description) {
		List types = description.getTypes();
		XSDSchema[] schemas = new XSDSchema[types.size()];
		for (int index = 0; index < types.size(); index++) {
			W11Type type = (W11Type) types.get(index);
			schemas[index] = (XSDSchema) type.getTarget();
		}
		
		return schemas;
	}
}

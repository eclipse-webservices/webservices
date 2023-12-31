/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDTypeDefinitionCommand;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLSetComponentHelper;
import org.eclipse.wst.xsd.ui.internal.adt.edit.IComponentDialog;
import org.eclipse.wst.xsd.ui.internal.dialogs.NewTypeDialog;
import org.eclipse.wst.xsd.ui.internal.editor.XSDTypeReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.search.IXSDSearchConstants;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;

public class WSDLXSDTypeReferenceEditManager extends XSDTypeReferenceEditManager {
	
	public WSDLXSDTypeReferenceEditManager(IFile currentFile, XSDSchema[] schemas) {
		super(currentFile, schemas);
	}
	
	public WSDLXSDTypeReferenceEditManager(IFile currentFile, XSDSchema[] schemas, IDescription description) {
		super(currentFile, null);
		if (schemas == null || schemas.length == 0) {
			setSchemas(getInlineSchemas(description));
		}
	}
	
	public IComponentDialog getNewDialog()
	{
		if (schemas.length > 0) {
			NewTypeDialog newTypeDialog = new NewTypeDialog(schemas[0]);
			newTypeDialog.allowAnonymousType(false);
			return newTypeDialog;
		}
		else {
			NewTypeDialog newTypeDialog = new NewTypeDialog();
			newTypeDialog.allowAnonymousType(false);
			return newTypeDialog;
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
				AddXSDTypeDefinitionCommand command = new AddXSDTypeDefinitionCommand(part.getEnclosingDefinition(), component.getName());
				if (component.getMetaName() == IXSDSearchConstants.COMPLEX_TYPE_META_NAME) {
					command.isComplexType(true);
				}
				else {
					command.isComplexType(false);
				}
				command.run();
				String tns = command.getXSDElement().getTargetNamespace();
				component.setQualifier(tns);
			}
			
			WSDLSetComponentHelper helper = new WSDLSetComponentHelper(file, part.getEnclosingDefinition());
			helper.setXSDTypeComponent(part, component);
		}
		else if (referencingObject instanceof XSDElementDeclaration) {
			super.modifyComponentReference(referencingObject, component);
		}
		else if (referencingObject instanceof XSDAttributeUse) {
			
		}
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

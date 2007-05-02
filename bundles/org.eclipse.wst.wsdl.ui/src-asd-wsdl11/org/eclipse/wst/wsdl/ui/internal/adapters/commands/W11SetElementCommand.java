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
package org.eclipse.wst.wsdl.ui.internal.adapters.commands;

import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.InternalWSDLMultiPageEditor;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Type;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.edit.WSDLXSDElementReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.adt.edit.IComponentDialog;
import org.eclipse.wst.xsd.ui.internal.editor.XSDElementReferenceEditManager;
import org.eclipse.xsd.XSDSchema;

public class W11SetElementCommand extends W11TopLevelElementCommand {
	private Part parent;
	private String action;
	private boolean continueApply;

	public W11SetElementCommand(Part parent, String action) {
        super(Messages._UI_ACTION_SET_ELEMENT, parent.getEnclosingDefinition());
		this.parent = parent;
		this.action = action;
	}
	
	public void execute()
	{
		try {
			beginRecording(definition.getElement());

			ComponentReferenceEditManager componentReferenceEditManager = getComponentReferenceEditManager();
			continueApply = true; 
			if (action.equals(IParameter.SET_NEW_ACTION_ID))
			{
				ComponentSpecification newValue = (ComponentSpecification)invokeDialog(componentReferenceEditManager.getNewDialog());

				// Set the reference to the new type
				if (continueApply)
					componentReferenceEditManager.modifyComponentReference(parent, newValue);
			}
			else
			{
				ComponentSpecification newValue = (ComponentSpecification)invokeDialog(componentReferenceEditManager.getBrowseDialog());
				if (continueApply)
					componentReferenceEditManager.modifyComponentReference(parent, newValue);
			}
			formatChild(parent.getElement());
		}
		finally {
			endRecording(definition.getElement());
		}
	}
	
	private Object invokeDialog(IComponentDialog dialog)
	{
		Object newValue = null;
		
		if (dialog == null)
		{
			return null;
		}
		
		if (dialog.createAndOpen() == Window.OK)
		{
			newValue = dialog.getSelectedComponent();
		}
		else
		{
			continueApply = false;
		}
		
		return newValue;
	}
	
	protected ComponentReferenceEditManager getComponentReferenceEditManager()
	{
		ComponentReferenceEditManager result = null;
		IEditorPart editor = ASDEditorPlugin.getActiveEditor();
		if (editor != null)
		{
			result = (ComponentReferenceEditManager)editor.getAdapter(XSDElementReferenceEditManager.class);
			
			if (editor instanceof InternalWSDLMultiPageEditor)
			{
				InternalWSDLMultiPageEditor wsdlEditor = (InternalWSDLMultiPageEditor) editor;
				
				List types = wsdlEditor.getModel().getTypes();
				XSDSchema[] schemas = new XSDSchema[types.size()];
				for (int index = 0; index < types.size(); index++) {
					W11Type type = (W11Type) types.get(index);
					schemas[index] = (XSDSchema) type.getTarget();
				}
				
				((WSDLXSDElementReferenceEditManager) result).setSchemas(schemas);
				
				return result;
			}
		}  
		return result;
	}
}

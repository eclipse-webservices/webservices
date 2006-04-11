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
package org.eclipse.wst.wsdl.ui.internal.adapters.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatProcessor;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.InternalWSDLMultiPageEditor;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Type;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.edit.WSDLXSDTypeReferenceEditManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.adt.edit.IComponentDialog;
import org.eclipse.wst.xsd.ui.internal.editor.XSDTypeReferenceEditManager;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Element;

public class W11SetTypeCommand extends Command {
	private Object parent;
	private String action;
	private boolean continueApply;
	
	public W11SetTypeCommand(Object parent, String action) {
		this.parent = parent;
		this.action = action;
	}
	
	public void execute()
	{
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
		
		// Format
		if (parent instanceof WSDLElement) {
			formatChild(((WSDLElement) parent).getElement());
		}
		else if (parent instanceof XSDConcreteComponent) {
			formatChild(((XSDConcreteComponent) parent).getElement());
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
			result = (ComponentReferenceEditManager)editor.getAdapter(XSDTypeReferenceEditManager.class);
			
			if (editor instanceof InternalWSDLMultiPageEditor)
			{
				InternalWSDLMultiPageEditor wsdlEditor = (InternalWSDLMultiPageEditor) editor;
				
				List types = wsdlEditor.getModel().getTypes();
				XSDSchema[] schemas = new XSDSchema[types.size()];
				for (int index = 0; index < types.size(); index++) {
					W11Type type = (W11Type) types.get(index);
					schemas[index] = (XSDSchema) type.getTarget();
				}
				
				((WSDLXSDTypeReferenceEditManager) result).setSchemas(schemas);
				
				return result;
			}
		}  
		return result;
	}
	
	protected void formatChild(Element child)
	{
		if (child instanceof IDOMNode)
		{
			IDOMModel model = ((IDOMNode)child).getModel();
			try
			{
				// tell the model that we are about to make a big model change
				model.aboutToChangeModel();
				
				IStructuredFormatProcessor formatProcessor = new FormatProcessorXML();
				formatProcessor.formatNode(child);
			}
			finally
			{
				// tell the model that we are done with the big model change
				model.changedModel(); 
			}
		}
	}
}

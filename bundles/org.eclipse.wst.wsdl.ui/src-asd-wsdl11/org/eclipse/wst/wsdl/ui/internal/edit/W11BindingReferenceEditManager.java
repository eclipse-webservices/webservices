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
package org.eclipse.wst.wsdl.ui.internal.edit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.common.ui.internal.search.dialogs.IComponentDescriptionProvider;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.asd.editor.ASDEditorPlugin;
import org.eclipse.wst.wsdl.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11EndPoint;
import org.eclipse.wst.wsdl.ui.internal.dialogs.W11BrowseComponentDialog;
import org.eclipse.wst.wsdl.ui.internal.dialogs.W11NewComponentDialog;
import org.eclipse.wst.wsdl.ui.internal.search.IWSDLSearchConstants;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLSetComponentHelper;
import org.eclipse.wst.xsd.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.adt.edit.IComponentDialog;

public class W11BindingReferenceEditManager implements ComponentReferenceEditManager {
	protected W11Description description;
	protected IFile iFile;
	
	public W11BindingReferenceEditManager(W11Description description, IFile iFile) {
		this.description = description;
		this.iFile = iFile;
	}
	
	public IComponentDialog getBrowseDialog() {
		return new W11BrowseComponentDialog(IWSDLSearchConstants.BINDING_META_NAME, iFile, description);
	}

	public IComponentDialog getNewDialog() {
		return new W11NewComponentDialog(IWSDLSearchConstants.BINDING_META_NAME, iFile, description);
	}
	
	private Definition getDefinition() {
		return (Definition) description.getTarget();
	}

	public void modifyComponentReference(Object referencingObject, ComponentSpecification referencedComponent) {
		W11EndPoint w11EndPoint = (W11EndPoint) referencingObject;
		Object bindingObject = referencedComponent.getObject();
		if (bindingObject == null) {
			// Need to figure out the IBinding based on the information contained in the ComponentSpecification

		}
		
		if (bindingObject instanceof ComponentSpecification) {
			Port port = (Port) w11EndPoint.getTarget();
			WSDLSetComponentHelper helper = new WSDLSetComponentHelper(iFile, getDefinition());
			helper.setWSDLComponent(port, "binding", (ComponentSpecification) bindingObject);
		}		
		else if (bindingObject instanceof IBinding){
			IBinding binding = (IBinding) bindingObject;
			Command command = w11EndPoint.getSetBindingCommand(binding);
			CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
			stack.execute(command);
		}
	}

	public IComponentDescriptionProvider getComponentDescriptionProvider() {
		return null;
	}

	public ComponentSpecification[] getQuickPicks() {
		List specList = new ArrayList();
		
		Iterator bindings = description.getBindings().iterator();
		while (bindings.hasNext()) {
			IBinding binding = (IBinding) bindings.next();
			String qualifier = "";
			String name = binding.getName();
			IFile file = null;
			
			ComponentSpecification spec = new ComponentSpecification(qualifier, name, file);
			spec.setObject(binding);
			specList.add(spec);
		}

		ComponentSpecification[] specArray = new ComponentSpecification[specList.size()];
		specList.toArray(specArray);
		
		return specArray;
	}

	public ComponentSpecification[] getHistory() {
		ComponentSpecification[] history = new ComponentSpecification[0];
		return history;
	}

	public void addToHistory(ComponentSpecification component) {

	}
}

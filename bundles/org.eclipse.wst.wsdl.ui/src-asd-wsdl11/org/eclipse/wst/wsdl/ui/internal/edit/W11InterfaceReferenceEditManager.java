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
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.common.ui.internal.search.dialogs.IComponentDescriptionProvider;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Binding;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.dialogs.W11BrowseComponentDialog;
import org.eclipse.wst.wsdl.ui.internal.dialogs.W11NewComponentDialog;
import org.eclipse.wst.wsdl.ui.internal.search.IWSDLSearchConstants;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLSetComponentHelper;
import org.eclipse.wst.xsd.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.adt.edit.IComponentDialog;

public class W11InterfaceReferenceEditManager implements ComponentReferenceEditManager {
	protected W11Description description;
	protected IFile iFile;
	
	public W11InterfaceReferenceEditManager(W11Description description, IFile iFile) {
		this.description = description;
		this.iFile = iFile;
	}
	
	public IComponentDialog getBrowseDialog() {
		return new W11BrowseComponentDialog(IWSDLSearchConstants.PORT_TYPE_META_NAME, iFile, description);
	}

	public IComponentDialog getNewDialog() {
		return new W11NewComponentDialog(IWSDLSearchConstants.PORT_TYPE_META_NAME, iFile, description);
	}

	private Definition getDefinition() {
		return (Definition) description.getTarget();
	}
	
	public void modifyComponentReference(Object referencingObject, ComponentSpecification referencedComponent) {
		W11Binding w11Binding = (W11Binding) referencingObject;
		Object interfaceObject = referencedComponent.getObject();
		if (interfaceObject == null) {
			// Need to figure out the IInterface based on the information contained in the ComponentSpecification

		}
		
		if (interfaceObject instanceof ComponentSpecification) {
			Binding binding = (Binding) w11Binding.getTarget();
			WSDLSetComponentHelper helper = new WSDLSetComponentHelper(iFile, getDefinition());
			helper.setWSDLComponent(binding, "type", (ComponentSpecification) interfaceObject);
		}
		else if (interfaceObject instanceof IInterface){
			IInterface interfaze = (IInterface) interfaceObject;
			Command command = w11Binding.getSetInterfaceCommand(interfaze);
			CommandStack stack = (CommandStack) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getAdapter(CommandStack.class);
			stack.execute(command);
		}
	}

	public IComponentDescriptionProvider getComponentDescriptionProvider() {

		return null;
	}

	public ComponentSpecification[] getQuickPicks() {
		List specList = new ArrayList();
		
		Iterator interfaces = description.getInterfaces().iterator();
		while (interfaces.hasNext()) {
			IInterface interfaze = (IInterface) interfaces.next();
			String qualifier = "";
			String name = interfaze.getName();
			IFile file = null;
			
			ComponentSpecification spec = new ComponentSpecification(qualifier, name, file);
			spec.setObject(interfaze);
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

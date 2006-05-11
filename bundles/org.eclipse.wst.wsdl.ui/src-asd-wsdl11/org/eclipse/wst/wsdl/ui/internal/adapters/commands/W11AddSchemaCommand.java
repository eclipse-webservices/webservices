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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDSchemaCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;

public class W11AddSchemaCommand extends Command {
	private Definition definition;
	
	public W11AddSchemaCommand(Definition definition) {
        super(Messages.getString("_UI_ACTION_ADD_SCHEMA"));
		this.definition = definition;
	}
	
	public void execute() {
		String tns = definition.getTargetNamespace();
		List existingNamespaces = new ArrayList();
		Iterator eeIt = definition.getETypes().getEExtensibilityElements().iterator();
		while (eeIt.hasNext()) {
			Object item = eeIt.next();
			if (item instanceof XSDSchemaExtensibilityElement) {
				String ns = ((XSDSchemaExtensibilityElement) item).getSchema().getTargetNamespace();
				existingNamespaces.add(ns);
			}
		}
		tns = NameUtil.getUniqueNameHelper(tns, existingNamespaces);
		
		AddXSDSchemaCommand command = new AddXSDSchemaCommand(definition, tns);
		command.run();
		selectNewElement(command.getWSDLElement());
	}
	
    // TODO: We should probably be selecting the new element at the "action level"....  However, our actions
    // are currently very generic, so we have no way of getting to the newly created element.  The action
    // only sees these commands as generic Command objects.
    private void selectNewElement(Notifier element) {
    	try {
	    	Object adapted = WSDLAdapterFactoryHelper.getInstance().adapt(element);
	        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	        if (editor != null && editor.getAdapter(ISelectionProvider.class) != null) {
	        	ISelectionProvider provider = (ISelectionProvider) editor.getAdapter(ISelectionProvider.class);
	        	if (provider != null) {
	        		provider.setSelection(new StructuredSelection(adapted));
	        	}
	        }
    	}
    	catch (Exception e) {}
    }
}
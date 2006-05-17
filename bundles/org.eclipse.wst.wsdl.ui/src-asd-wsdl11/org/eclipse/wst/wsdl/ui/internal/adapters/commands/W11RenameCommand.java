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

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.actions.SmartRenameAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Binding;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11BindingMessageReference;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11BindingOperation;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11EndPoint;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Interface;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Message;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11MessageReference;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Operation;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForPart;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Service;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.ui.internal.visitor.BindingRenamer;
import org.eclipse.wst.wsdl.ui.internal.visitor.MessageRenamer;
import org.eclipse.wst.wsdl.ui.internal.visitor.PortTypeRenamer;
import org.w3c.dom.Element;

public class W11RenameCommand extends Command {
	protected WSDLBaseAdapter object;
	protected String newName = ""; //$NON-NLS-1$

	public W11RenameCommand(WSDLBaseAdapter object, String newName) {
		super(Messages.getString("_UI_ACTION_RENAME"));
		this.object = object;
		this.newName = newName;
	}

	public void execute() {
		if (object instanceof W11Description) {
			Definition definition = (Definition) object.getTarget();
			W11TopLevelElementCommand.ensureDefinition(definition);
			String ns = definition.getQName().getNamespaceURI();
			definition.setQName(new QName(ns, newName));
		}
		else if (object instanceof W11Service) {
			Service service = (Service) object.getTarget();
			String ns = service.getQName().getNamespaceURI();
			service.setQName(new QName(ns, newName));
		}
		else if (object instanceof W11EndPoint) {
			Port port = (Port) object.getTarget();
			port.setName(newName);
		}
		else if (object instanceof W11Binding) {
			Binding binding = (Binding) object.getTarget();
			String ns = binding.getQName().getNamespaceURI();
			binding.setQName(new QName(ns, newName));
		}
		else if (object instanceof W11Interface) {
			PortType portType = (PortType) object.getTarget();
			String ns = portType.getQName().getNamespaceURI();
			portType.setQName(new QName(ns, newName));
		}
		else if (object instanceof W11Operation) {
			Operation operation = (Operation) object.getTarget();
			SmartRenameAction action = new SmartRenameAction(operation, newName);
			renameOperationHelper(operation);
			action.run();
		}
		else if (object instanceof W11MessageReference) {
			WSDLElement element = (WSDLElement) ((W11MessageReference) object).getTarget();
			renameIOFHelper(element);
		}
		else if (object instanceof W11Message) {
			Message message = (Message) ((W11Message) object).getTarget();
			SmartRenameAction action = new SmartRenameAction(message, newName);
			action.run();			
		}
		else if (object instanceof W11ParameterForPart) {
			Part part = (Part) ((W11ParameterForPart) object).getTarget();
			SmartRenameAction action = new SmartRenameAction(part, newName);
			action.run();
		}
		else if (object instanceof W11BindingOperation) {
			BindingOperation bindingOperation = (BindingOperation) ((W11BindingOperation) object).getTarget();
			bindingOperation.setName(newName);
			bindingOperation.getEOperation().setName(newName);
		}
		else if (object instanceof W11BindingMessageReference) {
			Object bindingMessageRef = ((W11BindingMessageReference) object).getTarget();
			if (bindingMessageRef instanceof BindingInput) {
				((BindingInput) bindingMessageRef).setName(newName);
				((BindingInput) bindingMessageRef).getEInput().setName(newName);
			}
			else if (bindingMessageRef instanceof BindingOutput) {
				((BindingOutput) bindingMessageRef).setName(newName);
				((BindingOutput) bindingMessageRef).getEOutput().setName(newName);
			}
			else if (bindingMessageRef instanceof BindingFault) {
				((BindingFault) bindingMessageRef).setName(newName);
				((BindingFault) bindingMessageRef).getEFault().setName(newName);
			}
		}
	}

	// TODO: We should look at using the refactor mechanism to do this renaming....
	private void renameOperationHelper(Operation operation) {
		Definition definition = operation.getEnclosingDefinition(); 
		ComponentReferenceUtil util = new ComponentReferenceUtil(definition);
		Iterator it = util.getBindingOperations(operation).iterator();

		while (it.hasNext()) {
			BindingOperation bindingOperation = (BindingOperation) it.next();
			Element element = WSDLEditorUtil.getInstance().getElementForObject(bindingOperation);
			if (element != null)
			{ 
				element.setAttribute("name", newName);
			}
			//		    TODO: We should go through the model to rename.... Not the Element...
			//			bindingOperation.setName(newName);
		}
	}

	// TODO: We should look at using the refactor mechanism to do this renaming....
	private void renameIOFHelper(WSDLElement object)
	{                         
		Definition definition = object.getEnclosingDefinition(); 
		ComponentReferenceUtil util = new ComponentReferenceUtil(definition);
		List list = null;

		if (object instanceof Input)
		{
			list = util.getBindingInputs((Input)object);
		}
		else if (object instanceof Output)
		{
			list = util.getBindingOutputs((Output)object);
		}
		else // fault
		{
			list = util.getBindingFaults((Fault)object);
		}

		if (list != null)
		{
			for (Iterator i = list.iterator(); i.hasNext(); )
			{
				Object bindingObject = i.next();
				renameModelObjectHelper(bindingObject, newName);
			}                                           
		}

		renameModelObjectHelper(object, newName);
	}

	// TODO: We should look at using the refactor mechanism to do this renaming....
	private void renameModelObjectHelper(Object modelObject, String theNewName)
	{
		// TODO: We should go through the model to rename.... Not the Element...
		Element element = WSDLEditorUtil.getInstance().getElementForObject(modelObject);
		if (element != null)
		{ 
			element.setAttribute("name", theNewName);
		}    

		if (modelObject instanceof Message)
		{
			MessageRenamer renamer = new MessageRenamer((Message)modelObject, theNewName);
			renamer.visitBindings();
		}
		else if (modelObject instanceof PortType)
		{
			PortTypeRenamer renamer = new PortTypeRenamer((PortType)modelObject, theNewName);
			renamer.visitBindings();
		}
		else if (modelObject instanceof Binding)
		{
			BindingRenamer renamer = new BindingRenamer((Binding)modelObject, theNewName);
			renamer.visitServices();
		}
	}
}
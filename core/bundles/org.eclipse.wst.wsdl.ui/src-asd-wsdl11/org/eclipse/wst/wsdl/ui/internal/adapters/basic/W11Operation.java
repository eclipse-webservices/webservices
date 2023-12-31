/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.adapters.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.OperationType;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.ui.internal.DefaultEditorMode;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.OpenInNewEditor;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddFaultParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddInputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddOutputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11ReorderMessageReferencesCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddFaultAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddInputAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddOperationAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddOutputAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.commands.AddBaseParameterCommand;
import org.eclipse.wst.xsd.ui.internal.adt.editor.EditorModeManager;

public class W11Operation extends WSDLBaseAdapter implements IOperation {
	public List getMessages()
	{
		List modelAdapterList = new ArrayList(getOperation().getEFaults().size() + 2);
		
		Input input = getOperation().getEInput();
		Output output = getOperation().getEOutput();
		
		OperationType operationType = getOperation().getStyle();
		if (operationType != null) {
			if (operationType.equals(OperationType.REQUEST_RESPONSE) && input != null && output != null) {
				// Input, Output
				modelAdapterList.add(createAdapter(input));
				modelAdapterList.add(createAdapter(output));
			}
			else if (operationType.equals(OperationType.SOLICIT_RESPONSE) && input != null && output != null) {
				// Output, Input
				modelAdapterList.add(createAdapter(output));
				modelAdapterList.add(createAdapter(input));
			}
			else if (operationType.equals(OperationType.ONE_WAY) && input != null) {
				// Input
				modelAdapterList.add(createAdapter(input));
			}
			else if (operationType.equals(OperationType.NOTIFICATION) && output != null) {
				//Output
				modelAdapterList.add(createAdapter(output));
			}
		}
		
		for (Iterator i = getOperation().getEFaults().iterator(); i.hasNext();)	{
			EObject o = (EObject) i.next();
			modelAdapterList.add(createAdapter(o));
		}

		return modelAdapterList;
	}
	
	// Convienence method
	public Operation getOperation()
	{
		return (Operation) target;
	}
	
	public String getName()
	{
		return getOperation().getName();
	}
	
	public IInterface getOwnerInterface()
	{
		return (IInterface)owner;
	}
	
	public String[] getActions(Object object) {
		List actions = new ArrayList();
		actions.add(ASDAddOperationAction.ID);
		actions.addAll(getValidInputOutpuActions());
		actions.add(ASDAddFaultAction.ID);
		actions.add(ASDDeleteAction.ID);
    if (isReadOnly()) {
      actions.add(OpenInNewEditor.ID);
    }
		
		String[] actionIDs = new String[actions.size()];
		for (int index = 0; index < actions.size(); index++) {
			actionIDs[index] = (String) actions.get(index);
		}
		
		return actionIDs;
	}
	
	public Command getAddInputCommand() {
		W11AddInputParameterCommand command = new W11AddInputParameterCommand(getOperation());
		
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        EditorModeManager manager = (EditorModeManager) editor.getAdapter(EditorModeManager.class);
		if (manager.getCurrentMode().getId() != DefaultEditorMode.class.getName()) {
			command.setParameterPattern(AddBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT);
		}

		return command;
	}
	
	public Command getAddOutputCommand() {
		W11AddOutputParameterCommand command = new W11AddOutputParameterCommand(getOperation());
		
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        EditorModeManager manager = (EditorModeManager) editor.getAdapter(EditorModeManager.class);
		if (manager.getCurrentMode().getId() != DefaultEditorMode.class.getName()) {
			command.setParameterPattern(AddBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT);
		}

		return command;
	}
	
	public Command getAddFaultCommand(Object fault) {
		W11AddFaultParameterCommand command = new W11AddFaultParameterCommand(getOperation(), null);
		
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        EditorModeManager manager = (EditorModeManager) editor.getAdapter(EditorModeManager.class);
		if (manager.getCurrentMode().getId() != DefaultEditorMode.class.getName()) {
			command.setParameterPattern(AddBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT);
		}

		return command;
	}
	
	public Command getReorderMessageReferencesCommand(IMessageReference leftSibling, IMessageReference rightSibling, IMessageReference movingMessageRef) {
		return new W11ReorderMessageReferencesCommand(leftSibling, rightSibling, movingMessageRef);
	}
	
	public List getValidInputOutpuActions() {
		List list = new ArrayList();
		
		if (getOperation().getEInput() == null) {
			list.add(ASDAddInputAction.ID);
		}
		if (getOperation().getEOutput() == null) {
			list.add(ASDAddOutputAction.ID);
		}
		
		return list;
	}
	
	public Image getImage() {
		return WSDLEditorPlugin.getInstance().getImage("icons/operation_obj.gif"); //$NON-NLS-1$
	}
	
	public String getText() {
		return "operation"; //$NON-NLS-1$
	}
	
	public ITreeElement[] getChildren() {
		List messages = getMessages();
		ITreeElement[] treeElements = new ITreeElement[messages.size()];
		
		for (int index = 0; index < messages.size(); index++) {
			treeElements[index] = (ITreeElement) messages.get(index);
		}
		
		return treeElements;
	}

	public boolean hasChildren() {
		if (getChildren().length > 0) {
			return true;
		}
		
		return false;
	}

	public ITreeElement getParent() {
		return null;
	}
}

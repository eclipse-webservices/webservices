/*******************************************************************************
 * Copyright (c) 2001, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.properties.sections;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetExistingInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetNewInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension.ContentGeneratorUIExtension;
import org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension.ContentGeneratorUIExtensionRegistry;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.util.ReferenceEditManagerHelper;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;

public class BindingSection extends ReferenceSection implements SelectionListener {
	protected ComponentReferenceEditManager refManager;
	private CLabel protocolValue;
//	private CLabel optionsValue;
	private Button regenBindingButton;
	
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory) {
		super.createControls(parent, factory);
		comboLabel.setText(org.eclipse.wst.wsdl.ui.internal.asd.Messages._UI_LABEL_PORTTYPE);
		
		CLabel protocolLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_BINDING_PROTOCOL_IN_PROPERTIES_SECTION);
		protocolValue= getWidgetFactory().createCLabel(composite, ""); //$NON-NLS-1$
		
		// Layout protocolLabel
		GridData data = new GridData();
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
	    data.grabExcessHorizontalSpace = false;
		protocolLabel.setLayoutData(data);
		
		// Layout protocolValue
		protocolValue.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		/*
		CLabel optionsLabel = getWidgetFactory().createCLabel(composite, Messages.getString("_UI_TITLE_OPTIONS") + ":");
		optionsValue = getWidgetFactory().createCLabel(composite, "");
		
		// Layout optionsLabel
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(optionsValue, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(optionsValue, 0, SWT.CENTER);
		optionsLabel.setLayoutData(data);
		
		// Layout optionsValue
		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(protocolValue, -ITabbedPropertyConstants.VSPACE);
//		data.top = new FormAttachment(protocolValue, +ITabbedPropertyConstants.VSPACE);
		optionsValue.setLayoutData(data);
		
		String buttonLabel = org.eclipse.wst.wsdl.ui.internal.asd.Messages.getString("_UI_GENERATE_BINDING_CONTENT");
		regenBindingButton = getWidgetFactory().createButton(composite, buttonLabel, SWT.PUSH);

		// Layout button
		data = new FormData();
		data.left = new FormAttachment(0, 0);
//		data.right = new FormAttachment(optionsValue, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(optionsLabel, 0);
		regenBindingButton.setLayoutData(data);
		*/

		String buttonLabel = org.eclipse.wst.wsdl.ui.internal.asd.Messages._UI_GENERATE_BINDING_CONTENT;
		regenBindingButton = getWidgetFactory().createButton(composite, buttonLabel, SWT.PUSH);

		// Layout button
		data = new GridData();
		data.horizontalSpan = 2;
		data.horizontalAlignment = GridData.BEGINNING;
		regenBindingButton.setLayoutData(data);

		regenBindingButton.addSelectionListener(this);
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(protocolValue, ASDEditorCSHelpIds.PROPERTIES_BINDING_PROTOCOL_TEXT);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(regenBindingButton, ASDEditorCSHelpIds.PROPERTIES_BINDING_GEN_BINDING_BUTTON);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(combo, ASDEditorCSHelpIds.PROPERTIES_BINDING_PORTTYPE_COMBO);
	}
	
	public void refresh() {
		super.refresh();
		IBinding binding = getIBinding();
		String protocol = binding.getProtocol();
		ContentGeneratorUIExtensionRegistry registry = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry();
		ContentGeneratorUIExtension ext = registry.getExtensionForName(protocol);
		if (ext != null) {
		  protocol = ext.getLabel();
		}

		if (protocol == null || protocol.equals("")) { //$NON-NLS-1$
			protocol = "----"; //$NON-NLS-1$
		}
		protocolValue.setText(protocol);

//		optionsValue.setText("");
	}
	
	protected ComponentReferenceEditManager getComponentReferenceEditManager() {
		if (refManager != null) {
			return refManager;
		}

		refManager = ReferenceEditManagerHelper.getInterfaceReferenceEditManager((IASDObject) getModel());
		
		return refManager;
	}

	protected List getComboItems() {
		ComponentReferenceEditManager manager = getComponentReferenceEditManager();
		
		List items = new ArrayList();
		items.add(BROWSE_STRING);
		items.add(NEW_STRING);

		if (manager != null) {
  		ComponentSpecification[] comboItems = manager.getQuickPicks();
      for (int index = 0; index < comboItems.length; index++)
      {
        items.add(comboItems[index]);
      }
		}
		return items;
	}

	protected Object getCurrentComboItem() {
		IBinding binding = getIBinding();
		return binding.getInterface();
	}

	protected String getComboItemName(Object item) {
		String name = ""; //$NON-NLS-1$
		if (item instanceof ComponentSpecification) {
			name = ((ComponentSpecification) item).getName();
		}
		else if (item instanceof IInterface) {
			name = ((IInterface) item).getName();
		}
		else if (item instanceof String) {
			name = (String) item;
		}
		
		return name;
	}

	protected void performComboSelection(Object item) {
		ComponentSpecification spec = null;
		
		if (item instanceof ComponentSpecification) {
			spec = (ComponentSpecification) item;
			ComponentReferenceEditManager manager = getComponentReferenceEditManager();
			manager.modifyComponentReference((IBinding) getModel(), spec);
		}
		else if (item instanceof String) {
			if (item.equals(BROWSE_STRING)) {
				IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
				ASDSetExistingInterfaceAction action = new ASDSetExistingInterfaceAction(part);
				action.setIBinding((IBinding) getModel());
				action.run();
			}
			else if (item.equals(NEW_STRING)) {
				IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
				ASDSetNewInterfaceAction action = new ASDSetNewInterfaceAction(part);
				action.setIBinding((IBinding) getModel());
				action.run();
			}
		}

		refresh();
	}

	private IBinding getIBinding() {
		return (IBinding) getModel();
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.widget == regenBindingButton) {
			Command command = getIBinding().getGenerateBindingCommand();
		    CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
		    stack.execute(command);
		}
	}
	
	public void doWidgetSelected(SelectionEvent e) {
	}	
}

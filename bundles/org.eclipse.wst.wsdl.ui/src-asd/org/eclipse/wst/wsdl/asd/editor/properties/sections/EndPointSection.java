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
package org.eclipse.wst.wsdl.asd.editor.properties.sections;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.asd.editor.ASDEditorPlugin;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDSetExistingBindingAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDSetNewBindingAction;
import org.eclipse.wst.wsdl.asd.facade.IBinding;
import org.eclipse.wst.wsdl.asd.facade.IEndPoint;
import org.eclipse.wst.wsdl.ui.internal.edit.W11BindingReferenceEditManager;
import org.eclipse.wst.xsd.adt.edit.ComponentReferenceEditManager;

public class EndPointSection extends ReferenceSection {
	protected Text addressText;
	protected CLabel protocolValueLabel;
	
	protected List bindingsInCombo = new ArrayList();
	
	protected ComponentReferenceEditManager refManager;
	
	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		FormData data;
		
		// Address row
		CLabel addressLabel = getWidgetFactory().createCLabel(composite, "Address:"); // TODO: Externalize String
		addressText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(addressText, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(addressText, 0, SWT.CENTER);
		addressLabel.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(combo, +ITabbedPropertyConstants.VSPACE);
		addressText.setLayoutData(data);

		// Protocol Row
		CLabel protocolLabel = getWidgetFactory().createCLabel(composite, "Protocol:"); // TODO: Externalize String
		protocolValueLabel = getWidgetFactory().createCLabel(composite, "----"); //$NON-NLS-1$
		
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(protocolValueLabel, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(protocolValueLabel, 0, SWT.CENTER);
		protocolLabel.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(addressText, +ITabbedPropertyConstants.VSPACE);
		protocolValueLabel.setLayoutData(data);
		protocolValueLabel.addListener(SWT.Modify, this);

		comboLabel.setText("Binding:");
	}

	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh() {
		addressText.removeListener(SWT.Modify, this);

		super.refresh();

		IEndPoint endPoint = (IEndPoint) getModel();
		if (endPoint.getAddress() != null) {
			addressText.setText(endPoint.getAddress());
		}
		if (endPoint.getBinding() != null) {
			String protocolValue = endPoint.getBinding().getProtocol();
			if (protocolValue.equals("")) {
				protocolValue = "----";
			}
			protocolValueLabel.setText(protocolValue);
		}
		
		addressText.addListener(SWT.Modify, this);
	}
	
	protected List getComboItems() {
		if (refManager == null) {
			IEditorPart editor = ASDEditorPlugin.getActiveEditor();
			// TODO: rmah: We should not know about W11BindingReferenceEditManager here....  We should a better
			// way to retrieve the appropriate Reference Manager
			refManager = (ComponentReferenceEditManager) editor.getAdapter(W11BindingReferenceEditManager.class);
		}
		
		List items = new ArrayList();
		items.add(BROWSE_STRING);
		items.add(NEW_STRING);

		ComponentSpecification[] comboItems = refManager.getQuickPicks();
		for (int index = 0; index < comboItems.length; index++) {
			items.add(comboItems[index]);
		}
		
		return items;
	}
	
	protected Object getCurrentComboItem() {
		IEndPoint endPoint = (IEndPoint) getModel();
		return endPoint.getBinding();
	}
	
	protected String getComboItemName(Object item) {
		String name = "";
		if (item instanceof ComponentSpecification) {
			name = ((ComponentSpecification) item).getName();
		}
		else if (item instanceof IBinding) {
			name = ((IBinding) item).getName();
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
			refManager.modifyComponentReference((IEndPoint) getModel(), spec);
		}
		else if (item instanceof String) {
			if (item.equals(BROWSE_STRING)) {
				IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
				ASDSetExistingBindingAction action = new ASDSetExistingBindingAction(part);
				action.setIEndPoint((IEndPoint) getModel());
				action.run();
			}
			else if (item.equals(NEW_STRING)) {
				IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
				ASDSetNewBindingAction action = new ASDSetNewBindingAction(part);
				action.setIEndPoint((IEndPoint) getModel());
				action.run();
			}
		}
		
		refresh();
	}
	
  public boolean shouldUseExtraSpace()
  {
    return false;
  }

  public void doHandleEvent(Event event)
  {
	  super.doHandleEvent(event);

	  if (event.widget == addressText && !addressText.isDisposed()) {
		  String newAddress = addressText.getText();
		  if (newAddress == null) {
			  newAddress = "";
		  }
		  
		  IEndPoint endPoint = (IEndPoint) getModel();
		  Command command = endPoint.getSetAddressCommand(newAddress);
		  CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
		  stack.execute(command);
	  }
  }
}

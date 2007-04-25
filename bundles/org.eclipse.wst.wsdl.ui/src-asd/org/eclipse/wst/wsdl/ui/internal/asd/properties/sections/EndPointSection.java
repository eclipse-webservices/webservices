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
package org.eclipse.wst.wsdl.ui.internal.asd.properties.sections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11EndPoint;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetExistingBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetNewBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IEndPoint;
import org.eclipse.wst.wsdl.ui.internal.util.ReferenceEditManagerHelper;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;

public class EndPointSection extends ReferenceSection {
	protected Text addressText;
	protected CCombo protocolCombo;
	
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
		CLabel addressLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_ADDRESS + ":"); //$NON-NLS-1$ //$NON-NLS-2$
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(addressText, ASDEditorCSHelpIds.PROPERTIES_PORT_ADDRESS_TEXT);

		// Protocol Row
		CLabel protocolLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_BINDING_PROTOCOL + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		protocolCombo = getWidgetFactory().createCCombo(composite); //$NON-NLS-1$

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(protocolCombo, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(protocolCombo, 0, SWT.CENTER);
		protocolLabel.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(addressText, +ITabbedPropertyConstants.VSPACE);
		protocolCombo.setLayoutData(data);
		protocolCombo.addListener(SWT.Modify, this);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(protocolCombo, ASDEditorCSHelpIds.PROPERTIES_PORT_PROTOCOL_TEXT);

		comboLabel.setText(Messages._UI_LABEL_BINDING + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(combo, ASDEditorCSHelpIds.PROPERTIES_PORT_BINDING_COMBO);
	}

	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh() {
		removeListeners(addressText);
		protocolCombo.removeListener(SWT.Modify, this);

		super.refresh();

		IEndPoint endPoint = (IEndPoint) getModel();
		if (endPoint.getAddress() != null) {
			if (!endPoint.getAddress().equals(addressText.getText())) {
				addressText.setText(endPoint.getAddress());
			}
		}
		
		protocolCombo.removeAll();
		if (endPoint instanceof W11EndPoint) {
			String protocolValue = ((W11EndPoint) endPoint).getProtocol();
			List protocols = ((W11EndPoint) getModel()).getApplicableProtocol();
			Iterator it = protocols.iterator();
			while (it.hasNext()) {
				protocolCombo.add((String) it.next());
			}
		
			protocolCombo.setText(protocolValue);
		}
		
		setControlForegroundColor(addressText);
		setControlForegroundColor(protocolCombo);
		applyTextListeners(addressText);
		protocolCombo.addListener(SWT.Modify, this);
	}
	
	protected ComponentReferenceEditManager getComponentReferenceEditManager() {
		if (refManager != null) {
			return refManager;
		}

		refManager = ReferenceEditManagerHelper.getBindingReferenceEditManager((IASDObject) getModel()); 
		
		return refManager;
	}

	protected List getComboItems() {
		ComponentReferenceEditManager manager = getComponentReferenceEditManager();
		
		List items = new ArrayList();
		items.add(BROWSE_STRING);
		items.add(NEW_STRING);

		ComponentSpecification[] comboItems = manager.getQuickPicks();
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
		String name = ""; //$NON-NLS-1$
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
			ComponentReferenceEditManager manager = getComponentReferenceEditManager();
			manager.modifyComponentReference((IEndPoint) getModel(), spec);
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
	  if (event.widget == addressText && !addressText.isDisposed()) {
		  String newAddress = addressText.getText();
		  if (newAddress == null) {
			  newAddress = ""; //$NON-NLS-1$
		  }
		  
		  String oldAddress = getOldAddress();
		  if (oldAddress.equals(newAddress))
			  return;
		  
		  IEndPoint endPoint = (IEndPoint) getModel();
		  Command command = endPoint.getSetAddressCommand(newAddress);
		  executeCommand(command);
	  }
	  else if (event.widget == protocolCombo && !protocolCombo.isDisposed()) {
		  String newProtocol = protocolCombo.getText();
		  if (newProtocol != null && getModel() instanceof W11EndPoint) {
			  W11EndPoint endPoint = (W11EndPoint) getModel();
			  endPoint.setProtocol(newProtocol);
		  }
	  }

	  else {
		  super.doHandleEvent(event);
	  }
  }

  private String getOldAddress() {
	  String value = null;
	  if (getModel() instanceof W11EndPoint) {
		  value = ((W11EndPoint) getModel()).getAddress();
	  }

	  if (value == null) {
		  value = ""; //$NON-NLS-1$
	  }
	  return value;
  }
}

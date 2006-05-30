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
package org.eclipse.wst.wsdl.ui.internal.properties.sections;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForPart;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.asd.properties.sections.ParameterSection;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.ReferenceEditManagerHelper;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;

public class W11ParameterSection extends ParameterSection {
	protected Button typeRadio;
	protected Button elementRadio;
	protected ComponentReferenceEditManager refManager;
	
	public void createControlArea()	{
		super.createControlArea();
		FormData data;
		
		typeRadio = getWidgetFactory().createButton(composite, Messages.getString("_UI_LABEL_TYPE"), SWT.RADIO); //$NON-NLS-1$
		elementRadio = getWidgetFactory().createButton(composite, Messages.getString("_UI_LABEL_ELEMENT"), SWT.RADIO);	 //$NON-NLS-1$
		CLabel referenceKindLabel = getWidgetFactory().createCLabel(composite, Messages.getString("_UI_LABEL_REFERENCE_KIND") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		
		typeRadio.addSelectionListener(this);
		elementRadio.addSelectionListener(this);
		
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(typeRadio, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(typeRadio, 0, SWT.CENTER);
		referenceKindLabel.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(elementRadio, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(combo, +ITabbedPropertyConstants.VSPACE);
		typeRadio.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0, 160);
		data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(combo, +ITabbedPropertyConstants.VSPACE);
		elementRadio.setLayoutData(data);
	}
	
	public void doWidgetSelected(SelectionEvent e) {
		W11ParameterForPart parameter = (W11ParameterForPart) this.getModel();
		Part part = (Part) parameter.getTarget();
		
		if (e.widget == typeRadio && typeRadio.getSelection()) {
			comboLabel.setText(Messages.getString("_UI_LABEL_TYPE") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
			ComponentReferenceUtil.setComponentReference(part, true, null);
			super.refreshCombo();
		}
		else if (e.widget == elementRadio && elementRadio.getSelection()) {
			comboLabel.setText(Messages.getString("_UI_LABEL_ELEMENT") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
			ComponentReferenceUtil.setComponentReference(part, false, null);
			refreshElementCombo();
		}
	}
	
	public void doHandleEvent(Event event) {
		if (event.widget == combo) {
			if (elementRadio.getSelection()) {
				// Handle Element selection
				String value = combo.getItem(combo.getSelectionIndex());
				
				W11ParameterForPart parameter = (W11ParameterForPart) this.getModel();
				
				if (value.equals(NEW_STRING)) {
					Command command = parameter.getSetElementCommand(IParameter.SET_NEW_ACTION_ID);
					command.execute();
				}
				else if (value.equals(BROWSE_STRING)) {
					Command command = parameter.getSetElementCommand(IParameter.SELECT_EXISTING_ACTION_ID);
					command.execute();
				}
				else {
					ComponentReferenceEditManager editManager = getElementComponentReferenceEditManager();
					ComponentSpecification spec = getComponentSpecificationForValue((String)value);
					if (spec != null) {
						editManager.modifyComponentReference(parameter, spec);
					}
				}
			}
		}
		
		super.doHandleEvent(event);
	}
	
	protected void refreshElementCombo() {
	
			// Refresh with Elements
			IParameter param = null;
			Object model = getModel();
			setListenerEnabled(false);
			
			if (model instanceof IParameter) {
				param = (IParameter) model;
			}
			
			String name = ""; //$NON-NLS-1$
			String elementName = "ParameterSection.java"; //$NON-NLS-1$
			if (param != null) {
				name = param.getName();
				elementName = param.getComponentName();
			}

			if (!nameText.isFocusControl()) {
				nameText.setText(name);
			}
			
			// Populate the Combo
			combo.removeAll();
			combo.add(BROWSE_STRING);
			combo.add(NEW_STRING);
			
			ComponentReferenceEditManager editManager = getElementComponentReferenceEditManager();
			if (editManager != null) {
				ComponentSpecification[] specs = editManager.getQuickPicks();
				for (int index = 0; index < specs.length; index++) {
					combo.add((String) specs[index].getName());
				}
			}

			// Display the element in the Combo
			String[] items = combo.getItems();
			int index;
			for (index = 0; index < items.length; index++) {
				if (items[index].equals(elementName)) {
					break;
				}
			}
			
			if (index < items.length) {
				// Found a match
				combo.select(index);
			}
			else {
				combo.setText(elementName);
			}
			
			setListenerEnabled(true);
			
	}
	
	public void refresh() {
		Object model = getModel();
		if (model instanceof W11ParameterForPart) {
			W11ParameterForPart param = (W11ParameterForPart) model;
			Part part = (Part) param.getTarget();
			if (part.getTypeDefinition() != null) {
				typeRadio.setSelection(true);
				elementRadio.setSelection(false);
				comboLabel.setText(Messages.getString("_UI_LABEL_TYPE") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
				handleTypeScenario = true;
			}
			else if (part.getElementDeclaration() != null) {
				typeRadio.setSelection(false);
				elementRadio.setSelection(true);
				comboLabel.setText(Messages.getString("_UI_LABEL_ELEMENT") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
				handleTypeScenario = false;
				refreshElementCombo();			
			}
			else {
				// Neither a Type or Element
				typeRadio.setSelection(false);
				elementRadio.setSelection(true);				
			}
		}
	
		setControlForegroundColor(elementRadio);
		setControlForegroundColor(typeRadio);
		
		super.refresh();
	}
	
	protected ComponentReferenceEditManager getElementComponentReferenceEditManager() {
		if (refManager != null) {
			return refManager;
		}
		
		refManager = ReferenceEditManagerHelper.getXSDElementReferenceEditManager((IASDObject) getModel());
		
		return refManager;
	}
}

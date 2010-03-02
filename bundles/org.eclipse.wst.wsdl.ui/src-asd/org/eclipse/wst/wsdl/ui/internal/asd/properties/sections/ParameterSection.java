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

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.util.ReferenceEditManagerHelper;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;

public class ParameterSection extends NameSection {
	protected CLabel comboLabel; 
	protected CCombo combo;
	protected boolean handleTypeScenario = true;
	protected ComponentReferenceEditManager parameterRefManager;
	
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		createControlArea();
	}
	
	public void createControlArea()
	{		
		comboLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_TYPE); 
		GridData data = new GridData();
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
	    data.grabExcessHorizontalSpace = false;
		comboLabel.setLayoutData(data);
		
		combo = getWidgetFactory().createCCombo(composite);
		combo.setBackground(composite.getBackground());
		combo.addListener(SWT.Modify, this);
		combo.addListener(SWT.DefaultSelection, this);
		combo.addListener(SWT.Traverse, this);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh() {
		super.refresh();
		if (nameText.isFocusControl()) {
			return;
		}
		
		setListenerEnabled(false);
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(combo, ASDEditorCSHelpIds.PROPERTIES_PART_ELEMENT_COMBO);

		if (handleTypeScenario) {
			refreshCombo();
			PlatformUI.getWorkbench().getHelpSystem().setHelp(combo, ASDEditorCSHelpIds.PROPERTIES_PART_TYPE_COMBO);
		}
		
		setControlForegroundColor(combo);
		
		setListenerEnabled(true);
	}
	
	protected void refreshCombo() {
		IParameter param = null;
		Object model = getModel();
		setListenerEnabled(false);
		
		if (model instanceof IParameter) {
			param = (IParameter) model;
		}
		
		String name = ""; //$NON-NLS-1$
		String typeName = ""; //$NON-NLS-1$
		if (param != null) {
			name = param.getName();
			typeName = param.getComponentName();
		}
		
		if (!nameText.isFocusControl()) {
			nameText.setText(name);
		}
		
		// Populate the type Combo
		combo.removeAll();
		combo.add(BROWSE_STRING);
		combo.add(NEW_STRING);
		
		ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
		if (editManager != null) {
			ComponentSpecification[] specs = editManager.getQuickPicks();
			for (int index = 0; index < specs.length; index++) {
				combo.add((String) specs[index].getName());
			}
			
			specs = editManager.getHistory();
			for (int index = 0; index < specs.length; index++) {
				combo.add((String) specs[index].getName());
			}
		}		
		
		// Display the type in the Combo
		String[] items = combo.getItems();
		int index;
		for (index = 0; index < items.length; index++) {
			if (items[index].equals(typeName)) {
				break;
			}
		}
		
		if (index < items.length) {
			// Found a match
			combo.select(index);
		}
		else {
			combo.setText(typeName);
		}
		
		setListenerEnabled(true);
	}
	
	public boolean shouldUseExtraSpace()
	{
		return false;
	}
	
	public void handleEvent(Event event)
	{
		if (event.widget == combo) {
			if (isListenerEnabled() && !isInDoHandle) 
			{
				if (event.type == SWT.Traverse) {
					if (event.detail == SWT.TRAVERSE_ARROW_NEXT || event.detail == SWT.TRAVERSE_ARROW_PREVIOUS) {
						isTraversing = true;
					}
				}
				else {
					isInDoHandle = true;
					startDelayedEvent(event);
					isInDoHandle = false;
				}
			}
		}
		else {
			super.handleEvent(event);
		}
	}
	
	public void doHandleEvent(Event event)
	{
		super.doHandleEvent(event);
		if (event.widget == combo && !combo.isDisposed()) {
			String selectedItem = combo.getItem(combo.getSelectionIndex());
			if (shouldPerformComboSelection(event, selectedItem))
			{
				handleComboSelection();
				refresh();
			}
		}
	}
	
	protected void handleComboSelection() {
		String value = combo.getItem(combo.getSelectionIndex());
		
		IParameter parameter = (IParameter) this.getModel();
		
		if (value.equals(NEW_STRING)) {
			Command command = parameter.getSetTypeCommand(IParameter.SET_NEW_ACTION_ID);
			command.execute();
		}
		else if (value.equals(BROWSE_STRING)) {
			Command command = parameter.getSetTypeCommand(IParameter.SELECT_EXISTING_ACTION_ID);
			command.execute();
		}
		else {
			ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
			ComponentSpecification spec = getComponentSpecificationForValue((String)value);
			if (spec != null) {
				editManager.modifyComponentReference(parameter, spec);
			}
		}
	}
	
	protected ComponentReferenceEditManager getComponentReferenceEditManager() {
		if (parameterRefManager != null) {
			return parameterRefManager;
		}		
        if (getModel() instanceof IASDObject)
        {  
		  parameterRefManager = ReferenceEditManagerHelper.getXSDTypeReferenceEditManager((IASDObject) getModel());
        }
        else
        {
          System.out.println("model" + getModel()); //$NON-NLS-1$
        }  
		return parameterRefManager;
	}
	
	// TODO: rmah: This code should live in a common place..... This code is also used in other UI scenarios when
	// a similar combo box is used.  For example in Direct Edit...(TypeReferenceDirectEditManager)  Also used in the XSDEditor...
	protected ComponentSpecification getComponentSpecificationForValue(String value)
	{
		ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
		if (editManager != null)
		{  
			ComponentSpecification[] quickPicks = editManager.getQuickPicks();
			if (quickPicks != null)
			{
				for (int i=0; i < quickPicks.length; i++)
				{
					ComponentSpecification componentSpecification = quickPicks[i];
					if (value.equals(componentSpecification.getName()))
					{
						return componentSpecification;
					}                
				}  
			}
			ComponentSpecification[] history = editManager.getHistory();
			if (history != null)
			{
				for (int i=0; i < history.length; i++)
				{
					ComponentSpecification componentSpecification = history[i];
					if (value.equals(componentSpecification.getName()))
					{  
						return componentSpecification;
					}
				}  
			}
		}
		return null;
	}
	
	public void dispose()
	{
		if (combo != null && !combo.isDisposed())
		{
			combo.removeListener(SWT.Modify, this);
			combo.removeListener(SWT.DefaultSelection, this);
			combo.removeListener(SWT.Traverse, this);
		}
		super.dispose();
	}
}

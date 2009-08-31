/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.properties.sections;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11MessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.properties.sections.NameSection;
import org.eclipse.wst.wsdl.ui.internal.util.ReferenceEditManagerHelper;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.adt.edit.IComponentDialog;

public class W11MessageReferenceSection extends NameSection {
	protected CLabel comboLabel; 
	protected CCombo combo;
	protected ComponentReferenceEditManager refManager;

	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		createControlArea();
	}
	
	public void createControlArea()
	{
		
		
		comboLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_MESSAGE + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		GridData data = new GridData();
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
	    data.grabExcessHorizontalSpace = false;
		comboLabel.setLayoutData(data);
		
		combo = getWidgetFactory().createCCombo(composite);
		combo.setBackground(composite.getBackground());
        combo.addListener(SWT.Modify, this);
        combo.addListener(SWT.DefaultSelection, this);
        combo.addListener(SWT.Traverse, this);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(combo, ASDEditorCSHelpIds.PROPERTIES_MESSAGE_REF_MESSAGE_COMBO);
		
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
		String refName = ""; //$NON-NLS-1$
		
		combo.removeAll();
		combo.add(BROWSE_STRING);
		combo.add(NEW_STRING);
		
		MessageReference messageRef = (MessageReference) ((W11MessageReference) getModel()).getTarget();
		if (messageRef != null && messageRef.getEMessage() != null) {
			refName = messageRef.getEMessage().getQName().getLocalPart();
		}
		
		ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
		if (editManager != null) {
			ComponentSpecification[] specs = editManager.getQuickPicks();
			for (int index = 0; index < specs.length; index++) {
				combo.add((String) specs[index].getName());
			}
		}		
		
		String[] items = combo.getItems();
		int index;
		for (index = 0; index < items.length; index++) {
			if (items[index].equals(refName)) {
				break;
			}
		}
		
		if (index < items.length) {
			// Found a match
			combo.select(index);
		}
		else {
			combo.setText(refName);
		}

		setControlForegroundColor(combo);
		setListenerEnabled(true);


//		MessageReference messageRef = (MessageReference) ((W11MessageReference) getModel()).getTarget();
//		Iterator it = messageRef.getEnclosingDefinition().getEMessages().iterator();
//		while (it.hasNext()) {
//			Message message = (Message) it.next();
//			combo.add(message.getQName().getLocalPart());
//		}
	}
	
	protected ComponentReferenceEditManager getComponentReferenceEditManager() {
		if (refManager != null) {
			return refManager;
		}

		refManager = ReferenceEditManagerHelper.getMessageReferenceEditManager((IASDObject) getModel());
        
		return refManager;
	}

	
	public boolean shouldUseExtraSpace()
	{
		return false;
	}
	
	public void handleEvent(Event event)
	{
		if (event.widget == combo) {
			if (isListenerEnabled() && !isInDoHandle) {
				if (event.type == SWT.Traverse) {
					if (event.detail == SWT.TRAVERSE_ARROW_NEXT || event.detail == SWT.TRAVERSE_ARROW_PREVIOUS)
						isTraversing = true;
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
		if (event.widget == combo) {
			String value = ""; //$NON-NLS-1$
			if (combo.getSelectionIndex() != -1) {
				value = combo.getItem(combo.getSelectionIndex());
			}
			if (shouldPerformComboSelection(event, value)) {
    			ComponentSpecification spec = null;
    			int continueApply = Window.OK;
    			IMessageReference messageRef = (IMessageReference) this.getModel();
    			
    			if (value.equals(NEW_STRING)) {
    				ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
    				IComponentDialog dialog = editManager.getNewDialog();
    				continueApply = dialog.createAndOpen();
    				spec = dialog.getSelectedComponent();
    			}
    			else if (value.equals(BROWSE_STRING)) {
    				ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
    				IComponentDialog dialog = editManager.getBrowseDialog();
    				continueApply = dialog.createAndOpen();
    				spec = dialog.getSelectedComponent();				
    			}
    			else {
    				spec = getComponentSpecificationForValue((String)value);
    			}
    			
    			if (continueApply == Window.OK) {
    				ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
    				if (spec != null) {
    					editManager.modifyComponentReference(messageRef, spec);
    				}
    			}
	        }
		}
	}
	
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
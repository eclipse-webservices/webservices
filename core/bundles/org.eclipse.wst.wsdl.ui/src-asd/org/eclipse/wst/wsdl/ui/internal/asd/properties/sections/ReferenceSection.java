/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.asd.properties.sections;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public abstract class ReferenceSection extends NameSection {
	protected CCombo combo;
	protected CLabel comboLabel;
	
	protected List itemsInCombo = new ArrayList();

	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		
		// Create Combo row of widgets
		comboLabel = getWidgetFactory().createCLabel(composite, "Reference"); //$NON-NLS-1$
		combo = getWidgetFactory().createCCombo(composite);
		combo.setBackground(composite.getBackground());

		// Layout Combo Label
		GridData data = new GridData();
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
	    data.grabExcessHorizontalSpace = false;
		comboLabel.setLayoutData(data);
		
		// Layout Combo
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
        combo.addListener(SWT.Modify, this);
        combo.addListener(SWT.DefaultSelection, this);
        combo.addListener(SWT.Traverse, this);
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

	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh() {
		combo.removeListener(SWT.Modify, this);
		combo.removeListener(SWT.DefaultSelection, this);
		combo.removeListener(SWT.Traverse, this);
		
		super.refresh();
		
		List comboItems = getComboItems();
		if (comboItems.size() > 0) {
			combo.removeAll();
			itemsInCombo.clear();

			// We need to populate this combo with the available references
			// TODO: rmah: We should include the prefix as well
			String currentComboItemName = getComboItemName(getCurrentComboItem());
			int currentBindingIndex = -1;
			
			List items = getComboItems();
			for (int index = 0; index < items.size(); index++) {
				Object item = items.get(index);
				String itemName = getComboItemName(item);
				combo.add(itemName);
				itemsInCombo.add(item);

				if (itemName.equals(currentComboItemName)) {
					currentBindingIndex = index;
				}
			}

			if (currentBindingIndex != -1) {
				combo.select(currentBindingIndex);	
			}
		}
		
		setControlForegroundColor(combo);
		combo.addListener(SWT.Modify, this);
		combo.addListener(SWT.DefaultSelection, this);
		combo.addListener(SWT.Traverse, this);
	}
	
	protected abstract List getComboItems();
	protected abstract Object getCurrentComboItem();
	protected abstract String getComboItemName(Object item);
	protected abstract void performComboSelection(Object item);
	
  public boolean shouldUseExtraSpace()
  {
    return false;
  }

  public void doHandleEvent(Event event)
  {
	  super.doHandleEvent(event);
	  if (event.widget == combo && !combo.isDisposed()) {
		  int selectionIndex = combo.getSelectionIndex();
		  Object selectedItem = itemsInCombo.get(selectionIndex);
		  
		  if (shouldPerformComboSelection(event, selectedItem))
		  {
		    performComboSelection(selectedItem);
		    refresh();
		  }
	  }
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

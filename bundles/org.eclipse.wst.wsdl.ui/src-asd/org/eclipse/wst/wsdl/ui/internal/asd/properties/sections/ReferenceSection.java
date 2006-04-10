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
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public abstract class ReferenceSection extends NameSection {
	public static String BROWSE_STRING = "Browse...";
	public static String NEW_STRING = "New...";
	protected CCombo combo;
	protected CLabel comboLabel;
	
	protected List itemsInCombo = new ArrayList();

	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		FormData data;
		
		// Create Combo row of widgets
		comboLabel = getWidgetFactory().createCLabel(composite, "Reference");
		combo = getWidgetFactory().createCCombo(composite);
		combo.setBackground(composite.getBackground());

		// Layout Combo Label
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(combo, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(combo, 0, SWT.CENTER);
		comboLabel.setLayoutData(data);
		
		// Layout Combo
		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(nameText, +ITabbedPropertyConstants.VSPACE);
		combo.setLayoutData(data);
		
		combo.addListener(SWT.Modify, this);
	}

	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh() {
		combo.removeListener(SWT.Modify, this);
		
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
		
		combo.addListener(SWT.Modify, this);
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
		  performComboSelection(itemsInCombo.get(selectionIndex));
		  refresh();
	  }
  }
}

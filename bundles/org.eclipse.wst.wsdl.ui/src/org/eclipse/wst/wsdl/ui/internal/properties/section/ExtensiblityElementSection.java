/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.properties.section;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.common.ui.properties.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterListener;
import org.eclipse.wst.wsdl.ui.internal.viewers.ExtensibilityElementViewer;
import org.eclipse.wst.wsdl.ui.internal.viewers.widgets.AttributesTable;

public class ExtensiblityElementSection extends AbstractSection implements ModelAdapterListener
{
  ExtensibilityElementViewer viewer;
  protected AttributesTable attributesTable;
	/**
	 * @see org.eclipse.wst.common.ui.properties.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);

		composite =	getWidgetFactory().createFlatFormComposite(parent);
	
//		viewer = new ExtensibilityElementViewer(composite, getActiveEditor(), true);
		attributesTable = new AttributesTable(getActiveEditor(), composite);
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		attributesTable.getControl().setLayoutData(data);
//		viewer.getControl().setLayoutData(data);
	}
	
  public void propertyChanged(Object object, String property)
  {
    if (isListenerEnabled())
    {
      setListenerEnabled(false);
      refresh();
      setListenerEnabled(true);
    }
  }  

	/*
	 * @see org.eclipse.wst.common.ui.properties.view.ITabbedPropertySection#refresh()
	 */
	public void refresh()
	{
    super.refresh();
    attributesTable.setInput(getElement().getElement());

    Runnable runnable = new Runnable()
    { 
      public void run()
      {           
        if (!attributesTable.getControl().isDisposed())
        {
          attributesTable.refresh();
        }
      }
    };               
    Display.getCurrent().asyncExec(runnable);

	}
}

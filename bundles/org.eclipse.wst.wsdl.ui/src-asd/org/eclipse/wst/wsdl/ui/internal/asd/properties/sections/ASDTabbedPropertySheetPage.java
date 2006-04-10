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

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class ASDTabbedPropertySheetPage extends TabbedPropertySheetPage implements ISelectionChangedListener//, IElementListener
{
  /**
   * @param tabbedPropertySheetPageContributor
   */
  public ASDTabbedPropertySheetPage(ITabbedPropertySheetPageContributor tabbedPropertySheetPageContributor)
  {
    super(tabbedPropertySheetPageContributor);
//    tabContributor = tabbedPropertySheetPageContributor;
  }
  
  public void createControl(Composite parent) {
  	super.createControl(parent);
  }
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event)
	{
		if (!event.getSelection().isEmpty()) {
			selectionChanged(getSite().getWorkbenchWindow().getActivePage().getActivePart(), event.getSelection());
		    //super.selectionChanged(getSite().getWorkbenchWindow().getActivePage().getActivePart(), event.getSelection());
		}
	}
  
	public void dispose() {
		super.dispose();
	}
}
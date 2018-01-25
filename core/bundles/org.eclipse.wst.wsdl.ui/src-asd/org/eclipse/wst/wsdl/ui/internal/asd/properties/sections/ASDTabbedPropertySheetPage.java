/*******************************************************************************
 * Copyright (c) 2001, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.properties.sections;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;

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

	// Bug 338126 - Properties view not synced with the design view
	// Work around change to PropertySheet where it was changed to a Post Selection Listener
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof StructuredSelection) {
			StructuredSelection structuredSelection = (StructuredSelection) selection;
			Object object = structuredSelection.getFirstElement();
			if (object instanceof WSDLBaseAdapter) {
				super.selectionChanged(part, selection);
			}
		}
	}
  
	public void dispose() {
		super.dispose();
	}
}
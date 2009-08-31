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
package org.eclipse.wst.wsdl.ui.internal.asd.properties.sections;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11EditNamespacesCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDEditNamespacesAction;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.AbstractModelCollection;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.INamedObject;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ICategoryAdapter;
import org.eclipse.wst.xml.ui.internal.nsedit.CommonEditNamespacesTargetFieldDialog;
import org.eclipse.wst.xml.ui.internal.nsedit.CommonNamespaceInfoTable;

public class NamespaceSection extends ASDAbstractSection {
	protected String targetNamespace;
	protected List namespaceInfoList;
	protected CommonEditNamespacesTargetFieldDialog editWSDLNamespacesControl;
	protected Button button;
	
	Text nameText;
	Text prefixText;
	Text targetNamespaceText;
	protected CommonNamespaceInfoTable tableViewer;
	private boolean handlingEvent;
	
	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		composite =	getWidgetFactory().createFlatFormComposite(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginRight = 10;
		composite.setLayout(gridLayout);		
		
		String nameString = Messages._UI_LABEL_NAME + ":"; //$NON-NLS-1$
		String prefixString = Messages._UI_LABEL_PREFIX + ":"; //$NON-NLS-1$
		String namespaceString = Messages._UI_LABEL_TARGET_NAMESPACE + ":"; //$NON-NLS-1$
		
		// name
		CLabel nameLabel = getWidgetFactory().createCLabel(composite, nameString); //$NON-NLS-1$
		GridData data = new GridData();
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
	    data.grabExcessHorizontalSpace = false;
	    nameLabel.setLayoutData(data);
		
		nameText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		applyTextListeners(nameText);
	    nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		PlatformUI.getWorkbench().getHelpSystem().setHelp(nameText, ASDEditorCSHelpIds.PROPERTIES_NAME_TEXT);
		
		// prefix
		CLabel prefixLabel = getWidgetFactory().createCLabel(composite, prefixString); //$NON-NLS-1$
		data = new GridData();
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
	    data.grabExcessHorizontalSpace = false;
	    prefixLabel.setLayoutData(data);
	    
		prefixText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		applyTextListeners(prefixText);
		prefixText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		PlatformUI.getWorkbench().getHelpSystem().setHelp(prefixText, ASDEditorCSHelpIds.PROPERTIES_DEF_PREFIX_TEXT);
		
		// targetnamespace
		CLabel targetNamespaceLabel = getWidgetFactory().createCLabel(composite, namespaceString); //$NON-NLS-1$
		data = new GridData();
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
	    data.grabExcessHorizontalSpace = false;
	    targetNamespaceLabel.setLayoutData(data);
	    
		targetNamespaceText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		applyTextListeners(targetNamespaceText);
		targetNamespaceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		PlatformUI.getWorkbench().getHelpSystem().setHelp(targetNamespaceText, ASDEditorCSHelpIds.PROPERTIES_DEF_TNS_TEXT);
		
		// Advanced button
		button = getWidgetFactory().createButton(composite, Messages._UI_SECTION_ADVANCED_ATTRIBUTES + "...", SWT.PUSH); //$NON-NLS-1$ //$NON-NLS-2$
		button.addSelectionListener(this);
		data = new GridData(SWT.RIGHT, SWT.END, false, false);
		data.horizontalSpan = 2;
		button.setLayoutData(data);
	}
	
	public void doHandleEvent(Event event)
	{
		handlingEvent = true;
		Object obj = getDescription();
		if (obj instanceof IDescription) {
			IDescription description = (IDescription) obj;

			if (event.widget == nameText) {
				String newName = nameText.getText();
				if (!newName.equals(description.getName())) {
					Command command = description.getSetNameCommand(newName);
					executeCommand(command);
				}
			}
			else if (event.widget == prefixText || event.widget == targetNamespaceText) {
				String newTargetNS = targetNamespaceText.getText();				
				String newPrefix = prefixText.getText();
				
				boolean targetNSdiff = !newTargetNS.equals(description.getTargetNamespace());
				boolean prefixDiff = !newPrefix.equals(description.getTargetNamespacePrefix());
				if (targetNSdiff || prefixDiff ) {
					// TODO: The code below is not generic.  We need to revisit this to ensure it is
					// generic.  IDescription needs a getNamespacesInfo() and getEditNamespacesCommand()...
					W11EditNamespacesCommand command = 
						(W11EditNamespacesCommand) ((W11Description) description).getEditNamespacesCommand();
					if (targetNSdiff)
					  command.setTargetNamespace(newTargetNS);
					if (prefixDiff)
					  command.setTargetNamespacePrefix(newPrefix);
					executeCommand(command);
				}
			}
		}

		handlingEvent = false;		
	}
	
	protected Object getDescription() {
		Object model = getModel();
		if (model instanceof AbstractModelCollection) {
			model = ((AbstractModelCollection) model).getModel();
			if (model instanceof IDescription) {
				return (IDescription) model;
			}
		}
		else if (model instanceof ICategoryAdapter) {
			return ((ICategoryAdapter) model).getOwnerDescription();
		}

		return model;
	}
	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh()
	{
		super.refresh();
		
		// Set nameText
		if (nameText == null || nameText.isFocusControl()) {
			return;
		}
		setListenerEnabled(false);
		nameText.setText(""); //$NON-NLS-1$
		if (getDescription() instanceof INamedObject) {
			nameText.setText(((INamedObject) getDescription()).getName());
		}		
		setListenerEnabled(true);
		
		
		// Set targetNamespaceText and prefixText
		if (targetNamespaceText == null || targetNamespaceText.isFocusControl() || handlingEvent)
		{
			return;
		}
		setListenerEnabled(false);
		Object obj = getDescription();
		if (obj instanceof IDescription)
		{
			IDescription description = (IDescription) obj;
			String targetNS = description.getTargetNamespace();
			String newPrefix = description.getTargetNamespacePrefix();            
			prefixText.setText(newPrefix != null ? newPrefix : ""); //$NON-NLS-1$
			
			// set targetnamespace field and prefix field
			if (targetNS != null)
			{
				targetNamespaceText.setText(targetNS);
			}
      else
      {
        targetNamespaceText.setText(""); //$NON-NLS-1$
      }
			if (newPrefix != null)
			{
				prefixText.setText(newPrefix);
			}
		}
		setListenerEnabled(true);
	}
	
	
	public void widgetSelected(SelectionEvent e)
	{
		if (e.widget == button)	{
			Object obj = getDescription();
			if (obj instanceof IDescription) {
				IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
				ASDEditNamespacesAction action = new ASDEditNamespacesAction(part, (IDescription) obj);
				action.run();
				refresh();
	        }
		}
	}
}

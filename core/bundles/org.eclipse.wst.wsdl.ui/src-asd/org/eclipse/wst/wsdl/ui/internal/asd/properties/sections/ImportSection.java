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

import java.util.Map;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Import;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11UpdateImportCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.dialogs.ImportSelectionDialog;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IImport;
import org.w3c.dom.Element;

public class ImportSection extends ASDAbstractSection
{
	protected Text namespaceText, prefixText, locationText;
	private String oldPrefixValue;
	Button button;
	/**
	 * @deprecated
	 */
	IEditorPart editorPart;

	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		Composite composite = getWidgetFactory().createFlatFormComposite(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);
		
		CLabel namespaceLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_NAMESPACE); //$NON-NLS-1$
		GridData data = new GridData();
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
	    data.grabExcessHorizontalSpace = false;
		namespaceLabel.setLayoutData(data);

		namespaceText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		namespaceText.setEditable(false);
		namespaceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// fill the 3rd column
		getWidgetFactory().createLabel(composite, ""); //$NON-NLS-1$

		CLabel prefixLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_PREFIX); //$NON-NLS-1$
		data = new GridData();
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
	    data.grabExcessHorizontalSpace = false;
		prefixLabel.setLayoutData(data);
		
		prefixText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		applyTextListeners(prefixText);
		prefixText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// fill the 3rd column
		getWidgetFactory().createLabel(composite, ""); //$NON-NLS-1$

		CLabel locationLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_LOCATION); //$NON-NLS-1$
		data = new GridData();
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
	    data.grabExcessHorizontalSpace = false;
		locationLabel.setLayoutData(data);
		
		locationText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		locationText.setEditable(false);
		locationText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		button = getWidgetFactory().createButton(composite, "", SWT.PUSH); //$NON-NLS-1$
		button.setImage(WSDLEditorPlugin.getInstance().getImage("icons/browsebutton.gif")); //$NON-NLS-1$

		button.addSelectionListener(this);
		button.setLayoutData(new GridData());

		
	}

	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh()
	{
		setListenerEnabled(false);  
		Object obj = getModel();
		boolean refreshPrefixText = true;
		boolean refreshLocationText = true;
		boolean refreshNamespaceText = true;
		if (prefixText.isFocusControl())
		{
			refreshPrefixText = false;
		}
		if (locationText.isFocusControl())
		{
			refreshLocationText = false;
		}
		if (namespaceText.isFocusControl())
		{
			refreshNamespaceText = false;
		}

		if (refreshPrefixText)
		{
			prefixText.setText(""); //$NON-NLS-1$
		}
		if (refreshLocationText)
		{
			locationText.setText(""); //$NON-NLS-1$
		}
		if (refreshNamespaceText)
		{
			namespaceText.setText(""); //$NON-NLS-1$
		}
		if (obj instanceof IImport)
		{
			IImport theImport = (IImport) obj;
			if (refreshLocationText)
			{
				String locationValue = theImport.getLocation(); 
				locationText.setText(locationValue != null ? locationValue : ""); //$NON-NLS-1$
			}

			String namespaceValue = theImport.getNamespace();
			if (refreshNamespaceText)
			{
				namespaceText.setText(namespaceValue != null ? namespaceValue : ""); //$NON-NLS-1$
			}

			if (refreshPrefixText)
			{
				String prefix = theImport.getNamespacePrefix();
				prefixText.setText(prefix != null ? prefix : ""); //$NON-NLS-1$
			}
		}
		oldPrefixValue = prefixText.getText();
		setListenerEnabled(true);
	}

	public void doHandleEvent(Event event) {
		// TODO: We have some WSDL11 Impl specific knowledge below... We should try to remove this...
	    setErrorMessage(null);
		if (event.widget == prefixText && locationText.getText().length() > 0 && namespaceText.getText().length() > 0 && getModel() instanceof W11Import) {
		  String newPrefix = prefixText.getText();
		  if (oldPrefixValue.equals(newPrefix)) {
		    return;
		  }
			
		  W11Import w11Import = (W11Import) getModel();
		  Import importObj = (Import) w11Import.getTarget();
		  Definition definition = importObj.getEnclosingDefinition();
		  Map namespacesMap = definition.getNamespaces();

		  if (namespacesMap.containsKey(newPrefix)) {
		    setErrorMessage(Messages._ERROR_LABEL_PREFIX_EXISTS);
		  }
		  else {
            Element definitionElement = definition.getElement();
            definitionElement.removeAttribute("xmlns:"+oldPrefixValue); //$NON-NLS-1$
            definitionElement.setAttribute("xmlns:" + newPrefix, namespaceText.getText()); //$NON-NLS-1$
		    oldPrefixValue = newPrefix;
		  }
		}
	}

	public void widgetSelected(SelectionEvent e)
	{
	  // TODO: We have some WSDL11 Impl specific knowledge below... We should try to remove this...
	  if (e.widget == button && getModel() instanceof W11Import)
	  {
	    W11Import w11Import = (W11Import) getModel();
	    Import importObj = (Import) w11Import.getTarget();

	    ImportSelectionDialog dialog = new ImportSelectionDialog(WSDLEditorPlugin.getShell(), null, true);
	    dialog.create();
	    int rc = dialog.open();
	    if (IDialogConstants.OK_ID == rc)
	    {
	      String locationURI = dialog.getImportLocation();
	      String namespaceURI = dialog.getImportNamespace();

	      String prefix = prefixText.getText();           
	      CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
	      W11UpdateImportCommand updateImportCommand = new W11UpdateImportCommand(importObj, locationURI, namespaceURI, prefix);
	      stack.execute(updateImportCommand);

	      Definition definition = importObj.getEnclosingDefinition();
	      String actualPrefix = definition.getPrefix(namespaceURI);
	      namespaceText.setText(namespaceURI);
	      locationText.setText(locationURI);
	      prefixText.setText(actualPrefix);
	    }
	    refresh();
	  }
	}

	/**
	 * @deprecated
	 */
	public void setEditorPart(IEditorPart editorPart)
	{
		this.editorPart = editorPart;
	}
}

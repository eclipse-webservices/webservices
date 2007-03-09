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

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.common.ui.internal.dialogs.SelectSingleFileDialog;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.internal.impl.ImportImpl;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Import;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IImport;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.emf.common.util.URI;
import org.w3c.dom.Element;

public class ImportSection extends ASDAbstractSection
{
	protected Text namespaceText, prefixText, locationText;
	private String oldPrefixValue;
	Button button;
	// TODO: Remove: IEditorPart editorPart
	IEditorPart editorPart;

	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		Composite composite = getWidgetFactory().createFlatFormComposite(parent);
		FormData data;

		namespaceText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		namespaceText.setEditable(false);
//		namespaceText.addListener(SWT.Modify, this);
		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		namespaceText.setLayoutData(data);

		CLabel namespaceLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_NAMESPACE); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(namespaceText, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(namespaceText, 0, SWT.CENTER);
		namespaceLabel.setLayoutData(data);

		prefixText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
//		prefixText.setEditable(false);
		applyTextListeners(prefixText);
		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(namespaceText, +ITabbedPropertyConstants.VSPACE);
		prefixText.setLayoutData(data);

		CLabel prefixLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_PREFIX); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(prefixText, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(prefixText, 0, SWT.CENTER);
		prefixLabel.setLayoutData(data);

		locationText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		locationText.setEditable(false);
//		locationText.addListener(SWT.Modify, this);

		CLabel locationLabel = getWidgetFactory().createCLabel(composite, Messages._UI_LABEL_LOCATION); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(locationText, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(locationText, 0, SWT.CENTER);
		locationLabel.setLayoutData(data);

		button = getWidgetFactory().createButton(composite, "", SWT.PUSH); //$NON-NLS-1$
		button.setImage(WSDLEditorPlugin.getInstance().getImage("icons/browsebutton.gif")); //$NON-NLS-1$

		button.addSelectionListener(this);
		data = new FormData();
		data.left = new FormAttachment(100, -rightMarginSpace + 2);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(locationText, 0, SWT.CENTER);
		button.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(button, 0);
		data.top = new FormAttachment(prefixText, +ITabbedPropertyConstants.VSPACE);
		locationText.setLayoutData(data);
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
		if (event.widget == prefixText && locationText.getText().length() > 0 && namespaceText.getText().length() > 0 && getModel() instanceof W11Import) {
			if (oldPrefixValue.equals(prefixText.getText()))
			  return;
			
			W11Import w11Import = (W11Import) getModel();
			Import importObj = (Import) w11Import.getTarget();
//			org.w3c.dom.Element importElement = WSDLEditorUtil.getInstance().getElementForObject(importObj);
			Map namespacesMap = importObj.getEnclosingDefinition().getNamespaces();

			if (namespacesMap.containsKey(prefixText.getText())) {
				// We should add error messages.........
				//           setErrorMessage(XSDEditorPlugin.getXSDString("_ERROR_LABEL_PREFIX_EXISTS"));
			}
			else {
				Element definitionElement = importObj.getEnclosingDefinition().getElement();
				definitionElement.removeAttribute("xmlns:"+oldPrefixValue); //$NON-NLS-1$
				definitionElement.setAttribute("xmlns:" + prefixText.getText(), namespaceText.getText()); //$NON-NLS-1$

//				clearErrorMessage();
				oldPrefixValue = prefixText.getText();
			}
		}
	}

	public void widgetSelected(SelectionEvent e)
	{
		// TODO: We have some WSDL11 Impl specific knowledge below... We should try to remove this...
		if (e.widget == button && getModel() instanceof W11Import)
		{
			W11Import w11Import = (W11Import) getModel();

			IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			IFile currentWSDLFile = ((IFileEditorInput) editor.getEditorInput()).getFile();

			SelectSingleFileDialog dialog = new SelectSingleFileDialog(WSDLEditorPlugin.getShell(), null, true);
			String [] filters = { "xsd", "wsdl" }; //$NON-NLS-1$ //$NON-NLS-2$
			IFile [] excludedFiles = { currentWSDLFile };

			dialog.addFilterExtensions(filters, excludedFiles);
			dialog.create();
			dialog.getShell().setText(org.eclipse.wst.wsdl.ui.internal.Messages._UI_TITLE_SELECT); //$NON-NLS-1$
			dialog.setTitle(org.eclipse.wst.wsdl.ui.internal.Messages._UI_TITLE_SELECT_FILE); //$NON-NLS-1$
			dialog.setMessage(org.eclipse.wst.wsdl.ui.internal.Messages._UI_DESCRIPTION_SELECT_WSDL_OR_XSD); //$NON-NLS-1$
			int rc = dialog.open();
			if (rc == IDialogConstants.OK_ID)
			{
				IFile selectedFile = dialog.getFile();

				//if (selectedFile.getLocation().toOSString().equals(currentWSDLFile.getLocation().toOSString()))
				//{
				//  System.out.println("SAME FILE:" + currentWSDLFile.getLocation());
				//}

				String location = ComponentReferenceUtil.computeRelativeURI(selectedFile, currentWSDLFile, true);

				Import importObj = (Import) w11Import.getTarget();
//				org.w3c.dom.Element importElement = WSDLEditorUtil.getInstance().getElementForObject(importObj);
				Definition definition = importObj.getEnclosingDefinition();
				org.w3c.dom.Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);

				String importTargetNamespace = ""; //$NON-NLS-1$
				String prefix = prefixText.getText();
				String uniquePrefix = ""; //$NON-NLS-1$

				URI uri = URI.createPlatformResourceURI(selectedFile.getFullPath().toString());      

				// note that the getTargetNamespaceURIForSchema works for both schema and wsdl files
				// I should change the name of this convenience method
				importTargetNamespace =  WSDLEditorUtil.getTargetNamespaceURIForSchema(uri.toString());

				if (prefix.trim().equals("")) //$NON-NLS-1$
				{
					uniquePrefix = getUniquePrefix(definition, uri.fileExtension());
				}
				else
				{
					uniquePrefix = prefix; 
				}


				if (importTargetNamespace == null ||
						(importTargetNamespace != null && importTargetNamespace.trim().length() == 0))
				{
					return;  // what to do with no namespace docs?
				}

				importObj.setLocationURI(location);
				importObj.setNamespaceURI(importTargetNamespace);
				((ImportImpl) importObj).importDefinitionOrSchema();

				definitionElement.setAttribute("xmlns:" + uniquePrefix, importTargetNamespace); //$NON-NLS-1$

				namespaceText.setText(importTargetNamespace);
				locationText.setText(location);
				prefixText.setText(uniquePrefix);
			}
			refresh();
		}
	}

	private String getUniquePrefix(Definition definition, String initPrefix)
	{
		String uniquePrefix;
		Map map = definition.getNamespaces();

		if (definition.getNamespace(initPrefix) == null)
		{
			uniquePrefix = initPrefix;
		}
		else // if used, then try to create a unique one
		{
			String tempPrefix = initPrefix;
			int i = 1;
			while(map.containsKey(tempPrefix + i))
			{
				i++;
			}
			uniquePrefix = tempPrefix + i;
		}
		return uniquePrefix;    
	}

	// TODO: Remove metod setEditorPart() below
	public void setEditorPart(IEditorPart editorPart)
	{
		this.editorPart = editorPart;
	}
}

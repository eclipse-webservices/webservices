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

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertyConstants;
import org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.EditNamespacesAction;
import org.eclipse.wst.xml.core.internal.document.AttrImpl;
import org.eclipse.wst.xml.ui.internal.nsedit.CommonEditNamespacesTargetFieldDialog;
import org.eclipse.wst.xml.ui.internal.nsedit.CommonNamespaceInfoTable;
import org.w3c.dom.Element;

public class NamespaceSection extends AbstractSection
{
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
		
		String nameString = WSDLEditorPlugin.getWSDLString("_UI_LABEL_NAME") + ":";
		String prefixString = WSDLEditorPlugin.getWSDLString("_UI_LABEL_PREFIX") + ":";
		String namespaceString = WSDLEditorPlugin.getWSDLString("_UI_LABEL_TARGET_NAMESPACE");
		GC gc = new GC(parent);
		int xoffset = Math.max(115, gc.textExtent(nameString).x + 20); // adds 20 due to borders
		xoffset = Math.max(xoffset, gc.textExtent(prefixString).x + 20); // adds 20 due to borders
		xoffset = Math.max(xoffset, gc.textExtent(namespaceString).x + 20); // adds 20 due to borders
		gc.dispose();

		// name
		CLabel nameLabel = getWidgetFactory().createCLabel(composite, nameString); //$NON-NLS-1$
		nameText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		
		// prefix
		CLabel prefixLabel = getWidgetFactory().createCLabel(composite, prefixString); //$NON-NLS-1$
		prefixText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$

		// targetnamespace
		CLabel targetNamespaceLabel = getWidgetFactory().createCLabel(composite, namespaceString); //$NON-NLS-1$
		targetNamespaceText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$

		// Advanced button
		button = getWidgetFactory().createButton(composite, WSDLEditorPlugin.getWSDLString("_UI_SECTION_ADVANCED_ATTRIBUTES") + "...", SWT.PUSH); //$NON-NLS-1$
		
		
		///////////////////// Labels
		// name layout
		FormData dataNameLabel = new FormData();
		dataNameLabel.top = new FormAttachment(nameText, 0, SWT.CENTER);
		nameLabel.setLayoutData(dataNameLabel);

		// prefix layout
		FormData dataPrefixLabel = new FormData();
		dataPrefixLabel.left = new FormAttachment(0, 0);
		dataPrefixLabel.top = new FormAttachment(prefixText, 0, SWT.CENTER);
		prefixLabel.setLayoutData(dataPrefixLabel);

		// targetNamespaceLabel layout
		FormData datatnsLabel = new FormData();
		datatnsLabel.left = new FormAttachment(0, 0);
    datatnsLabel.right = new FormAttachment(targetNamespaceText, 0);
		datatnsLabel.top = new FormAttachment(targetNamespaceText, 0, SWT.CENTER);
		targetNamespaceLabel.setLayoutData(datatnsLabel);
		

		///////////////////// Text Fields
		// name text field
		FormData dataNameText = new FormData();
		dataNameText.left = new FormAttachment(0, xoffset);
		dataNameText.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		dataNameText.top = new FormAttachment(0, 0);
		nameText.setLayoutData(dataNameText);
		nameText.addListener(SWT.Modify, this);

		// prefix text field
		FormData dataPrefixText = new FormData();
		dataPrefixText.left = new FormAttachment(0, xoffset);
		dataPrefixText.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		dataPrefixText.top = new FormAttachment(nameText, +ITabbedPropertyConstants.VSPACE);
		prefixText.setLayoutData(dataPrefixText);
		prefixText.addListener(SWT.Modify, this);

		// targetnamespace text field
		FormData data = new FormData();
		data.left = new FormAttachment(0, xoffset);
		data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(prefixText, +ITabbedPropertyConstants.VSPACE);
		targetNamespaceText.setLayoutData(data);
		targetNamespaceText.addListener(SWT.Modify, this);
		
		// Advanced button layout
		FormData dataButton = new FormData();
//		dataButton.left = new FormAttachment(100, -rightMarginSpace + 2);
		dataButton.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		dataButton.top = new FormAttachment(targetNamespaceText, +ITabbedPropertyConstants.VSPACE);
//		dataButton.top = new FormAttachment(targetNamespaceText, 0, SWT.CENTER);
		button.setLayoutData(dataButton);
		button.addSelectionListener(this);
	}

	public void doHandleEvent(Event event)
	{
		handlingEvent = true;
	  if (event.widget == nameText) {
	  	Object obj = getElement();
	  	if (obj instanceof Definition) {
	  		Definition definition = (Definition) obj;
	  		String uri = "";
	  		if (definition.getQName() != null) {
	  			uri = definition.getQName().getNamespaceURI();
	  		}
	  		definition.setQName(new QName(uri, nameText.getText()));
	  	}
	  }
	  else if (event.widget == prefixText) {
	  	Object obj = getElement();
	  	if (obj instanceof Definition) {
	  		Definition definition = (Definition) obj;
	  		Element element = definition.getElement();
	  		
	  		// Remove the old prefix
	  		String oldPrefix = definition.getPrefix(definition.getTargetNamespace());
	  		element.removeAttribute("xmlns:"+oldPrefix); 
	  		
	  		// Set the new prefix
	  	  	element.setAttribute("xmlns:" + prefixText.getText(), definition.getTargetNamespace());
	  	}
	  }
	  else if (event.widget == targetNamespaceText)
	  {
		  Object obj = getElement();
		  if (obj instanceof Definition)
		  {
		    Definition definition = (Definition)obj;
		    String newValue = targetNamespaceText.getText();
		    String prefix = definition.getPrefix(definition.getTargetNamespace());
		    definition.setTargetNamespace(newValue);
		    definition.getElement().setAttribute("xmlns:" + prefix, newValue);
		  }
	  }
	  handlingEvent = false;		
	}
	
	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh()
	{
    super.refresh();

    if (targetNamespaceText.isFocusControl() || handlingEvent)
    {
      return;
    }
	  setListenerEnabled(false);  
	  Object obj = getElement();
	  if (obj instanceof Definition)
	  {
	    Definition definition = (Definition)obj;
	    String targetNS = definition.getTargetNamespace();
	    
		// set targetnamespace field and prefix field
	    if (targetNS != null)
	    {
	    	targetNamespaceText.setText(targetNS);
	    	
	    	Element element = definition.getElement();

        String newPrefix = definition.getPrefix(targetNS);
        if (newPrefix == null) newPrefix = "";
        // TODO: remove this code
        if (element != null)
        {
  	    	for (int index = 0; index < element.getAttributes().getLength(); index++) {
  	    		AttrImpl attr = (AttrImpl) element.getAttributes().item(index);
  	    		String nodeName = attr.getNodeName();
  	    		String nsValue = attr.getNodeValue();
  	    		if (nsValue.equals(targetNS)) {
  	    			if (nodeName.indexOf(":") != -1) {
  	    				String xmlnsString = nodeName.substring(0, nodeName.indexOf(":"));
  	    				
  	    				if (xmlnsString.equals("xmlns")) {
  	    	    			newPrefix = attr.getLocalName();
  	    	    			break;
  	    				}
  	    			}
  	    		}
  	    	}
        }
	    	prefixText.setText(newPrefix);     
	    }
	  }
	  
	  // set name field
	  if (getElement() != null)
	  {
	    if (getElement().getElement()!= null)
	    {
	      String name = getElement().getElement().getAttribute("name"); //$NON-NLS-1$
	      if (name==null) name="";
	      nameText.setText(name);
	    }
	  }

    setListenerEnabled(true);
	}
	
	
  public void widgetSelected(SelectionEvent e)
  {
    if (e.widget == button)
    {
      Object obj = getElement();
      if (obj instanceof Definition)
      {
        Definition definition = (Definition)obj;
        EditNamespacesAction action = new EditNamespacesAction(definition);
      	action.run();
        refresh();
      }
    }
  }

}

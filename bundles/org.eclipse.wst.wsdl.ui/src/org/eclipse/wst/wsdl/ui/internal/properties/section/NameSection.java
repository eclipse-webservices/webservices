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

import javax.xml.namespace.QName;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertyConstants;
import org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.SmartRenameAction;


public class NameSection extends AbstractSection
{

	Text nameText;

	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent,TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		composite =	getWidgetFactory().createFlatFormComposite(parent);
		FormData data;
		

		nameText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		nameText.setLayoutData(data);

		CLabel nameLabel = getWidgetFactory().createCLabel(composite, WSDLEditorPlugin.getWSDLString("_UI_LABEL_NAME") + ":"); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(nameText, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(nameText, 0, SWT.CENTER);
		nameLabel.setLayoutData(data);
		
		nameText.addListener(SWT.Modify, this);
	}

	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh()
	{
    super.refresh();
    if (nameText.isFocusControl())
    {
      return;
    }
    setListenerEnabled(false);
	  nameText.setText(""); //$NON-NLS-1$
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
	
  public boolean shouldUseExtraSpace()
  {
    return false;
  }

  public void doHandleEvent(Event event)
  {
    if (event.widget == nameText && !nameText.isDisposed()) {
        String newValue = nameText.getText();
        Object wsdlElement = getElement();

        if (wsdlElement instanceof Definition) {
        	Definition definition = (Definition) wsdlElement;
        	String uri = "";
        	if (definition.getQName() != null) {
        		uri = definition.getQName().getNamespaceURI();	
        	}
        	definition.setQName(new QName(uri, newValue));
        }
        else if (wsdlElement instanceof Service) {        
        	Service service = (Service) wsdlElement;
        	String uri = service.getQName().getNamespaceURI(); 
        	service.setQName(new QName(uri, newValue));
        }
        else if (wsdlElement instanceof Port) {
        	new SmartRenameAction(wsdlElement, newValue).run();
//        	((Port) wsdlElement).setName(newValue);
        }
        else if (wsdlElement instanceof Binding) {
        	Binding binding = (Binding) wsdlElement;
        	String uri = binding.getQName().getNamespaceURI();
        	binding.setQName(new QName(uri, newValue));
        }
        else if (wsdlElement instanceof BindingOperation) {
        		((BindingOperation) wsdlElement).setName(newValue);
        }
        else if (wsdlElement instanceof BindingInput) {
        	if(((BindingInput) wsdlElement).getName() == null && newValue.equals("")) //$NON-NLS-1$
        		return;
        	
        	((BindingInput) wsdlElement).setName(newValue);
        }
        else if (wsdlElement instanceof BindingOutput) {
        	if (((BindingOutput) wsdlElement).getName() == null && newValue.equals("")) //$NON-NLS-1$
        		return;
        	
        	((BindingOutput) wsdlElement).setName(newValue);
        }
        else if (wsdlElement instanceof BindingFault) {
        	((BindingFault) wsdlElement).setName(newValue);
        }
        else if (wsdlElement instanceof PortType) {
        	PortType portType = (PortType) wsdlElement;
        	String uri = portType.getQName().getNamespaceURI();
        	portType.setQName(new QName(uri, newValue));
        }
        else if (wsdlElement instanceof Operation) {
        	new SmartRenameAction(wsdlElement, newValue).run();
//        	((Operation) wsdlElement).setName(newValue);
        }
        else if (wsdlElement instanceof MessageReference) {
        	if (((MessageReference) wsdlElement).getName() == null && newValue.equals("")) //$NON-NLS-1$
        		return;
        	
        	new SmartRenameAction(wsdlElement, newValue).run();
//        	((MessageReference) wsdlElement).setName(newValue);
        }
        else if (wsdlElement instanceof Message) {
/*
        	Message message = (Message) wsdlElement;
        	String uri = message.getQName().getNamespaceURI();
        	message.setQName(new QName(uri, newValue));
        	*/
        	new SmartRenameAction(wsdlElement, newValue).run();
        }
        else if (wsdlElement instanceof Part) {
//        	((Part) wsdlElement).setName(newValue);
        	new SmartRenameAction(wsdlElement, newValue).run();
        }
    }
  }
}

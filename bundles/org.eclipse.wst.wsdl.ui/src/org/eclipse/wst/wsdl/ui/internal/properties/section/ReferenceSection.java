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

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.common.ui.properties.ITabbedPropertyConstants;
import org.eclipse.wst.common.ui.properties.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.dialogs.InvokeSetDialog;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.viewers.widgets.ComponentNameComboHelper;

public class ReferenceSection extends AbstractSection
{
  protected CCombo componentNameCombo; 
  protected ComponentNameComboHelper componentNameComboHelper;
  Button button;
  IEditorPart editorPart;
  CLabel messageLabel;
  
	/**
	 * @see org.eclipse.wst.common.ui.properties.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		composite = getWidgetFactory().createFlatFormComposite(parent);
		FormData data;
    
    messageLabel = getWidgetFactory().createCLabel(composite, WSDLEditorPlugin.getWSDLString("_UI_LABEL_MESSAGE")); //$NON-NLS-1$
    componentNameCombo = getWidgetFactory().createCCombo(composite);
    componentNameCombo.setBackground(composite.getBackground());
    button = getWidgetFactory().createButton(composite, "", SWT.PUSH); //$NON-NLS-1$
    button.setImage(WSDLEditorPlugin.getInstance().getImage("icons/browsebutton.gif")); //$NON-NLS-1$
    button.addSelectionListener(this);

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(componentNameCombo, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(componentNameCombo, 0, SWT.CENTER);
		messageLabel.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(100, -rightMarginSpace + 2);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(componentNameCombo, 0, SWT.CENTER);
		button.setLayoutData(data);
    
    data = new FormData();
    data.left = new FormAttachment(0, 100);
    data.right = new FormAttachment(button, 0);
    componentNameCombo.setLayoutData(data);
    
    componentNameCombo.addListener(SWT.Modify, this);
	}

	/*
	 * @see org.eclipse.wst.common.ui.properties.view.ITabbedPropertySection#refresh()
	 */
	public void refresh()
	{
    super.refresh();
	  setListenerEnabled(false);  
	  componentNameCombo.removeListener(SWT.Modify, this);
	  Object obj = getElement();
	  if (obj instanceof MessageReference)
	  {
	    messageLabel.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_MESSAGE")); //$NON-NLS-1$
	    componentNameComboHelper = new ComponentNameComboHelper(componentNameCombo)
	    {
	      protected List getComponentNameList(ComponentReferenceUtil util)
	      {
	        return util.getMessageNames();
	      }

	      protected String getAttributeName()
	      {
	        return WSDLConstants.MESSAGE_ATTRIBUTE;
	      }
	    };

	  }
	  else if (obj instanceof Binding)
	  {
	    componentNameComboHelper = new ComponentNameComboHelper(componentNameCombo)
	    {
	      protected List getComponentNameList(ComponentReferenceUtil util)
	      {
	        return util.getPortTypeNames();
	      }

	      protected String getAttributeName()
	      {
	        return WSDLConstants.TYPE_ATTRIBUTE;
	      }
	    };

	    messageLabel.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_PORTTYPE")); //$NON-NLS-1$
	  }
	  else if (obj instanceof Port)
	  {
	    componentNameComboHelper = new ComponentNameComboHelper(componentNameCombo)
	    {
	      protected List getComponentNameList(ComponentReferenceUtil util)
	      {
	        return util.getBindingNames();
	      }

	      protected String getAttributeName()
	      {
	        return WSDLConstants.BINDING_ATTRIBUTE;
	      }
	    };
	    messageLabel.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_BINDING")); //$NON-NLS-1$
	  }
	  
    componentNameComboHelper.update(getElement());
    setListenerEnabled(true);
    componentNameCombo.addListener(SWT.Modify, this);
	}

  public void widgetSelected(SelectionEvent e)
  {
    if (e.widget == button)
    {
        InvokeSetDialog dialog = new InvokeSetDialog();
        dialog.run(getElement(), editorPart);
        
        WSDLEditor editor = (WSDLEditor) editorPart;
        editor.getSelectionManager().setSelection(new StructuredSelection(getElement()));
    }
  }
	
  public void handleEvent(Event event)
  {
    componentNameComboHelper.handleEventHelper(getElement().getElement(), event);
  }
  
  public void setEditorPart(IEditorPart editorPart)
  {
    this.editorPart = editorPart;
  }

}

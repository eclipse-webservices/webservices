/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.contentgenerator.ui;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.binding.http.internal.generator.HTTPContentGenerator;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.w3c.dom.Element;


public class HttpBindingOptionsPage extends BaseContentGeneratorOptionsPage 
{
  protected Button getButton;
  protected Button postButton;
  
  public Composite createControl(Composite parent)
  {
    control = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    control.setLayout(layout);

    Label separator = new Label(control, SWT.SEPARATOR | SWT.HORIZONTAL);
    GridData gd= new GridData();
    gd.horizontalAlignment= GridData.FILL;
    gd.grabExcessHorizontalSpace= true;
   
    separator.setLayoutData(gd);

    Label optionsHeading = new Label(control, SWT.NONE);
    optionsHeading.setText(Messages._UI_LABEL_HTTP_BINDING_OPTIONS); //$NON-NLS-1$

    getButton = new Button(control, SWT.RADIO);
    getButton.setText("HTTP GET"); //$NON-NLS-1$
    getButton.setSelection(true);

    postButton = new Button(control, SWT.RADIO);
    postButton.setText("HTTP POST"); //$NON-NLS-1$

    if (generator.getName() != null)
    {
      Definition definition = generator.getDefinition();
      QName qname = new QName(definition.getTargetNamespace(), generator.getName());
      Binding binding = (Binding) definition.getBinding(qname);
      if (binding != null)
      {
        List eeList = binding.getEExtensibilityElements();
        if (eeList.size() > 0)
        {
          ExtensibilityElement ee = (ExtensibilityElement) eeList.get(0);
          Element element = WSDLEditorUtil.getInstance().getElementForObject(ee);
          String verb = element.getAttribute("verb"); //$NON-NLS-1$
          if ("POST".equals(verb)) //$NON-NLS-1$
          {
            getButton.setSelection(false);
            postButton.setSelection(true);
          }
        }
      }
    }

    postButton.addSelectionListener(this);
    getButton.addSelectionListener(this);
    setOptionsOnGenerator();
	
    return control;
  }

  public void setOptionsOnGenerator() {
	  if (generator.getContentGenerator() instanceof HTTPContentGenerator) {
		  HTTPContentGenerator httpGenerator = (HTTPContentGenerator) generator.getContentGenerator();

		  if (postButton.getSelection()) {
			  httpGenerator.setVerb(HTTPContentGenerator.VERB_POST);
		  }	  
		  else if (getButton.getSelection()) {
			  httpGenerator.setVerb(HTTPContentGenerator.VERB_GET);
		  }
	  }
  }
  
  public String getMessage() {
	  if (getWSIPreferences().equals(PersistentWSIContext.STOP_NON_WSI)) {
		  return Messages._ERROR_WSI_COMPLIANCE_SOAP_PROTOCOL;
	  }
	  else if (getWSIPreferences().equals(PersistentWSIContext.WARN_NON_WSI)) {
		  return Messages._WARN_WSI_COMPLIANCE_SOAP_PROTOCOL;
	  }
	  
	  return ""; //$NON-NLS-1$
  }

  public int getMessageType() {
	  if (getWSIPreferences().equals(PersistentWSIContext.STOP_NON_WSI)) {
		  return IMessageProvider.ERROR;
	  }
	  else if (getWSIPreferences().equals(PersistentWSIContext.WARN_NON_WSI)) {
		  return IMessageProvider.WARNING;
	  }
	  
	  return IMessageProvider.NONE;
  }
}
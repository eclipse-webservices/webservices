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
package org.eclipse.wst.wsdl.ui.internal.contentgenerator.ui;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.AbstractGenerator;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.w3c.dom.Element;


public class HttpBindingOptionsPage implements ContentGeneratorOptionsPage, SelectionListener
{
  protected Button getButton;
  protected Button postButton;
  protected Composite control;
  protected AbstractGenerator generator;

  public HttpBindingOptionsPage()
  {
  }

  public void init(AbstractGenerator generator)
  {
    this.generator = generator;
  }

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
    optionsHeading.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_HTTP_BINDING_OPTIONS"));

    getButton = new Button(control, SWT.RADIO);
    getButton.setText("HTTP GET");
    getButton.setSelection(true);

    postButton = new Button(control, SWT.RADIO);
    postButton.setText("HTTP POST");

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
          String verb = element.getAttribute("verb");
          if ("POST".equals(verb))
          {
            getButton.setSelection(false);
            postButton.setSelection(true);
          }
        }
      }
    }

    postButton.addSelectionListener(this);
    getButton.addSelectionListener(this);
	computeOptions();
	
    return control;
  }
  
  public boolean isOverwriteApplicable()
  {
  	return true;
  }  

  public void widgetSelected(SelectionEvent event)
  {
	computeOptions();
  }

  protected void computeOptions()
  {
    Object[] options = new Object[1];
    options[0] = new Boolean(postButton.getSelection());
    generator.setOptions(options);
  }

  public void widgetDefaultSelected(SelectionEvent event)
  {
  }
}

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


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.AbstractGenerator;


public class HttpPortOptionsPage implements ContentGeneratorOptionsPage, ModifyListener
{
  protected Text addressField;
  protected Composite control;
  protected AbstractGenerator generator;

  public HttpPortOptionsPage()
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
    control.setLayoutData(new GridData(GridData.FILL_BOTH));

    Label separator = new Label(control, SWT.SEPARATOR | SWT.HORIZONTAL);
    GridData gd= new GridData();
    gd.horizontalAlignment= GridData.FILL;
    gd.grabExcessHorizontalSpace= true;
    separator.setLayoutData(gd);

    Label optionsHeading = new Label(control, SWT.NONE);
    optionsHeading.setText(WSDLEditorPlugin.getWSDLString("_UI_HTTP_PORT_DETAILS"));

    Composite nameValueGroup = new Composite(control, SWT.NONE);
    nameValueGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
    GridLayout layout2 = new GridLayout(2, false);
    layout2.marginWidth = 0;
    nameValueGroup.setLayout(layout2);

    Label addressLabel = new Label(nameValueGroup, SWT.NONE);
    addressLabel.setText(WSDLEditorPlugin.getWSDLString("_UI_LOCATION") + " ");
    addressField = new Text(nameValueGroup, SWT.BORDER);
    addressField.setText("http://example.com/");
    addressField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    addressField.addModifyListener(this);

    return control;
  }

  public boolean isOverwriteApplicable()
  {
    return false;
  }

  public void modifyText(ModifyEvent e)
  {
    Object[] options = new Object[2];
    options[1] = addressField.getText();
    generator.setOptions(options);
  }
}

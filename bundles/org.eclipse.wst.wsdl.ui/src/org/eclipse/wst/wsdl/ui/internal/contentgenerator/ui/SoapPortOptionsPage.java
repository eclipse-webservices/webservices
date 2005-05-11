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
import org.eclipse.wst.wsdl.binding.soap.internal.generator.SOAPContentGenerator;
import org.eclipse.wst.wsdl.internal.generator.BaseGenerator;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;


public class SoapPortOptionsPage implements ContentGeneratorOptionsPage, ModifyListener
{
  protected Text addressField;
  protected Composite control;
  protected BaseGenerator generator;

  public SoapPortOptionsPage()
  {
  }

  public void init(BaseGenerator generator)
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
    optionsHeading.setText(WSDLEditorPlugin.getWSDLString("_UI_SOAP_PORT_DETAILS"));

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
	  computeOptions();
  }
  
  private void computeOptions() {
	if (generator.getContentGenerator() instanceof SOAPContentGenerator) {
		((SOAPContentGenerator) generator.getContentGenerator()).setAddressLocation(addressField.getText());
	}
  }
  
  public void setOptionsOnGenerator() {
	  computeOptions();
  }
  
  public Composite getControl() {
	  return control;
  }
}

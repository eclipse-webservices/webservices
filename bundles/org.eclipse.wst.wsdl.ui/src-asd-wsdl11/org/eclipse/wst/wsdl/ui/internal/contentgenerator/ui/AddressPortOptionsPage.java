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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.wsdl.internal.generator.BaseGenerator;
import org.eclipse.wst.wsdl.internal.generator.PortGenerator;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.wizards.ContentGeneratorOptionsPage;

public class AddressPortOptionsPage implements ContentGeneratorOptionsPage, ModifyListener
{
  protected Text addressField;
  protected Composite control;
  protected PortGenerator generator;
  protected WizardPage wizardPage;

  public void init(BaseGenerator generator){
	  if (generator instanceof PortGenerator) {
		  this.generator = (PortGenerator) generator;	  
	  }
  }
  
  public void setWizardPage(WizardPage wizardPage) {
	  this.wizardPage = wizardPage;
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

    Label addressLabel = new Label(control, SWT.NONE);
    addressLabel.setText(Messages._UI_LABEL_ADDRESS + ":"); //$NON-NLS-1$
    addressField = new Text(control, SWT.BORDER);
    addressField.setText("http://example.org/"); //$NON-NLS-1$
    addressField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    addressField.addModifyListener(this);

    generator.setAddressLocation(addressField.getText());
    
    return control;
  }

  public boolean isOverwriteApplicable()
  {
    return false;
  }

  public void modifyText(ModifyEvent e)
  {
    generator.setAddressLocation(addressField.getText());
  }

  public void setOptionsOnGenerator() {
  }
  
  public Composite getControl() {
	  return control;
  }
}
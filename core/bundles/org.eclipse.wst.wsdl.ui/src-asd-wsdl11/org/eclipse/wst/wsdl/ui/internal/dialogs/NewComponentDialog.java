/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.dialogs;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.wsdl.ui.internal.Messages;

/*
 * TODO: rmah: look at removing this class.  Doesn't look like it's used.
 */
public class NewComponentDialog extends Dialog implements ModifyListener
{
  protected Text nameField; 
  protected Button okButton;
  protected String name;                             
  protected String title;
  protected Label errorMessageLabel;
  protected List usedNames;

  public NewComponentDialog(Shell parentShell, String title, String defaultName) 
  {
    super(parentShell);
    setShellStyle(getShellStyle() | SWT.RESIZE);
    name = defaultName;      
    this.title = title;
  }
  
  public NewComponentDialog(Shell parentShell, String title, String defaultName, List usedNames) 
  {
    super(parentShell);
    setShellStyle(getShellStyle() | SWT.RESIZE);
    name = defaultName;      
    this.title = title;
    this.usedNames = usedNames;
  }

  public int createAndOpen()
  {
    create();
    getShell().setText(title);
    setBlockOnOpen(true);
    return open();
  }

  protected Control createContents(Composite parent)  
  {
    Control control = super.createContents(parent);
    nameField.forceFocus();
    nameField.selectAll();  
    updateErrorMessage();
    return control;
  }


  protected void createButtonsForButtonBar(Composite parent) 
  {
    okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  protected void createHeaderContent(Composite parent)
  {
  }
  
  protected void createExtendedContent(Composite parent)
  {
  }

  protected Control createDialogArea(Composite parent) 
  {
    Composite dialogArea = (Composite)super.createDialogArea(parent);
    
    createHeaderContent(dialogArea);

    Composite composite = new Composite(dialogArea, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    layout.marginWidth = 0;
    composite.setLayout(layout);

    GridData gdFill= new GridData();
    gdFill.horizontalAlignment= GridData.FILL;
    gdFill.grabExcessHorizontalSpace= true;
    gdFill.verticalAlignment= GridData.FILL;
    gdFill.grabExcessVerticalSpace= true;
    composite.setLayoutData(gdFill);

    Label nameLabel = new Label(composite, SWT.NONE);
    nameLabel.setText(Messages._UI_LABEL_NAME); //$NON-NLS-1$

    nameField = new Text(composite, SWT.SINGLE | SWT.BORDER);
    GridData gd= new GridData();
    gd.horizontalAlignment= GridData.FILL;
    gd.grabExcessHorizontalSpace= true;
    gd.widthHint = 200;
    nameField.setLayoutData(gd);
    nameField.setText(name);
    nameField.addModifyListener(this);

    createExtendedContent(dialogArea);

    // error message
    errorMessageLabel = new Label(dialogArea, SWT.NONE);
    errorMessageLabel.setText("");          //$NON-NLS-1$
    GridData gd2 = new GridData();
    gd2.horizontalAlignment= GridData.FILL;
    gd2.grabExcessHorizontalSpace= true;
    gd2.widthHint = 200;
    errorMessageLabel.setLayoutData(gd2);          
//    Color color = new Color(errorMessageLabel.getDisplay(), 200, 0, 0);
//    errorMessageLabel.setForeground(color);

    return dialogArea;
  }
  
  public void modifyText(ModifyEvent e) 
  {                        
    updateErrorMessage();
  }        

  protected String computeErrorMessage(String name)
  {
  	if (usedNames == null)
  		return null;
  	
  	Iterator iterator = usedNames.iterator();
  	while (iterator.hasNext()) {
  		if (name.equalsIgnoreCase((String) iterator.next())) {
  			return Messages._UI_LABEL_WARNING_DUPLICATE_NAME_EXISTS; //$NON-NLS-1$
  		}
  	}
  	
  	return null;
  }

  protected void updateErrorMessage()
  {                 
    String errorMessage = null;
    String name = nameField.getText().trim();
    if (name.length() > 0)
    {                                
      errorMessage = computeErrorMessage(name);
    }   
    else
    {
      errorMessage = ""; //$NON-NLS-1$
    }  
    errorMessageLabel.setText(errorMessage != null ? errorMessage : ""); //$NON-NLS-1$
//    okButton.setEnabled(errorMessage == null);
  }
 
  protected void buttonPressed(int buttonId) 
  {
    if (buttonId == IDialogConstants.OK_ID)
    {
      name = nameField.getText();
    }
    super.buttonPressed(buttonId);
  }

  public String getName()
  {
    return name;
  }
}


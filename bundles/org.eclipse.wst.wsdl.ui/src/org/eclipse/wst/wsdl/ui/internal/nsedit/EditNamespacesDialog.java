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
package org.eclipse.wst.wsdl.ui.internal.nsedit;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.xml.ui.nsedit.CommonEditNamespacesTargetFieldDialog;

public class EditNamespacesDialog extends Dialog
{
  protected Button okButton;
  protected String title;
  protected String targetNamespace;
  protected List namespaceInfoList;
  protected CommonEditNamespacesTargetFieldDialog editWSDLNamespacesControl;
  protected IPath resourceLocation;
  //  protected Label errorMessageLabel;
  //  protected String errorMessage;  

  //protected Label errorMessageLabel;

  public EditNamespacesDialog(Shell parentShell, IPath resourceLocation, String title, String targetNamespace, List namespaceList)
  {
    super(parentShell);
    this.resourceLocation = resourceLocation;
    setShellStyle(getShellStyle() | SWT.RESIZE);
    this.title = title;
    this.targetNamespace = targetNamespace;
    this.namespaceInfoList = namespaceList;
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
    updateErrorMessage();
    return control;
  }

  protected void createButtonsForButtonBar(Composite parent)
  {
    okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  protected Control createDialogArea(Composite parent)
  {
    Composite dialogArea = (Composite) super.createDialogArea(parent);
    editWSDLNamespacesControl = new CommonEditNamespacesTargetFieldDialog(dialogArea, resourceLocation);
    editWSDLNamespacesControl.setNamespaceInfoList(namespaceInfoList);
    editWSDLNamespacesControl.setTargetNamespace(targetNamespace);

    return dialogArea;
  }

  public void modifyText(ModifyEvent e)
  {
    updateErrorMessage();
  }

  protected String computeErrorMessage(String name)
  {
    return null;
  }

  protected void updateErrorMessage()
  {
    String errorMessage = null;
    /*
    String name = nameField.getText().trim();
    if (name.length() > 0)
    {
      errorMessage = computeErrorMessage(name);
    }
    else
    {
      errorMessage = "";
    }*/

    //errorMessageLabel.setText(errorMessage != null ? errorMessage : "");
    okButton.setEnabled(errorMessage == null);
  }

  protected void buttonPressed(int buttonId)
  {
    if (buttonId == IDialogConstants.OK_ID)
    {
      targetNamespace = editWSDLNamespacesControl.getTargetNamespace();
    }
    super.buttonPressed(buttonId);
  }

  public List getNamespaceInfoList()
  {
    return namespaceInfoList;
  }

  public String getTargetNamespace()
  {
    return targetNamespace;
  }
}

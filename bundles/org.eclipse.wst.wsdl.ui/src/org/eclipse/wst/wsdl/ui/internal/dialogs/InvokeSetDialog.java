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
package org.eclipse.wst.wsdl.ui.internal.dialogs;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.wizards.SetBindingWizard;
import org.eclipse.wst.wsdl.ui.internal.wizards.SetComponentWizard;
import org.eclipse.wst.wsdl.ui.internal.wizards.SetMessageWizard;
import org.eclipse.wst.wsdl.ui.internal.wizards.SetPortTypeWizard;

public class InvokeSetDialog
{
  String kind;
  String newValue = "";
  
  public void setReferenceKind(String kind)
  {
    this.kind = kind;
  }
  
  public String getValue()
  {
    return newValue; 
  }
  
  public void run(Object input, IEditorPart editorPart)
  {
    if (input instanceof Binding)
    {
      SetPortTypeWizard wiz = new SetPortTypeWizard(input, editorPart);
      SetXWizardDialog dialog = new SetXWizardDialog(WSDLEditorPlugin.getShell(), wiz);
      dialog.setBlockOnOpen(true);
      dialog.create();
      dialog.open();
    }
    else if (input instanceof Port)
    {
      SetBindingWizard wiz = new SetBindingWizard(input, editorPart);
      SetXWizardDialog dialog = new SetXWizardDialog(WSDLEditorPlugin.getShell(), wiz);
      dialog.create();
      dialog.open();
    }
    else if (input instanceof Part)
    {
    	boolean kkk = false;
    	if (kkk == true) {
      SetComponentWizard wiz = new SetComponentWizard((Part)input, editorPart);
      SetXWizardDialog dialog = new SetXWizardDialog(WSDLEditorPlugin.getShell(), wiz);
      wiz.setReferenceKind(kind);
      dialog.create();
      dialog.open();
    	}
    	else {
  	WSDLSetTypeDialog typeDialog = new WSDLSetTypeDialog(WSDLEditorPlugin.getShell(), (Part) input, editorPart, WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_TYPE"), kind);
  	typeDialog.setBlockOnOpen(true);
  	typeDialog.create();
  	int result = typeDialog.open();
    }
    }
    else if (input instanceof Input || input instanceof Output || input instanceof Fault)
    {
      SetMessageWizard wiz = new SetMessageWizard(input, editorPart);
      SetXWizardDialog dialog = new SetXWizardDialog(WSDLEditorPlugin.getShell(), wiz);
      dialog.create();
      dialog.open();
    }
    else if (input == null)
    {
      //error
    }
  }

  class SetXWizardDialog extends WizardDialog
  {
    public SetXWizardDialog(Shell parentShell, IWizard newWizard)
    {
      super(parentShell, newWizard);
      setShellStyle(SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.RESIZE);
    } 
  }
}

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
package org.eclipse.wst.wsdl.ui.internal.viewers.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.dialogs.InvokeSetDialog;

// instead of using a Button directly (which causes sizing/layout problems)
// we use this class
public class InvokeDialogButton extends Composite implements SelectionListener
{ 
  protected Button button;
  protected Object input;
  protected Composite parent;
  protected IEditorPart editorPart;
  protected String kind;
                                             
  public InvokeDialogButton(Composite parent, Object input)
  {
    super(parent, SWT.NONE);
    this.parent = parent;
    this.input = input;
    GridData gd = new GridData();
    gd.grabExcessHorizontalSpace = false;
    gd.grabExcessVerticalSpace = false;
    setLayoutData(gd);
    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    setLayout(layout);

    button = new Button(this, SWT.NONE);
    gd = new GridData();
    gd.grabExcessHorizontalSpace = false;
    gd.heightHint = 17;
    gd.widthHint = 24;
    button.setLayoutData(gd);
    button.setText("...");
    button.addSelectionListener(this);
  }                         

  public Button getButton() 
  {
    return button;
  }
  
  public void setInput(Object input)
  {
    this.input = input;
  }

  public void setEditor(IEditorPart editorPart)
  {
    this.editorPart = editorPart; 
  }
  
  public void setReferenceKind(String kind)
  {
    this.kind = kind;
  }
  
  public void widgetSelected(SelectionEvent e)
  {
    if (e.widget == button)
    {
      InvokeSetDialog dialog = new InvokeSetDialog();
      if (input instanceof Part)
      {
        dialog.setReferenceKind(kind);
      }
      dialog.run(input, editorPart);
    }
  }

  public void widgetDefaultSelected(SelectionEvent e)
  {
    
  }
}
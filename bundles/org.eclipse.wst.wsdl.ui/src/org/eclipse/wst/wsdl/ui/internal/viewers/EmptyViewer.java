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
package org.eclipse.wst.wsdl.ui.internal.viewers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

public class EmptyViewer extends BaseViewer
{                                      
  protected Composite control;

  public EmptyViewer(Composite parent, int style)
  {
    super(getStatusLineManager(getActiveEditor()));
    createControl(parent);
  }

  public void createControl(Composite parent)
  {
    control = new Composite(parent, SWT.NONE);

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    control.setLayout(layout);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    control.setLayoutData(data);
  }                                    

  public Control getControl()
  {
    return control;
  }

  public void doSetInput(Object input)
  {
    
  }

  /*
   * @see BaseWindow#doHandleEvent(Event)
   */
  public void doHandleEvent(Event event)
  {
    if (event.type == SWT.Modify)
    {
    }
  }  
}
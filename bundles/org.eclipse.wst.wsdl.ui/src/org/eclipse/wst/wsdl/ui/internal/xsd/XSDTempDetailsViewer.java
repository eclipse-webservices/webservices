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
package org.eclipse.wst.wsdl.ui.internal.xsd;
                                 
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.wst.wsdl.ui.internal.viewers.BaseViewer;
       

public class XSDTempDetailsViewer extends BaseViewer
{   
  protected Composite control;

  public XSDTempDetailsViewer(Composite parent)
  { 
    super(null);
    createControl(parent);
  }
              
  public void doSetInput(Object input)
  {    
  }

  public void createControl(Composite parent)
  {
    control = flatViewUtility.createComposite(parent, 1, true); 
                                      
    flatViewUtility.createFlatPageHeader(control, "XSDViewer");
  }
  
  protected void update()
  {
  }

  public Control getControl()
  {
    return control;
  }

  public void doHandleEvent(Event event)
  {

  }
}
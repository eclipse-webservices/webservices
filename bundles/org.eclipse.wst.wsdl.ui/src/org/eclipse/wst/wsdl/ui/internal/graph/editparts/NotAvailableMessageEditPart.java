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
package org.eclipse.wst.wsdl.ui.internal.graph.editparts;
                          
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;

              

public class NotAvailableMessageEditPart extends AbstractGraphicalEditPart 
{            
  public NotAvailableMessageEditPart()
  {
  }   
  
  protected IFigure createFigure()
  {                            
    Label label = new Label(WSDLEditorPlugin.getWSDLString("_UI_LABEL_VIEW_NOT_AVAILABLE"));
    return label;
  }    

  protected List getModelChildren() 
  {                  
    return Collections.EMPTY_LIST;
  }    

  protected EditPart createChild(Object model)
  {
    return null;
  }
 
  protected void createEditPolicies()
  {  
  }   
}
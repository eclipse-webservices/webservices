/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.editparts;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBindingOperation;

// This class is used to represent a BindingOperation, BindingInput, BindingOutput and BindingFault
//
public class BindingContentEditPart extends BaseEditPart
{  
  Label label;
  protected IFigure createFigure()
  {
    Figure figure = new Figure();
    figure.setOpaque(true);
    figure.setBackgroundColor(ColorConstants.tooltipBackground);
    ToolbarLayout toolbarLayout = new ToolbarLayout(true);
    toolbarLayout.setStretchMinorAxis(true);
    figure.setLayoutManager(toolbarLayout);
    label = new Label();
    label.setBorder(new MarginBorder(5, 5, 2, 2));    
    figure.add(label);
    return figure;
  }
  
  
  protected List getModelChildren()
  {
    return Collections.EMPTY_LIST;
  }  
  
  protected void refreshVisuals()
  {
    if (getModel() instanceof IBindingOperation)
    {  
      IBindingOperation bindingOperation = (IBindingOperation) getModel();
      label.setIcon(bindingOperation.getImage());
      label.setText(bindingOperation.getName());
    }  
    super.refreshVisuals();    
  }  
  
  protected void createEditPolicies()
  {    
  }
}

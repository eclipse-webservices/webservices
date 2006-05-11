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
package org.eclipse.wst.wsdl.ui.internal.asd.design.layouts;

import java.util.Iterator;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.BindingEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.INamedEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.BindingContentPlaceHolder;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBindingMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBindingOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IOperation;

/**
 * This class is used to layout the contents of the binding 'ruler' displayed
 * to the left side of an interface. The contents are aligned with the interface
 * figures so that correspoding binding and interface constructs are side by side.
 */
public class BindingContentLayout extends AbstractLayout
{
  BindingEditPart bindingEditPart;
  
  public BindingContentLayout(BindingEditPart bindingEditPart)
  {
    this.bindingEditPart = bindingEditPart;
  }

  protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint)
  {
    if (bindingEditPart.isExpanded())
    {  
      Rectangle clientArea = container.getClientArea();
      return new Dimension(clientArea.width, clientArea.height);
    }
    else
    {
      return new Dimension(0,0);
    }  
  }

  public void layout(IFigure container)
  {     
    Rectangle clientArea = container.getClientArea();
    for (Iterator i = bindingEditPart.getChildren().iterator(); i.hasNext();)
    {
      AbstractGraphicalEditPart childEditPart = (AbstractGraphicalEditPart) i.next();
      AbstractGraphicalEditPart correspondingEditPart = null;
      Object model = childEditPart.getModel();
      if (model instanceof IBindingOperation)
      {
        IOperation operation = ((IBindingOperation)model).getOperation();
        correspondingEditPart = getEditPart(operation);       
      }
      else if (model instanceof IBindingMessageReference)
      {
        IMessageReference messageReference = ((IBindingMessageReference)model).getMessageReference();
        correspondingEditPart = getEditPart(messageReference);       
      }
      else if (model instanceof BindingContentPlaceHolder) {
    	  Object correspondingModel = ((BindingContentPlaceHolder) model).getModel();
    	  correspondingEditPart = getEditPart(correspondingModel);
      }
      
      if (correspondingEditPart != null)
      {
        IFigure figure = correspondingEditPart.getFigure();       
        if (correspondingEditPart instanceof INamedEditPart)
        {
          figure = ((INamedEditPart)correspondingEditPart).getLabelFigure();            
        }  
        Rectangle bounds = figure.getBounds();
        Rectangle newBounds = new Rectangle(clientArea.x, bounds.y, clientArea.width, bounds.height);
        childEditPart.getFigure().setBounds(newBounds);
      }  
    }
  }  
  
  protected AbstractGraphicalEditPart getEditPart(Object model)
  {
    if (model != null)
    {  
      EditPart editPart = (EditPart)bindingEditPart.getViewer().getEditPartRegistry().get(model);
      if (editPart instanceof AbstractGraphicalEditPart)
      {
        return (AbstractGraphicalEditPart)editPart; 
      }
    }
    return null;
  }
}

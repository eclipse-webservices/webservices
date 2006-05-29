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
import java.util.List;
import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.BindingEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;

public class BindingColumnLayout extends AbstractLayout
{
  EditPart bindingColumnEditPart;
  
  public BindingColumnLayout(EditPart bindingColumnEditPart)
  {
    super();
    this.bindingColumnEditPart = bindingColumnEditPart;
  }

  protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint)
  {
    Rectangle clientArea = container.getClientArea();
    Dimension d = calculateChildrenSize(container.getChildren(), wHint, hHint);
    d.height = Math.max(d.height, clientArea.height);
    return d;
  }

  private Dimension calculateChildrenSize(List children, int wHint, int hHint)
  {
    Dimension childSize;
    IFigure child;
    int height = 0, width = 0;
    for (int i = 0; i < children.size(); i++)
    {
      child = (IFigure) children.get(i);
      childSize = child.getPreferredSize(wHint, hHint);
      height += childSize.height;
      height += 80;
      width = Math.max(width, childSize.width);
    }
    return new Dimension(Math.max(width, 150), height);
  }

  public void layout(IFigure container)
  {
    //System.out.println("BindingColumnLayout.layout()" + container.getChildren().size());
    Rectangle clientArea = container.getClientArea();
    Rectangle r = new Rectangle();
    r.x = clientArea.x + 50;
    r.y = clientArea.y;
    int used = 0;

    for (Iterator i = container.getChildren().iterator(); i.hasNext();)
    {
      Figure child = (Figure) i.next();
      Dimension d = child.getPreferredSize(-1, -1);
      r.width = d.width;
      r.height = d.height;
      
      child.setBounds(r);
      used += 30;
      used += r.height;
      
      IFigure intefaceFigure = getInterfaceFigureForExpandedEditPartFigure(child);
      if (intefaceFigure != null)
      {
        Rectangle b = intefaceFigure.getBounds();
        int width = 30;
        child.setBounds(new Rectangle(b.x - width, b.y, width, b.height));
      }
      
      r.y += d.height;
      r.y += 80;
    }
  }
  
  public void setExpanded(BindingEditPart bindingEditPart, boolean isExpanded)
  {
    // First run through all of the bindings are are expanded
    // to ensure at most one binding is expanded for each interface since
    // we may need to collapse a binding in order to expand this one.
    //
    IInterface interfaze = getInterface(bindingEditPart);    
    if (interfaze != null)
    {  
      bindingEditPart.setExpanded(isExpanded);
      if (isExpanded)
      {  
        for (Iterator i = bindingColumnEditPart.getChildren().iterator(); i.hasNext(); )
        {
          BindingEditPart otherBindingEditPart = (BindingEditPart)i.next();
          if (otherBindingEditPart != bindingEditPart &&
              interfaze == getInterface(otherBindingEditPart))
          {
            otherBindingEditPart.setExpanded(false);
          }  
        }
      }
    }  
  }
  
  private IFigure getInterfaceFigureForExpandedEditPartFigure(Figure editPartFigure)
  {
    for (Iterator i = bindingColumnEditPart.getChildren().iterator(); i.hasNext(); )
    {
      BindingEditPart bindingEditPart = (BindingEditPart)i.next();
      if (bindingEditPart.isExpanded() && bindingEditPart.getFigure() == editPartFigure)
      {
        AbstractGraphicalEditPart interfaceEditPart = getMatchingEditPart(getInterface(bindingEditPart));
        if (interfaceEditPart != null)
        {
          return interfaceEditPart.getFigure();
        }          
      }  
    }
    return null;
  }
  
  private IInterface getInterface(BindingEditPart editPart)
  {
    IBinding binding = (IBinding)editPart.getModel();
    return binding.getInterface();
  }
  
  private AbstractGraphicalEditPart getMatchingEditPart(IInterface interfaze)
  {
    return(AbstractGraphicalEditPart)bindingColumnEditPart.getViewer().getEditPartRegistry().get(interfaze);    
  }
}
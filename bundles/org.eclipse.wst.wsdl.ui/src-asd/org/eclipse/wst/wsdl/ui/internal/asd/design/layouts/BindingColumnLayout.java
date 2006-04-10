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

public class BindingColumnLayout extends AbstractLayout
{
  IFigure expandedBindingFigure;
  IFigure snapToFigure;

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
      
      if (child == expandedBindingFigure && snapToFigure != null)
      {
        Rectangle b = snapToFigure.getBounds();
        int width = 30;
        child.setBounds(new Rectangle(b.x - width, b.y, width, b.height));
      }
      
      r.y += d.height;
      r.y += 80;
    }
    /*
    int space = clientArea.height - used;
    int spacePerThingee = space / container.getChildren().size() - 1;
    boolean isFirst = true;
    for (Iterator i = container.getChildren().iterator(); i.hasNext();)
    {
      Figure child = (Figure) i.next();
      if (isFirst)
      {
        isFirst = false;
      }
      else if (child != expandedBindingFigure || snapToFigure == null)      
      {        
        child.getBounds().y += spacePerThingee;
      }  
    }*/
  }

  public IFigure getExpandedBindingFigure()
  {
    return expandedBindingFigure;
  }

  public void setExpandedBindingFigure(IFigure expandedBindingFigure)
  {
    this.expandedBindingFigure = expandedBindingFigure;
  }

  public IFigure getSnapToFigure()
  {
    return snapToFigure;
  }

  public void setSnapToFigure(IFigure snapToFigure)
  {
    this.snapToFigure = snapToFigure;
  }
}

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
package org.eclipse.wst.wsdl.asd.design.layouts;

import java.util.List;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.wst.wsdl.asd.design.editparts.BindingEditPart;

// just a toolbar layout with a minor tweak to ensure the last figure fills
// the entire available space
public class BindingLayout extends ToolbarLayout
{
  BindingEditPart editPart;
  
  public BindingLayout(BindingEditPart editPart)
  {
    this.editPart = editPart;    
  }
  
  protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint)
  {
    Dimension d = super.calculatePreferredSize(container, wHint, hHint);
    //Rectangle clientArea = container.getClientArea();
    //d.width = Math.max(d.width, clientArea.width);
    //d.height = Math.max(d.height, clientArea.height);
    return d;
  }
  
  public void layout(IFigure container)
  {
    super.layout(container);
    if (editPart.isExpanded())
    {  
      Rectangle clientArea = container.getClientArea();    
      List children = container.getChildren();
      if (children.size() > 1)
      {
        Figure header = (Figure)children.get(0);        
        Figure contentPane = (Figure)children.get(children.size() - 1);
        if (contentPane.getChildren().size() > 0)
        {  
          Rectangle bounds = contentPane.getBounds();
          bounds.height = clientArea.height - header.getBounds().height;
        }  
      }
    }
  }
}

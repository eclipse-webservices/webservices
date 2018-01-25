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
package org.eclipse.wst.wsdl.ui.internal.asd.design.figures;

import java.util.Iterator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

public class ListFigure extends Figure
{
  boolean isOpaque = false;
  boolean isHorizontal = false;
  boolean paintFirstLine = true;
  
  public ListFigure(boolean isHorizontal)
  {
    this.isHorizontal = isHorizontal; 
  }
  
  public ListFigure()
  {
    this(false);
  }

  public void paint(Graphics graphics)
  {
    super.paint(graphics);
    boolean isFirst = true;
    for (Iterator i = getChildren().iterator(); i.hasNext();)
    {
      Figure figure = (Figure) i.next();
      if (isFirst && !paintFirstLine)
      {
        isFirst = false;
      }
      else
      {
        Rectangle r = figure.getBounds();
        if (isHorizontal)
        {  
          graphics.drawLine(r.x, r.y, r.x, r.y + r.height);
        }
        else
        {  
          graphics.drawLine(r.x, r.y, r.x + r.width, r.y);
        }            
      }
    }
  }

  public boolean isOpaque()
  {
    return isOpaque;
  }

  public void setOpaque(boolean isOpaque)
  {
    this.isOpaque = isOpaque;
  }

  public boolean isPaintFirstLine()
  {
    return paintFirstLine;
  }

  public void setPaintFirstLine(boolean paintFirstLine)
  {
    this.paintFirstLine = paintFirstLine;
  }
}

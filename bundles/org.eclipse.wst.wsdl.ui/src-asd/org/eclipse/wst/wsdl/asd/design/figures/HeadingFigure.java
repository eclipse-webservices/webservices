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
package org.eclipse.wst.wsdl.asd.design.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class HeadingFigure extends Figure
{
  Label label;
  protected Color[] gradientColor = {ColorConstants.white,  
            ColorConstants.lightGray,
            ColorConstants.lightBlue,
            ColorConstants.gray};
  protected boolean isSelected = false;
  protected boolean isReadOnly = false;
  
  public HeadingFigure()
  {
    label = new Label();
    label.setBorder(new MarginBorder(2));
    ToolbarLayout toolbarLayout = new ToolbarLayout(false);
    toolbarLayout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
    setLayoutManager(toolbarLayout);
    add(label);
  }
  
  public void setSelected(boolean isSelected)
  {
    this.isSelected = isSelected;
  }
  
  public void setIsReadOnly(boolean isReadOnly)
  {
    this.isReadOnly = isReadOnly;
  }
  
  public void paint(Graphics graphics)
  {
    super.paint(graphics);
//    Color oldForeground =  graphics.getForegroundColor();
    // Fill for the header section
    //
    Rectangle r = getBounds().getCopy();
    graphics.setBackgroundColor(ColorConstants.lightGray);
    //graphics.fillRectangle(r.x+1, r.y+1, r.width-1, barYcoordinate - r.y - 1);
    Color gradient1 = isSelected ? (isReadOnly ? gradientColor[3] : gradientColor[2]) : gradientColor[1];
    Color gradient2 = gradientColor[0];
    graphics.setForegroundColor(gradient1);
    graphics.setBackgroundColor(gradient2);
    Rectangle labelBounds = label.getBounds();
    graphics.fillGradient(r.x+1, r.y+1, r.width-2, labelBounds.height , true);
    graphics.setForegroundColor(ColorConstants.darkGray);
    label.paint(graphics);    
    graphics.setForegroundColor(isSelected ? gradientColor[1] : gradientColor[3]);
    //graphics.drawLine(r.x+1, r.y + 15, r.x + r.width, r.y + 15);
    //graphics.drawLine(r.x+1, r.y + labelBounds.height, r.x + r.width, r.y + labelBounds.height);
  }

  public Label getLabel()
  {
    return label;
  }  
}

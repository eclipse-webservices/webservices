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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;

public class BoxComponentFigure extends Figure
{
  HeadingFigure headingFigure;
  Figure contentPane;
  protected boolean isSelected = false;
  protected boolean isReadOnly = false;

  public void setSelected(boolean isSelected)
  {
    headingFigure.setSelected(isSelected);
  }

  public void setIsReadOnly(boolean isReadOnly)
  {
    this.isReadOnly = isReadOnly;
  }

  public BoxComponentFigure()
  {
    super();
    headingFigure = new HeadingFigure();
    add(headingFigure);
    contentPane = new ListFigure();
    contentPane.setLayoutManager(new ToolbarLayout());
    add(contentPane);
  }

  public Figure getContentPane()
  {
    return contentPane;
  }

  public Label getLabel()
  {
    return headingFigure.getLabel();
  }
}
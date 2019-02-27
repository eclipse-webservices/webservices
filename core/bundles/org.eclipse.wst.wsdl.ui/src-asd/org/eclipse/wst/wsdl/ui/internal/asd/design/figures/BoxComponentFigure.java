/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;

public class BoxComponentFigure extends Figure
{
  public HeadingFigure headingFigure;
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
  
  public Figure getHeadingFigure()
  {
    return headingFigure;
  }
}
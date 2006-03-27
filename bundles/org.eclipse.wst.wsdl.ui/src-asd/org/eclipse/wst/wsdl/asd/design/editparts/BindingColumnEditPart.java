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
package org.eclipse.wst.wsdl.asd.design.editparts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.wst.wsdl.asd.design.layouts.BindingColumnLayout;

public class BindingColumnEditPart extends ColumnEditPart
{
  protected IFigure createFigure()
  {
    Figure figure = new Figure();
    //figure.setOpaque(true);
    //figure.setBackgroundColor(ColorConstants.yellow);
    // custom layout that can do animation
    //
    BindingColumnLayout layout = new BindingColumnLayout();
    figure.setLayoutManager(layout);
    return figure;
  }  
  
  protected void register()
  {
    super.register();
    getFigure().addLayoutListener(LayoutAnimator.getDefault());    
  }
  
  protected void unregister()
  {
    getFigure().removeLayoutListener(LayoutAnimator.getDefault());   
    super.unregister();
  }
}

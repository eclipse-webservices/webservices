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

import java.util.Iterator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.gef.EditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.BindingColumnLayout;

public class BindingColumnEditPart extends ColumnEditPart
{
  protected IFigure createFigure()
  {
    Figure figure = new Figure();
    //figure.setOpaque(true);
    //figure.setBackgroundColor(ColorConstants.yellow);
    // custom layout that can do animation
    //
    BindingColumnLayout layout = new BindingColumnLayout(this);
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
  
  // this method will expand the binding edit part
  // and collapse any other bindings as appropriate
  //public void expand(BidingEditPart bindingEditPart)
  //{    
  //}
  
  public void refreshBindingEditParts()
  {
    for (Iterator i = getChildren().iterator(); i.hasNext(); )
    {
      EditPart editPart = (EditPart)i.next();
      editPart.refresh();       
    }
    getFigure().invalidateTree();           
    getFigure().revalidate();     
  }
}

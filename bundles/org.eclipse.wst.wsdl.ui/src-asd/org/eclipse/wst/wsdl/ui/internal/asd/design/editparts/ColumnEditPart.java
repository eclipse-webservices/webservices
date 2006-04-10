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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.AbstractModelCollection;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;

public class ColumnEditPart extends BaseEditPart
{   
  protected IFigure createFigure()
  {
    Figure figure = new Figure();
    ToolbarLayout layout = new ToolbarLayout(false);
    layout.setStretchMinorAxis(false);
    layout.setSpacing(20);
    figure.setLayoutManager(layout);
    return figure;
  }
  
  public IDescription getDescription()
  {
    return (IDescription)getModel();   
  }

  protected void createEditPolicies()
  {
    // TODO Auto-generated method stub
  }
  
  protected List getModelChildren()
  { 
    AbstractModelCollection collection = (AbstractModelCollection)getModel();
    Object children[] = collection.getChildren();
    List list = new ArrayList();
    for (int index = 0; index < children.length; index++) {
    	list.add(children[index]);
    }
    
    return list;
  }
  
  public void addFeedback() {
	  
  }
  
  public void removeFeedback() {
	  
  }
}




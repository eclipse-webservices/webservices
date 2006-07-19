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

import java.util.List;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.BoxComponentFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.RowLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;

public class InterfaceEditPart extends AbstractBoxtEditPart implements IFeedbackHandler
{     
  public InterfaceEditPart()
  {
    columnData.setColumnWeight("MessageLabel", 0); //$NON-NLS-1$
    columnData.setColumnWeight("MessageContentPane", 100); //$NON-NLS-1$
    columnData.setColumnWeight("parameterName", 50); //$NON-NLS-1$
    columnData.setColumnWeight("parameterType", 50);         //$NON-NLS-1$
  }
  
  protected IFigure createFigure()
  {
    BoxComponentFigure figure = (BoxComponentFigure)super.createFigure();
    figure.getLabel().setIcon(((IInterface) getModel()).getImage());
    figure.setBackgroundColor(ColorConstants.orange);
    figure.setBorder(new LineBorder(1));
    ToolbarLayout toolbarLayout = new ToolbarLayout(false);
    toolbarLayout.setStretchMinorAxis(true);
    figure.setLayoutManager(toolbarLayout);
    
    // rmah: The block of code below has been moved from refreshVisuals().  We're
    // assuming the read-only state of the EditPart will never change once the
    // EditPart has been created.
    if (isReadOnly()) 
    {
    	figure.getLabel().setForegroundColor(DesignViewGraphicsConstants.readOnlyLabelColor);
    }
    else
    {
    	figure.getLabel().setForegroundColor(ColorConstants.black);
    }
    
    return figure;
  }
  
  public static void attachToInterfaceEditPart(EditPart editPart, RowLayout rowLayout)
  {
    for (EditPart parent = editPart.getParent(); parent != null; parent = parent.getParent())
    {
      if (parent instanceof InterfaceEditPart)
      {
        InterfaceEditPart interfaceEditPart = (InterfaceEditPart)parent;
        rowLayout.setColumnData(interfaceEditPart.columnData);
        break;
      }  
    }    
  }
  
  protected void refreshChildren()
  {
    super.refreshChildren();
  }
  
  protected void refreshVisuals()
  {
    super.refreshVisuals();
    
    BoxComponentFigure fig = (BoxComponentFigure) getFigure();
  }

  protected List getModelChildren()
  {
    IInterface theInterface = (IInterface)getModel();
    return theInterface.getOperations();   
  }
  
  public void addFeedback() {
	  super.addFeedback();
  }

  public void removeFeedback() {
	  super.removeFeedback();
  } 
  

  public EditPart getRelativeEditPart(int direction)
  {
    if (direction == PositionConstants.WEST)
    {
      return EditPartNavigationHandlerUtil.getSourceConnectionEditPart(this);     
    }  
    return super.getRelativeEditPart(direction);
  }
}

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
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.LabelCellEditorLocator;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.LabelEditManager;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDLabelDirectEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.HeadingFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IService;

public class ServiceEditPart extends BaseEditPart implements INamedEditPart
{
  IFigure contentPane;
  HeadingFigure headingFigure;
  
  public void addFeedback()
  {
	  LineBorder boxFigureLineBorder = (LineBorder) figure.getBorder();
	  boxFigureLineBorder.setWidth(2);
//	  boxFigureLineBorder.setColor(ColorConstants.darkBlue);
	  headingFigure.setSelected(true);	  
	  figure.repaint();
  }
  
  public void removeFeedback()
  {
	  LineBorder boxFigureLineBorder = (LineBorder) figure.getBorder();
	  boxFigureLineBorder.setWidth(1);
	  boxFigureLineBorder.setColor(ColorConstants.black);
	  headingFigure.setSelected(false);
	  figure.repaint();
  }

  protected IFigure createFigure()
  {
    Figure figure = new Figure();
    figure.setBorder(new LineBorder(1));
    ToolbarLayout toolbarLayout = new ToolbarLayout(false);
    toolbarLayout.setStretchMinorAxis(true);
    figure.setLayoutManager(toolbarLayout);    
    headingFigure = new HeadingFigure();
   	headingFigure.getLabel().setIcon(((IService) getModel()).getImage());	
    figure.add(headingFigure);
        
    contentPane = new Figure()
    {
      public void paint(Graphics graphics) 
      {
        super.paint(graphics);
      boolean isFirst = false;
      for (Iterator i = getChildren().iterator(); i.hasNext(); )
      {
        Figure figure = (Figure)i.next();              
        if (isFirst)
        {
          isFirst = false;
        }  
        else
        {  
          Rectangle r = figure.getBounds();              
          graphics.drawLine(r.x, r.y +1, r.x + r.width, r.y + 1);
        }  
      }
      }
    };
    ToolbarLayout toolbarLayout2 = new ToolbarLayout(false);
    toolbarLayout2.setStretchMinorAxis(true);   
    contentPane.setLayoutManager(toolbarLayout2);
    figure.add(contentPane);
    
    // rmah: The block of code below has been moved from refreshVisuals().  We're
    // assuming the read-only state of the EditPart will never change once the
    // EditPart has been created.
    if (isReadOnly()) 
    {
      headingFigure.getLabel().setForegroundColor(DesignViewGraphicsConstants.readOnlyLabelColor);
    }
    else
    {
      headingFigure.getLabel().setForegroundColor(ColorConstants.black);
    }
    
    return figure;
  }
  
  public IFigure getContentPane()
  {
    return contentPane;
  }

  protected void createEditPolicies()
  {
      super.createEditPolicies();
	  installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ASDLabelDirectEditPolicy());
	  installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ASDSelectionEditPolicy());
  }
  
  private DirectEditManager manager;
  
  public void performDirectEdit(Point cursorLocation){
	  if (cursorLocation == null || hitTest(headingFigure.getLabel(), cursorLocation) && !isReadOnly()) {
		  manager = new LabelEditManager(this, new LabelCellEditorLocator(this, cursorLocation));
		  manager.show();
    }
    else if ((hitTest(headingFigure.getLabel(), cursorLocation) ||
              hitTestFigure(headingFigure, cursorLocation)) && isReadOnly()) {
      doOpenNewEditor();
	  }
  }
  
  public void performRequest(Request req) {
	  if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
		  performDirectEdit(null);
	  }
  }
  
  public Label getLabelFigure() {
	  return headingFigure.getLabel();
  }

  protected List getModelChildren()
  {
    IService service = (IService)getModel();
    return service.getEndPoints();
  }
  
  protected void refreshVisuals()
  { 
    IService service = (IService)getModel();
    headingFigure.setIsReadOnly(service.isReadOnly());
    headingFigure.getLabel().setText(service.getName());
    super.refreshVisuals();  
  }
}
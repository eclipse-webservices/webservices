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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.LabelCellEditorLocator;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.LabelEditManager;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDDragAndDropEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDGraphNodeDragTracker;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDLabelDirectEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.RowLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;

public class ParameterEditPart extends BaseEditPart implements IFeedbackHandler, INamedEditPart
{   
  protected Figure contentPane;
  protected Label parameterName;
  protected RowLayout rowLayout = new RowLayout();
  protected Image labelImage;
  
  protected ASDSelectionEditPolicy selectionHandlesEditPolicy = new ASDSelectionEditPolicy();

  protected IFigure createFigure()
  {
    IFigure figure = new Figure()
    {
      public void paint(Graphics graphics)
      {
        super.paint(graphics);
        graphics.pushState();
        // this bit of code is used to draw the dividing line between
        // the parameter name and the parameter type
        // we might want to consider moving this line drawing into the 
        // message reference's figure where the horizontal lines are down's
        Rectangle r = parameterName.getBounds();
        int x= r.x +  r.width -1;
        graphics.setForegroundColor(ColorConstants.lightGray);
        graphics.drawLine(x, r.y, x, r.y + r.height);
        graphics.popState();
      }
    };        
    //toolbarLayout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
    figure.setForegroundColor(ColorConstants.black);
    figure.setLayoutManager(rowLayout); 
    /*
    String x = "";
    int size = (int)(Math.random()*50);
    for (int i = 0; i < size; i++)
    {
      x += "x";
    }  
    String y = "";
    int sizey = (int)(Math.random()*50);
    for (int i = 0; i < sizey; i++)
    {
      y += "y";
    } */
    
    contentPane = new Figure();
    ToolbarLayout toolbarLayout2 = new ToolbarLayout(false);
    toolbarLayout2.setStretchMinorAxis(true);   
    contentPane.setLayoutManager(toolbarLayout2);

    IFigure parameterNamePane = new Panel();
    toolbarLayout2 = new ToolbarLayout(false);  
    toolbarLayout2.setStretchMinorAxis(true);   
    parameterNamePane.setLayoutManager(toolbarLayout2);
    
    parameterName = new Label();
    parameterName.setLabelAlignment(Label.LEFT);
    parameterName.setBorder(new MarginBorder(4,10,4,10));
    
    parameterNamePane.add(parameterName);
    figure.add(parameterNamePane);
    figure.add(contentPane);
    rowLayout.setConstraint(parameterNamePane, "parameterName"); //$NON-NLS-1$
    rowLayout.setConstraint(contentPane, "parameterType"); //$NON-NLS-1$
    
    labelImage = ((IParameter) getModel()).getImage();
    
    // rmah: The block of code below has been moved from refreshVisuals().  We're
    // assuming the read-only state of the EditPart will never change once the
    // EditPart has been created.
    if (isReadOnly()) 
    {
      parameterName.setForegroundColor(DesignViewGraphicsConstants.readOnlyLabelColor);
    }
    else
    {
      parameterName.setForegroundColor(ColorConstants.black);
    }
    
    return figure;
  }
  
  public void addNotify()
  {
    InterfaceEditPart.attachToInterfaceEditPart(this, rowLayout);
    super.addNotify();
  }
  
  private DirectEditManager manager;
  
  public void performDirectEdit(Point cursorLocation){
	  if (cursorLocation == null || hitTest(parameterName, cursorLocation) && !isReadOnly()) {
		manager = new LabelEditManager(this, new LabelCellEditorLocator(this, cursorLocation));
		manager.show();
	  }
    else if (hitTest(parameterName, cursorLocation) && isReadOnly()) {
      doOpenNewEditor();
    }
  }
  
  public void performRequest(Request req) {
	  if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
		  performDirectEdit(null);
	  }
  }
  
  protected void createEditPolicies()
  {
      super.createEditPolicies();
	  installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ASDLabelDirectEditPolicy());
	  if (!isReadOnly()) {
		  installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new ASDDragAndDropEditPolicy(getViewer(), selectionHandlesEditPolicy));
	  }
	  installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionHandlesEditPolicy);
  }
  
  public DragTracker getDragTracker(Request request)
  {
    return new ASDGraphNodeDragTracker((EditPart)this);
  }
  
  public Label getLabelFigure() {
	  return parameterName;
  }
  
  protected void refreshVisuals()
  {   
    super.refreshVisuals();
    IParameter parameter = (IParameter)getModel();
    parameterName.setText(parameter.getName());
    if (labelImage != null) {
    	parameterName.setIcon(labelImage);
    }
    
    // Resize column widths.  Sizes may have shrunk.
    rowLayout.getColumnData().clearColumnWidths();
    for (EditPart parent = getParent(); parent != null; parent = parent.getParent())
    {
      if (parent instanceof InterfaceEditPart)
      { 
        ((GraphicalEditPart)parent).getFigure().invalidateTree();
        break;
      }
    }
  }
  
  protected List getModelChildren()
  {
	  // On the facade level, one IParameter will have two editable values.
	  // A name and a type reference.  To make direct editing and selection
	  // feedback easier, we show this one facade object as two edit parts.
	  // The approach is to the same facade model, to drive the second (child
	  // EditPart.

      // TODO (cs) Rich consider creating 2 hard code edit parts
      // the first for the parameter name
      // the second for the paramter type
	  List kids = new ArrayList();
	  kids.add(getModel());
	  return kids;
  }
  
  public IFigure getContentPane()
  {
    return contentPane;
  }
  
  public void addFeedback() {	
	  parameterName.getParent().setBackgroundColor(DesignViewGraphicsConstants.tableCellSelectionColor);
  }

  public void removeFeedback() {
      parameterName.getParent().setBackgroundColor(figure.getBackgroundColor());
  }
  
  public EditPart getRelativeEditPart(int direction)
  {  
    EditPart editPart = super.getRelativeEditPart(direction);
    if (direction == PositionConstants.SOUTH && editPart == null)
    {
      editPart = EditPartNavigationHandlerUtil.getNextInterface(this);           
    }       
    return editPart;
  }  
}
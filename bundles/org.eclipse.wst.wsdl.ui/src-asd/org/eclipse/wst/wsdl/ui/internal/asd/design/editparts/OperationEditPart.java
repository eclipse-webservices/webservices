/*******************************************************************************
 * Copyright (c) 2001, 2010 IBM Corporation and others.
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
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.LabelCellEditorLocator;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.LabelEditManager;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDLabelDirectEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.ListFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;


public class OperationEditPart extends BaseEditPart implements INamedEditPart
{ 
  protected Figure contentPane;
  protected Label label;
  private Color unselectedColor;
  private Figure labelHolder;
  
  public OperationEditPart()
  {
  }

  protected IFigure createFigure()
  {
    Figure figure = new Figure();
    figure.setBackgroundColor(DesignViewGraphicsConstants.tableOperationHeadingColor);
    ToolbarLayout toolbarLayout = new ToolbarLayout(false);   
    toolbarLayout.setStretchMinorAxis(true);   
    toolbarLayout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
    figure.setLayoutManager(toolbarLayout);
    
    labelHolder = new Panel() {
      public void paint(Graphics graphics) {
        super.paint(graphics);
        Rectangle r = getBounds();
        // bug146932
        paintFocusCursor(new Rectangle(r.x, r.y, r.width, r.height), graphics);
      }
    };
    labelHolder.setBackgroundColor(DesignViewGraphicsConstants.tableOperationHeadingColor);
    labelHolder.setLayoutManager(new ToolbarLayout(true));
    figure.add(labelHolder);
    
    label = new Label("Operation"); //$NON-NLS-1$
//    label.setFont(DesignViewGraphicsConstants.smallBoldFont);
    label.setBorder(new MarginBorder(2, 2,2,2));
    label.setTextAlignment(Label.LEFT);  
    labelHolder.add(label);
    
    
    contentPane = new ListFigure();  
    ToolbarLayout toolbarLayout2 = new ToolbarLayout(false);
    toolbarLayout2.setStretchMinorAxis(true);
    contentPane.setLayoutManager(toolbarLayout2);
    figure.add(contentPane);
 
    // rmah: The block of code below has been moved from refreshVisuals().  We're
    // assuming the read-only state of the EditPart will never change once the
    // EditPart has been created.
    if (isReadOnly()) {
    	label.setForegroundColor(DesignViewGraphicsConstants.readOnlyLabelColor);
   	    figure.setBackgroundColor(DesignViewGraphicsConstants.readOnlyTableOperationHeadingColor);
   	    labelHolder.setBackgroundColor(DesignViewGraphicsConstants.readOnlyTableOperationHeadingColor);
    }
    else {
    	label.setForegroundColor(ColorConstants.black);
 	    figure.setBackgroundColor(DesignViewGraphicsConstants.tableOperationHeadingColor);
   	    labelHolder.setBackgroundColor(DesignViewGraphicsConstants.tableOperationHeadingColor);
    }
    
    return figure;
  }
  
  protected void createEditPolicies()
  {
      super.createEditPolicies();
	  installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ASDLabelDirectEditPolicy());
	  installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ASDSelectionEditPolicy());
  }
  
  private DirectEditManager manager;
  public void performDirectEdit(Point cursorLocation){
	  Rectangle textArea = getLabelFigure().getBounds();
	  textArea.width = getFigure().getBounds().width;
	  if (!isFileReadOnly() && (cursorLocation == null || hitTest(textArea, cursorLocation) && !isReadOnly())) {
		  manager = new LabelEditManager(this, new LabelCellEditorLocator(this, cursorLocation));
		  manager.show();
	  }
    else if (getFigure() instanceof Figure && hitTestFigure((Figure) getFigure(), cursorLocation) && isReadOnly()) {
      doOpenNewEditor();    
    }
  }
  
  public void performRequest(Request req) {
	  if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
		  performDirectEdit(null);
	  }
  }
  
  public Label getLabelFigure() {
	  return label;
  }
  
  protected void refreshVisuals()
  {   
    super.refreshVisuals();
    IOperation operation = (IOperation)getModel();
    label.setText(operation.getName());
    
    if (operation instanceof ITreeElement) {
    	label.setIcon(((ITreeElement) operation).getImage());
    }
  }
  
  public IFigure getContentPane()
  {
    return contentPane;
  }

  protected List getModelChildren()
  {
    IOperation theOperation = (IOperation)getModel();
    return theOperation.getMessages();
  }
  
  public void addFeedback() {
	  unselectedColor = labelHolder.getBackgroundColor();
//	  getFigure().setBackgroundColor(DesignViewGraphicsConstants.tableCellSelectionColor);
	  labelHolder.setBackgroundColor(DesignViewGraphicsConstants.tableCellSelectionColor);
  }
  
  public void removeFeedback() {
	  if (unselectedColor != null) {
		  labelHolder.setBackgroundColor(unselectedColor);
	  }
  }

  protected String getAccessibleName()
  {
    Label labelFigure = getLabelFigure();
    if (labelFigure != null) {
      return labelFigure.getText();
    }
    return ""; //$NON-NLS-1$
  }
}

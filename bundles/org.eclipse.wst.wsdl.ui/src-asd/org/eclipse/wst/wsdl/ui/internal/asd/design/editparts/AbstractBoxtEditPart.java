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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.LabelCellEditorLocator;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.LabelEditManager;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDLabelDirectEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.BoxComponentFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.ColumnData;

public abstract class AbstractBoxtEditPart extends BaseEditPart implements INamedEditPart
{
  protected ColumnData columnData = new ColumnData();
  protected BoxComponentFigure figure;
  
  protected IFigure createFigure()
  {
    figure = new BoxComponentFigure();
    figure.setBorder(new LineBorder(1));
    ToolbarLayout toolbarLayout = new ToolbarLayout();
    toolbarLayout.setStretchMinorAxis(true);
    // toolbarLayout.setMinorAlignment(ToolbarLayout.ALIGN_BOTTOMRIGHT);
    figure.setLayoutManager(toolbarLayout);
    // if(isScrollable())
    // figure.setScrollingActionListener(this);
    return figure;
  }

  public IFigure getContentPane()
  {
    return figure.getContentPane();
  }

  protected void createEditPolicies()
  {
      super.createEditPolicies();
	  installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ASDLabelDirectEditPolicy());
	  installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ASDSelectionEditPolicy());
  }

  private DirectEditManager manager;
  
  public void performDirectEdit(Point cursorLocation){
	  if (cursorLocation == null ||  hitTestFigure(figure.getHeadingFigure(), cursorLocation) && !isReadOnly()) {
		  manager = new LabelEditManager(this, new LabelCellEditorLocator(this, cursorLocation));
		  manager.show();
	  }
    else if ((hitTest(figure.getLabel(), cursorLocation) ||
        hitTestFigure(figure.getHeadingFigure(), cursorLocation)) && isReadOnly()) {
      doOpenNewEditor();
    }
  }
  
  public void performRequest(Request req) {
	  if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
		  Point location = null;
		  if (req instanceof DirectEditRequest) {
			  location = ((DirectEditRequest) req).getLocation();			  
		  }
		  performDirectEdit(location);
	  }
  }
  
  public Label getLabelFigure() {
	  return figure.getLabel();
  }

  protected void refreshChildren()
  {
    super.refreshChildren();
    // getFigure().invalidateTree();
  }

  protected void refreshVisuals()
  {
    super.refreshVisuals();
    WSDLBaseAdapter box = (WSDLBaseAdapter) getModel();
    figure.headingFigure.setIsReadOnly(box.isReadOnly());
    figure.getLabel().setText(box.getName());
  }

  public void addFeedback()
  {
	  LineBorder boxFigureLineBorder = (LineBorder) figure.getBorder();
	  boxFigureLineBorder.setWidth(2);
//	  boxFigureLineBorder.setColor(ColorConstants.darkBlue);
	  figure.setSelected(true);
	  figure.repaint();
  }

  public void removeFeedback()
  {
	  LineBorder boxFigureLineBorder = (LineBorder) figure.getBorder();
	  boxFigureLineBorder.setWidth(1);
	  boxFigureLineBorder.setColor(ColorConstants.black);
	  figure.setSelected(false);
	  figure.repaint();
  }

  public ColumnData getColumnData()
  {
    return columnData;
  }
}

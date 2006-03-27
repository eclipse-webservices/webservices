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

import java.util.Iterator;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.wst.wsdl.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.asd.design.editpolicies.WSDLSelectionEditPolicy;
import org.eclipse.wst.wsdl.asd.design.layouts.RowLayout;
import org.eclipse.wst.wsdl.asd.editor.ASDEditorPlugin;
import org.eclipse.wst.wsdl.asd.editor.ASDMultiPageEditor;
import org.eclipse.wst.wsdl.asd.editor.util.IOpenExternalEditorHelper;
import org.eclipse.wst.wsdl.asd.facade.IParameter;

import org.eclipse.draw2d.MouseMotionListener.Stub;

public class ParameterTypeEditPart extends BaseEditPart implements IFeedbackHandler, INamedEditPart
{   
	  protected Label parameterType;
	  protected RowLayout rowLayout = new RowLayout();

	  protected MyMouseEventListener mouseEventListener;
	  
	  protected IFigure createFigure()
	  {
	    IFigure figure = new Panel();
	    figure.setLayoutManager(rowLayout); 

	    parameterType = new Label();
	    parameterType.setLabelAlignment(Label.LEFT);
	    parameterType.setBorder(new MarginBorder(4,12,4,12));
        figure.add(parameterType);
        
	    return figure;
	  }

	  protected void refreshVisuals()
	  {   
	    super.refreshVisuals();
	    IParameter param = (IParameter) getModel();
	    String name = param.getComponentName();
	    parameterType.setText(name);
	  }
	  
	  public void addFeedback() {	 		          
		  figure.setBackgroundColor(DesignViewGraphicsConstants.tableCellSelectionColor);
	  }

	  public void removeFeedback() {
         figure.setBackgroundColor(figure.getParent().getBackgroundColor());
	  }
	  
	  public Label getLabelFigure() {
		  return parameterType;
	  }

	  protected void createEditPolicies()
	  {
		  installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new WSDLSelectionEditPolicy());
	  }
	  
	  public void performDirectEdit(Point cursorLocation) {
	  }
	  
	  public void activate() {
		  super.activate();
		  
		  // Setup a MouseMotionListener so we know when to display the 'open in XSD Editor' figure
		  IFigure feedBackLayer = getLayer(LayerConstants.FEEDBACK_LAYER);
		  if (!(feedBackLayer.getLayoutManager() instanceof XYLayout)) {
			  feedBackLayer.setLayoutManager(new XYLayout());					// We could probably move this line elsewhere
		  }
		  
		  IFigure primaryLayer = getLayer(LayerConstants.PRIMARY_LAYER);
		  mouseEventListener = new MyMouseEventListener(getModel());
		  primaryLayer.addMouseMotionListener(mouseEventListener);
		  primaryLayer.addMouseListener(mouseEventListener);
	  }
	  
	  public void deactivate() {
		  if (mouseEventListener != null) {
			  mouseEventListener.discardLinkFigure();
			  IFigure primaryLayer = getLayer(LayerConstants.PRIMARY_LAYER);
			  primaryLayer.removeMouseMotionListener(mouseEventListener);
			  primaryLayer.removeMouseListener(mouseEventListener);
		  }
	  }
	  
	  private class MyLinkFigure extends Polygon {
		  private PointList points = new PointList();
		  public int horizontalBuffer = 4;
		  public int verticalBuffer = 6;

		  public MyLinkFigure() {
			  // Draw the arrow
			  points.addPoint(new Point(horizontalBuffer + 0, 4 + verticalBuffer));
			  points.addPoint(new Point(horizontalBuffer + 9, 4 + verticalBuffer));
			  points.addPoint(new Point(horizontalBuffer + 9, 0 + verticalBuffer));
			  points.addPoint(new Point(horizontalBuffer + 14, 5 + verticalBuffer));
			  points.addPoint(new Point(horizontalBuffer + 9, 10 + verticalBuffer));
			  points.addPoint(new Point(horizontalBuffer + 9, 6 + verticalBuffer));
			  points.addPoint(new Point(horizontalBuffer + 0, 6 + verticalBuffer));
			  points.addPoint(new Point(horizontalBuffer + 0, 4 + verticalBuffer));
			  setFill(true);
		  }

		  public void setLocation(Point newLocation) {
			  PointList translatedPoints = new PointList();
			  
			  for (int index = 0; index < points.size(); index++) {
				  Point origPoint = points.getPoint(index);
				  translatedPoints.addPoint(origPoint.translate(newLocation));
			  }
			  
			  setPoints(translatedPoints);
		  }
	  }
	  
	  private class MyMouseEventListener extends Stub implements MouseListener {
		  private MyLinkFigure linkFigure;
		  private Object object;
		  private IOpenExternalEditorHelper openExternalEditorHelper;
		  
		  public MyMouseEventListener(Object object) {
			  this.object = object;
			  linkFigure = new MyLinkFigure();
			  unemphasizeLinkFigure();
		  }
		  
		  public void mouseMoved(MouseEvent me) {
			  Point pointer = me.getLocation();
			  Rectangle figBounds = getFigure().getParent().getParent().getBounds();
			  
			  if (getExternalEditorOpener().linkApplicable()) {
				  getLayer(LayerConstants.FEEDBACK_LAYER).add(linkFigure);
				  setLinkFigureLocation();
				  if (pointerInRange(figBounds, pointer)) {
					  emphasizeLinkFigure();
				  }
				  else {
					  unemphasizeLinkFigure();
				  }
			  }
			  else {
				  if (containsLinkFigure()) {
					  getLayer(LayerConstants.FEEDBACK_LAYER).remove(linkFigure);
				  }
			  }
		  }
		  
		  private boolean containsLinkFigure() {
			  IFigure figure = getLayer(LayerConstants.FEEDBACK_LAYER);
			  Iterator it = figure.getChildren().iterator();
			  while (it.hasNext()) {
				  Object item = it.next();
				  if (item.equals(linkFigure)) {
					  return true;
				  }
			  }
			  
			  return false;
		  }
		  
		  private Rectangle getLinkFigureBounds() {
			  if (containsLinkFigure()) {
			  return linkFigure.getBounds();
			  }
			  else {
				  return null;
			  }
		  }
		  
		  private boolean pointerInRange(Rectangle figBounds, Point pointer) {
			  Rectangle linkBounds = getLinkFigureBounds();
			  
			  int entireX = figBounds.x;
			  int entireY = figBounds.y;
			  int entireWidth = figBounds.width + linkBounds.width + linkFigure.horizontalBuffer;
			  int entireHeight = figBounds.height;
			  Rectangle entireBounds = new Rectangle(entireX, entireY, entireWidth, entireHeight);

			  return entireBounds.contains(pointer);
		  }
		  
		  protected void setLinkFigureLocation() {
			    Rectangle figureBounds = getFigure().getBounds();
			    int xStart = figureBounds.x + figureBounds.width;
			    int yStart = figureBounds.y;
				  
			    Point aPoint = new Point(xStart, yStart);
			    linkFigure.setLocation(aPoint);
		  }
		  
		  public void discardLinkFigure() {
			  if (containsLinkFigure()) {
				  getLayer(LayerConstants.FEEDBACK_LAYER).remove(linkFigure);
			  }
		  }
		  
		  protected void emphasizeLinkFigure() {
			  linkFigure.setForegroundColor(ColorConstants.blue);
			  linkFigure.setBackgroundColor(ColorConstants.blue);
		  }
		  
		  protected void unemphasizeLinkFigure() {
			  linkFigure.setForegroundColor(ColorConstants.lightGray);
			  linkFigure.setBackgroundColor(ColorConstants.lightGray);
		  }

		  public void mouseReleased(MouseEvent me) { }
		  public void mouseDoubleClicked(MouseEvent me) { }
		  public void mousePressed(MouseEvent me) {
			  Point pointer = me.getLocation();
			  Rectangle linkFigBounds = getLinkFigureBounds();
			  if (linkFigBounds == null) {
				  return;
			  }
			  
			  Rectangle testbounds = new Rectangle(linkFigBounds.x, linkFigBounds.y, 0, linkFigBounds.height);
			  
			  if (getExternalEditorOpener().linkApplicable() && pointerInRange(testbounds, pointer)) {
				  setLinkFigureLocation();
				  // Open in XSD Editor
				  getExternalEditorOpener().openExternalEditor();				  
			  }
		  }
		  
		  private IOpenExternalEditorHelper getExternalEditorOpener() {
			  if (openExternalEditorHelper == null) {
				  openExternalEditorHelper = ((ASDMultiPageEditor) ASDEditorPlugin.getActiveEditor()).getOpenExternalEditorHelper();
				  openExternalEditorHelper.setModel(object);
			  }
			  
			  return openExternalEditorHelper;
		  }
	  }
	}
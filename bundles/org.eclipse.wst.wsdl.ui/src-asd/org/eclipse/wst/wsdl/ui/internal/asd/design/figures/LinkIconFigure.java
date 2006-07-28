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
package org.eclipse.wst.wsdl.ui.internal.asd.design.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class LinkIconFigure extends Polygon {
	private AbstractGraphicalEditPart editPart;
	private Point figLocation;
	  private PointList points = new PointList();
	  public int horizontalBuffer = 5;
	  public int verticalBuffer = 7;

	  public LinkIconFigure(AbstractGraphicalEditPart ep) {
		  editPart = ep;
		  
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
		  setPoints(points);
	  }
	  
	  public void paintFigure(Graphics graphics) {
		  super.paintFigure(graphics);
	  }
	  
	  public void setFigureLocation(Point newStartingLocation) {
		  int dy = newStartingLocation.y;

		  if (figLocation != null) {
			  dy = newStartingLocation.y - figLocation.y;
		  }

		  // Update the points
		  PointList newPoints = new PointList();
		  PointList pList = getPoints();
		  for (int index = 0; index < pList.size(); index++) {
			  Point point = pList.getPoint(index);
			  // Add 5 for the padding
			  Point newPoint = new Point(point.x + horizontalBuffer, point.y + dy);
			  newPoints.addPoint(newPoint);
		  }
		  setPoints(newPoints);

		  figLocation = newStartingLocation;
	  }
	  
	  public void primTranslate(int dx, int dy) {
			bounds.x += dx;
			bounds.y += dy;
			
			PointList pList = getPoints();
			PointList newList = new PointList();
			for (int index = 0; index < pList.size(); index++) {
				Point point = pList.getPoint(index);
				Point newPoint = new Point(point.x + dx, point.y);
				newList.addPoint(newPoint);
			}
			setPoints(newList);
			
			if (useLocalCoordinates()) {
				fireCoordinateSystemChanged();
				return;
			}
		}
	  
	  public AbstractGraphicalEditPart getAssociatedEditPart() {
		  return editPart;
	  }
}

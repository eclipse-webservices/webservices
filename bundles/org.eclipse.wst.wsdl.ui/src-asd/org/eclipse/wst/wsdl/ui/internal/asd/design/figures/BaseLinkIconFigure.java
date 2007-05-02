/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.figures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Color;

public class BaseLinkIconFigure extends Figure {
	public static int VALID_SCHEMA_LINK_STYLE = 1;
	public static int INVALID_SCHEMA_LINK_STYLE = 2;

	private int linkIconStyle = VALID_SCHEMA_LINK_STYLE;	
	private AbstractGraphicalEditPart editPart;
	private List childFigures = new ArrayList();
	public int figureSpacing = 7;

	public BaseLinkIconFigure(AbstractGraphicalEditPart ep) {
		editPart = ep;

		ToolbarLayout layout = new ToolbarLayout(true) {
			public void layout(IFigure parent) {
				super.layout(parent);

				AbstractGraphicalEditPart ep = getAssociatedEditPart();
				IFigure associatedFigure = ep.getFigure();
				if (associatedFigure != null) {
					// Update the bounds
					Rectangle associatedBounds = associatedFigure.getBounds();
					int x = associatedBounds.x + associatedBounds.width;
					int y = associatedBounds.y;
					int width = getSize().width;
					int height = associatedBounds.height;

					Rectangle newFigBounds = new Rectangle(x, y, width, height);
					// Set my bounds based on my associated editpart
					setBounds(newFigBounds);

					// Layout the children
					Iterator it = parent.getChildren().iterator();
					while (it.hasNext()) {
						IFigure fig = (IFigure) it.next();
						Point newPoint = new Point(x, y);

						// Indent the figure to the right to show the "broken" schema link figure
						if (fig instanceof RightInvalidIconFigure) {
							newPoint.x = newPoint.x + figureSpacing;
						}

						fig.setLocation(newPoint);
					}
				}
			}
		};
		setLayoutManager(layout);

		// Default to a ValidLinkIconFigure
		linkIconStyle = VALID_SCHEMA_LINK_STYLE;
		addChildFigure(new ValidLinkIconFigure());
	}

	private void removeChildFigures() {
		Iterator it = childFigures.iterator();
		while (it.hasNext()) {
			remove((IFigure) it.next());
		}

		childFigures.clear();
	}

	private void addChildFigure(IFigure fig) {
		childFigures.add(fig);
		add(fig);
	}

	public void setLinkIconStyle(int style) {
		if (style == VALID_SCHEMA_LINK_STYLE && linkIconStyle != VALID_SCHEMA_LINK_STYLE) {
			removeChildFigures();
			addChildFigure(new ValidLinkIconFigure());
		}
		else if (style == INVALID_SCHEMA_LINK_STYLE && linkIconStyle != INVALID_SCHEMA_LINK_STYLE) {
			removeChildFigures();
			addChildFigure(new LeftInvalidIconFigure());
			addChildFigure(new RightInvalidIconFigure());
		}

		linkIconStyle = style;
	}

	public int getLinkIconStyle() {
		return linkIconStyle;
	}

	public void setColor(Color color) {
		Iterator it = childFigures.iterator();
		while (it.hasNext()) {
			IFigure fig = (IFigure) it.next();
			fig.setBackgroundColor(color);
			fig.setForegroundColor(color);
		}
	}

	public AbstractGraphicalEditPart getAssociatedEditPart() {
		return editPart;
	}

	private class LeftInvalidIconFigure extends LinkIconFigure {
		public LeftInvalidIconFigure() {
			super(null);
			PointList points = new PointList();
			
			// Draw the arrow
			points.addPoint(new Point(horizontalBuffer + 0, 4 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 5, 4 + verticalBuffer));

			points.addPoint(new Point(horizontalBuffer + 7, 0 + verticalBuffer));	// top slash
			points.addPoint(new Point(horizontalBuffer + 5, 4 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 2, 10 + verticalBuffer));	// bottom slash

			points.addPoint(new Point(horizontalBuffer + 4, 6 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 0, 6 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 0, 4 + verticalBuffer));

			setForegroundColor(ColorConstants.lightGray);
			setBackgroundColor(ColorConstants.lightGray);
			setFill(true);
			setPoints(points);
		}

		// We increase the size of the width because we indent the figure towards the right.
		// So we indent by that same amount stored in horizontalBuffer
		public Dimension getPreferredSize(int wHint, int hHint) {
			Dimension dimension = super.getPreferredSize(wHint, hHint);
			dimension.width = dimension.width + horizontalBuffer;

			return dimension;
		}

		public void setLocation(Point point) {
			super.setLocation(point);
			// Update the points with the following method call
			setFigureLocation(point);
		}
	}

	private class RightInvalidIconFigure extends LinkIconFigure {
		public RightInvalidIconFigure() {
			super(null);
			PointList points = new PointList();

			points.addPoint(new Point(horizontalBuffer + 5, 4 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 7, 0 + verticalBuffer));	// top slash
			points.addPoint(new Point(horizontalBuffer + 5, 4 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 10, 4 + verticalBuffer));

			points.addPoint(new Point(horizontalBuffer + 10, 0 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 15, 5 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 10, 10 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 10, 6 + verticalBuffer));

			points.addPoint(new Point(horizontalBuffer + 4, 6 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 2, 10 + verticalBuffer));	// bottom slash
			points.addPoint(new Point(horizontalBuffer + 5, 4 + verticalBuffer));

			setForegroundColor(ColorConstants.lightGray);
			setBackgroundColor(ColorConstants.lightGray);
			setFill(true);
			setPoints(points);
		}

		public void setLocation(Point point) {
			super.setLocation(point);
			setFigureLocation(point);
		}
	}

	private class ValidLinkIconFigure extends LeftInvalidIconFigure {
		public ValidLinkIconFigure() {
			PointList points = new PointList();

			// Draw the arrow
			points.addPoint(new Point(horizontalBuffer + 0, 4 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 15, 4 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 15, 0 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 20, 5 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 15, 10 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 15, 6 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 0, 6 + verticalBuffer));
			points.addPoint(new Point(horizontalBuffer + 0, 4 + verticalBuffer));

			setForegroundColor(ColorConstants.lightGray);
			setBackgroundColor(ColorConstants.lightGray);
			setFill(true);
			setPoints(points);
		}
	}
}

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
package org.eclipse.wst.wsdl.asd.design.directedit;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.wsdl.asd.design.editparts.INamedEditPart;

public class LabelCellEditorLocator implements CellEditorLocator {
	private INamedEditPart namedEditPart;
	private Point cursorLocation;

	public LabelCellEditorLocator(INamedEditPart namedEditPart, Point cursorLocation) {
		this.namedEditPart = namedEditPart;
		this.cursorLocation = cursorLocation;
	}
	
	public void relocate(CellEditor celleditor) {
		Text text = (Text)celleditor.getControl();
		if (text.getBounds().x <= 0 && namedEditPart.getLabelFigure() != null) {
			Label label = namedEditPart.getLabelFigure();
			Rectangle boundingRect = label.getTextBounds();

			// Reduce the width by the amount we shifted along the x-axis
			int delta = Math.abs(boundingRect.x - label.getParent().getBounds().x);
//			boundingRect.width = tableCellFigure.getPreferredSize().width - delta;
			
			label.getParent().translateToAbsolute(boundingRect);
			org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
			boundingRect.translate(trim.x, trim.y);
//			boundingRect.width = boundingRect.width - trim.x;
			boundingRect.height = boundingRect.height - trim.y;
			
			boundingRect.width = label.getParent().getBounds().width - delta;
			text.setBounds(boundingRect.x, boundingRect.y, boundingRect.width, boundingRect.height);
			
			// Translate point
			Point translatedPoint = new Point(cursorLocation.x - boundingRect.x,cursorLocation.y - boundingRect.y);
			
			// Calculate text offset corresponding to the translated point
			text.setSelection(0, 0);
			int xCaret = text.getCaretLocation().x;
			int offset = text.getCaretPosition();
			while (xCaret < translatedPoint.x) {
				text.setSelection(offset + 1, offset + 1);			
				xCaret = text.getCaretLocation().x;
				int newOffset = text.getCaretPosition();
				if (newOffset == offset) {
					break;
				}
				offset++;
			}
			text.setSelection(offset, offset);
		}
	}
}

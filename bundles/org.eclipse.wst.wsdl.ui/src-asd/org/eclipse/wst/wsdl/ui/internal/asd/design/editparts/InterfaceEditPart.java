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
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.BoxComponentFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.LinkIconFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.RowLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;

public class InterfaceEditPart extends AbstractBoxtEditPart implements IFeedbackHandler
{
	private RectangleFigure linkIconColumn;
	protected BoxComponentFigure boxFigure;
  public InterfaceEditPart()
  {
    columnData.setColumnWeight("MessageLabel", 0); //$NON-NLS-1$
    columnData.setColumnWeight("MessageContentPane", 100); //$NON-NLS-1$
    columnData.setColumnWeight("parameterName", 50); //$NON-NLS-1$
    columnData.setColumnWeight("parameterType", 50);         //$NON-NLS-1$
  }
  
  protected IFigure createFigure()
  {
	IFigure outer = new Figure();
	outer.setLayoutManager(new ToolbarLayout(true));
    boxFigure = (BoxComponentFigure)super.createFigure();
    boxFigure.getLabel().setIcon(((IInterface) getModel()).getImage());
    boxFigure.setBackgroundColor(ColorConstants.orange);
    boxFigure.setBorder(new LineBorder(1));
    ToolbarLayout toolbarLayout = new ToolbarLayout(false);
    toolbarLayout.setStretchMinorAxis(true);
    boxFigure.setLayoutManager(toolbarLayout);
    outer.add(boxFigure);
    linkIconColumn = new RectangleFigure();
    linkIconColumn.setOutline(false);
    linkIconColumn.setLayoutManager(new ToolbarLayout() {
		public void layout(IFigure parent) {
			super.layout(parent);
			
			// We need to layout on the y-axis
			Iterator children = parent.getChildren().iterator();
			while (children.hasNext()) {
				Object item = children.next();
				if (item instanceof LinkIconFigure) {
					LinkIconFigure linkFigure = (LinkIconFigure) item;
					AbstractGraphicalEditPart ep = linkFigure.getAssociatedEditPart();
					IFigure associatedFigure = ep.getFigure();
					if (associatedFigure != null) {
						// Update the bounds
						Rectangle associatedBounds = associatedFigure.getBounds();
						Rectangle linkFigureBounds = linkFigure.getBounds();
						if (linkFigureBounds.y == associatedBounds.y) {
							break;
						}
						
						linkFigure.setFigureLocation(new Point(associatedBounds.x, associatedBounds.y));
					}
				}
			}
		}
		
		protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
			Dimension dimension = super.calculatePreferredSize(container, wHint, hHint);
			
			// Calculate the height
			Iterator it = getFigure().getChildren().iterator();
			while (it.hasNext()) {
				Object item = it.next();
				if (item instanceof BoxComponentFigure) {
					dimension.height = ((IFigure) item).getPreferredSize().height;
					break;
				}
			}

			// Calculate the width
			it = container.getChildren().iterator();
			while (it.hasNext()) {
				Object item = it.next();
				if (item instanceof LinkIconFigure) {
					dimension.width = dimension.width + ((LinkIconFigure) item).horizontalBuffer;					
					break;
				}
			}

			return dimension;
		}
    });
    
    outer.add(linkIconColumn);
    
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
    
    return outer;
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
  
  public IFigure getLinkIconColumn() {
	  return linkIconColumn;
  }
}

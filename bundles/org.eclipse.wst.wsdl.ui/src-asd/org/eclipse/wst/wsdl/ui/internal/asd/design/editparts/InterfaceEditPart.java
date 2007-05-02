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
import org.eclipse.gef.EditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.BoxComponentFigure;
//import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.BaseLinkIconFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.RowLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

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
		IFigure outer = new Figure() {
			public void validate() {
				super.validate();
				// If we don't layout our Link Icon Column at this point, the link icons
				// will not be properly updated in the scenario where a porttype is added
				// or removed.  The link icons of the existing porttypes will not be updated.
				getLinkIconColumn().getLayoutManager().layout(getLinkIconColumn());
			}
		};
		
		outer.setLayoutManager(new ToolbarLayout(true));
	    boxFigure = (BoxComponentFigure) super.createFigure();
	    
	    if (getModel() instanceof ITreeElement) {
	    	boxFigure.getLabel().setIcon(((ITreeElement) getModel()).getImage());	
	    }
	    
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
					if (item instanceof IFigure) {
						IFigure figure = (IFigure) item;
						figure.getLayoutManager().layout(figure);
					}
				}
			}
			
			protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
				Dimension dimension = super.calculatePreferredSize(container, wHint, hHint);
				
				// Calculate the height
				Iterator it = container.getParent().getChildren().iterator();
				while (it.hasNext()) {
					Object item = it.next();
					if (item instanceof BoxComponentFigure) {
						dimension.height = ((IFigure) item).getPreferredSize().height;
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

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
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.InterfaceColumn;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.ServiceColumn;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.ComponentReferenceConnection;


class EditPartNavigationHandlerUtil
{
  static GraphicalEditPart getRelativeEditPart(GraphicalEditPart focusEditPart, int direction)
  {
    // these 'delta' values need to be set to some value that's large enough to cross the gap
    // between adjacent edit parts (since some layouts may space the edit parts out a bit)
    int dx = 5;
    int dy = 5;
        
    IFigure contentPane = focusEditPart.getContentPane();
    
    // we attempt to compute a rectangle that represents our current location
    Rectangle r = focusEditPart.getFigure().getBounds().getCopy();
    focusEditPart.getFigure().translateToAbsolute(r);
    if (contentPane != focusEditPart.getFigure())
    {
      // if the EditPart has a contentPane (that's no the figure) then we assume
      // effective rectangle is only the header portion of the figure
      //
      Rectangle contentPaneBounds = contentPane.getBounds().getCopy();
      contentPane.translateToAbsolute(contentPaneBounds);
      
      // we assume that when the editpart's figure and contentPane are different
      // that portion of the 'hittable' portion of editpart should not include the contentPane
      if (contentPaneBounds.y > r.y)
      {
        // if the contentPane is lower than the figure we only want the rectangle
        // above the contentPane
        r.height = contentPaneBounds.y - r.y;
        // we adjust the rectangle the right slightly since the content is nested a bit     
        r.x = contentPaneBounds.x;
      }
      if (contentPaneBounds.x > r.x)
      {
        // if the contentPane is to the right of the figure we only want the rectangle
        // to the left of the content pane
        r.width = contentPaneBounds.x - r.x;
      }
    }
    Point p = null;
    if (direction == PositionConstants.NORTH)
    {
      p = r.getTopLeft();
      p.y -= dy;
    }
    else if (direction == PositionConstants.SOUTH)
    {
      p = r.getBottomLeft();
      p.x += dx;
      p.y += dy;
    }
    else if (direction == PositionConstants.EAST)
    {
      p = r.getTopRight();
      p.x += dx;
    }
    else if (direction == PositionConstants.WEST)
    {
      p = r.getLeft();
      p.x -= dx;
    }  
    EditPart t = focusEditPart.getViewer().findObjectAt(p);
        
    if (t instanceof ColumnEditPart || t instanceof DefinitionsEditPart)
    {
      t = focusEditPart;
      while ((t instanceof ColumnEditPart || t instanceof DefinitionsEditPart) && t.getChildren().size() > 0)
      {
    	  t = (EditPart) t.getChildren().get(0);
      }
    }      
    return (GraphicalEditPart)t;
  }
  
  static EditPart getNextSibling(EditPart editPart)
  {    
    EditPart result = null;    
    EditPart parent = editPart.getParent();
    if (parent != null)
    {  
      List children = parent.getChildren();
      int index = children.indexOf(editPart);
      if (index + 1 < children.size())
      {
        result = (EditPart)children.get(index + 1);
      }
    }
    return result;
  }
  
  static EditPart getPrevSibling(EditPart editPart)
  {    
    EditPart result = null;
    EditPart parent = editPart.getParent();
    if (parent != null)
    {  
      List children = parent.getChildren();
      int index = children.indexOf(editPart);
      if (index - 1 >= 0)
      {
        // if this is the first child
        //        
        result = (EditPart)children.get(index - 1);
      } 
    }
    return result;
  } 
  
  static EditPart getNextInterface(EditPart editPart)
  { 
    EditPart result = null;
    for (EditPart e = editPart; e != null; e = e.getParent())
    {
      if (e instanceof InterfaceEditPart)
      {
        InterfaceEditPart ie = (InterfaceEditPart)e;
        result = EditPartNavigationHandlerUtil.getNextSibling(ie);
        break;
      }  
    }  
    return result;
  }          
  
  static EditPart getNextService(EditPart editPart)
  {
    EditPart result = null;
    for (EditPart e = editPart; e != null; e = e.getParent())
    {
      if (e instanceof ServiceEditPart)
      {
        ServiceEditPart ie = (ServiceEditPart)e;
        result = EditPartNavigationHandlerUtil.getNextSibling(ie);
        break;
      }  
    }  
    return result;
  }
  
  static EditPart getFirstBinding(EditPart editPart) 
  {
	  if (editPart == null) return null;
	  
	  RootEditPart rootEditPart = editPart.getRoot();
	  if (rootEditPart == null) return null;
	  
	  List rootEditPartChildren = rootEditPart.getChildren();
	  if (rootEditPartChildren == null || rootEditPartChildren.size() != 1) return null;
	  
	  Object definitionObject = rootEditPartChildren.get(0);
	  if (! (definitionObject instanceof DefinitionsEditPart)) return null;
	  
	  DefinitionsEditPart definitionsEditPart = (DefinitionsEditPart) definitionObject;
	  
	  List children = definitionsEditPart.getChildren();
	  for (int i = 0; i < children.size(); i++) {
		  if (children.get(i) instanceof BindingColumnEditPart) {
			  List bindings = ((BindingColumnEditPart) children.get(i)).getChildren();
			  if (bindings != null && bindings.size() > 0 && bindings.get(0) instanceof BindingEditPart) 
				  return (BindingEditPart) bindings.get(0);
		  }
	  }
	  return null;
  }
  
  static EditPart getFirstService(EditPart editPart) 
  {
	  if (editPart == null) return null;
	  
	  RootEditPart rootEditPart = editPart.getRoot();
	  if (rootEditPart == null) return null;
	  
	  List rootEditPartChildren = rootEditPart.getChildren();
	  if (rootEditPartChildren == null || rootEditPartChildren.size() != 1) return null;
	  
	  Object definitionObject = rootEditPartChildren.get(0);
	  if (! (definitionObject instanceof DefinitionsEditPart)) return null;
	  
	  DefinitionsEditPart definitionsEditPart = (DefinitionsEditPart) definitionObject;
	  
	  List children = definitionsEditPart.getChildren();
	  for (int i = 0; i < children.size(); i++) {
		  Object child = children.get(i);
		  if (child instanceof ColumnEditPart) {
			  ColumnEditPart column = (ColumnEditPart) child;
			  Object model = column.getModel();
			  
			  if (model instanceof ServiceColumn) {
				  List services = column.getChildren();
				  if (services != null && services.size() > 0 && services.get(0) instanceof ServiceEditPart) {
					  return (ServiceEditPart) services.get(0);
				  }
			  }
		  }
	  }
	  return null;
  }
  
  static EditPart getFirstInterface(EditPart editPart) 
  {
	  if (editPart == null) return null;
	  
	  RootEditPart rootEditPart = editPart.getRoot();
	  if (rootEditPart == null) return null;
	  
	  List rootEditPartChildren = rootEditPart.getChildren();
	  if (rootEditPartChildren == null || rootEditPartChildren.size() != 1) return null;
	  
	  Object definitionObject = rootEditPartChildren.get(0);
	  if (! (definitionObject instanceof DefinitionsEditPart)) return null;
	  
	  DefinitionsEditPart definitionsEditPart = (DefinitionsEditPart) definitionObject;
	  
	  List children = definitionsEditPart.getChildren();
	  for (int i = 0; i < children.size(); i++) {
		  Object child = children.get(i);
		  if (child instanceof ColumnEditPart) {
			  ColumnEditPart column = (ColumnEditPart) child;
			  Object model = column.getModel();
			  
			  if (model instanceof InterfaceColumn) {
				  List interfaces = column.getChildren();
				  if (interfaces != null && interfaces.size() > 0 && interfaces.get(0) instanceof InterfaceEditPart) {
					  return (InterfaceEditPart) interfaces.get(0);
				  }
			  }
		  }
	  }
	  return null;
  }
  

  static EditPart getSourceConnectionEditPart(AbstractGraphicalEditPart editPart)
  {
    // find the first connection that targets this editPart
    // navigate backward along the connection (to the left) to find the sourc edit part
    EditPart result = null;
    EditPartViewer viewer = editPart.getViewer();
    LayerManager manager = (LayerManager)editPart.getViewer().getEditPartRegistry().get(LayerManager.ID);
    IFigure layer = manager.getLayer(LayerConstants.CONNECTION_LAYER);    
    for (Iterator i = layer.getChildren().iterator(); i.hasNext(); )
    {
      Figure figure = (Figure)i.next();
      if (figure instanceof ComponentReferenceConnection)
      {
        ComponentReferenceConnection componentReferenceConnection = (ComponentReferenceConnection)figure;
        ConnectionAnchor targetAnchor = componentReferenceConnection.getTargetAnchor();
        if (targetAnchor.getOwner() == editPart.getFigure())
        {  
          ConnectionAnchor sourceAnchor = componentReferenceConnection.getSourceAnchor();
          IFigure sourceFigure = sourceAnchor.getOwner();          
          EditPart part = null;
          while (part == null && sourceFigure != null) 
          {
            part = (EditPart)viewer.getVisualPartMap().get(sourceFigure);
            sourceFigure = sourceFigure.getParent();
          }          
          result = part;
          break;
        }  
      }                
    }    
    return result;    
  }
}

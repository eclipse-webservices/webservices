/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.graph.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.MoveHandleLocator;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.IFeedbackHandler;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.WSDLTreeNodeEditPart;

public class WSDLSelectionHandlesEditPolicyImpl	extends SelectionHandlesEditPolicy
{
  protected IFigure feedback;
  protected Rectangle originalLocation;
  protected WSDLDragAndDropCommand dragAndDropCommand;

  protected List createSelectionHandles()
  {              
    List list = new ArrayList();
    EditPart editPart = getHost();  

    if (editPart instanceof GraphicalEditPart)
    {
      GraphicalEditPart graphicalEditPart = (GraphicalEditPart)editPart;
      IFigure figure = (graphicalEditPart instanceof WSDLTreeNodeEditPart) ? 
                          ((WSDLTreeNodeEditPart)graphicalEditPart).getSelectionFigure() :
                          graphicalEditPart.getFigure();
   
      MoveHandleLocator loc = new MoveHandleLocator(figure);    
      MoveHandle moveHandle = new MoveHandle(graphicalEditPart, loc);     
      list.add(moveHandle);
    }

    return list;
  }   
  

  public boolean understandsRequest(Request request)
  {    
    boolean result = false;

	  if (REQ_MOVE.equals(request.getType()))
    {  
		  result = false; // return false to disable move for now 
    }
    else
    {
	    result = super.understandsRequest(request);
    }
    return result;
  }
  

  public org.eclipse.gef.commands.Command getCommand(Request request) 
  {                                          
    return null;  
  }   

  public void setDragAndDropCommand(WSDLDragAndDropCommand dragAndDropCommand)
  {
    this.dragAndDropCommand = dragAndDropCommand;
  }

  protected org.eclipse.gef.commands.Command getMoveCommand(ChangeBoundsRequest request) 
  {
	  ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_MOVE_CHILDREN);
	  req.setEditParts(getHost());
	
	  req.setMoveDelta(request.getMoveDelta());
	  req.setSizeDelta(request.getSizeDelta());
	  req.setLocation(request.getLocation());

	  return getHost().getParent().getCommand(req);
  } 

  public void showSourceFeedback(Request request)
  {
  	eraseChangeBoundsFeedback(null);
  	
  	if (dragAndDropCommand != null && dragAndDropCommand.canExecute()) {
	  if (REQ_MOVE.equals(request.getType()) || REQ_ADD.equals(request.getType())) {
		  showMoveChangeBoundsFeedback((ChangeBoundsRequest) request);
	  }
	  else if (REQ_CLONE.equals(request.getType())) {
	  	  showCopyChangeBoundsFeedback((ChangeBoundsRequest) request);
	  }
  	}
  }

  protected void showMoveChangeBoundsFeedback(ChangeBoundsRequest request)
  {
  	if (dragAndDropCommand != null && dragAndDropCommand.getFeedbackFigure() != null) {
  		feedback = dragAndDropCommand.getFeedbackFigure();
  		addFeedback(feedback);
  	}
  }        

  protected void showCopyChangeBoundsFeedback(ChangeBoundsRequest request)
  {
  	/*
  	IFigure p = getDragSourceFeedbackFigure();
  	Rectangle r = originalLocation.getTranslated(request.getMoveDelta());
  	Dimension resize = request.getSizeDelta();
  	r.width += resize.width;
  	r.height+= resize.height;
  
  	((GraphicalEditPart)getHost()).getFigure().translateToAbsolute(r);
  	p.translateToRelative(r);
                
    Rectangle pBounds = r.getCopy();                            

    if (dragAndDropCommand != null && dragAndDropCommand.canExecute())
    {                     
      int size = request.getEditParts().size();
      if (size > 0 && request.getEditParts().get(size - 1) == getHost())
      {         
        PointList pointList = dragAndDropCommand.getConnectionPoints(r);
        if (pointList != null && pointList.size() > 0)
        {
          polyLine.setPoints(pointList);
          
          Point firstPoint = pointList.getFirstPoint();
          if (firstPoint != null)
          {
            pBounds = pBounds.getUnion(new Rectangle(firstPoint.x, firstPoint.y, 1, 1));
          }
        }
      }
    }
    p.setBounds(pBounds);
    ghostShape.setBounds(r);
  	p.validate();
  	*/
  }        
/*
  protected IFigure getDragSourceFeedbackFigure() 
  {
    EditPart editPart = getHost(); 
    if (feedback == null && editPart instanceof WSDLTreeNodeEditPart)
    {                                       
      WSDLTreeNodeEditPart baseEditPart = (WSDLTreeNodeEditPart)editPart;
      originalLocation = new Rectangle(baseEditPart.getSelectionFigure().getBounds());
      feedback = createDragSourceFeedbackFigure(baseEditPart.getSelectionFigure());
	}
	return feedback;
  }

  protected IFigure createDragSourceFeedbackFigure(IFigure draggedFigure)
  {  	
  	Polyline polyLine = new Polyline();                         
//    polyLine.setLineStyle(Graphics.LINE_DASHDOT);      
    polyLine.setLineWidth(1);
    addFeedback(polyLine);
			
  	return polyLine;
	} 
	*/

  public void deactivate()
  {
	  if (feedback != null)
    {
		  removeFeedback(feedback);
		  feedback = null;
	  }
	  hideFocus();
	  super.deactivate();
  }

  /**
   * Erase feedback indicating that the receiver object is 
   * being dragged.  This method is called when a drag is
   * completed or cancelled on the receiver object.
   * @param dragTracker org.eclipse.gef.tools.DragTracker The drag tracker of the tool performing the drag.
   */
  protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) 
  {
	  if (feedback != null) 
    {		      
		  removeFeedback(feedback);
	  }
	  feedback = null;
	  originalLocation = null;
  }

  /**
   * Erase feedback indicating that the receiver object is 
   * being dragged.  This method is called when a drag is
   * completed or cancelled on the receiver object.
   * @param dragTracker org.eclipse.gef.tools.DragTracker The drag tracker of the tool performing the drag.
   */
  public void eraseSourceFeedback(Request request) 
  {
    if (REQ_MOVE.equals(request.getType()) ||  REQ_ADD.equals(request.getType()))
    {
		  eraseChangeBoundsFeedback((ChangeBoundsRequest)request);
    }
  }

  protected void hideSelection()
  {
    EditPart editPart = getHost();
    if (editPart instanceof IFeedbackHandler)
    {
      ((IFeedbackHandler)editPart).removeFeedback();
    }
  }

  protected void showSelection()
  {
    EditPart editPart = getHost();
    if (editPart instanceof IFeedbackHandler)
    {
      ((IFeedbackHandler)editPart).addFeedback();
    }
  }
}
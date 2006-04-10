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
package org.eclipse.wst.wsdl.asd.design.editpolicies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.wst.wsdl.asd.design.editparts.IFeedbackHandler;

public class ASDSelectionEditPolicy extends SelectionEditPolicy {
	protected ASDDragAndDropCommand dragAndDropCommand;
	protected IFigure feedback;

	  public void setDragAndDropCommand(ASDDragAndDropCommand dragAndDropCommand) {
	    this.dragAndDropCommand = dragAndDropCommand;
	  }
	  
	  public void showSourceFeedback(Request request)
	  {
	  	eraseChangeBoundsFeedback(null);
	  	
	  	if (dragAndDropCommand != null && dragAndDropCommand.canExecute()) {
		  if (REQ_MOVE.equals(request.getType()) || REQ_ADD.equals(request.getType())) {
			  showMoveChangeBoundsFeedback((ChangeBoundsRequest) request);
		  }
//		  else if (REQ_CLONE.equals(request.getType())) {
//		  	  showCopyChangeBoundsFeedback((ChangeBoundsRequest) request);
//		  }
	  	}
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
	  }
	  
	  protected void showMoveChangeBoundsFeedback(ChangeBoundsRequest request)
	  {
	  	if (dragAndDropCommand != null && dragAndDropCommand.getFeedbackFigure() != null) {
	  		feedback = dragAndDropCommand.getFeedbackFigure();
	  		addFeedback(feedback);
	  	}
	  } 
	  
	protected void hideSelection() {
		if (getHost() instanceof IFeedbackHandler) {
				((IFeedbackHandler) getHost()).removeFeedback();
		}
	}

	protected void showSelection() {
		if (getHost() instanceof IFeedbackHandler) {
			((IFeedbackHandler) getHost()).addFeedback();
		}
	}
}
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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.wst.wsdl.asd.design.editparts.MessageReferenceEditPart;
import org.eclipse.wst.wsdl.asd.design.editparts.ParameterEditPart;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDDragAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDDragReorderAction;
import org.eclipse.wst.wsdl.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.asd.facade.IParameter;

public class ASDDragAndDropCommand extends Command {
	  protected EditPartViewer viewer;    
	  protected ChangeBoundsRequest request;
	  protected Point location;
	  protected ASDDragAction dragAction;    
	  protected boolean canExecute = false;

	  public ASDDragAndDropCommand(EditPartViewer viewer, ChangeBoundsRequest request)
	  {
	    this.viewer = viewer;
	    this.request = request;

	    location = request.getLocation();   
	    EditPart targetEditPart = viewer.findObjectAt(location); 

	    if (targetEditPart instanceof ParameterEditPart || targetEditPart instanceof MessageReferenceEditPart) {
	    	((GraphicalEditPart)viewer.getRootEditPart()).getFigure().translateToRelative(location);
	    	GraphicalEditPart movingChildEditPart;
	    	Vector dragActions = new Vector();
	    	if (request.getType().equals(RequestConstants.REQ_ADD)) { // This really shouldn't be REQ_ADD....
	    		List list = request.getEditParts();
	    		if (list.size() <= 0) {
	    			canExecute = false;
	    			return;
	    		}
	    		
	    		// Grabbing the fist selection
	    		movingChildEditPart = (GraphicalEditPart) list.get(0);
	    		Object model = movingChildEditPart.getModel();
	    		EditPart pointerEditPart = viewer.findObjectAt(location);
	    		
	    		if (model instanceof IParameter) {
	    			dragActions.add(new ASDDragReorderAction(movingChildEditPart, pointerEditPart, getPointerLocation(location)));
	    		}
	    		else if (model instanceof IMessageReference) {
	    			IMessageReference messageRef = (IMessageReference) model;
	    			if (messageRef.getKind() == IMessageReference.KIND_INPUT || messageRef.getKind() == IMessageReference.KIND_OUTPUT) {
	    				dragActions.add(new ASDDragReorderAction(movingChildEditPart, pointerEditPart, getPointerLocation(location)));
//	    				dragActions.add(new WSDLDragSetMessageAction(movingChildEditPart, pointerEditPart, location));
	    			}
	    		}
	    		
//	    		else if (model instanceof Fault) {
//	    		dragActions.add(new WSDLDragSetMessageAction(movingChildEditPart, pointerEditPart, location));
//	    		}            
	    	}
//	    	else if (request.getType().equals(RequestConstants.REQ_CLONE)) {
//	    	List list = request.getEditParts();
//	    	if (list.size() > 0)
//	    	{
//	    	// Grabbing the fist selection
//	    	movingChildEditPart = (WSDLTreeNodeEditPart) list.get(0);
//	    	
//	    	dragActions.add(new CopyWSDLElementAction(viewer,
//	    	(WSDLElement) movingChildEditPart.getModel(),
//	    	(WSDLElement) targetEditPart.getModel(),
//	    	null));
//	    	}
//	    	}
	    	
	    	Iterator it = dragActions.iterator();
	    	while (it.hasNext()) {
	    		dragAction = (ASDDragAction) it.next();
	    		canExecute = dragAction.canExecute();
	    		if (canExecute)
	    			break;
	    	}
	    }
	  }
	  
	  public boolean canExecute()
	  { 
	    return canExecute;
	  }     
	  
	  public void execute() {
	    if (canExecute)
	    { 
	      dragAction.run(); 
	    }
	  }
	  
	  public IFigure getFeedbackFigure() {
	  	if (dragAction != null) {
	  		return dragAction.getFeedbackFigure();
	  	}
	  	else {
	  		return null;
	  	}
	  }
	  
	  /*
	   * This method compensates for the current scroll position
	   */
	  private Point getPointerLocation(Point origPointerLocation) {
	  	 Point compensatedLocation = origPointerLocation;
	  	 FigureCanvas figureCanvas = (FigureCanvas) viewer.getControl();
	     int yOffset = figureCanvas.getViewport().getVerticalRangeModel().getValue();
	     compensatedLocation.y = compensatedLocation.y + yOffset;
	     
	     return compensatedLocation;
	  }
}
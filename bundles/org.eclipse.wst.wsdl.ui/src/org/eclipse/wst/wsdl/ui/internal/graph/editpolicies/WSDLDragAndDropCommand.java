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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.actions.CopyWSDLElementAction;
import org.eclipse.wst.wsdl.ui.internal.actions.WSDLDragAction;
import org.eclipse.wst.wsdl.ui.internal.actions.WSDLDragReorderAction;
import org.eclipse.wst.wsdl.ui.internal.actions.WSDLDragSetMessageAction;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.WSDLTreeNodeEditPart;
import org.eclipse.wst.xml.core.document.XMLNode;


public class WSDLDragAndDropCommand extends Command //AbstractCommand
{ 
  protected EditPartViewer viewer;    
  protected ChangeBoundsRequest request;
  protected Point location;
  protected WSDLDragAction dragAction;    
  protected boolean canExecute = false;

  public WSDLDragAndDropCommand(EditPartViewer viewer, ChangeBoundsRequest request)
  {
  	boolean constructor = false;
    this.viewer = viewer;                    
    this.request = request;

    location = request.getLocation();   
    EditPart targetEditPart = viewer.findObjectAt(location); 

    // Only support drag and drop for WSDLTreeNodeEditPart
  	if (targetEditPart instanceof WSDLTreeNodeEditPart) {
  		WSDLElement wsdlElement = (WSDLElement) targetEditPart.getModel();
  		if (wsdlElement.getElement() instanceof XMLNode) {
  			((GraphicalEditPart)viewer.getRootEditPart()).getFigure().translateToRelative(location);
  			Rectangle targetRectangle; 
  			WSDLTreeNodeEditPart targetParentEditPart;
  			WSDLTreeNodeEditPart movingChildEditPart;
  			Vector dragActions = new Vector();

  			if (request.getType().equals(RequestConstants.REQ_ADD))  { // This really shouldn't be REQ_ADD....
  				List list = request.getEditParts();
  				if (list.size() <= 0) {
  					canExecute = false;
  					return;
  				}
            
  				// Grabbing the fist selection
  				movingChildEditPart = (WSDLTreeNodeEditPart) list.get(0);
  				Object model = movingChildEditPart.getModel();
  				EditPart pointerEditPart = viewer.findObjectAt(location);
 
  				if (model instanceof Input || model instanceof Output) {
  					dragActions.add(new WSDLDragReorderAction(movingChildEditPart, pointerEditPart, getPointerLocation(location)));
  					dragActions.add(new WSDLDragSetMessageAction(movingChildEditPart, pointerEditPart, location));
  				}
  				else if (model instanceof Part) {
  					dragActions.add(new WSDLDragReorderAction(movingChildEditPart, pointerEditPart, getPointerLocation(location)));     
  				}
  				else if (model instanceof Fault) {
  					dragActions.add(new WSDLDragSetMessageAction(movingChildEditPart, pointerEditPart, location));
  				}            
  			}
  			else if (request.getType().equals(RequestConstants.REQ_CLONE)) {
  				List list = request.getEditParts();
  				if (list.size() > 0)
  				{
  					// Grabbing the fist selection
  					movingChildEditPart = (WSDLTreeNodeEditPart) list.get(0);
             
  					dragActions.add(new CopyWSDLElementAction(viewer,
             											  (WSDLElement) movingChildEditPart.getModel(),
             										      (WSDLElement) targetEditPart.getModel(),
														  null));
  				}
  			}
        
  			Iterator it = dragActions.iterator();
  			while (it.hasNext()) {
  				dragAction = (WSDLDragAction) it.next();
  				canExecute = dragAction.canExecute();
  				if (canExecute)
  					break;
  			}
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
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
package org.eclipse.wst.wsdl.asd.editor.actions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.asd.facade.IOperation;
import org.eclipse.wst.wsdl.asd.facade.IParameter;

public class ASDDragReorderAction extends ASDDragAction {
	  protected IASDObject movingChild;
	  protected IASDObject newParent;
	  protected GraphicalEditPart leftSiblingEditPart;
	  protected GraphicalEditPart rightSiblingEditPart;
	  protected IASDObject leftSibElement;
	  protected IASDObject rightSibElement;
	  protected boolean canNeverExecute = false;
	  
	  public ASDDragReorderAction(GraphicalEditPart movingChildEditPart, EditPart pointerEditPart, Point pointerLocation) {
		  super(null);
	  	this.movingChildEditPart = movingChildEditPart;
	  	this.pointerLocation = pointerLocation;

		EditPart possibleParent = getParentEditPart(pointerEditPart);
		
		if (!(possibleParent instanceof GraphicalEditPart)) {
			canNeverExecute = true;
			return;
		}
	  	
		GraphicalEditPart targetParentEditPart = (GraphicalEditPart) possibleParent;
		
		if (pointerEditPart.getParent() != null) {
			// Sort the siblings (and itself)
			List targetEditPartSiblings = pointerEditPart.getParent().getChildren();
			Collections.sort(targetEditPartSiblings, new VerticalEditPartComparator());
	  
			// Get 'left' and 'right' siblings
			leftSiblingEditPart = getVerticalLeftSibling(targetEditPartSiblings, movingChildEditPart, pointerLocation);
			rightSiblingEditPart = getVerticalRightSibling(targetEditPartSiblings, movingChildEditPart, pointerLocation);           	

			if (movingChildEditPart != null)
				movingChild = (IASDObject) movingChildEditPart.getModel();
			if (leftSiblingEditPart != null)
				leftSibElement = (IASDObject) leftSiblingEditPart.getModel();
			if (rightSiblingEditPart != null)
				rightSibElement = (IASDObject) rightSiblingEditPart.getModel();
			
			newParent = (IASDObject) targetParentEditPart.getModel();
		}
		else {
			newParent = (IASDObject) targetParentEditPart.getModel();
			leftSibElement = null;
			rightSibElement= null;
		}
	  }
	            
	  public boolean canExecute()
	  {
	    boolean result = false;
	    
	    if (canNeverExecute || newParent == null || movingChild == null) {
	    	return false;
	    }
	    
	    if (movingChild instanceof IMessageReference) {
	    	IMessageReference messageRef = (IMessageReference) movingChild;
	    	if (messageRef.getKind() == IMessageReference.KIND_INPUT || messageRef.getKind() == IMessageReference.KIND_OUTPUT) {
	    		IMessageReference leftSibMessageRef = (IMessageReference) leftSibElement;

	    		result = true;
	    		if (leftSibMessageRef != null) {
	    			if (leftSibMessageRef.getKind() == IMessageReference.KIND_FAULT) {
	    				result = false;
	    			}
	    		}
	    		if (!(result && newParent.equals(messageRef.getOwnerOperation()))) {
	    			result = false;
	    		}
	    	}
	    }
	    else if (movingChild instanceof IParameter) {
	    	if (newParent instanceof IMessageReference && newParent.equals(((IParameter) movingChild).getOwner())) {
	    		result = true;
	    	}
	    }
	    
	    return result;
	  }           

	  /*
	   * @see IAction#run()
	   */
	  public void run()
	  {                            
//	    beginRecording();
	  	if (canExecute()) {
	  		if (movingChild instanceof IMessageReference) {
	  			Object messageRefOwner = ((IMessageReference) movingChild).getOwnerOperation();
	  			if (messageRefOwner instanceof IOperation) {
	  				IMessageReference leftSib = (IMessageReference) leftSibElement;
	  				IMessageReference rightSib = (IMessageReference) rightSibElement;
	  				IMessageReference movingSib = (IMessageReference) movingChild;
	  				Command command = ((IOperation) messageRefOwner).getReorderMessageReferencesCommand(leftSib, rightSib, movingSib);
	  				command.execute();
	  			}
	  		}
	  		else if (movingChild instanceof IParameter) {
	  			Object paramOwner = ((IParameter) movingChild).getOwner();
	  			if (paramOwner instanceof IMessageReference) {
	  				IParameter leftSib = (IParameter) leftSibElement;
	  				IParameter rightSib = (IParameter) rightSibElement;
	  				IParameter movingSib = (IParameter) movingChild;
	  				Command command = ((IMessageReference) paramOwner).getReorderParametersCommand(leftSib, rightSib, movingSib);
	  				command.execute();
	  			}
	  		}
//	  		wsdlEditor.getSelectionManager().setSelection(new StructuredSelection(movingChild));
	  	}
//	    endRecording();
	  }
	  
	  public IFigure getFeedbackFigure() {
	  	IFigure feedbackFigure = null;
	    if (feedbackFigure == null && movingChildEditPart instanceof GraphicalEditPart)
	    {                                       
	      feedbackFigure = new Polyline();
	      ((Polyline)feedbackFigure).setLineWidth(2);	
	      drawLines((Polyline) feedbackFigure);
	    }
	    
	  	return feedbackFigure;
	  }

	  /* Return a PointList which will be used to add a feedback figure.  The feedback figure will be a
	   * Polyline composed of the Points in the PointList
	   */
	  private PointList drawLines(Polyline polyLine) {
	    PointList pointList = new PointList(); 
	    
	    if (leftSiblingEditPart != null) {
	    	Rectangle leftRectangle = leftSiblingEditPart.getFigure().getBounds();
		   	int xCoord = leftRectangle.x;
		  	int yCoord = leftRectangle.y;
		   	int height = leftRectangle.height;
		   	int width = leftRectangle.width;

		   	// Draw left end line
		   	addLineToPolyline(polyLine, xCoord, yCoord + height + 3, xCoord, yCoord + height - 3);
		   	addLineToPolyline(polyLine, xCoord, yCoord + height - 3, xCoord, yCoord + height);
		   	
		   	// Draw horizontal line
		   	addLineToPolyline(polyLine, xCoord, yCoord + height, xCoord + width, yCoord + height);
		   	
		   	// Draw right end line
		   	addLineToPolyline(polyLine, xCoord + width, yCoord + height, xCoord + width, yCoord + height - 3);
		  	addLineToPolyline(polyLine, xCoord + width, yCoord + height, xCoord + width, yCoord + height + 3);   	
	    }
	    else if (rightSiblingEditPart != null) {
	    	Rectangle rightRectangle = rightSiblingEditPart.getFigure().getBounds();
	    	int xCoord = rightRectangle.x;
	    	int yCoord = rightRectangle.y;
	   		int width = rightRectangle.width;	

	   		// Draw left end line
		   	addLineToPolyline(polyLine, xCoord, yCoord + 3, xCoord, yCoord - 3);
		   	addLineToPolyline(polyLine, xCoord, yCoord - 3, xCoord, yCoord);
		   	
		   	// Draw horizontal line
		   	addLineToPolyline(polyLine, xCoord, yCoord, xCoord + width, yCoord);
		   	
		   	// Draw right end line
		   	addLineToPolyline(polyLine, xCoord + width, yCoord, xCoord + width, yCoord - 3);
		  	addLineToPolyline(polyLine, xCoord + width, yCoord, xCoord + width, yCoord + 3);   
	    }

	    return pointList;
	  }                    
	  
	  protected Polyline addLineToPolyline(Polyline polyline, int x1, int y1, int x2, int y2) {
		polyline.addPoint(new Point(x1, y1));
		polyline.addPoint(new Point(x2, y2));

		return polyline;
	  }                                          
	  
	  /*
	   * Pre-condition: The List siblings should be sorted by it's y-coordinate 
	   */
	  private GraphicalEditPart getVerticalRightSibling(List siblings, GraphicalEditPart movingEditPart, Point pointerLocation) {
	  	GraphicalEditPart rightSibling = null;
	  	int pointerYLocation = pointerLocation.y;

	  	for (int index = 0; index < siblings.size(); index++) {
	  		GraphicalEditPart sibling = (GraphicalEditPart) siblings.get(index);
	  		int siblingYLocation = sibling.getFigure().getBounds().getCenter().y;

	  		if (siblingYLocation > pointerYLocation) {
	  			rightSibling = sibling;
	  			break;
	  		}
	  	}
	  	
	  	return rightSibling;
	  }

	  /*
	   * Pre-condition: The List siblings should be sorted by it's y-coordinate 
	   */
	  private GraphicalEditPart getVerticalLeftSibling(List siblings, GraphicalEditPart movingEditPart, Point pointerLocation) {
	  	GraphicalEditPart leftSibling = null;
	  	int pointerYLocation = pointerLocation.y;

	  	int index;
	  	for (index = 0; index < siblings.size(); index++) {
	  		GraphicalEditPart sibling = (GraphicalEditPart) siblings.get(index);
	  		int siblingYLocation = sibling.getFigure().getBounds().getCenter().y;
	  	  	
	  		if (siblingYLocation > pointerYLocation) {
	  			if (index > 0) {
	  				leftSibling = (GraphicalEditPart) siblings.get(index - 1);
	  			}

	  			break;
	  		}
	  	}

	  	if (index == siblings.size()) {
	  		leftSibling = (GraphicalEditPart) siblings.get(index - 1);
	  	}

	  	return leftSibling;
	  }
	  
	  protected EditPart getParentEditPart(EditPart target)
	  {
	  	return target.getParent();
	  }

	  private class VerticalEditPartComparator implements Comparator {
	  	
	  	public VerticalEditPartComparator() {
	  	}
	  	
	  	public int compare(Object part1, Object part2) {
	  		int compareValue = 0;

	  		if (part1 instanceof GraphicalEditPart && part2 instanceof GraphicalEditPart) {
	  			GraphicalEditPart editPart1 = (GraphicalEditPart) part1;
	  			GraphicalEditPart editPart2 = (GraphicalEditPart) part2;
	  			
	  			int editPart1Y = editPart1.getFigure().getBounds().getCenter().y;
	  			int editPart2Y = editPart2.getFigure().getBounds().getCenter().y;
	  			
	  			Integer integerPart1 = new Integer(editPart1Y);
	  			Integer integerPart2 = new Integer(editPart2Y);
	  			
	  			compareValue = integerPart1.compareTo(integerPart2);
	  		}

	  		return compareValue;
	  	}
	  }
}
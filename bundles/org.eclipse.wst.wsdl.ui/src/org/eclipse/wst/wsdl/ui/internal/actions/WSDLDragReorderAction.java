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
package org.eclipse.wst.wsdl.ui.internal.actions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.wsdl.OperationType;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.OperationImpl;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.graph.WSDLComponentViewer;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.WSDLTreeNodeEditPart;


public class WSDLDragReorderAction extends WSDLDragAction
{
  protected WSDLEditor wsdlEditor;
  protected WSDLElement movingChild;
  protected WSDLElement newParent;
  protected WSDLTreeNodeEditPart leftSiblingEditPart;
  protected WSDLTreeNodeEditPart rightSiblingEditPart;
  protected WSDLElement leftSibElement;
  protected WSDLElement rightSibElement;
  protected boolean canNeverExecute = false;
  
  public WSDLDragReorderAction(WSDLTreeNodeEditPart movingChildEditPart, EditPart pointerEditPart, Point pointerLocation) {
  	this.movingChildEditPart = movingChildEditPart;
  	this.pointerLocation = pointerLocation;

	EditPart possibleParent = getParentEditPart(pointerEditPart);
	
	if (!(possibleParent instanceof WSDLTreeNodeEditPart)) {
		canNeverExecute = true;
		return;
	}
  	
	WSDLTreeNodeEditPart targetParentEditPart = (WSDLTreeNodeEditPart) possibleParent;
	wsdlEditor = ((WSDLComponentViewer) possibleParent.getViewer()).getWSDLEditor();
	
	if (pointerEditPart.getParent() != null) {
		// Sort the siblings (and itself)
		List targetEditPartSiblings = pointerEditPart.getParent().getChildren();
		Collections.sort(targetEditPartSiblings, new VerticalEditPartComparator());
  
		// Get 'left' and 'right' siblings
		leftSiblingEditPart = getVerticalLeftSibling(targetEditPartSiblings, movingChildEditPart, pointerLocation);
		rightSiblingEditPart = getVerticalRightSibling(targetEditPartSiblings, movingChildEditPart, pointerLocation);           	

		if (movingChildEditPart != null)
			movingChild = (WSDLElement) movingChildEditPart.getModel();
		if (leftSiblingEditPart != null)
			leftSibElement = (WSDLElement) leftSiblingEditPart.getModel();
		if (rightSiblingEditPart != null)
			rightSibElement = (WSDLElement) rightSiblingEditPart.getModel();
		
		newParent = (WSDLElement) targetParentEditPart.getModel();
	}
	else {
		newParent = (WSDLElement) targetParentEditPart.getModel();
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
    
    if (movingChild instanceof Input) {
    	if (!(leftSibElement instanceof Fault) && newParent.equals(movingChild.eContainer())) {
    		result = true;
    	}
    }
    else if (movingChild instanceof Output) {
    	if (!(leftSibElement instanceof Fault) && newParent.equals(movingChild.eContainer())) {
    		result = true;
    	}
    }
    else if (movingChild instanceof Part) {
    	if (newParent instanceof Message && newParent.equals(movingChild.eContainer())) {
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
//    beginRecording();
  	if (canExecute()) {
  		if (movingChild instanceof Input) {
  			Input input = (Input) movingChild;
  			OperationImpl operation = (OperationImpl) input.eContainer();
  			
  			if (leftSiblingEditPart == null) {
  				// Input/Output style
  				setInputOutputOrder(operation, true);
  			}
  			else {
  				// Output/Input style
  				setInputOutputOrder(operation, false);
  			}
  		}
  		else if (movingChild instanceof Output) {
 			Output output = (Output) movingChild;
  			OperationImpl operation = (OperationImpl) output.eContainer();
  			
  			if (leftSiblingEditPart == null) {
  				// Output/Input style
  				setInputOutputOrder(operation, false);
  			}
  			else {
  				// Input/Output style
  				setInputOutputOrder(operation, true);
  			}
  		}
  		else if (movingChild instanceof Part) {
  			Message message = (Message) newParent;
  			List parts = message.getEParts();

  			parts.remove(movingChild);

  			int leftIndex = -1, rightIndex = -1;
  			if (leftSibElement != null) {
  				leftIndex = parts.indexOf(leftSibElement);
  			}
  			if (rightSibElement != null) {
  				rightIndex = parts.indexOf(rightSibElement);
  			}

  			if (leftIndex == -1) {
  				// Add moving child to the front
  				parts.add(0, movingChild);  				
  			}
  			else if (rightIndex == -1) {
  				// Add moving child to the end
  				parts.add(movingChild);
  			}
  			else {
  				// Add moving child after the occurence of the left sibling
  				parts.add(leftIndex + 1, movingChild);
  			}
  		}
  		wsdlEditor.getSelectionManager().setSelection(new StructuredSelection(movingChild));
  	}
//    endRecording();
  }
  
  public IFigure getFeedbackFigure() {
  	IFigure feedbackFigure = null;
    if (feedbackFigure == null && movingChildEditPart instanceof WSDLTreeNodeEditPart)
    {                                       
      feedbackFigure = new Polyline();                         
//  polyLine.setLineStyle(Graphics.LINE_DASHDOT);      
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
	   	int xCoord = leftRectangle.x + 15;
	  	int yCoord = leftRectangle.y;
	   	int height = leftRectangle.height;
	   	int width = leftRectangle.width - 15;

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
    	int xCoord = rightRectangle.x + 15;
    	int yCoord = rightRectangle.y;
   		int width = rightRectangle.width - 15;	

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
  private WSDLTreeNodeEditPart getVerticalRightSibling(List siblings, WSDLTreeNodeEditPart movingEditPart, Point pointerLocation) {
  	WSDLTreeNodeEditPart rightSibling = null;
  	int pointerYLocation = pointerLocation.y;

  	for (int index = 0; index < siblings.size(); index++) {
  		WSDLTreeNodeEditPart sibling = (WSDLTreeNodeEditPart) siblings.get(index);
  		int siblingYLocation = sibling.getSelectionFigure().getBounds().getCenter().y;

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
  private WSDLTreeNodeEditPart getVerticalLeftSibling(List siblings, WSDLTreeNodeEditPart movingEditPart, Point pointerLocation) {
  	WSDLTreeNodeEditPart leftSibling = null;
  	int pointerYLocation = pointerLocation.y;

  	int index;
  	for (index = 0; index < siblings.size(); index++) {
  		WSDLTreeNodeEditPart sibling = (WSDLTreeNodeEditPart) siblings.get(index);
  		int siblingYLocation = sibling.getSelectionFigure().getBounds().getCenter().y;
  	  	
  		if (siblingYLocation > pointerYLocation) {
  			if (index > 0) {
  				leftSibling = (WSDLTreeNodeEditPart) siblings.get(index - 1);
  			}

  			break;
  		}
  	}

  	if (index == siblings.size()) {
  		leftSibling = (WSDLTreeNodeEditPart) siblings.get(index - 1);
  	}

  	return leftSibling;
  }
  
  protected EditPart getParentEditPart(EditPart target)
  {
  	return target.getParent();
  }
  
  // boolean inputFirst = true if the Input is the first in the order of Input and Output
  private void setInputOutputOrder(OperationImpl operation, boolean inputFirst) {
  	// We need to determine more info before we can set the style	
  	if (inputFirst && operation.getEInput() != null) {
  		// Check if there is an Output
  		if (operation.getEOutput() != null) {
  			// Order is: Input, Output
/*  			
  			Node operationNode = operation.getElement();		
  			NodeList childNodeList = operationNode.getChildNodes();
  			
  			Node inputNode = operation.getEInput().getElement();
  			Node outputNode = operation.getEOutput().getElement();
  			
  			Node inputTextNode = inputNode.getNextSibling();
  			Node outputTextNode = outputNode.getNextSibling();
  			
  			int inputNodeIndex = getNodeIndex(childNodeList, inputNode);
  			int outputNodeIndex = getNodeIndex(childNodeList, outputNode);
  			
  			if (inputNodeIndex > outputNodeIndex) {
  				// We need to swap Input and Output
  				operationNode.removeChild(inputNode);
  				operationNode.removeChild(outputNode);
  				
  				operationNode.insertBefore(inputNode, outputTextNode);
  				operationNode.insertBefore(outputNode, inputTextNode);
  			}
*/  			
  			
  			setOperationStyle(operation, OperationType.REQUEST_RESPONSE);
  		}
  		/*
  		else {
  			// Order is: Input
  			setOperationStyle(operation, OperationType.ONE_WAY);
  		}
  		*/
  	}
  	else if (operation.getEOutput() != null) {
  		// Check if there is an Input
  		if (operation.getEInput() != null) {
  			// Order is: Output, Input
/*  			
  			Node operationNode = operation.getElement();		
  			NodeList childNodeList = operationNode.getChildNodes();
  			
  			Node inputNode = operation.getEInput().getElement();
  			Node outputNode = operation.getEOutput().getElement();
  			
  			Node inputTextNode = inputNode.getNextSibling();
  			Node outputTextNode = outputNode.getNextSibling();
  			
  			int inputNodeIndex = getNodeIndex(childNodeList, inputNode);
  			int outputNodeIndex = getNodeIndex(childNodeList, outputNode);
  			
  			if (outputNodeIndex > inputNodeIndex) {
  				// We need to swap Input and Output
  				operationNode.removeChild(inputNode);
  				operationNode.removeChild(outputNode);

  				operationNode.insertBefore(inputNode, outputTextNode);
  				operationNode.insertBefore(outputNode, inputTextNode);
  			}
*/  			
  			setOperationStyle(operation, OperationType.SOLICIT_RESPONSE);
  		}
  		/*
  		else {
  			// Order is: Output
  			setOperationStyle(operation, OperationType.NOTIFICATION);
  		}
  		*/
  	}
  }

/*
 * return -1 if node is not found in the given nodeList
 */
//	private int getNodeIndex(NodeList nodeList, Node node) {
//		int index = 0;
//		while (index < nodeList.getLength() && !(nodeList.item(index).equals(node))) {
//			index++;
//		}
//		
//		if (index >= nodeList.getLength()) {
//			index = -1;
//		}
//		
//		return index;
//	}
  
  private void setOperationStyle(OperationImpl operation, OperationType operationType) {
  	// If there is no style to begin with, don't set one now
  	if (operation.getStyle() == null) {
  		return;
  	}
  	
  	operation.setStyle(operationType);
  }
  
  private class VerticalEditPartComparator implements Comparator {
  	
  	public VerticalEditPartComparator() {
  	}
  	
  	public int compare(Object part1, Object part2) {
  		int compareValue = 0;

  		if (part1 instanceof WSDLTreeNodeEditPart && part2 instanceof WSDLTreeNodeEditPart) {
  			WSDLTreeNodeEditPart editPart1 = (WSDLTreeNodeEditPart) part1;
  			WSDLTreeNodeEditPart editPart2 = (WSDLTreeNodeEditPart) part2;
  			
  			int editPart1Y = editPart1.getSelectionFigure().getBounds().getCenter().y;
  			int editPart2Y = editPart2.getSelectionFigure().getBounds().getCenter().y;
  			
  			Integer integerPart1 = new Integer(editPart1Y);
  			Integer integerPart2 = new Integer(editPart2Y);
  			
  			compareValue = integerPart1.compareTo(integerPart2);
  		}

  		return compareValue;
  	}
  }
}

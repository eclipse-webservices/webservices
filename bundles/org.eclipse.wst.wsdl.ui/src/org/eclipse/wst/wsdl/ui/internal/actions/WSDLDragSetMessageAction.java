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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.WSDLElement; 
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.IConnectedFigure;
import org.eclipse.wst.wsdl.ui.internal.graph.WSDLComponentViewer;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.GroupEditPart;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.WSDLTreeNodeEditPart;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;

public class WSDLDragSetMessageAction extends WSDLDragAction {
	protected EditPart pointerEditPart;
	protected WSDLElement movingChild;
	protected WSDLElement pointerModelElement;
	
	public WSDLDragSetMessageAction(WSDLTreeNodeEditPart movingChildEditPart, EditPart pointerEditPart, Point pointerLocation) {
		this.movingChildEditPart = movingChildEditPart;
		this.pointerEditPart = pointerEditPart;
		this.pointerLocation = pointerLocation;
		
		movingChild = (WSDLElement) movingChildEditPart.getModel();
		pointerModelElement = (WSDLElement) pointerEditPart.getModel();
		
		// Allow the user to hover over a Part (of a Message) to set the Message
		if (pointerModelElement instanceof Part) {
			this.pointerEditPart = pointerEditPart.getParent();
			pointerModelElement = (WSDLElement) this.pointerEditPart.getModel();
		}
	}

	public boolean canExecute() {
	    boolean result = false;

	    if (pointerModelElement == null || movingChild == null) {
	    	return false;
	    }
	    
	    if (movingChild instanceof Input || movingChild instanceof Output || movingChild instanceof Fault) {
	    	if (pointerModelElement instanceof Message) {
	    		result = true;
	    	}
	    }

	    return result;
	}
	
	public void run() {
		if (canExecute()) {
			MessageReference messageReference = (MessageReference) movingChild;
			messageReference.setEMessage((Message) pointerModelElement);
			((WSDLElementImpl) messageReference).updateElement(true);
			
			WSDLEditor wsdlEditor = ((WSDLComponentViewer) pointerEditPart.getViewer()).getWSDLEditor();
			wsdlEditor.getSelectionManager().setSelection(new StructuredSelection(movingChild));
		}
	}

	public IFigure getFeedbackFigure() {
		Polyline polyfigure = new Polyline();
		GroupEditPart leftGroupEditPart = getGroupEditPart(movingChildEditPart);
		GroupEditPart rightGroupEditPart = getGroupEditPart(pointerEditPart);
		int mx = rightGroupEditPart.getFigure().getBounds().x - 5;                                                      
 
        drawLine(polyfigure, leftGroupEditPart.outputConnection.getFigure(), ((AbstractGraphicalEditPart) pointerEditPart).getFigure(), mx);
		
		return polyfigure;
	}
	
	  protected void drawLine(Polyline feedbackFigure, IFigure a, IFigure b, int mx)
	  {
	        Rectangle r1 = getConnectionBounds(a);
	        Rectangle r2 = getConnectionBounds(b);
	        int x1 = r1.x + r1.width;
	        int y1 = r1.y + r1.height / 2;
	        int x2 = r2.x - 1;
	        int y2 = r2.y + 8;
                                          
	        // draw horizontal line
	       	addLineToPolyline(feedbackFigure, x1, y1, mx, y1);

	        // draw horizontal line
	       	addLineToPolyline(feedbackFigure, mx, y2, x2 - 1, y2);

	        // draw the arrow head
	       	addLineToPolyline(feedbackFigure, x2 - 1, y2, x2 - 4, y2 - 3);
	       	addLineToPolyline(feedbackFigure, x2 - 1, y2, x2 - 4, y2 + 3);
	  }

	  protected Rectangle getConnectionBounds(IFigure figure)
	  {                 
	    Rectangle r = null;
	    if (figure instanceof IConnectedFigure)
	    {
	      IConnectedFigure connectedFigure = (IConnectedFigure)figure;
	      r = connectedFigure.getConnectionFigure().getBounds();
	    }
	    else
	    {
	      r = figure.getBounds();
	    }
	    return r; 
	  }                      
	  
	  protected Polyline addLineToPolyline(Polyline polyline, int x1, int y1, int x2, int y2) {
      	polyline.addPoint(new Point(x1, y1));
      	polyline.addPoint(new Point(x2, y2));
      	polyline.setLineWidth(1);
      	//polyline.setLineStyle(Graphics.LINE_DOT);
      	
      	return polyline;
	  }
	  
	  private GroupEditPart getGroupEditPart(EditPart editPart) {
	  	EditPart parentPart = editPart.getParent();
	  	
	  	if (parentPart == null) {
	  		return null;
	  	}
	  	if (!(parentPart instanceof GroupEditPart)) {
	  		parentPart = getGroupEditPart(parentPart);
	  	}
	  	
	  	return (GroupEditPart) parentPart;
	  }
}
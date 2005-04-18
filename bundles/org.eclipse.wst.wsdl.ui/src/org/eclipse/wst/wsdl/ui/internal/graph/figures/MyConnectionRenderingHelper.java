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
package org.eclipse.wst.wsdl.ui.internal.graph.figures;

import java.util.Iterator;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.DefinitionEditPart;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.GroupEditPart;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.PartReferenceSectionEditPart;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.IConnectedFigure;
              

public class MyConnectionRenderingHelper //implements ISelectionChangedListener
{               
  protected boolean isOutlined = true;
  protected EditPartViewer viewer;

  public MyConnectionRenderingHelper(EditPartViewer viewer)
  {
    this.viewer = viewer;    
    //viewer.addSelectionChangedListener(this);
    //setFocusTraversable(false); 
    //setEnabled(false); 
  }      
  
  //public void selectionChanged(SelectionChangedEvent event) 
  //{
  //}
            
  public void fillShapeHelper(Graphics graphics)
  { 
    drawLines(graphics, viewer.getRootEditPart());    
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
            
  final static int DOTTED_LINE_HEIGHT = 20;
  protected int getClippedY(int y, Rectangle bounds)
  {                        
    if (bounds != null)
    {
      if (y < bounds.y)
      {
        y = bounds.y;
      }     
      if (y  > (bounds.y + bounds.height))
      {
        y = bounds.y + bounds.height; 
      }
    }
    return y;
  }
    
  protected void drawLine(Graphics graphics, IFigure a, IFigure b, int mx, Rectangle bounds)
  {
        Rectangle r1 = getConnectionBounds(a);
        Rectangle r2 = getConnectionBounds(b);
        int x1 = r1.x + r1.width;
        int y1 = r1.y + r1.height / 2;
        int x2 = r2.x - 1;
        int y2 = r2.y + r2.height / 2;
          
        int clippedY1 = getClippedY(y1, bounds);
        int clippedY2 = getClippedY(y2, bounds);

        graphics.setForegroundColor(ColorConstants.black);

        if (clippedY1 == y1 || clippedY2 == y2)
        {                       
          if (clippedY1 == y1)
          {                                                
            // draw horizontal line
            graphics.drawLine(x1, y1, mx, y1); 
          }
       
          if (clippedY2 == y2)
          {        
            // draw horizontal line
            graphics.drawLine(mx, y2, x2 - 1, y2);    

            // draw the arrow head
            //
            graphics.drawLine(x2 - 1, y2, x2 - 4, y2 - 3); 
            graphics.drawLine(x2 - 1, y2, x2 - 4, y2 + 3); 
          }  
                  
            
          // draw the vertical line including dotted ends
          //
          int lowClippedY = Math.min(clippedY1, clippedY2);
          int highClippedY = Math.max(clippedY1, clippedY2);
          int lowY = Math.min(y1, y2);
          int highY = Math.max(y1, y2);
            
          if (lowY == lowClippedY && highY == highClippedY)
          {
            graphics.drawLine(mx, lowClippedY, mx, highClippedY);
          }
          else
          { 
            int dottedLineLength = Math.min(DOTTED_LINE_HEIGHT, (highY - lowY)/3);
            if (lowY != lowClippedY)
            {
              graphics.setLineStyle(Graphics.LINE_DOT);
              int dottedY = lowClippedY + dottedLineLength;
              graphics.drawLine(mx, lowClippedY, mx, dottedY); 
              graphics.setLineStyle(Graphics.LINE_SOLID);
              graphics.drawLine(mx, dottedY, mx, highClippedY); 
            }
            else //if (highY != highClippedY)
            {
              graphics.setLineStyle(Graphics.LINE_DOT);
              int dottedY = highClippedY - dottedLineLength;
              graphics.drawLine(mx, highClippedY, mx, dottedY); 
              graphics.setLineStyle(Graphics.LINE_SOLID);
              graphics.drawLine(mx, dottedY, mx, lowClippedY); 
            }                              
          }
        }
  }

  protected void drawLines(Graphics graphics, EditPart editPart)
  { 
    if (editPart instanceof GroupEditPart)
    { 
      GroupEditPart leftGroupEditPart = (GroupEditPart)editPart;
      GroupEditPart rightGroupEditPart = leftGroupEditPart.getNext();

      if (leftGroupEditPart != null && rightGroupEditPart != null)
      {                                                  
        if (leftGroupEditPart.outputConnection != null && rightGroupEditPart.inputConnection != null)
        {
          int mx = rightGroupEditPart.getFigure().getBounds().x - 5;                                                      

          Rectangle l = leftGroupEditPart.outerPane.getBounds();
          Rectangle r = leftGroupEditPart.outerPane.getBounds();

          // here we compute the union of the group bounds... rectangle.union() doesn't seem to work
          //
          int ux1 = Math.min(l.x, r.x); 
          int uy1 = Math.min(l.y, r.y);       
          int ux2 = Math.max(l.x + l.width, r.x + r.width);
          int uy2 = Math.max(l.y + l.height, r.y + r.height);
          Rectangle bounds = new Rectangle(ux1, uy1, ux2 - ux1, uy2 - uy1);       
          drawLine(graphics, leftGroupEditPart.outputConnection.getFigure(), rightGroupEditPart.inputConnection.getFigure(), mx, bounds);      
        }
      }                                              
    }  
    else if (editPart instanceof PartReferenceSectionEditPart)
    {
      AbstractGraphicalEditPart child = (editPart.getChildren().size() > 0) ? 
                                        (AbstractGraphicalEditPart)editPart.getChildren().get(0) :
                                        null;
      if (child != null)
      {
        DefinitionEditPart def = (DefinitionEditPart)editPart.getParent();
        GroupEditPart groupEditPart = def.getGroupEditPart(WSDLGroupObject.MESSAGES_GROUP);
        if (groupEditPart != null && groupEditPart.outputConnection != null)
        {
          int mx = child.getFigure().getBounds().x - 12;
          drawLine(graphics, groupEditPart.outputConnection.getFigure(), child.getFigure(), mx, null);
        }
      }
    }
    else
    {
      for (Iterator i = editPart.getChildren().iterator(); i.hasNext(); )
      {
        EditPart child = (EditPart)i.next();
        drawLines(graphics, child);
      }
    } 
  }    
}     

/*
    /*
    List children = figure.getChildren();
    for (Iterator i = children.iterator(); i.hasNext(); )
    {
      IFigure child = (IFigure)i.next();
      drawLines(graphics, child);    
    } */ 
      /*  
      List connectedFigures = graphNodeFigure.getConnectedFigures(IConnectedEditPartFigure.RIGHT_CONNECTION);
      int connectedFiguresSize = connectedFigures.size();              

      if (connectedFiguresSize > 0) 
      {                                         
        IConnectedEditPartFigure firstGraphNodeFigure = (IConnectedEditPartFigure)connectedFigures.get(0);
        Rectangle r = graphNodeFigure.getConnectionFigure().getBounds();    
          
        int x1 = r.x + r.width;
        int y1 = r.y + r.height/2;
                                                                                   
        int startOfChildBox = firstGraphNodeFigure.getConnectionFigure().getBounds().x;
        int x2 = x1 + (startOfChildBox - x1) / 3;
        int y2 = y1;
      
        if (connectedFiguresSize == 1)
        {
          graphics.drawLine(x1, y1, startOfChildBox, y2);   
        }
        else // (connectedFigures.length > 1)
        { 
          graphics.drawLine(x1, y1, x2, y2);

          int minY = Integer.MAX_VALUE;
          int maxY = -1;

          for (Iterator i = connectedFigures.iterator(); i.hasNext(); )
          {                                 
            IConnectedEditPartFigure connectedFigure = (IConnectedEditPartFigure)i.next();
            Rectangle childConnectionRectangle = connectedFigure.getConnectionFigure().getBounds();
            int y = childConnectionRectangle.y + childConnectionRectangle.height / 2;
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
            graphics.drawLine(x2, y, childConnectionRectangle.x, y);
          }                   
          graphics.drawLine(x2, minY, x2, maxY);
        }                          
      }                             
    }            

    //boolean visitChildren = true;
    List children = figure.getChildren();
    for (Iterator i = children.iterator(); i.hasNext(); )
    {
      IFigure child = (IFigure)i.next();
      drawLines(graphics, child);
    }
*/

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
package org.eclipse.wst.wsdl.ui.internal.graph.editparts;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.MoveHandleLocator;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.graph.WSDLComponentViewer;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.ContainerFigure;

public class DrillDownEditPart extends AbstractGraphicalEditPart
{
  protected Label drillDownButtonLabel; 
  protected Label label;            

  protected IFigure createFigure()
  {                                     
    ContainerFigure figure = new ContainerFigure();  
    drillDownButtonLabel = new Label();
    drillDownButtonLabel.setIcon(WSDLEditorPlugin.getInstance().getImage("icons/forward.gif"));
    figure.add(drillDownButtonLabel);
    label = new Label();
    label.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_UNKNOWN"));
    
//  evil hack to provide underlines
    boolean isLinux = java.io.File.separator.equals("/");
    if (!isLinux)
    {
      Font underlineFont = new Font(Display.getCurrent(), "Tahoma", 8, SWT.NONE); 
      FontData oldData = underlineFont.getFontData()[0];
      FontData fontData = new FontData(oldData.getName(), oldData.getHeight(), SWT.NONE);
      try
      {
        // TODO... clean this awful code up... we seem to be leaking here too
        // we can't call this directly since the methods are OS dependant
        // fontData.data.lfUnderline = 1
        // so instead we use reflection
        Object data = fontData.getClass().getField("data").get(fontData);
        System.out.println("data" + data.getClass());
        data.getClass().getField("lfUnderline").setByte(data, (byte)1);
        Font font = new Font(Display.getCurrent(), fontData);
        label.setFont(font);        
      }
      catch (Exception e)
      {    
      }
    }
    figure.add(label);
    
    return figure;
  }    


  protected void createEditPolicies() 
  {
  	DrillDownEditPartSelectionHandlesEditPolicy policy = new DrillDownEditPartSelectionHandlesEditPolicy();
    installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, policy);      
  }

  public void performRequest(Request request)
  {  
	  if (request.getType() == RequestConstants.REQ_DIRECT_EDIT ||
        request.getType() == RequestConstants.REQ_OPEN)
    {                                         
      if (request instanceof LocationRequest)
      {
        LocationRequest locationRequest = (LocationRequest)request;
        Point p = locationRequest.getLocation();
        
        if (hitTest(drillDownButtonLabel, p) || hitTest(label, p))
        {
  		    performDrillDownAction();
        }              
      }
    }
  }  

  protected EditPart createChild(Object model) 
  {
    return null;
  }  

  protected List getModelChildren() 
  {   
    return Collections.EMPTY_LIST;
  } 

  public boolean hitTest(IFigure target, Point location)
  {
    Rectangle b = target.getBounds().getCopy();
    target.translateToAbsolute(b);  
    return b.contains(location);
  }  

  protected void performDrillDownAction()
  {                                                                                    
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        ((WSDLComponentViewer)getViewer()).setInput(getModel());
      }
    };
    Display.getCurrent().asyncExec(runnable);
  }
  
  private class DrillDownEditPartSelectionHandlesEditPolicy extends SelectionHandlesEditPolicy {
  	 protected List createSelectionHandles()
  	  {              
  	    List list = new ArrayList();
  	    EditPart editPart = getHost();  
  	  
  	    if (editPart instanceof GraphicalEditPart)
  	    {
  	      GraphicalEditPart graphicalEditPart = (GraphicalEditPart)editPart;
  	      IFigure figure = graphicalEditPart.getFigure();

  	      MoveHandleLocator loc = new MoveHandleLocator(figure);    
  	      MoveHandle moveHandle = new MoveHandle(graphicalEditPart, loc);     
  	      list.add(moveHandle);
  	    }

  	    return list;
  	  }
  }
}
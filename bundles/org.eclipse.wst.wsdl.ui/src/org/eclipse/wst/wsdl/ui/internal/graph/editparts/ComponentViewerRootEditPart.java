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
                                    
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.gef.EditPart;
import org.eclipse.wst.xsd.ui.internal.gef.util.editparts.AbstractComponentViewerRootEditPart;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.ContainerLayout;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
              

public class ComponentViewerRootEditPart extends AbstractComponentViewerRootEditPart
{                       
  protected final static String MESSAGE_PLACE_HOLDER = "MESSAGE_PLACE_HOLDER";
  protected Object input; 

  public void setInput(Object newInput)
  {
    input = newInput;  
    setModel(input);
    refreshChildren();
  }

  protected IFigure createFigure()
  {
    Panel panel = new Panel();
    panel.setBorder(new MarginBorder(30, 30, 30, 30));
    //panel.setBackgroundColor(GraphicsConstants.red);
    
    ContainerLayout layout = new ContainerLayout();
    layout.setHorizontal(false);
    panel.setLayoutManager(layout);
    return panel;
  } 
   

  protected EditPart createChild(Object model)
  {
    EditPart editPart = null;
    if (model == MESSAGE_PLACE_HOLDER)
    {
      editPart = new NotAvailableMessageEditPart();
      editPart.setModel(model);
    } 
    else
    {
      editPart = ExtensibleEditPartFactory.getInstance().createEditPart(this, model);
    }
    return editPart;
  }  
  

  protected List getModelChildren() 
  {   
    List result = new ArrayList();

    if (input != null)
    {              
      result.add(input);
    }     
    else
    {
      result.add(MESSAGE_PLACE_HOLDER);
    }       

    return result;
  }    
                                                           

  public void propertyChanged(Object object, String property)
  {                                                                                                  
    if (property == ModelAdapter.CHILDREN_PROPERTY)
    {
      refreshChildren();
    }
    else if (property == ModelAdapter.DETAIL_PROPERTY)
    { 
      refreshVisuals(); 
    }                                       
    else
    {
      refreshChildren();
      refreshVisuals();
    }
  }                                          
}
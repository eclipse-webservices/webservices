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
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.ContainerFigure;
import org.eclipse.wst.wsdl.ui.internal.graph.model.WSDLGraphModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterListener;
import org.eclipse.xsd.XSDElementDeclaration;
                                                   

public class PartReferenceSectionEditPart extends AbstractGraphicalEditPart implements ModelAdapterListener
{ 
  protected Object input;
  protected Definition definition;
              
  protected IFigure createFigure()
  {                                                  
    ContainerFigure outer = new ContainerFigure(); 
    return outer;
  } 

  protected void setInput(Object object)
  {                    
    if (input != null)
    {
      removeModelAdapterListener(input, this);
	  removeModelAdapterListener(definition, this);
    }
    input = object;
    if (input != null)
    {
      addModelAdapterListener(input, this);
	  definition = getParent() != null ? (Definition)getParent().getModel() : null;
      addModelAdapterListener(definition, this);
    }
    refresh();
    getFigure().repaint();
  }
  
	public void deactivate() 
	  {       
		  if (input != null)
		  { 	
	        removeModelAdapterListener(input, this);
	        removeModelAdapterListener(definition, this);
		  }  
			super.deactivate(); 
		}  

  protected List getModelChildren() 
  { 
    List list = new ArrayList();
    if (input instanceof Part)
    {
      Part part = (Part)input;
      Object component = part.getTypeDefinition();
      if (component != null) 
      {
        // TODO... i'm pulling a fast one here.... we should call the extension
        // to see if he wants to show a child here
        if (component instanceof org.eclipse.xsd.XSDComplexTypeDefinition)
        {
          list.add(component);
        }
      }
      else
      {
        // This is before the new org.eclipse.wst.wsdl model
        // where it handled DOM nodes
        // component = part.getElement();
        XSDElementDeclaration ed = part.getElementDeclaration();
        if (ed != null && ed.getContainer() != null)
        {
          list.add(ed);
        }
      }
    }                    
    return list;
  }
       
  protected void createEditPolicies() 
  {
  }       

  protected EditPart createChild(Object model) 
  {
    return getEditPartFactory().createEditPart(this, model);
  } 

  protected EditPartFactory getEditPartFactory()
  {
    return ExtensibleEditPartFactory.getInstance();
  } 

  public void propertyChanged(Object object, String property)
  {                     
    refreshChildren();
  }  

  protected ModelAdapter getModelAdapter(Object modelObject)
  {
    return WSDLGraphModelAdapterFactory.getWSDLGraphModelAdapterFactory().getAdapter(modelObject);
  } 

  protected void addModelAdapterListener(Object modelObject, ModelAdapterListener listener)
  {
    ModelAdapter adapter = getModelAdapter(modelObject);
    if (adapter != null)
    {
      adapter.addListener(listener);
    }
  }

  protected void removeModelAdapterListener(Object modelObject, ModelAdapterListener listener)
  {
    ModelAdapter adapter = getModelAdapter(modelObject);
    if (adapter != null)
    {
      adapter.removeListener(listener);
    }
  }  

}   
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
                                     
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.gef.util.editparts.MultiContentPaneEditPart;
import org.eclipse.wst.wsdl.ui.internal.graph.GraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.graph.model.WSDLGraphModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterListener;


public abstract class WSDLEditPart extends MultiContentPaneEditPart implements ModelAdapterListener, GraphicsConstants
{
	/**
	 * Activates the <code>EditPart</code> by setting the
	 * appropriate flags, and activating its children.
	 * activation signals to the EditPart that is should start observing
	 * it's model, and that is should support editing at this time.
	 * An EditPart will have a parent prior to activiation.
	 * @see #deactivate()
	 */
	public void activate() 
  {
		super.activate();         
    addModelAdapterListener(getModel(), this);   
	}
	/** 
	 * Apart from the deactivation done in super, the source
	 * and target connections are deactivated, and the visual
	 * part of the this is removed.
	 *
	 * @see #activate() 
	 */
	public void deactivate() 
  {                    
    removeModelAdapterListener(getModel(), this); 
		super.deactivate(); 
	}   
    
  protected void createEditPolicies() 
  {
    SelectionEditPolicy policy = new SelectionEditPolicy()
    {        
      protected  void hideSelection() 
      {
        EditPart editPart = getHost();
        if (editPart instanceof IFeedbackHandler)
        {
          ((IFeedbackHandler)editPart).removeFeedback();
        }
      }

      protected  void showSelection()  
      { 
        EditPart editPart = getHost();
        if (editPart instanceof IFeedbackHandler)
        {
          ((IFeedbackHandler)editPart).addFeedback();
        }
      }             
    };
    installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, policy);           
  }  

  protected EditPart createChild(Object model) 
  {
    return getEditPartFactory().createEditPart(this, model);
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

  protected List getModelChildren() 
  {   
    List result = null;
    ModelAdapter modelAdapter = getModelAdapter(getModel());
    if (modelAdapter != null)
    {
      result = (List)modelAdapter.getProperty(getModel(), ModelAdapter.CHILDREN_PROPERTY);
    }
    return result != null ? result : Collections.EMPTY_LIST;
  } 

  protected EditPartFactory getEditPartFactory()
  {
    return ExtensibleEditPartFactory.getInstance();
  } 

  protected ModelAdapter getModelAdapter(Object modelObject)
  {
    // TODO : port check
    return WSDLGraphModelAdapterFactory.getWSDLGraphModelAdapterFactory().getAdapter(modelObject);
//    return WSDLGraphModelAdapterFactory.EcoreUtil.getAdapter(getWSDLGraphModelAdapterFactory().eAdapters(),modelObject);
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

  public boolean hitTest(IFigure target, Point location)
  {
    Rectangle b = target.getBounds().getCopy();
    target.translateToAbsolute(b);  
    return b.contains(location);
  }
}
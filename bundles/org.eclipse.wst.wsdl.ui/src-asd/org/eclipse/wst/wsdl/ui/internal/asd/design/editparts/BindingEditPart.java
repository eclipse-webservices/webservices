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
package org.eclipse.wst.wsdl.ui.internal.asd.design.editparts;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.connections.CenteredConnectionAnchor;
import org.eclipse.wst.wsdl.ui.internal.asd.design.connections.TopLeftConnectionAnchor;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.BoxComponentFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.ComponentReferenceConnection;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.BindingColumnLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.BindingContentLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.BindingLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;

public class BindingEditPart extends BaseEditPart
{
  protected ComponentReferenceConnection connectionFigure;
  protected BoxComponentFigure figure;
  protected boolean isExpanded = false;
  private Label hoverHelpLabel = new Label("");

  protected IFigure createFigure()
  {
    figure = new BoxComponentFigure();
    figure.getLabel().setBorder(new MarginBorder(2, 2, 1, 2));
    figure.setBorder(new LineBorder(0));
    figure.getContentPane().setLayoutManager(new BindingContentLayout(this));
    figure.getContentPane().setOpaque(true);
    figure.getContentPane().setBackgroundColor(ColorConstants.yellow);
    figure.setLayoutManager(new BindingLayout(this));
    // if(isScrollable())
    // figure.setScrollingActionListener(this);
    return figure;
  }

  public IFigure getContentPane()
  {
    return ((BoxComponentFigure) getFigure()).getContentPane();
  }

  protected List getModelChildren()
  {
    IBinding binding = (IBinding) getModel();  
    return isExpanded ? binding.getBindingContentList() : Collections.EMPTY_LIST;
  }

  public void activate()
  {
    super.activate();
    activateConnection();
  }

  public void deactivate()
  {
    super.deactivate();
    deactivateConnection();
  }

  public void addFeedback()
  {
    super.addFeedback();
    LineBorder boxFigureLineBorder = (LineBorder) figure.getBorder();
    boxFigureLineBorder.setWidth(2);
//    boxFigureLineBorder.setColor(ColorConstants.darkBlue);
    figure.setSelected(true);
    figure.repaint();
    if (connectionFigure != null)
    {
      connectionFigure.setHighlight(true);
      getLayer(LayerConstants.FEEDBACK_LAYER).add(connectionFigure);
    }
  }

  public void removeFeedback()
  {
    super.removeFeedback();
    LineBorder boxFigureLineBorder = (LineBorder) figure.getBorder();
    boxFigureLineBorder.setWidth(1);
    boxFigureLineBorder.setColor(ColorConstants.black);
    figure.setSelected(false);
    figure.repaint();
    if (connectionFigure != null)
    {
      connectionFigure.setHighlight(false);
      getLayer(LayerConstants.CONNECTION_LAYER).add(connectionFigure);
    }
  }


  protected void activateConnection()
  {
    // If appropriate, create our connectionFigure and add it to the appropriate
    // layer
    if (createConnectionFigure() != null)
    {
      // Add our editpolicy as a listener on the connection, so it can stay in
      // synch
      // connectionFigure.addPropertyChangeListener((AttributeSelectionFeedbackPolicy)
      // getEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE));
      // connectionFigure.addMouseListener(this);
      getLayer(LayerConstants.CONNECTION_LAYER).add(connectionFigure);
    }
  }

  protected void deactivateConnection()
  {
    if (connectionFigure != null)
    {
      boolean removed = false;
      removed = removeConnectionFigure(getLayer(LayerConstants.CONNECTION_LAYER));
      if (!removed)
      {
        removeConnectionFigure(getLayer(LayerConstants.FEEDBACK_LAYER));
      }
    }
  }
  

  private boolean removeConnectionFigure(IFigure parent)
  {
    boolean contains = false;
    Iterator it = parent.getChildren().iterator();
    while (it.hasNext())
    {
      IFigure fig = (IFigure) it.next();
      if (fig.equals(connectionFigure))
      {
        contains = true;
        break;
      }
    }
    if (contains)
    {
      parent.remove(connectionFigure);
    }
    return contains;
  }

  public ComponentReferenceConnection createConnectionFigure()
  {
    if (connectionFigure == null && shouldDrawConnection())
    {
      IBinding binding = (IBinding) getModel();
      Object typeBeingRef = binding.getInterface();
      if (typeBeingRef != null)
      {
        AbstractGraphicalEditPart referenceTypePart = (AbstractGraphicalEditPart) getViewer().getEditPartRegistry().get(typeBeingRef);
        if (referenceTypePart != null)
        {
          connectionFigure = new ComponentReferenceConnection();
          refreshConnections();
        }
      }
    }
    return connectionFigure;
  }

  protected boolean shouldDrawConnection()
  {
    if (isExpanded)
    {  
      return false;
    }  
    IBinding binding = (IBinding) getModel();
    Object typeBeingRef = binding.getInterface();
    if (typeBeingRef != null)
    {
      AbstractGraphicalEditPart referenceTypePart = (AbstractGraphicalEditPart) getViewer().getEditPartRegistry().get(typeBeingRef);
      if (referenceTypePart != null)
      {
        return true;
      }
    }
    return false;
  }

  protected void refreshVisuals()
  {
    refreshConnections();
    if (getModel() instanceof IBinding)
    {  
      IBinding binding = (IBinding) getModel();
      ((BoxComponentFigure) getFigure()).headingFigure.setIsReadOnly(((IBinding) getModel()).isReadOnly());
      figure.getLabel().setIcon(binding.getImage());
      
      // Show the name of the IBinding with hover help
      hoverHelpLabel.setText(" " + binding.getName() + " ");
      figure.setToolTip(hoverHelpLabel);
    }

    //getViewer().getEditPartRegistry().put(Lay.class, controller);
    super.refreshVisuals();    
  }
  
  protected AbstractGraphicalEditPart getConnectionTargetEditPart()
  {
    IBinding binding = (IBinding) getModel();
    Object typeBeingRef = binding.getInterface();
    if (connectionFigure != null)
    {
      AbstractGraphicalEditPart referenceTypePart = (AbstractGraphicalEditPart) getViewer().getEditPartRegistry().get(typeBeingRef);
      return referenceTypePart;
    }
    return null;
  }

  protected void refreshConnections()
  {
    if (shouldDrawConnection())
    {
      IBinding binding = (IBinding) getModel();
      Object typeBeingRef = binding.getInterface();
      if (connectionFigure != null)
      {
        AbstractGraphicalEditPart referenceTypePart = (AbstractGraphicalEditPart) getViewer().getEditPartRegistry().get(typeBeingRef);
        BoxComponentFigure boxComponentFigure = (BoxComponentFigure) referenceTypePart.getFigure();
        connectionFigure.setSourceAnchor(new CenteredConnectionAnchor(getFigure(), CenteredConnectionAnchor.RIGHT, 0));
        connectionFigure.setTargetAnchor(new TopLeftConnectionAnchor(boxComponentFigure, boxComponentFigure.getLabel()));
        connectionFigure.setHighlight(false);
        connectionFigure.setVisible(true);
      }
      else
      {
        activateConnection();
      }
    }
    else if (connectionFigure != null)
    {
      connectionFigure.setVisible(false);
    }
  }

  protected void createEditPolicies()
  {
     installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ASDSelectionEditPolicy());
  }

  public boolean isExpanded()
  {
    return isExpanded;
  }

  public void setExpanded(boolean isExpanded)
  {
    this.isExpanded = isExpanded;
  }
  
  public void performRequest(Request request)
  {  
	  if (request.getType() == RequestConstants.REQ_OPEN)
    {                                         
		  isExpanded = !isExpanded;
	      LayoutManager layoutManager = getFigure().getParent().getLayoutManager();
	      AbstractGraphicalEditPart snapToEditPart = getConnectionTargetEditPart();
	      if (layoutManager instanceof BindingColumnLayout && snapToEditPart != null)
	      {           
	        BindingColumnLayout animatedBindingLayout = (BindingColumnLayout)layoutManager;
	        if (!isExpanded)
	        {
	          animatedBindingLayout.setExpandedBindingFigure(null);
	        }
	        else
	        {  
	          animatedBindingLayout.setExpandedBindingFigure(getFigure());
	        }          
	        animatedBindingLayout.setSnapToFigure(snapToEditPart.getFigure());
	      }  
	      //Animation.markBegin();                
	      //Animation.run(700);        
	      refresh();
	      getFigure().invalidate();
	      getFigure().getParent().invalidateTree();           
	      getFigure().getParent().revalidate();
    }
  } 
}

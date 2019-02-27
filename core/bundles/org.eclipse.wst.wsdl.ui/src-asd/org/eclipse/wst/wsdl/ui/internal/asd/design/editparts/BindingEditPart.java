/*******************************************************************************
 * Copyright (c) 2001, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.editparts;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.connections.CenteredConnectionAnchor;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.BoxComponentFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.BindingColumnLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.BindingContentLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.BindingLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public class BindingEditPart extends BaseConnectedEditPart
{
  protected BoxComponentFigure figure;
  protected boolean isExpanded = false;
  private Label hoverHelpLabel = new Label(""); //$NON-NLS-1$

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
    return figure.getContentPane();
  }

  protected List getModelChildren()
  {
    IBinding binding = (IBinding) getModel();  
    return isExpanded ? binding.getBindingContentList() : Collections.EMPTY_LIST;
  }

  public void addFeedback()
  {
    LineBorder boxFigureLineBorder = (LineBorder) figure.getBorder();
    boxFigureLineBorder.setWidth(2);
//    boxFigureLineBorder.setColor(ColorConstants.darkBlue);
    figure.setSelected(true);
    figure.repaint();
    
    super.addFeedback();
  }

  public void removeFeedback()
  {
    LineBorder boxFigureLineBorder = (LineBorder) figure.getBorder();
    boxFigureLineBorder.setWidth(1);
    boxFigureLineBorder.setColor(DesignViewGraphicsConstants.defaultForegroundColor);
    figure.setSelected(false);
    figure.repaint();
    
    super.removeFeedback();
  }

  protected boolean shouldDrawConnection()
  {
    if (isExpanded)
    {  
      return false;
    }
    return super.shouldDrawConnection();
  }
  
  protected void refreshVisuals()
  {
    refreshConnections();
    if (getModel() instanceof IBinding)
    {  
      IBinding binding = (IBinding) getModel();
      ((BoxComponentFigure) getFigure()).headingFigure.setIsReadOnly(((IBinding) getModel()).isReadOnly());
      
      if (binding instanceof ITreeElement) {
    	  figure.getLabel().setIcon(((ITreeElement) binding).getImage());
      }
      
      // Show the name of the IBinding with hover help
      String prependString = Messages._UI_LABEL_BINDING;
      hoverHelpLabel.setText(" " + prependString + " : " + binding.getName() + " "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      figure.setToolTip(hoverHelpLabel);
    }

    //getViewer().getEditPartRegistry().put(Lay.class, controller);
    super.refreshVisuals();    
  }
  
  protected AbstractGraphicalEditPart getConnectionTargetEditPart()
  {
    IBinding binding = (IBinding) getModel();
    Object typeBeingRef = binding.getInterface();
    if (typeBeingRef != null)
    {
      AbstractGraphicalEditPart referenceTypePart = (AbstractGraphicalEditPart) getViewer().getEditPartRegistry().get(typeBeingRef);
      return referenceTypePart;
    }
    return null;
  }
  
  public void refreshConnections()
  {
    if (shouldDrawConnection())
    {
      if (connectionFigure != null)
      {
        AbstractGraphicalEditPart referenceTypePart = getConnectionTargetEditPart();
        IFigure refFigure= referenceTypePart.getFigure();
        connectionFigure.setSourceAnchor(new CenteredConnectionAnchor(getFigure(), CenteredConnectionAnchor.RIGHT, 0));
        connectionFigure.setTargetAnchor(new CenteredConnectionAnchor(refFigure, CenteredConnectionAnchor.HEADER_LEFT, 0, 11));
        connectionFigure.setHighlight(false);
        connectionFigure.setVisible(true);

        if (connectionFeedbackFigure != null)
          addConnectionFeedbackFigure();
      }
      else
      {
        activateConnection();
      }
    }
    else if (connectionFigure != null)
    {
      connectionFigure.setVisible(false);
      removeConnectionFeedbackFigure();
    }
  }

  protected void createEditPolicies()
  {
     super.createEditPolicies();    
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
      LayoutManager layoutManager = getFigure().getParent().getLayoutManager();
      if (layoutManager instanceof BindingColumnLayout)
      {             
        BindingColumnLayout bindingColumnLayout = (BindingColumnLayout)layoutManager;
        bindingColumnLayout.setExpanded(this, !isExpanded);
        ((BindingColumnEditPart)getParent()).refreshBindingEditParts();
      }  
    }
  } 
  
  public EditPart getRelativeEditPart(int direction)
  {
    if (direction == PositionConstants.EAST)
    {
      // navigate forward along the connection (to the right)
      if (connectionFigure != null)
      {
        return getConnectionTargetEditPart();
      }
      else
      {
    	  // or to the first unconnected interface
    	  return EditPartNavigationHandlerUtil.getFirstInterface(this);
      }
    }  
    else if (direction == PositionConstants.WEST)
    {
      // navigate backward along the connection (to the left) or to the first unconnected service
    	EditPart connectedService = EditPartNavigationHandlerUtil.getSourceConnectionEditPart(this);
    	if (connectedService != null) {
    		return connectedService;
    	} else {
    		return EditPartNavigationHandlerUtil.getFirstService(this);
    	}
    }
    else if (direction == PositionConstants.NORTH) {
    	return EditPartNavigationHandlerUtil.getPrevSibling(this);
    } else if (direction == PositionConstants.SOUTH && !isExpanded()) {
    	EditPart nextSibling = EditPartNavigationHandlerUtil.getNextSibling(this);
    	if (nextSibling != null) {
    		return nextSibling;
    	} else {
    		return this; 
    	}
    }
    return super.getRelativeEditPart(direction);
  }

  protected String getAccessibleName() {
      IBinding binding = (IBinding)getModel();
      String bindingName = binding != null && binding.getName() != null ? binding.getName() : ""; //$NON-NLS-1$ 
	  return bindingName;
  }
}
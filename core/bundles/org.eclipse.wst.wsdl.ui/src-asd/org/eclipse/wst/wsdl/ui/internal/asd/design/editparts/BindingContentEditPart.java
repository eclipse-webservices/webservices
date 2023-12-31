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
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.BindingContentPlaceHolder;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBindingMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBindingOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.INamedObject;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

// This class is used to represent a BindingOperation, BindingInput, BindingOutput and BindingFault
//
public class BindingContentEditPart extends BaseEditPart
{  
  Label label;
  protected ASDSelectionEditPolicy selectionHandlesEditPolicy = new ASDSelectionEditPolicy();
  protected IFigure createFigure()
  {
    Figure figure = new Figure()
    {
      public void paint(Graphics graphics) {
        super.paint(graphics);
        Rectangle r = getBounds();
        // bug146932
        paintFocusCursor(new Rectangle(r.x, r.y, r.width, r.height), graphics);
      }
    };
    figure.setOpaque(true);
    figure.setBackgroundColor(ColorConstants.tooltipBackground);
    ToolbarLayout toolbarLayout = new ToolbarLayout(true);
    toolbarLayout.setStretchMinorAxis(true);
    figure.setLayoutManager(toolbarLayout);
    label = new Label();
    label.setBorder(new MarginBorder(5, 5, 2, 2));
    figure.add(label);
    
    return figure;
  }
  
  
  protected List getModelChildren()
  {
    return Collections.EMPTY_LIST;
  }  
  
  protected void refreshVisuals()
  {
	  if (getModel() instanceof IBindingOperation)
	  {  
		  IBindingOperation bindingOperation = (IBindingOperation) getModel();

		  if (bindingOperation instanceof ITreeElement) {
			  label.setIcon(((ITreeElement) bindingOperation).getImage());
		  }
//		  label.setText(bindingOperation.getName());
	  }
	  else if (getModel() instanceof IBindingMessageReference)
	  {
		  label.setIcon(WSDLEditorPlugin.getInstance().getImage("icons/bind_asct_val_obj.gif")); //$NON-NLS-1$
//		  label.setIcon(messageRef.getImage());
//		  label.setText(messageRef.getName());
	  }
	  else if (getModel() instanceof BindingContentPlaceHolder) {
		  label.setIcon(WSDLEditorPlugin.getInstance().getImage("icons/bind_asct_val_not_obj.gif")); //$NON-NLS-1$
	  }

	  super.refreshVisuals();    
  }  
  
  protected void createEditPolicies() {
      super.createEditPolicies();
	  installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionHandlesEditPolicy);
  }
  
  public void addFeedback() {
	  label.getParent().setBackgroundColor(DesignViewGraphicsConstants.tableCellSelectionColor);
  }

  public void removeFeedback() {
	  label.getParent().setBackgroundColor(ColorConstants.tooltipBackground);
  }


  protected String getAccessibleName()
  {
    String accessibleName = ""; //$NON-NLS-1$
    if (getModel() instanceof INamedObject)
    {  
        INamedObject namedObject = (INamedObject) getModel();
        accessibleName = namedObject.getName();
    }
    return accessibleName;
  }


  public EditPart getRelativeEditPart(int direction) {
	  EditPart editPart = super.getRelativeEditPart(direction);
	  if (direction == PositionConstants.SOUTH && (editPart == null || editPart == this))
	  {
		  editPart = EditPartNavigationHandlerUtil.getNextSibling(getParent());           
	  }       
	  return editPart;
  }
}
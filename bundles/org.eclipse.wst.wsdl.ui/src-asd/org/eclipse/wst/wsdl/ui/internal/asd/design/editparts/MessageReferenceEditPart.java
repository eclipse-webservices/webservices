/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDDragAndDropEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDGraphNodeDragTracker;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.ListFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.ModelDiagnosticInfo;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.RowLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public class MessageReferenceEditPart extends BaseEditPart implements IFeedbackHandler
{   
  protected Figure contentPane;  
  protected Label label;
  protected RowLayout rowLayout;
  
  private Figure messageLabelWrapper;
  private Label messageLabel;
  
  protected ASDSelectionEditPolicy selectionHandlesEditPolicy = new ASDSelectionEditPolicy();
  
  protected IFigure createFigure()
  {
    ListFigure figure = new ListFigure(true);
    figure.setOpaque(true);
    figure.setPaintFirstLine(false);
    figure.setBackgroundColor(ColorConstants.tooltipBackground);

    rowLayout = new RowLayout();
    figure.setLayoutManager(rowLayout); 
    label = new Label();
    label.setLabelAlignment(Label.LEFT);
    //label.setFont(DesignViewGraphicsConstants.mediumFont); 
    label.setBorder(new MarginBorder(2, 16, 2 ,10));
    figure.add(label);  
    
    contentPane = new ListFigure();
    ((ListFigure)contentPane).setOpaque(true);
    contentPane.setBackgroundColor(ColorConstants.listBackground);
    ToolbarLayout toolbarLayout = new ToolbarLayout(false);
    
    /*
    ToolbarLayout toolbarLayout = new ToolbarLayout(false)
    {
       // TODO (cs) consider minor tweak here to ensure that the row fills up the available space             
       // vertically... without this a bit of trim is visible at the bottom of the list
       // when the param labels are given a margin width top and bottom <  '4' 
       public void layout(IFigure parent)
       {
         super.layout(parent);
         if (children.size() == 1)
         {
            Figure child = (Figure)children.get(0);
            //child.getBounds().height = parent.getClientArea().height;
         }  
       }
    };     
     */
    toolbarLayout.setStretchMinorAxis(true);
    contentPane.setLayoutManager(toolbarLayout);
    figure.add(contentPane);
    
    rowLayout.setConstraint(label, "MessageLabel"); //$NON-NLS-1$
    rowLayout.setConstraint(contentPane, "MessageContentPane"); //$NON-NLS-1$
   
    // rmah: The block of code below has been moved from refreshVisuals().  We're
    // assuming the read-only state of the EditPart will never change once the
    // EditPart has been created.
    if (isReadOnly()) 
    {
      label.setForegroundColor(DesignViewGraphicsConstants.readOnlyLabelColor);
      label.getParent().setBackgroundColor(DesignViewGraphicsConstants.readOnlyMessageRefHeadingColor);
    }
    else
    {
      label.setForegroundColor(ColorConstants.black);
      label.getParent().setBackgroundColor(ColorConstants.tooltipBackground);
    }
    
    return figure;
  }
  
  protected void createEditPolicies()
  {
      super.createEditPolicies();  
	  if (!isReadOnly()) {
		  installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new ASDDragAndDropEditPolicy(getViewer(), selectionHandlesEditPolicy));
	  }
	  installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionHandlesEditPolicy);
  }
  
  public DragTracker getDragTracker(Request request)
  {
    return new ASDGraphNodeDragTracker((EditPart)this);
  }
  
  public IFigure getContentPane()
  {
    return contentPane;
  }
  
  protected void refreshVisuals()
  {   
    super.refreshVisuals();
    IMessageReference message = (IMessageReference)getModel();
    
    if (message instanceof ITreeElement) {
    	label.setText(((ITreeElement) message).getText());
    	label.setIcon(((ITreeElement) message).getImage());
    }
    
    // Resize column widths.  Sizes may have shrunk.
    rowLayout.getColumnData().clearColumnWidths();
    for (EditPart parent = getParent(); parent != null; parent = parent.getParent())
    {
      if (parent instanceof InterfaceEditPart)
      { 
        ((GraphicalEditPart)parent).getFigure().invalidateTree();
        break;
      }
    }
  }
  
  protected void refreshChildren() {
	  super.refreshChildren();
      refreshMessage();
  }
  
  protected void refreshMessage() {
	  if (getModelChildren().size() > 0) {
		  if (messageLabelWrapper != null) {
			  contentPane.remove(messageLabelWrapper);
			  messageLabelWrapper = null;
		  }
	  }
	  else {
		  if (messageLabelWrapper == null) {
			  messageLabelWrapper = new Figure();
			  messageLabel = new Label();
			  
			  ToolbarLayout toolbarLayout = new ToolbarLayout(false)
			    {
				  // We want to center the text
			       public void layout(IFigure parent)
			       {
			         super.layout(parent);

			         if (parent.getChildren().size() == 1 && parent.getChildren().get(0) instanceof Label)
			         {
			            parent.setSize(parent.getSize().width, parent.getSize().height + 3);
			            Label child = (Label) parent.getChildren().get(0);
			            int newXAmount = (parent.getSize().width - child.getSize().width) / 2;
			            Point p = child.getLocation();
			            child.setLocation(new Point(p.x + newXAmount, p.y + 3));
			         }  
			       }
			    };     

			  messageLabelWrapper.setLayoutManager(toolbarLayout);
			  messageLabelWrapper.add(messageLabel);
			  contentPane.add(messageLabelWrapper);
		  }
		  
		  List errorList = getErrors();
		  List warnList = getWarnings();

		  if (errorList.size() > 0) {
			  ModelDiagnosticInfo info = (ModelDiagnosticInfo) errorList.get(0);
			  messageLabel.setText("  " + info.getDescriptionText() + "  ");  //$NON-NLS-1$ //$NON-NLS-2$
			  messageLabel.setForegroundColor(info.getDescriptionTextColor());
		  }
		  else if (warnList.size() > 0){
			  ModelDiagnosticInfo info = (ModelDiagnosticInfo) warnList.get(0);
			  messageLabel.setText("  " + info.getDescriptionText() + "  ");  //$NON-NLS-1$ //$NON-NLS-2$
			  messageLabel.setForegroundColor(info.getDescriptionTextColor());
		  }
		  else {
			  messageLabel.setText(""); //$NON-NLS-1$
			  messageLabel.setForegroundColor(ColorConstants.black);
		  }
	  }
  }
  
  protected List getErrors() {
	  IMessageReference messageRef = (IMessageReference) getModel();
	  return getDiagnosticMessageType(messageRef.getDiagnosticMessages(), ModelDiagnosticInfo.ERROR_TYPE);
  }
  
  protected List getWarnings() {
	  IMessageReference messageRef = (IMessageReference) getModel();
	  return getDiagnosticMessageType(messageRef.getDiagnosticMessages(), ModelDiagnosticInfo.WARNING_TYPE);
  }
  
  protected List getDiagnosticMessageType(List diagnosticInfo, int type) {
	  List listType = new ArrayList();
	  for (int index = 0; index < diagnosticInfo.size(); index++) {
		  ModelDiagnosticInfo info = (ModelDiagnosticInfo) diagnosticInfo.get(index);
		  if (info.getType() == type) {
			  listType.add(info);
		  }
	  }
	  
	  return listType;
  }

  protected List getModelChildren()
  {
    IMessageReference theMessage = (IMessageReference)getModel();
    return theMessage.getParameters();   
  }

  public void addNotify()
  {
    InterfaceEditPart.attachToInterfaceEditPart(this, rowLayout);
    super.addNotify();    
  }
  
  public void addFeedback() {
	  label.getParent().setBackgroundColor(DesignViewGraphicsConstants.tableCellSelectionColor);
  }

  public void removeFeedback() {
	  if (isReadOnly()) {
	    label.getParent().setBackgroundColor(DesignViewGraphicsConstants.readOnlyMessageRefHeadingColor);
	  }
	  else {
		  label.getParent().setBackgroundColor(ColorConstants.tooltipBackground);
	  }
  }
  
  public EditPart getRelativeEditPart(int direction)
  {  
    EditPart editPart = super.getRelativeEditPart(direction);
    if (direction == PositionConstants.SOUTH && editPart == null)
    {
      editPart = EditPartNavigationHandlerUtil.getNextInterface(this);
    }        
    return editPart;
  }
}

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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.graph.editpolicies.WSDLDragAndDropEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.graph.editpolicies.WSDLGraphNodeDragTracker;
import org.eclipse.wst.wsdl.ui.internal.graph.editpolicies.WSDLSelectionHandlesEditPolicyImpl;
import org.eclipse.wst.wsdl.ui.internal.graph.figures.TreeNodeContentFigure;
import org.eclipse.wst.wsdl.ui.internal.graph.model.WSDLGraphModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;

public class WSDLTreeNodeEditPart extends TreeNodeEditPart
{ 
  protected Label label2;                                     
  protected boolean isEmphasized = false;  

  protected static final Color emphasisColor = new Color(null, 238, 238, 238);
  protected static final Color label2ForegroundColor = new Color(null, 82, 82, 158);        
  protected static final Color label2ForegroundColorSelected = new Color(null, 182, 182, 255);

  protected ModelAdapter getModelAdapter(Object modelObject)
  {
    return WSDLGraphModelAdapterFactory.getWSDLGraphModelAdapterFactory().getAdapter(modelObject);
  }                                       

  protected void createContentFigure()
  {
	contentFigure = new TreeNodeContentFigure()
	{
	  public IFigure getConnectionFigure()
	  {  	
  	    return isTopLevel() ? outlinedArea : iconArea;
	  }   	  
	};      
  }
  
  protected boolean isTopLevel()
  {
    return getParent() instanceof GroupEditPart;
  }
	
  protected void createFigureContent()
  {                  
    super.createFigureContent();
      
    if (getModel() instanceof Part)
    {
      label2 = new Label();
      labelHolder.add(label2);
      label2.setBorder(new MarginBorder(0, 5, 0, 0));
      label2.setForegroundColor(label2ForegroundColor);
    }
  }   

  public void setEmphasized(boolean isEmphasized)
  {
    this.isEmphasized = isEmphasized;
    refreshVisuals();
  }
  
  protected void updateEmphasis()
  {
    boolean isEmphasisShowing = isEmphasized && (!isSelected || getModelChildren().size() > 0);
    if (isEmphasisShowing)
    {              
      contentFigure.getOutlinedArea().setBackgroundColor(emphasisColor); 
    }                                                                                    

    contentFigure.getOutlinedArea().setFill(isEmphasisShowing);
    contentFigure.getOutlinedArea().setOutlined(isEmphasisShowing);    
    contentFigure.setForegroundColor(elementBorderColor);
  }

  public void refreshVisuals()
  {   
    updateEmphasis();

    super.refreshVisuals();
   
    if (label2 != null)
    {
      Part part = (Part)getModel();
	  //String label = ComponentReferenceUtil.isType(part) ? "type" : "element";
	  String value = ComponentReferenceUtil.getPartReferenceValue(part);  
      label2.setText("(" + value + ")");
    }
  }         

  public void performRequest(Request request)
  {  
    if (request.getType() == RequestConstants.REQ_OPEN)
    {                                        
      for (EditPart parent = (EditPart)getParent(); parent != null; parent = parent.getParent())
      {
        if (parent instanceof DefinitionEditPart)
        {
          ((DefinitionEditPart)parent).handleOpenRequest(this);
          break;
        }
      }
    }    
    //else if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
    //{                            
    //  performDirectEdit();
    //}
  }     

  public void addFeedback()
  {    
    super.addFeedback();
    if (label2 != null)        
    {
      label2.setForegroundColor(label2ForegroundColorSelected);
    }    
    updateEmphasis();
  }   

  public void removeFeedback()
  { 
    super.removeFeedback();
    if (label2 != null)        
    {
      label2.setForegroundColor(label2ForegroundColor);
    }
    updateEmphasis();
  }                     
  
  protected void createEditPolicies()
  { 
    super.createEditPolicies();
    selectionHandlesEditPolicy = new WSDLSelectionHandlesEditPolicyImpl();
    installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new WSDLDragAndDropEditPolicy(getViewer(), selectionHandlesEditPolicy));
    installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionHandlesEditPolicy);
  }  
  
  WSDLSelectionHandlesEditPolicyImpl selectionHandlesEditPolicy;

  public IFigure getSelectionFigure()
  {
    return getFigure();
  }

  public DragTracker getDragTracker(Request request)
  {
    return new WSDLGraphNodeDragTracker((EditPart)this);
  }
}
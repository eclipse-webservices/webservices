/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design.editparts;

import java.util.Iterator;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDMultiPageEditor;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.connections.CenteredConnectionAnchor;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.LabelCellEditorLocator;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.LabelEditManager;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDLabelDirectEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.ComponentReferenceConnection;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IEndPoint;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public class EndPointEditPart extends BaseEditPart implements IFeedbackHandler, INamedEditPart
{
  protected Label nameLabel;
  protected Label addressLabel;
  private Label hoverHelpLabel = new Label(""); //$NON-NLS-1$
  protected Figure addressBoxFigure;
  protected ComponentReferenceConnection connectionFigure;
  private ComponentReferenceConnection connectionFeedbackFigure;
  protected final static int MAX_ADDRESS_WIDTH = 150;

  protected IFigure createFigure()
  {
    figure = new Panel() {
      public void paint(Graphics graphics) {
        super.paint(graphics);
        Rectangle r = getBounds();
        // bug146932
        paintFocusCursor(new Rectangle(r.x, r.y + 1, r.width, r.height - 1), graphics);
      }
    };
    figure.setBorder(new MarginBorder(4));
    ToolbarLayout layout = new ToolbarLayout(false);
    // layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
    layout.setStretchMinorAxis(true);
    figure.setLayoutManager(layout);
    nameLabel = new Label();

    if (getModel() instanceof ITreeElement) {
    	nameLabel.setIcon(((ITreeElement) getModel()).getImage());
    }
    
    nameLabel.setLabelAlignment(Label.LEFT);
    // nameLabel.setTextPlacement(PositionConstants.WEST);
    figure.add(nameLabel);
    Figure f1 = new Figure();
    f1.setBorder(new MarginBorder(0, 20, 0, 0));
    f1.setLayoutManager(new ToolbarLayout());
    figure.add(f1);
    addressBoxFigure = new RectangleFigure();
    addressBoxFigure.setForegroundColor(ColorConstants.lightGray);
    addressBoxFigure.setLayoutManager(new ToolbarLayout()
    {
      // here' we tweak the layout so that address labels aren't sized too wide
      // the Label class automatically handles the addition of the '...' 
      //
      protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint)
      {
        Dimension d = super.calculatePreferredSize(container, wHint, hHint);
        d.width = Math.min(d.width, MAX_ADDRESS_WIDTH);
        return d;
      }
    });
    f1.add(addressBoxFigure);
    addressLabel = new Label();

    addressLabel.setForegroundColor(DesignViewGraphicsConstants.defaultForegroundColor);
    addressLabel.setBorder(new MarginBorder(2, 6, 2, 6));
    addressLabel.setLabelAlignment(Label.LEFT);
    addressBoxFigure.add(addressLabel);
    
    // rmah: The block of code below has been moved from refreshVisuals().  We're
    // assuming the read-only state of the EditPart will never change once the
    // EditPart has been created.
    if (isReadOnly())
    {  
      nameLabel.setForegroundColor(DesignViewGraphicsConstants.readOnlyLabelColor);
      addressLabel.setForegroundColor(DesignViewGraphicsConstants.readOnlyLabelColor);
    }
    else 
    {
      nameLabel.setForegroundColor(DesignViewGraphicsConstants.defaultForegroundColor);
      addressLabel.setForegroundColor(DesignViewGraphicsConstants.defaultForegroundColor);
    }
    
    return figure;
  }

  protected void createEditPolicies()
  {
    super.createEditPolicies();
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ASDLabelDirectEditPolicy());
    installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ASDSelectionEditPolicy());
  }
  private DirectEditManager manager;
  private Point cursorLocation;

  public void performDirectEdit(Point cursorLocation)
  {
    this.cursorLocation = cursorLocation;
    if (!isReadOnly() && !isFileReadOnly() && (cursorLocation == null || hitTest(getLabelFigure(), cursorLocation))) {
    	manager = new LabelEditManager(this, new LabelCellEditorLocator(this, cursorLocation));
    	manager.show();
    }
    else if (hitTest(getFigure().getBounds(), cursorLocation) && isReadOnly()) {
      doOpenNewEditor();
    }
  }
  
  public void performRequest(Request req) {
	  if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
		  performDirectEdit(null);
	  }
  }

  public Label getLabelFigure()
  {
    if (cursorLocation == null || translateBounds(nameLabel.getBounds()).contains(cursorLocation))
    {
      return nameLabel;
    }
    else if (translateBounds(addressLabel.getBounds()).contains(cursorLocation))
    {
      return addressLabel;
    }
    return null;
  }
  
  protected Rectangle translateBounds(Rectangle bounds) {
	  
		ASDMultiPageEditor editor = (ASDMultiPageEditor) ASDEditorPlugin.getActiveEditor();
		FigureCanvas canvas = (FigureCanvas) ((EditPartViewer) editor.getAdapter(GraphicalViewer.class)).getControl();
		Point viewLocation = canvas.getViewport().getViewLocation();
		
		return new Rectangle(bounds.x + viewLocation.x, bounds.y + viewLocation.y, bounds.width, bounds.height);
  }

  protected Object getReferencedModel()
  {
    IEndPoint endPoint = (IEndPoint) getModel();
    return endPoint.getBinding();
  }

  protected void refreshVisuals()
  {
    IEndPoint endPoint = (IEndPoint) getModel();
    nameLabel.setText(endPoint.getName());
    addressLabel.setText(endPoint.getAddress());
    
    Rectangle textBounds = addressLabel.getTextBounds();
    Rectangle bounds = addressLabel.getBounds();
    int textPlacement = addressLabel.getTextPlacement();
    int xDelta = textBounds.x - bounds.x;
    int xTextIncrease = textPlacement - xDelta;
    int textSize = textBounds.width + xTextIncrease;
    int boundSize = bounds.width;

    if (textSize > boundSize) {
    	hoverHelpLabel.setText(" " + endPoint.getAddress()  + " "); //$NON-NLS-1$ //$NON-NLS-2$
        addressLabel.setToolTip(hoverHelpLabel);
    }
    else {
        addressLabel.setToolTip(null);
    }

    refreshConnections();
    super.refreshVisuals();
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

  protected void activateConnection()
  {
    if (createConnectionFigure() != null)
    {
      getLayer(LayerConstants.CONNECTION_LAYER).add(connectionFigure);
    }
  }

  protected void deactivateConnection()
  {
    if (connectionFigure != null)
	{
	  removeConnectionFigure(getLayer(LayerConstants.CONNECTION_LAYER));
	}
	if (connectionFeedbackFigure != null)
	{
	  removeConnectionFigure(getLayer(LayerConstants.FEEDBACK_LAYER));
	}
  }
  
  private boolean removeConnectionFigure(IFigure parent) {
	  boolean contains = false;
	  Iterator it = parent.getChildren().iterator();
	  while (it.hasNext()) {
		  IFigure fig = (IFigure) it.next();
		  if (fig.equals(connectionFigure)) {
			  contains = true;
			  break;
		  }
	  }
	  
	  if (contains) {
		  parent.remove(connectionFigure);
	  }
	  
	  return contains;
  }

  protected boolean shouldDrawConnection()
  {
	  Object typeBeingRef = getReferencedModel();
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
  

  public ComponentReferenceConnection createConnectionFigure()
  {
	  if (connectionFigure == null && shouldDrawConnection())
	    {
	      Object typeBeingRef = getReferencedModel();
	      if (typeBeingRef != null)
	      {
	        AbstractGraphicalEditPart referenceTypePart = (AbstractGraphicalEditPart) getViewer().getEditPartRegistry().get(typeBeingRef);
	        if (referenceTypePart != null)
	        {
	          // We do reference another Type. Build a connection. We can make
	          // assumptions about anchor positioning since an attribute can only
	          // have a "reference" relationship, no "inheritance", no
	          // "is-referenced-by"
	          connectionFigure = new ComponentReferenceConnection();
	          refreshConnections();
	        }
	      }
	    }
	    return connectionFigure;
  }

  EditPart getConnectionTargetEditPart()
  {
    Object typeBeingRef = getReferencedModel();
    if (connectionFigure != null) {
      AbstractGraphicalEditPart referenceTypePart = (AbstractGraphicalEditPart) getViewer().getEditPartRegistry().get(typeBeingRef);
      return referenceTypePart;
    }
    return null;
  }
  
  public void refreshConnections() {
	  if (shouldDrawConnection()) {
		  Object typeBeingRef = getReferencedModel();
		  if (connectionFigure != null) {
			  AbstractGraphicalEditPart referenceTypePart = (AbstractGraphicalEditPart) getViewer().getEditPartRegistry().get(typeBeingRef);
	          connectionFigure.setSourceAnchor(new CenteredConnectionAnchor(addressBoxFigure, CenteredConnectionAnchor.RIGHT, 0, 0));
	          IFigure targetFigure = referenceTypePart.getFigure();
	          connectionFigure.setTargetAnchor(new CenteredConnectionAnchor(targetFigure, CenteredConnectionAnchor.HEADER_LEFT, 0, 10));
	          connectionFigure.setHighlight(false);
	          connectionFigure.setVisible(true);
		  }
		  else {
			  activateConnection();
		  }
	  }
	  else if (connectionFigure != null){
		  connectionFigure.setVisible(false);
	  }
  }

  private IFigure getFigureForFeedback()
  {
    return figure;
  }

  public void addFeedback()
  {
    IFigure figure = getFigureForFeedback();
    figure.setBackgroundColor(DesignViewGraphicsConstants.tableCellSelectionColor);
    
    if (connectionFigure != null) 
    {
      connectionFigure.setHighlight(true);
    	
      connectionFeedbackFigure = new ComponentReferenceConnection();
      connectionFeedbackFigure.setSourceAnchor(connectionFigure.getSourceAnchor());
      connectionFeedbackFigure.setTargetAnchor(connectionFigure.getTargetAnchor());
      connectionFeedbackFigure.setHighlight(true);
      getLayer(LayerConstants.FEEDBACK_LAYER).add(connectionFeedbackFigure);
    }
  }

  public void removeFeedback()
  {
    IFigure figure = getFigureForFeedback();
    figure.setBackgroundColor(figure.getParent().getBackgroundColor());

    if (connectionFeedbackFigure != null)
    {
      connectionFeedbackFigure.setHighlight(false);
      getLayer(LayerConstants.FEEDBACK_LAYER).remove(connectionFeedbackFigure);
      connectionFeedbackFigure = null;
    }
    
    if (connectionFigure != null)
      connectionFigure.setHighlight(false);
  }

  // TODO: rmah: VERY UGLY HACK.... I don't see any other way to solve this
  // issue at this time.
  // EndPointEditPart contains two labels which can be direct edited. The facade
  // driving EndPointEditPart
  // contains a getSetNameCommand() and a getSetAddressCommand()... however,
  // this class (LabelDirectEditPolicy)
  // only knows about getSetNameCommand()..... This is the problem we face when
  // combining two labels into
  // one EditPart.....
  // See LabelDirectEditPolicy.getDirectEditCommand(DirectEditRequest)
  public Command getSetProperLabelCommand(String newValue)
  {
    // TODO: rmah: We need to translate the point...
    if (cursorLocation == null || translateBounds(nameLabel.getBounds()).contains(cursorLocation))
    {
      return ((IEndPoint) getModel()).getSetNameCommand(newValue);
    }
    else if (translateBounds(addressLabel.getBounds()).contains(cursorLocation))
    {
      return ((IEndPoint) getModel()).getSetAddressCommand(newValue);
    }
    return null;
  }
  
  public EditPart getRelativeEditPart(int direction)
  {
    if (direction == PositionConstants.EAST)
    {      
      // navigate forward along the connection (to the right)
      return getConnectionTargetEditPart();
    }
    
    if (direction == PositionConstants.SOUTH)
    {
    	return EditPartNavigationHandlerUtil.getNextService(this);
    }

    return super.getRelativeEditPart(direction);
  }  
}

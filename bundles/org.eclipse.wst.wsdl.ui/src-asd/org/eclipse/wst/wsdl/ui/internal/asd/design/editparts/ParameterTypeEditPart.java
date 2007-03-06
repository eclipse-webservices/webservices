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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.MouseMotionListener.Stub;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.TypeReferenceDirectEditManager;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.BaseLinkIconFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.ModelDiagnosticInfo;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.RowLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.asd.util.IOpenExternalEditorHelper;
import org.eclipse.wst.wsdl.ui.internal.util.W11OpenExternalEditorHelper;
import org.eclipse.wst.wsdl.ui.internal.util.XSDGraphViewerDialog;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;

public class ParameterTypeEditPart extends BaseEditPart implements IFeedbackHandler, INamedEditPart
{   
	protected SimpleDirectEditPolicy simpleDirectEditPolicy = new SimpleDirectEditPolicy();
	protected Label parameterType;
	protected RowLayout rowLayout = new RowLayout();

	  public void performRequest(Request req) {
		  if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
			  performDirectEdit(null);
		  }
	  }
	  
	protected MyMouseEventListener mouseEventListener;
	private BaseLinkIconFigure linkIconFigure;

	protected IFigure createFigure()
	{
		IFigure figure = new Panel();
		figure.setLayoutManager(rowLayout); 

		parameterType = new Label();
		parameterType.setLabelAlignment(Label.LEFT);
		parameterType.setBorder(new MarginBorder(4,12,4,20));
		figure.add(parameterType);

		// rmah: The block of code below has been moved from refreshVisuals().  We're
		// assuming the read-only state of the EditPart will never change once the
		// EditPart has been created.
		if (isReadOnly()) 
		{
			parameterType.setForegroundColor(DesignViewGraphicsConstants.readOnlyLabelColor);
		}
		else
		{
			parameterType.setForegroundColor(ColorConstants.black);
		}

		return figure;
	}

	protected void refreshVisuals()
	{   
		super.refreshVisuals();
		IParameter param = (IParameter) getModel();
		String name = param.getComponentName();
		parameterType.setText(name);


		// TODO (cs) this evil bit of code needs to be fixed post WTP 1.5 when we have more freedom to 
		// clean up our internal code structure.  We shouldn't have hardcoded adapter references here!
		if (getModel() instanceof W11ParameterForPart)
		{
			Image image = ((W11ParameterForPart)getModel()).getSecondaryImage();
			if (image != null)
			{
				parameterType.setIcon(image);
			}

			parameterType.setForegroundColor(ColorConstants.black);

			List diagnosticMessages = new ArrayList(); 
			// TODO: rmah: We should not know about W11ParameterForPart here.  Modify
			// IParameter post WTP 1.5 to include the getDiagnosticMessages() method.
			if (getModel() instanceof W11ParameterForPart) {
				W11ParameterForPart paramForPart = (W11ParameterForPart) getModel();
				diagnosticMessages = paramForPart.getDiagnosticMessages();
			}

			Iterator it = diagnosticMessages.iterator();
			ModelDiagnosticInfo errorInfo = null;
			while (it.hasNext() && errorInfo == null) {
				ModelDiagnosticInfo temp = (ModelDiagnosticInfo) it.next();
				if (temp.getType() == ModelDiagnosticInfo.ERROR_TYPE) {
					errorInfo = temp;
					break;
				}
			}

			if (errorInfo != null) {
				parameterType.setText(errorInfo.getDescriptionText());
				parameterType.setForegroundColor(errorInfo.getDescriptionTextColor());
				parameterType.setIcon(null);
			}
		}

		// Force the LinkIconColumn to resize and relayout itself.
		if (getInterfaceEditPart() != null) {
			((Figure) getInterfaceEditPart().getLinkIconColumn()).invalidate();
			refreshLinkFigure(new Point(-1, -1));
		}
	}

	private InterfaceEditPart getInterfaceEditPart() {
		EditPart ep = getParent();
		while (ep != null && !(ep instanceof InterfaceEditPart)) {
			ep = ep.getParent();
		}

		if (ep instanceof InterfaceEditPart) {
			return (InterfaceEditPart) ep;
		}

		return null;
	}

	public void addFeedback() {	 		          
		figure.setBackgroundColor(DesignViewGraphicsConstants.tableCellSelectionColor);
	}

	public void removeFeedback() {
		figure.setBackgroundColor(figure.getParent().getBackgroundColor());
	}

	public Label getLabelFigure() {
		return parameterType;
	}

	protected void createEditPolicies()
	{
		super.createEditPolicies();
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ASDSelectionEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, simpleDirectEditPolicy);
	}

	public void performDirectEdit(Point cursorLocation) {
		if (cursorLocation == null || (hitTest(parameterType, cursorLocation) && !isReadOnly())) { 
			IParameter param = (IParameter) getModel();

			TypeReferenceDirectEditManager manager = new TypeReferenceDirectEditManager(param, this, parameterType);   
			simpleDirectEditPolicy.setDelegate(manager);
			manager.show();


		}
		else if (parameterType.getParent() instanceof Figure &&
				 hitTestFigure((Figure) parameterType.getParent(), cursorLocation) && isReadOnly()) {
			doOpenNewEditor();
		}
	}

	public void activate() {
		super.activate();

		IFigure primaryLayer = getLayer(LayerConstants.PRIMARY_LAYER);
		mouseEventListener = new MyMouseEventListener();
		primaryLayer.addMouseMotionListener(mouseEventListener);
		primaryLayer.addMouseListener(mouseEventListener);
	}

	public void deactivate() {
		if (mouseEventListener != null) {
			IFigure primaryLayer = getLayer(LayerConstants.PRIMARY_LAYER);
			primaryLayer.removeMouseMotionListener(mouseEventListener);
			primaryLayer.removeMouseListener(mouseEventListener);
		}

		InterfaceEditPart ep = getInterfaceEditPart();
		if (ep != null && linkIconFigure != null) {
			IFigure fig = ep.getLinkIconColumn();
			if (fig.getChildren().contains(linkIconFigure)) {
				ep.getLinkIconColumn().remove(linkIconFigure);
			}
		}
	}


	private class SimpleDirectEditPolicy extends DirectEditPolicy 
	{
		protected TypeReferenceDirectEditManager delegate;

		public void setDelegate(TypeReferenceDirectEditManager delegate)
		{                                           
			this.delegate = delegate;
		}

		protected org.eclipse.gef.commands.Command getDirectEditCommand(final DirectEditRequest request) 
		{ 
			return new Command() //AbstractCommand()
			{
				public void execute()
				{                       
					if (delegate != null)
					{
						delegate.performEdit(request.getCellEditor());
					}  
				}     

				public void redo()
				{
				}  

				public void undo()
				{
				}     

				public boolean canExecute()
				{
					return true;
				}
			};
		}

		protected void showCurrentEditValue(DirectEditRequest request) 
		{      
			//hack to prevent async layout from placing the cell editor twice.
			getHostFigure().getUpdateManager().performUpdate();
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

	private class MyMouseEventListener extends Stub implements MouseListener {
		public void mouseReleased(MouseEvent me) { }
		public void mouseDoubleClicked(MouseEvent me) { }

		public void mouseMoved(MouseEvent me) {
			Point pointer = me.getLocation();
			refreshLinkFigure(pointer);
		}

		public void mousePressed(MouseEvent me) {
			Point pointer = me.getLocation();
      openExternalEditor(pointer);
		}
    
    public void mouseHover(MouseEvent me)
    {
      Point pointer = me.getLocation();
      showXSDDialog(pointer);
		}
	}

	// Methods below handle the Link Figure.....
	private void emphasizeLinkFigure() {
		linkIconFigure.setColor(ColorConstants.blue);
	}

	private void unemphasizeLinkFigure() {
		linkIconFigure.setColor(ColorConstants.lightGray);
	}

	private boolean pointerInRange(Rectangle figBounds, Point pointer) {
		Rectangle linkBounds = getLinkFigureBounds();

		int entireX = figBounds.x;
		int entireY = figBounds.y;
		int entireWidth = figBounds.width + linkBounds.width;
		int entireHeight = figBounds.height;
		Rectangle entireBounds = new Rectangle(entireX, entireY, entireWidth, entireHeight);

		return entireBounds.contains(pointer);
	}

	private boolean containsLinkFigure() {
		Iterator it = getInterfaceEditPart().getLinkIconColumn().getChildren().iterator();
		while (it.hasNext()) {
			Object item = it.next();
			if (item.equals(linkIconFigure)) {
				return true;
			}
		}

		return false;
	}

	private Rectangle getLinkFigureBounds() {
		if (containsLinkFigure()) {
			return linkIconFigure.getBounds();
		}
		else {
			return null;
		}
	}
 
	private IOpenExternalEditorHelper getExternalEditorOpener() {
		IOpenExternalEditorHelper openExternalEditorHelper = null;
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null) {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if (page.getActiveEditor() != null) {
					Object adapted = page.getActiveEditor().getAdapter(IOpenExternalEditorHelper.class);
					if (adapted instanceof IOpenExternalEditorHelper) {
						openExternalEditorHelper = (IOpenExternalEditorHelper) adapted;
						openExternalEditorHelper.setModel(getModel());
					}
				}
			}

		return openExternalEditorHelper;
	}

	private void refreshLinkFigure(Point point) {
		Rectangle figBounds = getFigure().getParent().getParent().getBounds();
		// TODO: rmah:  We should not know about W11OpenExternalEditorHelper here.  Post WTP 1.5, we should add the
		// isValid() method to interface IOpenExternalEditorHelper.java
		if (getExternalEditorOpener() instanceof W11OpenExternalEditorHelper) {
			W11OpenExternalEditorHelper openHelper = (W11OpenExternalEditorHelper) getExternalEditorOpener();

			if (openHelper.linkApplicable()) {
				if (!figureContainsLinkFigure(getInterfaceEditPart().getLinkIconColumn())) {
					linkIconFigure = new BaseLinkIconFigure(this);
					getInterfaceEditPart().getLinkIconColumn().add(linkIconFigure);
				}
				
				if (openHelper.isValid()) {
					linkIconFigure.setLinkIconStyle(BaseLinkIconFigure.VALID_SCHEMA_LINK_STYLE);
					
					if (pointerInRange(figBounds, point)) {
						emphasizeLinkFigure();
					}
					else {
						unemphasizeLinkFigure();
					}
				}
				else {
					linkIconFigure.setLinkIconStyle(BaseLinkIconFigure.INVALID_SCHEMA_LINK_STYLE);
				}
					
			}
			else {
				if (containsLinkFigure()) {
					getInterfaceEditPart().getLinkIconColumn().remove(linkIconFigure);
				}
			}
		}
	}

	private boolean figureContainsLinkFigure(IFigure parent) {
		Iterator it = parent.getChildren().iterator();
		while (it.hasNext()) {
			if (it.next().equals(linkIconFigure)) {
				return true;
			}
		}

		return false;
	}

  private void openExternalEditor(Point point) {
    Rectangle linkFigBounds = getLinkFigureBounds();
    if (linkFigBounds == null || getExternalEditorOpener() == null) {
      return;
    }

    Rectangle testbounds = new Rectangle(linkFigBounds.x, linkFigBounds.y, 0, linkFigBounds.height);

    if (getExternalEditorOpener().linkApplicable() && pointerInRange(testbounds, point)) {
      // Open in XSD Editor
      getExternalEditorOpener().openExternalEditor();         
    }
  }

  private void showXSDDialog(Point point) {
    Rectangle linkFigBounds = getLinkFigureBounds();
    if (linkFigBounds == null) {
      return;
    }

    Rectangle testbounds = new Rectangle(linkFigBounds.x, linkFigBounds.y, 0, linkFigBounds.height);

    if (pointerInRange(testbounds, point)) {
      W11ParameterForPart param = (W11ParameterForPart)getModel();
      Object xsdModel = null;
      String title = null;
      String info = null;
      if (param.isType()) {
        XSDTypeDefinition type = ((Part)param.getTarget()).getTypeDefinition();
        xsdModel = type;
        title = type.getName();
        info = type.getTargetNamespace(); 
      }
      else {
        XSDElementDeclaration elem = ((Part)param.getTarget()).getElementDeclaration();
        xsdModel = elem;
        title = elem.getName();
        info = elem.getTargetNamespace(); 
      }
      XSDGraphViewerDialog dialog = new XSDGraphViewerDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title, info, xsdModel);
      dialog.setOpenExternalEditor(getExternalEditorOpener());
      dialog.create();
      dialog.open();
      dialog.getShell().setFocus();
    }
  }
}

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

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
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
import org.eclipse.gef.EditPartListener;
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
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.ASDCCombo;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.TypeReferenceDirectEditManager;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies.ASDSelectionEditPolicy;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.BaseLinkIconFigure;
import org.eclipse.wst.wsdl.ui.internal.asd.design.figures.ModelDiagnosticInfo;
import org.eclipse.wst.wsdl.ui.internal.asd.design.layouts.RowLayout;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.asd.util.IOpenExternalEditorHelper;

public class ParameterTypeEditPart extends BaseEditPart implements IFeedbackHandler, INamedEditPart
{   
	protected SimpleDirectEditPolicy simpleDirectEditPolicy = new SimpleDirectEditPolicy();
	protected Label parameterType;
	protected RowLayout rowLayout = new RowLayout();

	  public void performRequest(Request req) {
		  if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
			  performDirectEdit(null);
		  }
		  
		  if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			  showPreviewDialog(null);
		  }
	  }
	  
	protected MyMouseEventListener mouseEventListener;
	private BaseLinkIconFigure linkIconFigure;
	private EditPartListener editPartListener;

	protected IFigure createFigure()
	{
		IFigure figure = new Panel() {
			public void paint(Graphics graphics) {
				super.paint(graphics);
				Rectangle r = getBounds();
				// bug146932
				paintFocusCursor(new Rectangle(r.x, r.y, r.width, r.height), graphics);
			}
		};
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
			parameterType.setForegroundColor(DesignViewGraphicsConstants.defaultForegroundColor);
		}

		return figure;
	}

	protected void refreshVisuals()
	{   
		super.refreshVisuals();
		IParameter param = (IParameter) getModel();
		String name = param.getComponentName();
		parameterType.setText(name);

		Image image = ((ITreeElement)getModel()).getSecondaryImage();
		if (image != null)
		{
			parameterType.setIcon(image);
		}

		parameterType.setForegroundColor(DesignViewGraphicsConstants.defaultForegroundColor);

		List diagnosticMessages = param.getDiagnosticMessages();
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
		if (!isFileReadOnly() && (cursorLocation == null || (hitTest(parameterType, cursorLocation) && !isReadOnly()))) { 
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
		
		editPartListener = new EditPartListener() {

			public void childAdded(EditPart child, int index) {
				// do nothing
			}

			public void partActivated(EditPart editpart) {
				// do nothing
			}

			public void partDeactivated(EditPart editpart) {
				// do nothing
			}

			public void removingChild(EditPart child, int index) {
				// do nothing
			}

			public void selectedStateChanged(EditPart editpart) {
				IOpenExternalEditorHelper openHelper = getExternalEditorOpener();
				if (openHelper != null && openHelper.linkApplicable()) {
					if (!figureContainsLinkFigure(getInterfaceEditPart().getLinkIconColumn())) {
						linkIconFigure = new BaseLinkIconFigure(ParameterTypeEditPart.this);
						getInterfaceEditPart().getLinkIconColumn().add(linkIconFigure);
					}

					if (openHelper.isValid()) {
						linkIconFigure.setLinkIconStyle(BaseLinkIconFigure.VALID_SCHEMA_LINK_STYLE);
						if (editpart.getSelected() == EditPart.SELECTED_NONE)
							unemphasizeLinkFigure();
						else
							emphasizeLinkFigure();
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
			}};
			
		addEditPartListener(editPartListener);		
	}

	public void deactivate() {
		if (mouseEventListener != null) {
			IFigure primaryLayer = getLayer(LayerConstants.PRIMARY_LAYER);
			primaryLayer.removeMouseMotionListener(mouseEventListener);
			primaryLayer.removeMouseListener(mouseEventListener);
		}

		if (editPartListener != null) {
			removeEditPartListener(editPartListener);
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
                  // dont execute if name is not changing
                  IParameter parameter = (IParameter)getModel();
                  Object newValue = ((ASDCCombo)request.getCellEditor().getControl()).getText();
                  if (newValue instanceof String)
                  {
                      return !newValue.equals(parameter.getComponentName());
                  }
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
		if (direction == PositionConstants.SOUTH && (editPart == null || editPart == this))
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
		
		public void mouseHover(MouseEvent me) {
			Point pointer = me.getLocation();
			showPreviewDialog(pointer);
		}
	}
	
	private void showPreviewDialog(Point point) {
		Rectangle linkFigBounds = getLinkFigureBounds();
		if (linkFigBounds == null) {
			return;
		}

		Rectangle testbounds = new Rectangle(linkFigBounds.x, linkFigBounds.y, 0, linkFigBounds.height);

		if (pointerInRange(testbounds, point)) {
			IOpenExternalEditorHelper helper = getExternalEditorOpener();
			helper.showPreview();
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
		if (pointer == null) {
		  return true;
		}
		
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

		IOpenExternalEditorHelper openHelper = getExternalEditorOpener();
		if (openHelper != null && openHelper.linkApplicable()) {
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

	protected String getAccessibleName()
	{
	  Label labelFigure = getLabelFigure();
	  if (labelFigure != null) {
	    return labelFigure.getText();
	  }
	  return ""; //$NON-NLS-1$
	}
}

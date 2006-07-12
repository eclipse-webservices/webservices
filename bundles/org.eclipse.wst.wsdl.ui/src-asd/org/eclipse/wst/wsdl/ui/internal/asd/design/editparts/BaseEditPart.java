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

import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.ui.internal.actions.OpenInNewEditor;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.IActionProvider;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObjectListener;
import org.eclipse.wst.xsd.ui.internal.adt.design.editparts.IEditPartNavigationHandler;


public abstract class BaseEditPart extends AbstractGraphicalEditPart implements IActionProvider, IASDObjectListener, IFeedbackHandler, IEditPartNavigationHandler
{
  protected static final String[] EMPTY_ACTION_ARRAY = {};
  
  public String[] getActions(Object object)
  {
    Object model = getModel();
    if (model instanceof IActionProvider)
    {
      return ((IActionProvider)model).getActions(object);
    }  
    return EMPTY_ACTION_ARRAY;
  }
  
  protected void addActionsToList(List list, String[] actions)
  {
    for (int i = 0; i < actions.length; i++)
    {
      list.add(actions[i]);
    }  
  }
  
  public void activate()
  {
    super.activate();
    Object model = getModel();    
    if (model instanceof IASDObject)
    {
      IASDObject object = (IASDObject)model;
      object.registerListener(this);
    }      
  }
  
  public void deactivate()
  {
    Object model = getModel();
    if (model instanceof IASDObject)
    {
      IASDObject object = (IASDObject)model;
      object.unregisterListener(this);
    }   
    super.deactivate();
  }  
  
  public void propertyChanged(Object object, String property)
  {
    //System.out.println("propertyChanged " + this.getClass().getName());
    refresh();
  }
  
  public void refreshConnections() {
	  Iterator kids = getChildren().iterator();
	  while (kids.hasNext()) {
		  Object item = kids.next();
		  if (item instanceof BaseEditPart) {
			  ((BaseEditPart) item).refreshConnections();
		  }
	  }
  }
  
  public void addFeedback() {
	  
  }
  public void removeFeedback() {
	  
  }
  
  protected boolean hitTest(Label target, Point location) {
	  Rectangle origB = target.getTextBounds().getCopy();
	  Rectangle transB = target.getTextBounds().getCopy();

	  target.translateToAbsolute(transB);

	  int newX = origB.x + Math.abs(transB.x - origB.x);
	  int newY = origB.y + Math.abs(transB.y - origB.y);	  
	  Rectangle finalB = new Rectangle(newX, newY, origB.width, origB.height);

	  return finalB.contains(location);
  }
  
  protected boolean hitTestFigure(Figure target, Point location) {
    Rectangle origB = target.getBounds().getCopy();
    Rectangle transB = target.getBounds().getCopy();

    target.translateToAbsolute(transB);

    int newX = origB.x + Math.abs(transB.x - origB.x);
    int newY = origB.y + Math.abs(transB.y - origB.y);    
    Rectangle finalB = new Rectangle(newX, newY, origB.width, origB.height);

    return finalB.contains(location);
  }

  public boolean isReadOnly() {
	  Object model = getModel();
	  if (model instanceof IASDObject) {
		  return ((IASDObject) model).isReadOnly();
	  }
	  
	  return false;
  }
  
  protected void doOpenNewEditor()
  {
    IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    IEditorPart editorPart = workbenchWindow.getActivePage().getActiveEditor();
    ActionRegistry registry = (ActionRegistry) editorPart.getAdapter(ActionRegistry.class);
    if (registry != null)
    {
      IAction action = registry.getAction(OpenInNewEditor.ID);
      action.run();
    }
  }
  
  public EditPart getRelativeEditPart(int direction)
  {
    return EditPartNavigationHandlerUtil.getRelativeEditPart(this, direction);
  }
}

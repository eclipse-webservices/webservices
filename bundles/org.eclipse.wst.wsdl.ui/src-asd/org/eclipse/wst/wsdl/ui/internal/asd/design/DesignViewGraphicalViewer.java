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
package org.eclipse.wst.wsdl.ui.internal.asd.design;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.xsd.ui.internal.adt.design.editparts.IEditPartNavigationHandler;
import org.eclipse.wst.xsd.ui.internal.adt.editor.CommonSelectionManager;

public class DesignViewGraphicalViewer extends ScrollingGraphicalViewer implements ISelectionChangedListener
{
  protected ASDSelectionChangedListener internalSelectionProvider = new ASDSelectionChangedListener();
//TODO Make this generic
	  public DesignViewGraphicalViewer(IEditorPart editor, CommonSelectionManager manager)
	  {
	    super();
	    setContextMenu(new DesignViewContextMenuProvider(this, this));
	    editor.getEditorSite().registerContextMenu("org.eclipse.wst.wsdl.editor.popup.graph", getContextMenu(), internalSelectionProvider, false); //$NON-NLS-1$

	    // make the internalSelectionProvider listen to graph view selection changes
	    addSelectionChangedListener(internalSelectionProvider);    
	    internalSelectionProvider.addSelectionChangedListener(manager);
	    manager.addSelectionChangedListener(this);  
        
	    setKeyHandler(new InternalGraphicalViewerKeyHandler(this));
	  }
	  
	  // this method is called when something changes in the selection manager
	  // (e.g. a selection occured from another view)
	  public void selectionChanged(SelectionChangedEvent event)
	  {
	    Object selectedObject = ((StructuredSelection) event.getSelection()).getFirstElement();
	    if (event.getSource() != internalSelectionProvider)
	    {
	    	EditPart editPart = getEditPart(getRootEditPart(), selectedObject);
	    	if (editPart != null)
	    	{
	    		setSelection(new StructuredSelection(editPart));
	    	}   
	    }
	  }
	  
	  protected EditPart getEditPart(EditPart editPart, Object model)  
	  {                     
	    EditPart result = null;
	    if (editPart.getModel() == model)
	    {
	      result = editPart;      
	    }                   
	    else
	    {
	      for (Iterator i = editPart.getChildren().iterator(); i.hasNext(); )
	      {
	        result = getEditPart((EditPart)i.next(), model);
	        if (result != null)
	        {
	          break;
	        }
	      }
	    }
	    return result;
	  }
	  
	private class InternalGraphicalViewerKeyHandler extends GraphicalViewerKeyHandler
    {
      InternalGraphicalViewerKeyHandler(GraphicalViewer viewer)
      {
        super(viewer);
      }

      public boolean keyPressed(KeyEvent event)
      {
        int direction = -1;
        switch (event.keyCode) 
        {
          case SWT.ARROW_LEFT:
          { 
            direction = PositionConstants.WEST;
            break;
          }                
          case SWT.ARROW_RIGHT:
          { 
            direction = PositionConstants.EAST;
            break;
          }  
          case SWT.ARROW_UP:
          { 
            direction = PositionConstants.NORTH;
            break;
          }                
          case SWT.ARROW_DOWN:
          { 
            direction = PositionConstants.SOUTH;
            break;
          }                
        }    
        if (direction != -1)
        {
          GraphicalEditPart focusEditPart = getFocusEditPart();
          if (focusEditPart instanceof IEditPartNavigationHandler)
          {
            IEditPartNavigationHandler handler = (IEditPartNavigationHandler)focusEditPart;
            EditPart target = handler.getRelativeEditPart(direction);
            if (target != null)
            {
              navigateTo(target, event);
              return true;                   
            }                     
          }                   
        }  
        switch (event.keyCode)
        {
        case SWT.PAGE_DOWN:
            if (scrollPage(event, PositionConstants.SOUTH))
                return true;
            break;
        case SWT.PAGE_UP:
            if (scrollPage(event, PositionConstants.NORTH))
                return true;
        }    
        return super.keyPressed(event);
      }
      
      private boolean scrollPage(KeyEvent event, int direction)
      {
        if (!(getViewer().getControl() instanceof FigureCanvas))
          return false;
        FigureCanvas figCanvas = (FigureCanvas)getViewer().getControl();
        Point loc = figCanvas.getViewport().getViewLocation();
        Rectangle area = figCanvas.getViewport().getClientArea(Rectangle.SINGLETON).scale(.8); 
        if (direction == PositionConstants.NORTH)
        {            
          figCanvas.scrollToY(loc.y - area.height);
        }
        else
        {
          figCanvas.scrollToY(loc.y + area.height);
        }
        return true;
      }}

      /*
	   * We need to convert from edit part selections to model object selections
	   */
	  class ASDSelectionChangedListener implements ISelectionProvider, ISelectionChangedListener
	  {
	    protected List listenerList = new ArrayList();
	    protected ISelection selection = new StructuredSelection();

	    public void addSelectionChangedListener(ISelectionChangedListener listener)
	    {
	      listenerList.add(listener);
	    }

	    public void removeSelectionChangedListener(ISelectionChangedListener listener)
	    {
	      listenerList.remove(listener);
	    }

	    public ISelection getSelection()
	    {
	      return selection;
	    }

	    protected void notifyListeners(SelectionChangedEvent event)
	    {
	      for (Iterator i = listenerList.iterator(); i.hasNext();)
	      {
	        ISelectionChangedListener listener = (ISelectionChangedListener) i.next();
	        listener.selectionChanged(event);
	      }
	    }

	    public StructuredSelection convertSelectionFromEditPartToModel(ISelection editPartSelection)
	    {
	      List selectedModelObjectList = new ArrayList();
	      if (editPartSelection instanceof IStructuredSelection)
	      {
	        for (Iterator i = ((IStructuredSelection) editPartSelection).iterator(); i.hasNext();)
	        {
	          Object obj = i.next();
	          Object model = null;
	          if (obj instanceof EditPart)
	          {
	            EditPart editPart = (EditPart) obj;
	            model = editPart.getModel();          
	          }
	          if (model != null)
	          {
	            selectedModelObjectList.add(model);
	          }
	        }
	      }
	      return new StructuredSelection(selectedModelObjectList);
	    }

	    public void setSelection(ISelection selection)
	    {
	      this.selection = selection;
	    }

	    public void selectionChanged(SelectionChangedEvent event)
	    {
	      ISelection newSelection = convertSelectionFromEditPartToModel(event.getSelection());
	      this.selection = newSelection;
	      SelectionChangedEvent newEvent = new SelectionChangedEvent(this, newSelection);
	      notifyListeners(newEvent);
	    }
	  } 
}    
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.ViewportLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.gef.util.figures.RoundedLineBorder;
import org.eclipse.wst.wsdl.ui.internal.graph.WSDLComponentViewer;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;

import org.eclipse.wst.xsd.ui.internal.gef.util.figures.ContainerFigure;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.ContainerLayout;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.FillLayout;

public class GroupEditPart extends WSDLEditPart
{
  protected ScrollPane scrollpane;
  protected Label label;

  protected Object emphasizedModelObject;
  protected Object inputConnectionModelObject;
  protected Object outputConnectionModelObject;

  public WSDLTreeNodeEditPart emphasizedEditPart;
  public AbstractGraphicalEditPart inputConnection;
  public AbstractGraphicalEditPart outputConnection;

  public GroupEditPart nextGroupEditPart;
  public IConnectionManager connectionManager;

  protected InternalFigureListener figureListener = new InternalFigureListener();

  class InternalFigureListener implements FigureListener
  {
    public void figureMoved(IFigure source)
    {
      ScalableRootEditPart root = (ScalableRootEditPart)getViewer().getRootEditPart();
      root.getLayer(LayerConstants.CONNECTION_LAYER).repaint();
    }
  }

  public Object getEmphasizedModelObject()
  {
    return emphasizedModelObject;
  }

  public Definition getDefinition()
  {
    return ((WSDLGroupObject)getModel()).getDefinition();
  }

  //protected void setEmphasizedObject(EditPart editPart)
  //{                                                            
  //  UnknownObjectEditPart ep = null;
  //  if (editPart instanceof UnknownObjectEditPart)
  //  {
  //   ep = (UnknownObjectEditPart)editPart;
  //  }
  //
  //  if (emphasizedEditPart != null)
  //  {
  //    emphasizedEditPart.setEmphasized(false);
  //  }
  //  if (ep != null)
  //  {
  //    emphasizedEditPart = ep;
  //    emphasizedEditPart.setEmphasized(true);
  //  }
  //}  

  public void setEmphasizedModelObject(Object model)
  {
    emphasizedModelObject = model;

    if (emphasizedEditPart != null)
    {
      emphasizedEditPart.setEmphasized(false);
    }

    EditPart editPart = getMatchingChildEditPart(model);
    emphasizedEditPart = (editPart instanceof WSDLTreeNodeEditPart) ? (WSDLTreeNodeEditPart)editPart : null;

    if (emphasizedEditPart != null)
    {
      emphasizedEditPart.setEmphasized(true);
    }
  }

  public void scrollToRevealEditPart(EditPart editPart)
  {
    if (editPart instanceof AbstractGraphicalEditPart)
    {
      scrollToEditPart((AbstractGraphicalEditPart)editPart);
    }
  }

  public void scrollToRevealInputConnection()
  {
    if (inputConnection != null)
    {
      scrollToEditPart(inputConnection);
    }
  }

  public void scrollToRevealOutputConnection()
  {
    if (outputConnection != null)
    {
      scrollToEditPart(outputConnection);
    }
  }

  public void setInputConnectionModelObject(Object model)
  {
    inputConnection = getMatchingChildEditPart(model);
    inputConnectionModelObject = model;
  }

  public Object getInputConnectionModelObject()
  {
    return inputConnectionModelObject;
  }

  public Object getOutputConnectionModelObject()
  {
    return outputConnectionModelObject;
  }

  public void setOutputConnectionModelObject(Object model)
  {
    outputConnection = getMatchingChildEditPart(model);
    outputConnectionModelObject = model;
  }

  protected void createConnectionManager()
  {
    switch (getType())
    {
      case WSDLGroupObject.MESSAGES_GROUP :
        {
          connectionManager = new MessagesGroupConnectionManager(this);
          break;
        }
      case WSDLGroupObject.SERVICES_GROUP :
        {
          connectionManager = new ServicesGroupConnectionManager(this);
          break;
        }
      case WSDLGroupObject.BINDINGS_GROUP :
        {
          connectionManager = new BindingsGroupConnectionManager(this);
          break;
        }
      case WSDLGroupObject.PORT_TYPES_GROUP :
        {
          connectionManager = new PortTypesGroupConnectionManager(this);
          break;
        }
    }
  }

  public IConnectionManager getConnectionManager()
  {
    return connectionManager;
  }

  public ContainerFigure outerPane;

  public IFigure createFigure()
  {
    createConnectionManager();

    outerPane = new ContainerFigure();
    outerPane.setBorder(new RoundedLineBorder(1, 6));
    outerPane.setForegroundColor(groupBorderColor);

    ContainerFigure r = new ContainerFigure();
    //r.setCornerDimensions(new Dimension(4, 4));   
    r.setOutline(false);
    r.setMinimumSize(new Dimension(0, 0));

    r.setFill(true);

    r.setBackgroundColor(groupHeaderColor);
    outerPane.add(r);

    //ContainerFigure labelHolder = new ContainerFigure();                             
    //labelHolder.add(label);
    label = new Label();
    label.setForegroundColor(ColorConstants.black);
    label.setBorder(new MarginBorder(2, 4, 2, 4));
    r.add(label); //Holder);

    RectangleFigure line = new RectangleFigure();
    line.setPreferredSize(20, 1);
    outerPane.add(line);

    int minHeight = 250;
    switch (getType())
    {
    	case WSDLGroupObject.IMPORTS_GROUP :
		case WSDLGroupObject.TYPES_GROUP :
		{
			minHeight = 50;
			break;	
		}
		case WSDLGroupObject.EXTENSIBILITY_ELEMENTS_GROUP :
		{
			minHeight = 150;
			break;
		}
    }

	final int theMinHeight = minHeight;
    FillLayout outerLayout = new FillLayout()
    {
      protected Dimension calculatePreferredSize(IFigure parent, int width, int height)
      {
        Dimension d = super.calculatePreferredSize(parent, width, height);
        d.union(new Dimension(100, theMinHeight));
        return d;
      }
    };
    outerLayout.setHorizontal(false);
    //layout.setSpacing(5);
    outerPane.setLayoutManager(outerLayout);

    scrollpane = new ScrollPane();
    scrollpane.setForegroundColor(ColorConstants.black);
    scrollpane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC); //ScrollPane.ALWAYS);
    outerPane.add(scrollpane);

    ContainerFigure pane = new ContainerFigure();
    pane.setBorder(new MarginBorder(5, 8, 5, 8));
    ContainerLayout layout = new ContainerLayout();
    layout.setHorizontal(false);
    layout.setSpacing(0);
    pane.setLayoutManager(layout);

    Viewport viewport = new Viewport();
    viewport.setContentsTracksHeight(true);
    ViewportLayout viewportLayout = new ViewportLayout()
    {
      protected Dimension calculatePreferredSize(IFigure parent, int width, int height)
      {
        Dimension d = super.calculatePreferredSize(parent, width, height);
        d.height = Math.min(d.height, theMinHeight - 25); //getViewer().getControl().getBounds().height);
        return d;
      }
    };
    viewport.setLayoutManager(viewportLayout);

    scrollpane.setViewport(viewport);
    scrollpane.setContents(pane);
    pane.addFigureListener(figureListener);

    return outerPane;
  }

  protected void refreshVisuals()
  {
    ModelAdapter adapter = getModelAdapter(getModel());
    if (adapter != null)
    {
      label.setText((String)adapter.getProperty(getModel(), ModelAdapter.LABEL_PROPERTY));
      //label.setIcon((Image)adapter.getProperty(getModel(), ModelAdapter.IMAGE_PROPERTY));
    }
    super.refreshVisuals();
  }

  protected void createEditPolicies()
  {
  }

  public IFigure getContentPane()
  {
    return scrollpane.getContents();
  }

  protected void scrollToEditPart(final AbstractGraphicalEditPart targetEditPart)
  {
    Runnable r = new Runnable()
    {
      public void run()
      {
        scrollToEditPartHelper(targetEditPart);
      }
    };
    Display.getCurrent().asyncExec(r);
  }

  protected void scrollToEditPartHelper(AbstractGraphicalEditPart targetEditPart)
  {
    if (targetEditPart != null)
    {
      Rectangle r1 = targetEditPart.getFigure().getBounds();
      Rectangle r2 = getContentPane().getBounds();
      scrollpane.scrollVerticalTo((r1.y - r2.y) - scrollpane.getBounds().height / 2);
    }
  }

  public AbstractGraphicalEditPart getMatchingChildEditPart(Object model)
  {
    List list = new ArrayList();
    Definition definition = getDefinition();
    for (Object o = model; o != null && !(o instanceof Definition); o = getParent(o))
    {
      list.add(0, o);
    }
    return getMatchingChildEditPart(this, list, 0, true);
  }

  protected Object getParent(Object model)
  {
    Object parent = null;
    if (model instanceof EObject)
    {
      parent = ((EObject)model).eContainer();
    }
    return parent;
  }

  protected AbstractGraphicalEditPart getMatchingChildEditPart(AbstractGraphicalEditPart editPart, List list, int index, boolean getUnexpandedParent)
  {
    AbstractGraphicalEditPart result = (getUnexpandedParent && editPart != this) ? editPart : null;
    Object model = list.size() > index ? list.get(index) : null;
    if (model != null && editPart != null)
    {
      for (Iterator i = editPart.getChildren().iterator(); i.hasNext();)
      {
        AbstractGraphicalEditPart child = (AbstractGraphicalEditPart)i.next();
        if (child.getModel() == model)
        {
          result = child;
          break;
        }
      }
      index++;
      if (index < list.size())
      {
        result = getMatchingChildEditPart(result, list, index, getUnexpandedParent);
      }
    }
    return result;
  }

  public int getType()
  {
    return ((WSDLGroupObject)getModel()).getType();
  }

  public GroupEditPart getNext()
  {
    return ((DefinitionEditPart)getParent()).getNextGroupEditPart(this);
  }

  public GroupEditPart getPrevious()
  {
    return ((DefinitionEditPart)getParent()).getPreviousGroupEditPart(this);
  }

  private void _refreshChildren()
  {
    int i;
    EditPart editPart;
    Object model;

    Map modelToEditPart = new HashMap();
    List children = getChildren();

    for (i = 0; i < children.size(); i++)
    {
      editPart = (EditPart)children.get(i);
      modelToEditPart.put(editPart.getModel(), editPart);
    }

    List modelObjects = getModelChildren();

    for (i = 0; i < modelObjects.size(); i++)
    {
      model = modelObjects.get(i);

      //Do a quick check to see if editPart[i] == model[i]
      if (i < children.size())
      {
        editPart = (EditPart)children.get(i);
        Object editPartModel = editPart.getModel();
        if (editPartModel == model)
        {
          continue;
        }
        else if (editPartModel.getClass() == model.getClass())
        {
          modelToEditPart.remove(editPartModel);

          if (isActive())
            editPart.deactivate();

          editPart.setModel(model);

          if (isActive())
          {
            editPart.activate();
            editPart.refresh();
          }
          continue;
        }
      }

      //Look to see if the EditPart is already around but in the wrong location
      editPart = (EditPart)modelToEditPart.get(model);

      if (editPart != null)
        reorderChild(editPart, i);
      else
      {
        //An editpart for this model doesn't exist yet.  Create and insert one.
        editPart = createChild(model);
        addChild(editPart, i);
      }
    }
    List trash = new ArrayList();
    for (; i < children.size(); i++)
      trash.add(children.get(i));
    for (i = 0; i < trash.size(); i++)
    {
      EditPart ep = (EditPart)trash.get(i);
      removeChild(ep);
    }
  }

  public void refreshChildren()
  {
    boolean reuseChildren = false;
    if (getViewer() instanceof WSDLComponentViewer)
    {
      WSDLComponentViewer wsdlComponentViewer = (WSDLComponentViewer)getViewer();
      reuseChildren = wsdlComponentViewer.isPreserveExpansionEnabled();
    }

    if (reuseChildren)
    {
      _refreshChildren();
    }
    else
    {
      super.refreshChildren();
    }
  }
}
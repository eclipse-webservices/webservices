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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.gef.util.editparts.InteractorHelper;
import org.eclipse.wst.wsdl.ui.internal.graph.GraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.graph.WSDLComponentViewer;
import org.eclipse.wst.wsdl.ui.internal.graph.editpolicies.WSDLSelectionHandlesEditPolicyImpl;
import org.eclipse.wst.wsdl.ui.internal.graph.figures.TreeNodeContentFigure;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterListener;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.ContainerFigure;

public abstract class TreeNodeEditPart extends AbstractGraphicalEditPart implements ModelAdapterListener, GraphicsConstants, IFeedbackHandler
{
  protected Label label;
  protected Label label2;
  protected ContainerFigure labelHolder = new ContainerFigure();
  protected TreeNodeContentFigure contentFigure;
  protected InteractorHelper interactorHelper;
  protected boolean isSelected = false;
  protected boolean isReadOnly = false;

  public void activate()
  {
    super.activate();
    addModelAdapterListener(getModel(), this);
    //viewer = getViewer();
  }

  int removeNotifyCount = 0;

  public void removeNotify()
  {
    removeNotifyCount++;
    if (removeNotifyCount == 1)
    {
      super.removeNotify();
    }
  }

  /** 
   * Apart from the deactivation done in super, the source
   * and target connections are deactivated, and the visual
   * part of the this is removed.
   *
   * @see #activate() 
   */
  public void deactivate()
  {
    removeModelAdapterListener(getModel(), this);
    super.deactivate();
  }

  protected void createEditPolicies()
  {
    //SelectionHandlesEditPolicyImpl policy = new SelectionHandlesEditPolicyImpl();
    //installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, policy);

    SelectionEditPolicy feedBackSelectionEditPolicy = new WSDLSelectionHandlesEditPolicyImpl();
    installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, feedBackSelectionEditPolicy);
  }

  protected EditPart createChild(Object model)
  {
    return getEditPartFactory().createEditPart(this, model);
  }

  public void propertyChanged(Object object, String property)
  {
    if (property == ModelAdapter.CHILDREN_PROPERTY)
    {
      refreshChildren();
    }
    else if (property == ModelAdapter.DETAIL_PROPERTY)
    {
      refreshVisuals();
    }
    else
    {
      refreshChildren();
      refreshVisuals();
    }
  }

  protected IFigure createFigure()
  {
    createContentFigure();
    createFigureContent();
    return contentFigure;
  }

  protected void createContentFigure()
  {
    contentFigure = new TreeNodeContentFigure();
  }

  protected void createFigureContent()
  {
    // add a bit of space between the interactor and icon
    //                      
    RectangleFigure space = new RectangleFigure();
    space.setVisible(false);
    space.setPreferredSize(new Dimension(3, 3));
    contentFigure.getIconArea().add(space);

    labelHolder = new ContainerFigure();
    labelHolder.setFill(true);
    contentFigure.getIconArea().add(labelHolder);

    label = new Label(WSDLEditorPlugin.getWSDLString("_UI_LABEL_UNKNOWN_OBJECT"));
    label.setForegroundColor(ColorConstants.black);
    labelHolder.add(label);

    interactorHelper = new InteractorHelper(this, contentFigure.getInteractor(), contentFigure.getInnerContentArea());
  }

  public IFigure getContentPane()
  {
    return contentFigure.getInnerContentArea();
  }

  public Color computeLabelColor()
  {
    Color color = ColorConstants.black;
    if (isSelected)
    {
      color = ColorConstants.white;
    }
    else if (isReadOnly)
    {
      color = ColorConstants.gray;
    }
    return color;
  }

  public void refreshVisuals()
  {
    ModelAdapter adapter = getModelAdapter(getModel());
    if (adapter != null)
    {
      isReadOnly = Boolean.TRUE.equals(adapter.getProperty(getModel(), "isReadOnly"));
      label.setForegroundColor(computeLabelColor());
      label.setText((String)adapter.getProperty(getModel(), ModelAdapter.LABEL_PROPERTY));
      label.setIcon((Image)adapter.getProperty(getModel(), ModelAdapter.IMAGE_PROPERTY));
    }
    else
    {
      label.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_UNKNOWN_NODE") + getModel().getClass().getName());
    }
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

    contentFigure.getInteractor().setVisible(hasChildren());
  }

  public List getModelChildren()
  {
    return contentFigure.getInteractor().isExpanded() ? getModelChildrenHelper() : Collections.EMPTY_LIST;
  }

  protected List getModelChildrenHelper()
  {
    List result = null;
    ModelAdapter modelAdapter = getModelAdapter(getModel());
    if (modelAdapter != null)
    {
      result = (List)modelAdapter.getProperty(getModel(), ModelAdapter.CHILDREN_PROPERTY);
    }
    return result != null ? result : Collections.EMPTY_LIST;
  }

  protected boolean hasChildren()
  {
    return getModelChildrenHelper().size() > 0;
  }

  protected abstract ModelAdapter getModelAdapter(Object model);

  protected EditPartFactory getEditPartFactory()
  {
    return ExtensibleEditPartFactory.getInstance();
  }

  protected void addModelAdapterListener(Object modelObject, ModelAdapterListener listener)
  {
    ModelAdapter adapter = getModelAdapter(modelObject);
    if (adapter != null)
    {
      adapter.addListener(listener);
    }
  }

  protected void removeModelAdapterListener(Object modelObject, ModelAdapterListener listener)
  {
    ModelAdapter adapter = getModelAdapter(modelObject);
    if (adapter != null)
    {
      adapter.removeListener(listener);
    }
  }

  public void addFeedback()
  {
    isSelected = true;
    labelHolder.setBackgroundColor(ColorConstants.black);
    label.setForegroundColor(computeLabelColor());
    labelHolder.setFill(true);
  }

  public void removeFeedback()
  {
    isSelected = false;
    labelHolder.setBackgroundColor(null);
    label.setForegroundColor(computeLabelColor());
    labelHolder.setFill(false);
  }

  public boolean isExpanded()
  {
    return contentFigure.getInteractor().isExpanded();
  }

  public void setExpanded(boolean arg)
  {
    interactorHelper.setExpanded(arg);
  }
}
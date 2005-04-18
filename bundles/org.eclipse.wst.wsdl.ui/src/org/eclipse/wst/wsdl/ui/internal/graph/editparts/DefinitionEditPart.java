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
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.gef.util.figures.RoundedLineBorder;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.ContainerFigure;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.ContainerLayout;
import org.eclipse.wst.xsd.ui.internal.gef.util.figures.FillLayout;

public class DefinitionEditPart extends WSDLEditPart implements ISelectionChangedListener //, ModelAdapterListener
{
  protected Label label;
  protected ContainerFigure outlined;

  protected ContainerFigure rightContentPane;
  protected ContainerFigure topContentPane;
  protected ContainerFigure middleContentPane;
  protected ContainerFigure bottomContentPane;
  protected static final String SELECTED_EDIT_PART_REFERENCE = "SELECTED_EDIT_PART_REFERENCE";

  public void activate()
  {
    super.activate();
    getViewer().addSelectionChangedListener(this);
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
    getViewer().removeSelectionChangedListener(this);
    super.deactivate();
  }

  protected IFigure[] initContentPanes()
  {
    IFigure[] contentPanes = new IFigure[4];
    contentPanes[0] = rightContentPane;
    contentPanes[1] = topContentPane;
    contentPanes[2] = middleContentPane;
    contentPanes[3] = bottomContentPane;
    return contentPanes;
  }

  protected int getContentPane(Object model)
  {
    int result = 0;

    if (model == SELECTED_EDIT_PART_REFERENCE)
    {
      result = 0;
    }
    else if (model instanceof WSDLGroupObject)
    {
      WSDLGroupObject groupObject = (WSDLGroupObject)model;
      switch (groupObject.getType())
      {
        case WSDLGroupObject.IMPORTS_GROUP :
        case WSDLGroupObject.TYPES_GROUP :
          //case WSDLGroupObject.EXTENSIBILITY_ELEMENTS_GROUP :
          {
            result = 1;
            break;
          }
        case WSDLGroupObject.BINDINGS_GROUP :
        case WSDLGroupObject.MESSAGES_GROUP :
        case WSDLGroupObject.PORT_TYPES_GROUP :
        case WSDLGroupObject.SERVICES_GROUP :
          {
            result = 2;
            break;
          }
        case WSDLGroupObject.EXTENSIBILITY_ELEMENTS_GROUP :
          {
            result = 3;
            break;
          }
      }
    }
    return result;
  }

  protected EditPart createChild(Object model)
  {
    EditPart editPart = null;
    if (model == SELECTED_EDIT_PART_REFERENCE)
    {
      editPart = new PartReferenceSectionEditPart();
      editPart.setModel(model);
      editPart.setParent(this);
    }
    else
    {
      editPart = super.createChild(model);
    }
    return editPart;
  }

  protected IFigure createFigure()
  {
    ContainerFigure outer = new ContainerFigure();
    ContainerLayout anchoredLayout = new ContainerLayout()
    {
      protected int alignFigure(IFigure parent, IFigure child)
      {
        return (child == outlined) ? 0 : -1;
      }
    };
    outer.setLayoutManager(anchoredLayout);
    /*
    ContainerFigure outer = new ConnectedEditPartFigure(this)
    {
      public IFigure getConnectionFigure()
      {
        return selectedPartEditPart != null ? selectedPartEditPart.getFigure() : this;
      }               
    };*/
    outer.getContainerLayout().setHorizontal(true);
    //outer.setBorder(new MarginBorder(0, 20, 0, 0));

    outlined = new ContainerFigure();
    outer.add(outlined);
    //outlined.getContainerLayout().setHorizontal(false);
    outlined.setLayoutManager(new FillLayout());
    outlined.setBorder(new RoundedLineBorder(1, 6));
    outlined.setForegroundColor(groupBorderColor);

    ContainerFigure rightPane = new ContainerFigure();
    rightPane.setBorder(new MarginBorder(0, 0, 0, 300));
    rightPane.getContainerLayout().setHorizontal(false);
    outer.add(rightPane);

    //RectangleFigure spacer = new RectangleFigure();
    //rightPane.add(spacer);
    //spacer.setPreferredSize(new Dimension(450, 200));

    rightContentPane = new ContainerFigure();
    rightPane.add(rightContentPane);
    rightContentPane.setBorder(new MarginBorder(15, 20, 15, 15));

    ContainerFigure labelHolder = new ContainerFigure();
    outlined.add(labelHolder);

    label = new Label();
    labelHolder.add(label);
    label.setBorder(new MarginBorder(2, 5, 0, 5));
    label.setFont(mediumFont);
    label.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_DEFINITION"));
    label.setForegroundColor(ColorConstants.black);

    topContentPane = new ContainerFigure();
    outlined.add(topContentPane);

    FillLayout fillLayout = new FillLayout(15);
    fillLayout.setHorizontal(true);
    topContentPane.setLayoutManager(fillLayout);
    topContentPane.setBorder(new MarginBorder(10, 10, 10, 10));

    middleContentPane = new ContainerFigure();
    outlined.add(middleContentPane);
    fillLayout = new FillLayout(15);
    fillLayout.setHorizontal(true);
    middleContentPane.setLayoutManager(fillLayout);
    middleContentPane.setBorder(new MarginBorder(0, 10, 10, 10));

    bottomContentPane = new ContainerFigure();
    outlined.add(bottomContentPane);
    bottomContentPane.setLayoutManager(new FillLayout());
    bottomContentPane.setBorder(new MarginBorder(0, 10, 10, 10));

    return outer;
  }

  //public IFigure getContentPane()
  //{
  //  return contentPane;
  //}   
  public PartReferenceSectionEditPart getPartReferenceSectionEditPart()
  {
    PartReferenceSectionEditPart result = null;
    for (Iterator i = getChildren().iterator(); i.hasNext();)
    {
      EditPart editPart = (EditPart)i.next();
      if (editPart.getModel() == SELECTED_EDIT_PART_REFERENCE)
      {
        result = (PartReferenceSectionEditPart)editPart;
        break;
      }
    }
    return result;
  }

  public GroupEditPart getGroupEditPart(int type)
  {
    GroupEditPart result = null;
    for (Iterator i = getChildren().iterator(); i.hasNext();)
    {
      Object o = i.next();
      if (o instanceof GroupEditPart)
      {
        GroupEditPart groupEditPart = (GroupEditPart)o;
        if (groupEditPart.getType() == type)
        {
          result = groupEditPart;
          break;
        }
      }
    }
    return result;
  }

  public List getModelChildren()
  {
    List list = new ArrayList();
    list.add(SELECTED_EDIT_PART_REFERENCE);
    list.addAll(super.getModelChildren());
    return list;
  }

  public void handleOpenRequest(EditPart editPart)
  {
    GroupEditPart groupEditPart = null;
    if (editPart.getModel() instanceof Import)
    {
      Import theImport = (Import)editPart.getModel();
      Definition definition = (Definition)getModel();
      OpenOnSelectionHelper helper = new OpenOnSelectionHelper(definition);
      helper.openEditor(theImport);
    }
    else
    {
      for (EditPart parent = (EditPart)editPart.getParent(); parent != null; parent = parent.getParent())
      {
        if (parent instanceof GroupEditPart)
        {
          groupEditPart = (GroupEditPart)parent;
          break;
        }
      }

      if (groupEditPart != null)
      {
        GroupEditPart nextGroupEditPart = groupEditPart.getNext();
        if (nextGroupEditPart != null)
        {
          if (nextGroupEditPart.inputConnection instanceof TreeNodeEditPart)
          {
            ((TreeNodeEditPart) (nextGroupEditPart.inputConnection)).setExpanded(true);
          }
        }
        GroupEditPart prevGroupEditPart = groupEditPart.getPrevious();
        if (prevGroupEditPart != null)
        {
          if (prevGroupEditPart.outputConnection instanceof TreeNodeEditPart)
          {
            ((TreeNodeEditPart) (prevGroupEditPart.outputConnection)).setExpanded(true);
          }
        }
      }
      updateConnections();
    }
  }

  public void selectionChanged(SelectionChangedEvent event)
  {
    updateConnections();
  }

  public void updateConnections()
  {
    // todo         
    if (isActive())
    {
      try
      {
        final AbstractGraphicalEditPart selectedEditPart = getSelectedEditPart(getViewer().getSelection());
        GroupEditPart group = getContainingGroup(selectedEditPart);
        if (group != null && group.getConnectionManager() != null)
        {
          group.getConnectionManager().setSelectedModelObject(selectedEditPart.getModel());
        }
        else if (!isAncestor(getPartReferenceSectionEditPart(), selectedEditPart))
        {
          group = getGroupEditPart(WSDLGroupObject.PORT_TYPES_GROUP);
          group.getConnectionManager().setSelectedModelObject(null);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  public GroupEditPart getContainingGroup(AbstractGraphicalEditPart editPart)
  {
    GroupEditPart result = null;
    for (EditPart parent = editPart.getParent(); parent != null; parent = parent.getParent())
    {
      if (parent instanceof GroupEditPart)
      {
        result = (GroupEditPart)parent;
        break;
      }
    }
    return result;
  }

  public GroupEditPart getNextGroupEditPart(GroupEditPart groupEditPart)
  {
    GroupEditPart result = null;
    GroupEditPart prev = null;
    for (Iterator i = getChildren().iterator(); i.hasNext();)
    {
      Object o = i.next();
      if (o instanceof GroupEditPart)
      {
        GroupEditPart editPart = (GroupEditPart)o;
        if (prev == groupEditPart)
        {
          result = editPart;
          break;
        }
        prev = editPart;
      }
    }
    return result;
  }

  public GroupEditPart getPreviousGroupEditPart(GroupEditPart groupEditPart)
  {
    GroupEditPart prev = null;
    for (Iterator i = getChildren().iterator(); i.hasNext();)
    {
      Object o = i.next();
      if (o instanceof GroupEditPart)
      {
        GroupEditPart editPart = (GroupEditPart)o;
        if (editPart == groupEditPart)
        {
          break;
        }
        prev = editPart;
      }
    }
    return prev;
  }

  protected AbstractGraphicalEditPart getSelectedEditPart(ISelection selection)
  {
    AbstractGraphicalEditPart editPart = null;
    if (selection instanceof IStructuredSelection)
    {
      IStructuredSelection s = (IStructuredSelection)selection;
      Object first = s.getFirstElement();
      if (first instanceof AbstractGraphicalEditPart)
      {
        editPart = (AbstractGraphicalEditPart)first;
      }
    }
    return editPart;
  }

  public void refreshChildren()
  {
    super.refreshChildren();
    updateConnections();
  }

  public boolean isAncestor(EditPart ancestor, EditPart child)
  {
    boolean result = false;
    if (ancestor != null && child != null)
    {
      for (EditPart editPart = child; editPart != null; editPart = editPart.getParent())
      {
        if (editPart == ancestor)
        {
          result = true;
          break;
        }
      }
    }
    return result;
  }
}
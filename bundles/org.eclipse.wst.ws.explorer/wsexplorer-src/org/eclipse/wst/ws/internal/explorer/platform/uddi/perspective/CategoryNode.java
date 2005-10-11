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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import java.util.Enumeration;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.SelectCategoryNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.ToggleCategoryNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIFrameNames;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.CategoryElement;

public class CategoryNode extends Node
{
  public CategoryNode(TreeElement element,NodeManager nodeManager,int nodeDepth)
  {
    super(element,nodeManager,nodeDepth,"uddi/images/category.gif");
    setVisibilityOfChildren(false);
  }

  public final void createChildren()
  {
    Enumeration children = element_.getElements(UDDIModelConstants.REL_SUBCATEGORIES);
    if (children != null)
    {
      while (children.hasMoreElements())
      {
        CategoryElement categoryElement = (CategoryElement)children.nextElement();
        CategoryNode categoryNode = new CategoryNode(categoryElement,nodeManager_,nodeDepth_+1);
        addChild(categoryNode);
        categoryNode.createChildren();
      }
    }
  }

  public final String getNodeName()
  {
    CategoryElement categoryElement = (CategoryElement)element_;
    return categoryElement.getNameForTree();
  }

  protected final String getToggleNodeActionHref()
  {
    return ToggleCategoryNodeAction.getActionLink(getNodeManager().getController().getSessionId(),nodeId_,isOpen_);
  }

  protected final String getLinkActionHref()
  {
    return SelectCategoryNodeAction.getActionLink(getNodeManager().getController().getSessionId(),nodeId_);
  }

  protected final String getToggleNodeActionTarget()
  {
    return UDDIFrameNames.CATEGORIES_WORKAREA;
  }

  protected final String getLinkActionTarget()
  {
    return UDDIFrameNames.CATEGORIES_WORKAREA;
  }

  protected final void initTools()
  {
  }
}

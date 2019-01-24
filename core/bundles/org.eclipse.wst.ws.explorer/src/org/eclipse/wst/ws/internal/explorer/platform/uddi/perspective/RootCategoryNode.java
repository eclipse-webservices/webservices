/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import java.util.Enumeration;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.CategoryElement;

public class RootCategoryNode extends Node
{
  public RootCategoryNode(TreeElement element,NodeManager nodeManager)
  {
    super(element,nodeManager,1,"images/root_main.gif");
    setVisibilityOfChildren(true);
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

  // Root node: no need for toggle action.
  protected final String getToggleNodeActionHref()
  {
    return null;
  }
  
  // Text only node.
  protected final String getLinkActionHref()
  {
    return null;
  }
  
  protected final void initTools()
  {
  }
}

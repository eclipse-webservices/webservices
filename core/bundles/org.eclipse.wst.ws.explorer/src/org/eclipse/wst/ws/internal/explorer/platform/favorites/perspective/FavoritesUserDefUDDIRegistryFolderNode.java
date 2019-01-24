/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective;

import org.eclipse.wst.ws.internal.datamodel.ElementAdapter;
import org.eclipse.wst.ws.internal.datamodel.RelAddEvent;
import org.eclipse.wst.ws.internal.datamodel.RelRemoveEvent;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.actions.SelectFavoritesUserDefUDDIRegistryNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class FavoritesUserDefUDDIRegistryFolderNode extends FavoritesNavigatorFolderNode
{
  public FavoritesUserDefUDDIRegistryFolderNode(TreeElement treeElement, NodeManager nodeManager, int nodeDepth)
  {
    super(treeElement, nodeManager, nodeDepth);
    treeElement.addListener(new ElementAdapter()
    {
      public void relAdded(RelAddEvent event)
      {
        String rel = event.getOutBoundRelName();
        if (rel.equals(FavoritesModelConstants.REL_USER_DEF_UDDI_REGISTRY_NODE))
        {
          TreeElement regElement = (TreeElement)event.getParentElement();
          String imagePath = "favorites/images/uddi_registry_node.gif";
          FavoritesUserDefUDDIRegistryNode favUserDefUDDIRegistryNode = new FavoritesUserDefUDDIRegistryNode(regElement, nodeManager_, nodeDepth_ + 1, imagePath);
          addChild(favUserDefUDDIRegistryNode);
        }
      }

      public void relRemoved(RelRemoveEvent event)
      {
        TreeElement childElement = null;
        if (event.getInBoundRelName().equals(FavoritesModelConstants.REL_USER_DEF_UDDI_REGISTRY_NODE))
        {
          childElement = (TreeElement)event.getInboundElement();
        }
        if (event.getOutBoundRelName().equals(FavoritesModelConstants.REL_USER_DEF_UDDI_REGISTRY_NODE))
        {
          childElement = (TreeElement)event.getOutBoundElement();
        }
        removeChildNode(childElement);
      }
    });
  }

  protected void initTools()
  {
    FavoritesPerspective favPerspective = nodeManager_.getController().getFavoritesPerspective();
    new ListFavoriteUserDefUDDIRegistryTool(toolManager_, favPerspective.getMessage("ALT_LIST_FAVORITE_USER_DEF_UDDI_REGISTRY"));
  }

  protected String getLinkActionHref() {
      return SelectFavoritesUserDefUDDIRegistryNodeAction.getActionLink(nodeId_, false);
  }


}

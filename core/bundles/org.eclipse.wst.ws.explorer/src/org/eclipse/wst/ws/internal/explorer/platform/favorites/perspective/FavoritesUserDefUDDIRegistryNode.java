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

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class FavoritesUserDefUDDIRegistryNode extends FavoritesNavigatorNode
{
  public FavoritesUserDefUDDIRegistryNode(TreeElement treeElement, NodeManager nodeManager, int nodeDepth, String imagePath)
  {
    super(treeElement, nodeManager, nodeDepth, imagePath);
  }

  protected void initTools()
  {
    FavoritesPerspective favPerspective = nodeManager_.getController().getFavoritesPerspective();
    new FavoritesUserDefUDDIRegistryDetailsTool(toolManager_, favPerspective.getMessage("ALT_FAVORITES_USER_DEF_UDDI_REGISTRY_DETAILS"));
    new AddToUDDIPerspectiveTool(toolManager_, favPerspective.getMessage("ALT_ADD_TO_UDDI_PERSPECTIVE"));
  }
}

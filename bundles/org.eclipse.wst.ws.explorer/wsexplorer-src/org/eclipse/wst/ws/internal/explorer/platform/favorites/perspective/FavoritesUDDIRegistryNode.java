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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class FavoritesUDDIRegistryNode extends FavoritesNavigatorNode {
    public FavoritesUDDIRegistryNode(TreeElement treeElement, NodeManager nodeManager, int nodeDepth,String imagePath) {
        super(treeElement, nodeManager, nodeDepth, imagePath);
    }

    protected void initTools() {
        FavoritesPerspective favPerspective = nodeManager_.getController().getFavoritesPerspective();
        new FavoritesUDDIRegistryDetailsTool(toolManager_, favPerspective.getMessage("ALT_FAVORITES_UDDI_REGISTRY_DETAILS"));
        new AddToUDDIPerspectiveTool(toolManager_, favPerspective.getMessage("ALT_ADD_TO_UDDI_PERSPECTIVE"));
    }
}

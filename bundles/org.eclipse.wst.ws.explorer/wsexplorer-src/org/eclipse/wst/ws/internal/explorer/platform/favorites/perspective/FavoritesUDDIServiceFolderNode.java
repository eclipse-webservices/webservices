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

import org.eclipse.wst.ws.internal.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class FavoritesUDDIServiceFolderNode extends FavoritesNavigatorFolderNode {
    public FavoritesUDDIServiceFolderNode(TreeElement treeElement, NodeManager nodeManager, int nodeDepth) {
        super(treeElement, nodeManager, nodeDepth);
        treeElement.addListener(new ElementAdapter() {
                                                   public void relAdded(RelAddEvent event) {
                                                       String rel = event.getOutBoundRelName();
                                                       if (rel.equals(FavoritesModelConstants.REL_UDDI_SERVICE_NODE)) {
                                                           FavoritesUDDIServiceNode favoritesUDDIServiceNode = new FavoritesUDDIServiceNode((TreeElement)event.getParentElement(), nodeManager_, nodeDepth_ + 1);
                                                           addChild(favoritesUDDIServiceNode);
                                                       }
                                                   }

                                                   public void relRemoved(RelRemoveEvent event) {
                                                       TreeElement childElement = null;
                                                       if (event.getInBoundRelName().equals(FavoritesModelConstants.REL_UDDI_SERVICE_NODE)) {
                                                           childElement = (TreeElement)event.getInboundElement();
                                                       }
                                                       if (event.getOutBoundRelName().equals(FavoritesModelConstants.REL_UDDI_SERVICE_NODE)) {
                                                           childElement = (TreeElement)event.getOutBoundElement();
                                                       }
                                                       removeChildNode(childElement);
                                                   }
                                               });
    }

    protected void initTools() {
        FavoritesPerspective favPerspective = nodeManager_.getController().getFavoritesPerspective();
        new ListFavoriteUDDIServiceTool(toolManager_, favPerspective.getMessage("ALT_LIST_FAVORITE_UDDI_SERVICE"));
    }
}

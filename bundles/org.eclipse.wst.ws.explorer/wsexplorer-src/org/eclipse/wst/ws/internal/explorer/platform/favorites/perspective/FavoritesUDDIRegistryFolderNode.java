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
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

import java.io.*;

public class FavoritesUDDIRegistryFolderNode extends FavoritesNavigatorFolderNode {
    public FavoritesUDDIRegistryFolderNode(TreeElement treeElement, NodeManager nodeManager, int nodeDepth) {
        super(treeElement, nodeManager, nodeDepth);
        treeElement.addListener(new ElementAdapter() {
                                                   public void relAdded(RelAddEvent event) {
                                                       String rel = event.getOutBoundRelName();
                                                       if (rel.equals(FavoritesModelConstants.REL_UDDI_REGISTRY_NODE)) {
                                                           TreeElement regElement = (TreeElement)event.getParentElement();
                                                           String imagePath;
                                                           StringBuffer categoriesDirectory = new StringBuffer();
                                                           FavoritesUDDIRegistryFolderElement.formCategoriesDirectory(categoriesDirectory,nodeManager_.getController().getServletEngineStateLocation(),regElement.getName());
                                                           File categoryDirectoryFile = new File(categoriesDirectory.toString());
                                                           String[] categoryFiles = categoryDirectoryFile.list();
                                                           if (categoryFiles != null && categoryFiles.length > 0)
                                                             imagePath = "favorites/images/uddi_registry_cat_node.gif";
                                                           else
                                                             imagePath = "favorites/images/uddi_registry_node.gif";
                                                           FavoritesUDDIRegistryNode favoritesUDDIRegistryNode = new FavoritesUDDIRegistryNode(regElement, nodeManager_, nodeDepth_ + 1, imagePath);
                                                           addChild(favoritesUDDIRegistryNode);
                                                       }
                                                   }

                                                   public void relRemoved(RelRemoveEvent event) {
                                                       TreeElement childElement = null;
                                                       if (event.getInBoundRelName().equals(FavoritesModelConstants.REL_UDDI_REGISTRY_NODE)) {
                                                           childElement = (TreeElement)event.getInboundElement();
                                                       }
                                                       if (event.getOutBoundRelName().equals(FavoritesModelConstants.REL_UDDI_REGISTRY_NODE)) {
                                                           childElement = (TreeElement)event.getOutBoundElement();
                                                       }
                                                       removeChildNode(childElement);
                                                   }
                                               });
    }

    protected void initTools() {
        FavoritesPerspective favPerspective = nodeManager_.getController().getFavoritesPerspective();
        new ListFavoriteUDDIRegistryTool(toolManager_, favPerspective.getMessage("ALT_LIST_FAVORITE_UDDI_REGISTRY"));
    }
}

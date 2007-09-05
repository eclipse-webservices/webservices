/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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

public class FavoritesUDDIServiceInterfaceNode extends FavoritesNavigatorNode {
    public FavoritesUDDIServiceInterfaceNode(TreeElement treeElement, NodeManager nodeManager, int nodeDepth) {
        super(treeElement, nodeManager, nodeDepth, "favorites/images/uddi_service_interface_node.gif");
    }

    protected void initTools() {
        FavoritesPerspective favPerspective = nodeManager_.getController().getFavoritesPerspective();
        new FavoritesUDDIServiceInterfaceDetailsTool(toolManager_, favPerspective.getMessage("ALT_FAVORITES_UDDI_SERVICE_INTERFACE_DETAILS"));
        new AddToUDDIPerspectiveTool(toolManager_, favPerspective.getMessage("ALT_ADD_TO_UDDI_PERSPECTIVE"));
    }
}

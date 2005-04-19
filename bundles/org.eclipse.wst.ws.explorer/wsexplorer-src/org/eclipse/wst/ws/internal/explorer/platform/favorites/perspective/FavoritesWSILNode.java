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

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;

public class FavoritesWSILNode extends FavoritesNavigatorNode {
    public FavoritesWSILNode(TreeElement treeElement, NodeManager nodeManager, int nodeDepth) {
        super(treeElement, nodeManager, nodeDepth, "favorites/images/wsil_node.gif");
    }

    protected void initTools() {
        FavoritesPerspective favPerspective = nodeManager_.getController().getFavoritesPerspective();
        new FavoritesWSILDetailsTool(toolManager_, favPerspective.getMessage("ALT_FAVORITES_WSIL_DETAILS"));
        new AddWSILToWSILPerspectiveTool(toolManager_, favPerspective.getMessage("ALT_ADD_WSIL_TO_WSIL_PERSPECTIVE"));
    }
}

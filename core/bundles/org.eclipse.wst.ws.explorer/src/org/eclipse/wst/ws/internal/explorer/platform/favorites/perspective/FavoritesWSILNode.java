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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

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

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
import org.eclipse.wst.ws.internal.explorer.platform.favorites.actions.FavoritesToggleNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.actions.SelectFavoritesNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FolderNode;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public abstract class FavoritesNavigatorFolderNode extends FolderNode {

    public FavoritesNavigatorFolderNode(TreeElement treeElement, NodeManager nodeManager, int nodeDepth) {
        super(treeElement, nodeManager, nodeDepth);
    }

    protected String getToggleNodeActionHref() {
        return FavoritesToggleNodeAction.getActionLink(nodeId_,isOpen_);
    }

    protected String getLinkActionHref() {
        return SelectFavoritesNodeAction.getActionLink(nodeId_, false);
    }

}

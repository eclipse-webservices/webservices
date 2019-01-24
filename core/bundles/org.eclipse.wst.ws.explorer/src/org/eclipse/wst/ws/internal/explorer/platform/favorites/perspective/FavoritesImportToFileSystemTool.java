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

import org.eclipse.wst.ws.internal.explorer.platform.actions.ImportToFileSystemAction;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.actions.SelectFavoritesToolAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ImportToFileSystemTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;

public class FavoritesImportToFileSystemTool extends ImportToFileSystemTool {

    public FavoritesImportToFileSystemTool(ToolManager toolManager, String alt) {
        super(toolManager, alt);
    }

    public String getSelectToolActionHref(boolean forHistory) {
        Node selectedNode = toolManager_.getNode();
        return SelectFavoritesToolAction.getActionLink(selectedNode.getNodeId(), toolId_, selectedNode.getViewId(), selectedNode.getViewToolId(), forHistory);
    }

    public String getActionLink() {
        Node selectedNode = toolManager_.getNode();
        return ImportToFileSystemAction.getActionLink(selectedNode.getNodeId(), toolId_, selectedNode.getViewId(), selectedNode.getViewToolId(),"favorites/actions/FavoritesImportToFileSystemActionJSP.jsp");
    }

}

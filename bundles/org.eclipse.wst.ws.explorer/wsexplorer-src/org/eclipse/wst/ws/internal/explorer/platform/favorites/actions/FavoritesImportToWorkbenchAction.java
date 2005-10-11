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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ImportToFileSystemAction;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ImportToWorkbenchAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class FavoritesImportToWorkbenchAction extends ImportToWorkbenchAction {
    public FavoritesImportToWorkbenchAction(Controller controller) {
        super(controller);
    }

    public FormTool getSelectedFormTool() {
        FavoritesPerspective favoritesPerspective = controller_.getFavoritesPerspective();
        return (FormTool)favoritesPerspective.getNodeManager().getSelectedNode().getToolManager().getSelectedTool();
    }

    public ImportToFileSystemAction newImportToFileSystemAction() {
        FavoritesImportToFileSystemAction action = new FavoritesImportToFileSystemAction(controller_);
        Hashtable table = action.getPropertyTable();

        FavoritesPerspective favoritesPerspective = controller_.getFavoritesPerspective();
        NodeManager nodeManager = favoritesPerspective.getNodeManager();
        Node selectedNode = nodeManager.getSelectedNode();

        table.put(ActionInputs.NODEID, String.valueOf(selectedNode.getNodeId()));
        table.put(ActionInputs.VIEWID, String.valueOf(selectedNode.getViewId()));

        return action;
    }

    public final String getStatusContentVar()
    {
      return controller_.getFavoritesPerspective().getStatusContentVar();
    }

    public final String getStatusContentPage()
    {
      return controller_.getFavoritesPerspective().getStatusContentPage();
    }
}

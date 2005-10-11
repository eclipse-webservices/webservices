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

import org.eclipse.wst.ws.internal.explorer.platform.actions.ToggleNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ToolTypes;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;

public class FavoritesToggleNodeAction extends ToggleNodeAction {
    public FavoritesToggleNodeAction(Controller controller) {
        super(controller, controller.getFavoritesPerspective().getNodeManager());
    }

    public static String getActionLink(int nodeId, boolean open) {
        StringBuffer actionLink = new StringBuffer("favorites/actions/FavoritesToggleNodeActionJSP.jsp?");
        actionLink.append(ActionInputs.NODEID);
        actionLink.append('=');
        actionLink.append(nodeId);
        actionLink.append('&');
        actionLink.append(ActionInputs.OPEN);
        actionLink.append('=');
        if (open)
            actionLink.append(ActionInputs.OPEN_NODE);
        else
            actionLink.append(ActionInputs.CLOSE_NODE);
        return actionLink.toString();
    }

    public final String getActionLinkForHistory() {
        ToolManager toolManager = toggledNode_.getToolManager();
        Tool selectedTool = toolManager.getSelectedTool();
        int nodeId = toggledNode_.getNodeId();
        int toolId = selectedTool.getToolId();
        int viewId = toggledNode_.getViewId();
        int viewToolId = toggledNode_.getViewToolId();
        if (selectedTool.getToolType() != ToolTypes.ACTION)
            return SelectFavoritesToolAction.getActionLink(nodeId, toolId, viewId, viewToolId, true);
        else
            return SelectFavoritesNodeAction.getActionLink(nodeId, true);
    }

  public final String getTreeContentVar()
  {
    return controller_.getFavoritesPerspective().getTreeContentVar();
  }

  public final String getTreeContentPage()
  {
    return controller_.getFavoritesPerspective().getTreeContentPage();
  }

  public final String getPropertiesContainerVar()
  {
    return controller_.getFavoritesPerspective().getPropertiesContainerVar();
  }

  public final String getPropertiesContainerPage()
  {
    return controller_.getFavoritesPerspective().getPropertiesContainerPage();
  }
  
  public final int getPerspectiveId()
  {
    return ActionInputs.PERSPECTIVE_FAVORITES;
  }
}

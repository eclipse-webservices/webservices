/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.SelectNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ToolTypes;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;

public class SelectFavoritesNodeAction extends SelectNodeAction
{
    public SelectFavoritesNodeAction(Controller controller)
    {
        super(controller, controller.getFavoritesPerspective().getNodeManager());
    }

    public static String getActionLink(int nodeID, boolean keepHistory)
    {
        StringBuffer actionLink = new StringBuffer("favorites/actions/SelectFavoritesNodeActionJSP.jsp?");
        actionLink.append(ActionInputs.NODEID);
        actionLink.append('=');
        actionLink.append(nodeID);
        if (keepHistory) {
            actionLink.append('&');
            actionLink.append(ActionInputs.ISHISTORY);
            actionLink.append("=1");
        }
        return actionLink.toString();
    }

    protected String getActionLinkForHistory()
    {
        Node selectedNode = getSelectedNode();
        Tool selectedTool = selectedNode.getToolManager().getSelectedTool();
        if (selectedTool == null)
            return null;
        else if (selectedTool.getToolType() != ToolTypes.ACTION)
            return SelectFavoritesToolAction.getActionLink(selectedNode.getNodeId(), selectedTool.getToolId(), selectedNode.getViewId(), selectedNode.getViewToolId(), true);
        else
            return getActionLink(selectedNode.getNodeId(), true);
    }

    public final String getTreeContentVar() {
        return controller_.getFavoritesPerspective().getTreeContentVar();
    }

    public final String getTreeContentPage() {
        return controller_.getFavoritesPerspective().getTreeContentPage();
    }

    public final String getPropertiesContainerVar() {
        return controller_.getFavoritesPerspective().getPropertiesContainerVar();
    }

    public final String getPropertiesContainerPage() {
        return controller_.getFavoritesPerspective().getPropertiesContainerPage();
    }
    
    public final int getPerspectiveId()
    {
      return ActionInputs.PERSPECTIVE_FAVORITES;
    }
}

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

import org.eclipse.wst.ws.internal.explorer.platform.actions.SelectNodeToolAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;

public class SelectFavoritesToolAction extends SelectNodeToolAction
{
    public SelectFavoritesToolAction(Controller controller)
    {
        super(controller, controller.getFavoritesPerspective().getNodeManager());
    }

    public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID, boolean keepHistory)
    {
        StringBuffer actionLink = new StringBuffer("favorites/actions/SelectFavoritesToolActionJSP.jsp?");
        actionLink.append(ActionInputs.NODEID);
        actionLink.append('=');
        actionLink.append(nodeID);
        actionLink.append('&');
        actionLink.append(ActionInputs.TOOLID);
        actionLink.append('=');
        actionLink.append(toolID);
        if (viewID != ActionInputs.VIEWID_DEFAULT) {
            actionLink.append('&');
            actionLink.append(ActionInputs.VIEWID);
            actionLink.append('=');
            actionLink.append(viewID);
            actionLink.append('&');
            actionLink.append(ActionInputs.VIEWTOOLID);
            actionLink.append('=');
            actionLink.append(viewToolID);
        }
        if (keepHistory) {
            actionLink.append('&');
            actionLink.append(ActionInputs.ISHISTORY);
            actionLink.append("=1");
        }
        return actionLink.toString();
    }

    protected String getActionLinkForHistory()
    {
        if (getSelectedTool().getToolType() != ToolTypes.ACTION) {
            Node selectedNode = getSelectedNode();
            return getActionLink(selectedNode.getNodeId(), getSelectedTool().getToolId(), selectedNode.getViewId(), selectedNode.getViewToolId(), true);
        }
        else
            return null;
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

    public final String getPropertiesContentVar() {
        return controller_.getFavoritesPerspective().getPropertiesContentVar();
    }

    public final String getPropertiesContentPage() {
        return controller_.getFavoritesPerspective().getPropertiesContentPage();
    }
    
    public final int getPerspectiveId()
    {
      return ActionInputs.PERSPECTIVE_FAVORITES;
    }
}

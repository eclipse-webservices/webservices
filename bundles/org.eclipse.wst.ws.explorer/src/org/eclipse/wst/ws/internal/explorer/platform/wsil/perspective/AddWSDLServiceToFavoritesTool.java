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

package org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.ActionTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.AddToFavoritesAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.SelectWSILToolAction;

public class AddWSDLServiceToFavoritesTool extends ActionTool {
    public AddWSDLServiceToFavoritesTool(ToolManager toolManager, String alt) {
        super(toolManager, "images/favorites_enabled.gif", "images/favorites_highlighted.gif", alt);
    }

    public String getSelectToolActionHref(boolean forHistory) {
        Node selectedNode = toolManager_.getNode();
        return SelectWSILToolAction.getActionLink(selectedNode.getNodeId(), selectedNode.getToolManager().getSelectedToolId(), selectedNode.getViewId(), toolId_, forHistory);
    }

    public String getActionLink() {
        Node selectedNode = toolManager_.getNode();
        return AddToFavoritesAction.getActionLink(selectedNode.getNodeId(), selectedNode.getToolManager().getSelectedToolId(), selectedNode.getViewId(), toolId_);
    }

}

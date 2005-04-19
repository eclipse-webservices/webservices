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

package org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.ActionTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.AddServiceToUDDIPerspectiveAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.SelectWSILToolAction;

public class AddServiceToUDDIPerspectiveTool extends ActionTool {
    public AddServiceToUDDIPerspectiveTool(ToolManager toolManager, String alt) {
        super(toolManager, "wsil/images/add_to_UDDI_perspective_enabled.gif", "wsil/images/add_to_UDDI_perspective_highlighted.gif", alt);
    }

    public String getSelectToolActionHref(boolean forHistory) {
        Node selectedNode = toolManager_.getNode();
        return SelectWSILToolAction.getActionLink(selectedNode.getNodeId(), selectedNode.getToolManager().getSelectedToolId(), selectedNode.getViewId(), toolId_, forHistory);
    }

    public String getActionLink() {
        Node selectedNode = toolManager_.getNode();
        return AddServiceToUDDIPerspectiveAction.getActionLink(selectedNode.getNodeId(), selectedNode.getToolManager().getSelectedToolId(), selectedNode.getViewId(), toolId_);
    }

}

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

package org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.actions.ProxyLoadPageAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ViewTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.SelectWSILToolAction;

public class ListUDDIBusinessTool extends ViewTool {
    public ListUDDIBusinessTool(ToolManager toolManager, String alt) {
        super(toolManager, "wsil/images/list_business_enabled.gif", "wsil/images/list_business_highlighted.gif", alt);
    }

    public String getSelectToolActionHref(boolean forHistory) {
        Node selectedNode = toolManager_.getNode();
        return SelectWSILToolAction.getActionLink(selectedNode.getNodeId(), toolId_, selectedNode.getViewId(), selectedNode.getViewToolId(), forHistory);
    }

    protected void addSetDefaultViewTool(ToolManager viewToolManager, int index) {
        new WsilSetDefaultViewTool(viewToolManager,  toolManager_.getNode().getNodeManager().getController().getMessage("ALT_BACK_TO_TOP"));
    }

    protected void addTools(ToolManager viewToolManager, int index) {
        new UddiBusinessDetailsTool(viewToolManager, toolManager_.getNode().getNodeManager().getController().getWSILPerspective().getMessage("ALT_UDDI_LINK_DETAILS"));
        new AddBusinessToUDDIPerspectiveTool(viewToolManager, toolManager_.getNode().getNodeManager().getController().getWSILPerspective().getMessage("ALT_ADD_BUSINESS_TO_UDDI_PERSPECTIVE"));
        new AddBusinessToFavoritesTool(viewToolManager, toolManager_.getNode().getNodeManager().getController().getWSILPerspective().getMessage("ALT_ADD_UDDI_BUSINESS_TO_FAVORITES"));
        new RefreshUDDIBusinessTool(viewToolManager, toolManager_.getNode().getNodeManager().getController().getWSILPerspective().getMessage("ALT_REFRESH_UDDI_BUSINESS"));
    }

    public String getFormLink() {
        return ProxyLoadPageAction.getActionLink("wsil/views/UddiBusinessView.jsp");
    }
}

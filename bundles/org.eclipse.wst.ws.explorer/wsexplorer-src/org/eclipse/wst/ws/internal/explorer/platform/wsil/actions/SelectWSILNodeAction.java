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
package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.SelectNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;

public class SelectWSILNodeAction extends SelectNodeAction
{
    public SelectWSILNodeAction(Controller controller)
    {
        super(controller, controller.getWSILPerspective().getNodeManager());
    }

    public static String getActionLink(int nodeID, boolean keepHistory)
    {
        StringBuffer actionLink = new StringBuffer("wsil/actions/SelectWSILNodeActionJSP.jsp?");
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
        if (selectedTool.getToolType() != ToolTypes.ACTION)
            return SelectWSILToolAction.getActionLink(selectedNode.getNodeId(), selectedTool.getToolId(), selectedNode.getViewId(), selectedNode.getViewToolId(), true);
        else
            return getActionLink(selectedNode.getNodeId(), true);
    }

    public final String getTreeContentVar() {
        return "wsilNavigatorContent";
    }

    public final String getTreeContentPage() {
        return "wsil/wsil_navigator_content.jsp";
    }

    public final String getPropertiesContainerVar() {
        return "wsilPropertiesContainer";
    }

    public final String getPropertiesContainerPage() {
        return "wsil/wsil_properties_container.jsp";
    }
    
    public final int getPerspectiveId()
    {
      return ActionInputs.PERSPECTIVE_WSIL;
    }
}

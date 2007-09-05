/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.SelectNodeToolAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;

public class SelectWSDLPropertiesToolAction extends SelectNodeToolAction
{

    public SelectWSDLPropertiesToolAction(Controller controller)
    {
        super(controller, controller.getWSDLPerspective().getNodeManager());
    }

    public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID, boolean keepHistory)
    {
        StringBuffer actionLink = new StringBuffer("wsdl/actions/SelectWSDLPropertiesToolActionJSP.jsp?");
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
        int nodeId = selectedNode_.getNodeId();
        int toolId = selectedTool_.getToolId();
        int viewId = selectedNode_.getViewId();
        int viewToolId = selectedNode_.getViewToolId();
        return getActionLink(nodeId,toolId,viewId,viewToolId,true);
    }

    public final String getTreeContentVar() {
        return "wsdlNavigatorContent";
    }

    public final String getTreeContentPage() {
        return "wsdl/wsdl_navigator_content.jsp";
    }

    public final String getPropertiesContainerVar() {
        return "wsdlPropertiesContainer";
    }

    public final String getPropertiesContainerPage() {
        return "wsdl/wsdl_properties_container.jsp";
    }

    public final String getPropertiesContentVar() {
        return "wsdlPropertiesContent";
    }

    public final String getPropertiesContentPage() {
        return "wsdl/wsdl_properties_content.jsp";
    }
    
    public final int getPerspectiveId()
    {
      return ActionInputs.PERSPECTIVE_WSDL;
    }
}

/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.SelectNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ToolTypes;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;

public class SelectWSDLNavigatorNodeAction extends SelectNodeAction
{

    public SelectWSDLNavigatorNodeAction(Controller controller)
    {
        super(controller,controller.getWSDLPerspective().getNodeManager());
    }

    public static String getActionLink(int nodeID, boolean forHistory)
    {
        StringBuffer actionLink = new StringBuffer("wsdl/actions/SelectWSDLNavigatorNodeActionJSP.jsp?");
        actionLink.append(ActionInputs.NODEID).append('=').append(nodeID);
        if (forHistory)
            actionLink.append('&').append(ActionInputs.ISHISTORY).append("=1");
        return actionLink.toString();
    }

    protected String getActionLinkForHistory()
    {
      ToolManager toolManager = selectedNode_.getToolManager();
      Tool selectedTool = toolManager.getSelectedTool();
      int nodeId = selectedNode_.getNodeId();
      
      if (selectedTool.getToolType() != ToolTypes.ACTION)
        return selectedTool.getSelectToolActionHref(true);
      else
        return getActionLink(nodeId,true);
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
    
    public final int getPerspectiveId()
    {
      return ActionInputs.PERSPECTIVE_WSDL;
    }
}

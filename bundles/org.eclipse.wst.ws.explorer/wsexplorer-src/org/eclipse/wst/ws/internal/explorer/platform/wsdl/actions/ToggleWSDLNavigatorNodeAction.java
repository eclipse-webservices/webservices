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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.ToggleNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ToolTypes;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;

public class ToggleWSDLNavigatorNodeAction extends ToggleNodeAction
{
  public ToggleWSDLNavigatorNodeAction(Controller controller)
  {
    super(controller,controller.getWSDLPerspective().getNodeManager());
  }

  // uddi/actions/ToggleNavigatorNodeAction.jsp?nodeId=...&open=...
  public static String getActionLink(int nodeId,boolean open)
  {
    StringBuffer actionLink = new StringBuffer("wsdl/actions/ToggleWSDLNavigatorNodeActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    actionLink.append('&').append(ActionInputs.OPEN).append('=').append(open?ActionInputs.OPEN_NODE:ActionInputs.CLOSE_NODE);
    return actionLink.toString();
  }

  // This should be called only to select the toggled node when, at the time of
  // the collapse event, a descendant node was selected.
  public final String getActionLinkForHistory()
  {
    ToolManager toolManager = toggledNode_.getToolManager();
    Tool selectedTool = toolManager.getSelectedTool();
    int nodeId = toggledNode_.getNodeId();
    int toolId = selectedTool.getToolId();
    int viewId = toggledNode_.getViewId();
    int viewToolId = toggledNode_.getViewToolId();
    if (selectedTool.getToolType() != ToolTypes.ACTION)
      return SelectWSDLPropertiesToolAction.getActionLink(nodeId,toolId,viewId,viewToolId,true);
    else
      return SelectWSDLNavigatorNodeAction.getActionLink(nodeId,true);
  }

    public final String getTreeContentVar()
    {
      return "wsdlNavigatorContent";
    }

    public final String getTreeContentPage()
    {
      return "wsdl/wsdl_navigator_content.jsp";
    }

    public final String getPropertiesContainerVar()
    {
      return "wsdlPropertiesContainer";
    }

    public final String getPropertiesContainerPage()
    {
      return "wsdl/wsdl_properties_container.jsp";
    }
    
    public final int getPerspectiveId()
    {
      return ActionInputs.PERSPECTIVE_WSDL;
    }
}

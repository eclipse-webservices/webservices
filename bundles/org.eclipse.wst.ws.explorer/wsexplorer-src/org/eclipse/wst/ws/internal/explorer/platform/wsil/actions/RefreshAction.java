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

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.actions.Action;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.ListUDDIBusinessTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.ListUDDIServicesTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WsilNode;

public abstract class RefreshAction extends MultipleLinkAction
{
  protected NodeManager nodeManager_;

  public RefreshAction(Controller controller)
  {
    super(controller);
    nodeManager_ = controller.getWSILPerspective().getNodeManager();
  }

  public static Action newAction(HttpServletRequest request, Controller controller)
  {
    NodeManager nodeManager = controller.getWSILPerspective().getNodeManager();
    String nodeIDString = request.getParameter(ActionInputs.NODEID);
    String viewIDString = request.getParameter(ActionInputs.VIEWID);
    int nodeID;
    try
    {
      nodeID = Integer.parseInt(nodeIDString);
    }
    catch (NumberFormatException nfe)
    {
      nodeID = nodeManager.getSelectedNodeId();
    }
    Node selectedNode = controller.getWSILPerspective().getNodeManager().getNode(nodeID);
    if (selectedNode == null || !(selectedNode instanceof WsilNode))
      return new NullAction();
    else
    {
      Tool selectedTool = selectedNode.getToolManager().getSelectedTool();
      RefreshAction action;
      if (viewIDString == null)
        action = new RefreshWSILAction(controller);
      else if (selectedTool instanceof ListUDDIServicesTool)
        action = new RefreshUDDIServiceAction(controller);
      else if (selectedTool instanceof ListUDDIBusinessTool)
        action = new RefreshUDDIBusinessAction(controller);
      else
        return new NullAction();
      action.getPropertyTable().put(ActionInputs.NODEID, String.valueOf(nodeID));
      return action;
    }
  }

  public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID)
  {
    StringBuffer actionLink = new StringBuffer("wsil/actions/RefreshActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID);
    actionLink.append('=');
    actionLink.append(nodeID);
    actionLink.append('&');
    actionLink.append(ActionInputs.TOOLID);
    actionLink.append('=');
    actionLink.append(toolID);
    actionLink.append('&');
    actionLink.append(ActionInputs.VIEWID);
    actionLink.append('=');
    actionLink.append(viewID);
    actionLink.append('&');
    actionLink.append(ActionInputs.VIEWTOOLID);
    actionLink.append('=');
    actionLink.append(viewToolID);
    return actionLink.toString();
  }

  public static String getBaseActionLink()
  {
    return "wsil/actions/RefreshActionJSP.jsp";
  }
}

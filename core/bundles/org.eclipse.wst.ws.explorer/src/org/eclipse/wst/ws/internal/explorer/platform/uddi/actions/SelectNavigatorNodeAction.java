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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.SelectNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ToolTypes;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;

public class SelectNavigatorNodeAction extends SelectNodeAction
{
  public SelectNavigatorNodeAction(Controller controller)
  {
    super(controller,controller.getUDDIPerspective().getNavigatorManager());
  }

  // uddi/actions/SelectNavigatorNodeAction.jsp?nodeId=...<&isHistory=1>
  public static String getActionLink(int nodeId,boolean forHistory)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/SelectNavigatorNodeActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    if (forHistory)
      actionLink.append('&').append(ActionInputs.ISHISTORY).append("=1");
    return actionLink.toString();
  }

  public String getActionLinkForHistory()
  {
    ToolManager toolManager = selectedNode_.getToolManager();
    Tool selectedTool = toolManager.getSelectedTool();
    int nodeId = selectedNode_.getNodeId();
    selectedTool.getToolId();
    selectedNode_.getViewId();
    selectedNode_.getViewToolId();
    if (selectedTool.getToolType() != ToolTypes.ACTION)
      return selectedTool.getSelectToolActionHref(true);
    else
      return getActionLink(nodeId,true);
  }

  public final String getTreeContentVar()
  {
    return "navigatorContent";
  }

  public final String getTreeContentPage()
  {
    return "uddi/navigator_content.jsp";
  }

  public final String getPropertiesContainerVar()
  {
    return "propertiesContainer";
  }

  public final String getPropertiesContainerPage()
  {
    return "uddi/properties_container.jsp";
  }
  
  public final int getPerspectiveId()
  {
    return ActionInputs.PERSPECTIVE_UDDI;
  }
}

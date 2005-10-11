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
package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.SelectNodeToolAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;

public class SelectPropertiesToolAction extends SelectNodeToolAction
{
  public SelectPropertiesToolAction(Controller controller)
  {
    super(controller, controller.getUDDIPerspective().getNavigatorManager());
  }

  // /uddi/actions/SelectPropertiesToolAction.jsp?nodeId=...&toolId=...<&viewId=...&viewToolId=...><&isHistory=1>
  public static final String getActionLink(int nodeId,int toolId,int viewId,int viewToolId,boolean forHistory)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/SelectPropertiesToolActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    actionLink.append('&').append(ActionInputs.TOOLID).append('=').append(toolId);
    if (viewId != ActionInputs.VIEWID_DEFAULT)
    {
      actionLink.append('&').append(ActionInputs.VIEWID).append('=').append(viewId);
      actionLink.append('&').append(ActionInputs.VIEWTOOLID).append('=').append(viewToolId);
    }
    if (forHistory)
      actionLink.append('&').append(ActionInputs.ISHISTORY).append("=1");
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

  public final String getPropertiesContentVar()
  {
    return "propertiesContent";
  }

  public final String getPropertiesContentPage()
  {
    return "uddi/properties_content.jsp";
  }
  
  public final int getPerspectiveId()
  {
    return ActionInputs.PERSPECTIVE_UDDI;
  }
}
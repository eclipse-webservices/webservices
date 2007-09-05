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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.ActionTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.RefreshUDDINodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.SelectPropertiesToolAction;

public class RefreshTool extends ActionTool
{
  public RefreshTool(ToolManager toolManager,String alt)
  {
    super(toolManager,"images/refresh_enabled.gif","images/refresh_highlighted.gif",alt);
  }

  public String getSelectToolActionHref(boolean forHistory)
  {
    Node node = toolManager_.getNode();
    return SelectPropertiesToolAction.getActionLink(node.getNodeId(),toolId_,node.getViewId(),node.getViewToolId(),forHistory);
  }

  public String getActionLink()
  {
    return RefreshUDDINodeAction.getActionLink(toolManager_.getNode().getNodeId());
  }
}

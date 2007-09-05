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

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ViewTool;

public class SubQueryTransferTarget
{
  private int nodeId_;
  private int toolId_;
  private int viewId_;
  private int viewToolId_;
  private String subQueryKey_;
  private NodeManager nodeManager_;
  Hashtable parentQueryData_;

  public SubQueryTransferTarget(Node node,String subQueryKey,Hashtable parentQueryData)
  {
    nodeId_ = node.getNodeId();
    toolId_ = node.getToolManager().getSelectedToolId();
    viewId_ = node.getViewId();
    viewToolId_ = node.getViewToolId();
    subQueryKey_ = subQueryKey;
    nodeManager_ = node.getNodeManager();
    parentQueryData_ = parentQueryData;
  }

  public final int getNodeId()
  {
    return nodeId_;
  }

  public final int getToolId()
  {
    return toolId_;
  }

  public final int getViewId()
  {
    return viewId_;
  }

  public final int getViewToolId()
  {
    return viewToolId_;
  }

  public final String getSubQueryKey()
  {
    return subQueryKey_;
  }

  public final FormTool getTargetFormTool()
  {
    Node node = nodeManager_.getNode(nodeId_);
    if (node == null)
      return null;

    Tool tool = node.getToolManager().getTool(toolId_);

    if (viewId_ != ActionInputs.VIEWID_DEFAULT)
    {
      ViewTool viewTool = (ViewTool)tool;
      ToolManager viewToolManager = viewTool.getToolManager(viewId_);
      if (viewToolManager != null)
        tool = viewToolManager.getTool(viewToolId_);
      else
        tool = null;
    }
    return (FormTool)tool;
  }
  
  public final Hashtable getParentQueryData()
  {
    return parentQueryData_;
  }
}

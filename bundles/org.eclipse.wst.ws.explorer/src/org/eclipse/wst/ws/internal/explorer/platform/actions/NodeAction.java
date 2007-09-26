/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.actions;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public abstract class NodeAction extends LinkAction
{
  protected NodeManager nodeManager_;
  private boolean requiresTreeViewRefresh_;
  private boolean requiresNodeSelection_;
  private boolean requiresViewSelection_;
  private boolean requiresStatusUpdate_;

  public NodeAction(Controller controller,NodeManager nodeManager)
  {
    super(controller);
    nodeManager_ = nodeManager;
    requiresTreeViewRefresh_ = false;
    requiresNodeSelection_ = false;
    requiresViewSelection_ = false;
    requiresStatusUpdate_ = false;
  }

  // ...jsp?nodeId=...<&isHistory=1>
  protected boolean processLinkParameters(HttpServletRequest request)
  {
    String nodeIdString = request.getParameter(ActionInputs.NODEID);
    // Perform data validation.
    try
    {
      Integer.parseInt(nodeIdString);
    }
    catch (NumberFormatException e)
    {
      // Validation failed!
      return false;
    }
    propertyTable_.put(ActionInputs.NODEID,nodeIdString);
    return true;
  }

  protected final boolean isStaleNode(int nodeId)
  {
    return (nodeManager_.getNode(nodeId) == null);
  }

  /**
  * Make a node visible. A node is visible when all of its ancestors are expanded.
  * @param Node The node to be made visible.
  * @return boolean Indicator for whether or not the tree structure was changed (i.e. expanded to show the node in question).
  */
  protected final boolean makeNodeVisible(Node node)
  {
    requiresTreeViewRefresh_ = node.getNodeManager().makeNodeVisible(node);
    return requiresTreeViewRefresh_;
  }

  /**
   * Make a node visible. A node is visible when all of its ancestors are expanded.
   * @param Node The node to be made visible.
   * @return boolean Indicator for whether or not the tree structure was changed (i.e. expanded to show the node in question).
   */
   public final void setTreeRefreshNeeded()
   {
     requiresTreeViewRefresh_ = true;
     
   }
  
  /**
  * Select a node with id nodeId.
  * @param int The id of the node to be selected.
  */
  protected final void setSelectedNodeId(int nodeId)
  {
    if (nodeManager_.getSelectedNodeId() != nodeId)
    {
      requiresNodeSelection_ = true;
      nodeManager_.setSelectedNodeId(nodeId);
    }
  }

  protected final void setSelectedViewId(int viewId)
  {
    Node selectedNode = nodeManager_.getSelectedNode();
    if (selectedNode.getViewId() != viewId)
    {
      requiresViewSelection_ = true;
      selectedNode.setViewId(viewId);
    }
  }

  // Determine whether or not the treeview requires reloading as a result of this action.
  // Reloading is required when branches are added/removed.
  public boolean requiresTreeViewRefresh()
  {
    return requiresTreeViewRefresh_;
  }

  // Determine whether or not a new node has been selected as a result of the action.
  public boolean requiresNodeSelection()
  {
    return requiresNodeSelection_;
  }

  // Determine if a new view element is selected.
  public boolean requiresViewSelection()
  {
    return requiresViewSelection_;
  }

  // Determine if any status messages arose from this action.
  public boolean requiresStatusUpdate()
  {
    return requiresStatusUpdate_;
  }

  protected abstract String getActionLinkForHistory();
}

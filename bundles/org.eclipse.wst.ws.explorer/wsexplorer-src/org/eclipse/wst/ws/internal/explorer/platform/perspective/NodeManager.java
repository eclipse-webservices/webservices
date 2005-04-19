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

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import javax.servlet.http.*;
import java.util.*;

public class NodeManager
{
  // paths to icon images
  public static final String PATH_PLUS_NOTLAST = "images/plus_notlast.gif";
  public static final String PATH_PLUS_LAST = "images/plus_last.gif";
  public static final String PATH_MINUS_NOTLAST = "images/minus_notlast.gif";
  public static final String PATH_MINUS_LAST = "images/minus_last.gif";
  public static final String PATH_LINE = "images/line.gif";
  public static final String PATH_LINE_NOTLAST = "images/line_notlast.gif";
  public static final String PATH_LINE_LAST = "images/line_last.gif";
  public static final String PATH_SPACE = "images/space.gif";

  private Controller controller_;
  private Node rootNode_;
  private Hashtable nodeTable_;
  private int focusedNodeId_;
  private int selectedNodeId_;
  private int previousSelectedNodeId_;
  private int maxDepthVisible_;
  private int nextId_;

  public NodeManager(Controller controller)
  {
    controller_ = controller;
    rootNode_ = null;
    nodeTable_ = new Hashtable();
    focusedNodeId_ = -1;
    selectedNodeId_ = -1;
    previousSelectedNodeId_ = -1;
    maxDepthVisible_ = 1;
    nextId_ = 0;
  }

  /**
  * Hook for the controller.
  * @return Controller The perspective controller.
  */
  public Controller getController()
  {
    return controller_;
  }

  /**
  * Sets the root node of the tree.
  * @param rootNode The node being designated as the root node.
  */
  public void setRootNode(Node rootNode)
  {
    rootNode_ = rootNode;
  }

  /**
  * Returns the root node of the tree managed by this NodeManager.
  * @return Node The root node.
  */
  public Node getRootNode()
  {
    return rootNode_;
  }

  /**
  * @param Integer The id of the node being added.
  * @param Node The node being added.
  * @return The nodeId assigned to this node.
  */
  public int addToNodeTable(Node node)
  {
    int assignedNodeId = nextId_;
    nodeTable_.put(String.valueOf(assignedNodeId),node);
    nextId_++;
    return assignedNodeId;
  }

  public void removeFromNodeTable(int nodeId)
  {
    Vector nodesToRemove = new Vector();
    Node node = getNode(nodeId);
    gatherNodesToRemove(nodesToRemove, node);
    for (Iterator it = nodesToRemove.iterator(); it.hasNext();) {
      nodeTable_.remove(String.valueOf(((Node)it.next()).getNodeId()));
    }
  }

  private void gatherNodesToRemove(Vector nodesToRemove, Node node) {
    if (node != null) {
      nodesToRemove.add(node);
      Vector childNodes = node.getChildNodes();
      for (Iterator it = childNodes.iterator(); it.hasNext();) {
        gatherNodesToRemove(nodesToRemove, (Node)it.next());
      }
    }
  }

  /**
  * Returns the depth of the deepest visible node in the tree.
  * @return int
  */
  public int getMaxDepthVisible()
  {
    return maxDepthVisible_;
  }

  /**
  * Updates the depth of the deepest visible node in the tree. Nodes
  * must call this when the visibility of their children changes.
  * (e.g. expand/collapse of a parent node).
  */
  public void updateMaxDepthVisible()
  {
    if (rootNode_ != null)
      maxDepthVisible_ = rootNode_.getMaxDepthVisible();
  }

  /**
  * Returns the next available id for a newly created node.
  * @return int
  */
  public int getNextNodeId()
  {
    return nextId_;
  }

  /**
  * Returns the id of the currently focused node in the tree. A node
  * becomes focused when any of the following events occur:
  * 1) The node's icon or image are selected by a user.
  * 2) The node's expand/collapse icon is clicked.
  * 3) Programmatically set by the programmer.
  * The treeview will always scroll to the current focused node when loaded.
  * @return int The id.
  */
  public int getFocusedNodeId()
  {
    return focusedNodeId_;
  }

  /**
  * Allows modification of the focused node.
  * @param int The id of the node that is in focus.
  */
  public void setFocusedNodeId(int nodeId)
  {
    focusedNodeId_ = nodeId;
  }

  public Node getFocusedNode()
  {
    return getNode(focusedNodeId_);
  }

  /**
  * Returns the id of the currently selected node in the tree.
  * @return int The id.
  */
  public int getSelectedNodeId()
  {
    return selectedNodeId_;
  }

  public Node getSelectedNode()
  {
    return getNode(selectedNodeId_);
  }

  /**
  * Retrieve a reference to a Node in this manager's node table.
  * @return Node The node.
  */
  public Node getNode(int nodeId)
  {
    return (Node)nodeTable_.get(String.valueOf(nodeId));
  }

  /**
  * Allows modification of the selected Node. Note that setting a node to become
  * selected automatically makes it the focused node. However, this is only a one
  * way relationship.
  * @param int The id of the node that is being selected.
  */
  public void setSelectedNodeId(int nodeId)
  {
    previousSelectedNodeId_ = selectedNodeId_;
    selectedNodeId_ = nodeId;
    setFocusedNodeId(selectedNodeId_);
  }

  public int getPreviousSelectedNodeId()
  {
    return previousSelectedNodeId_;
  }

  public Node getPreviousSelectedNode()
  {
    return (Node)nodeTable_.get(String.valueOf(previousSelectedNodeId_));
  }

  /**
  * Render the HTML tree view for this node.
  * @param HttpServletResponse For encoding URLs.
  * @return String The HTML representing the tree view.
  */
  public String renderTreeView(HttpServletResponse response)
  {
    StringBuffer treeView = new StringBuffer();
    rootNode_.renderNode(response,treeView,"",false);
    return treeView.toString();
  }
  
  /**
  * Make the provided node is visible by ensuring that its ancestors are visible.
  * @param Node The node to be made visible.
  * @return boolean Indicator for whether or not the treeview needs refreshing (i.e. an ancestor was not visible).
  */
  public final boolean makeNodeVisible(Node node)
  {
    boolean requiresTreeViewRefresh = false;
    while ((node = node.getParent()) != null)
    {
      if (!node.isOpen())
      {
        requiresTreeViewRefresh = true;
        node.setVisibilityOfChildren(true);
      }
    }
    return requiresTreeViewRefresh;
  }
  
  /**
  * Make the currently selected node visible by ensuring that its ancestors are visible.
  */
  public final boolean makeSelectedNodeVisible()
  {
    return makeNodeVisible(getSelectedNode());
  }
}

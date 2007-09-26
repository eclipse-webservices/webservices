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
package org.eclipse.wst.ws.internal.explorer.platform.actions;

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.NodeIdTransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public abstract class ToggleNodeAction extends NodeAction
{
  protected Node toggledNode_;
  public ToggleNodeAction(Controller controller,NodeManager nodeManager)
  {
    super(controller,nodeManager);
    toggledNode_ = null;
  }

  // Toggle the current node and update the selection iff the currently selected
  // is a related descendant of a collapsed node.
  /**
  * Toggle the current node and update the selection iff the currently selected
  * node is a related descendant of the current node and the current node is
  * now collapsed as a result of this action.
  *
  * @param int The node ID of the node for which this action applies to.
  * @return boolean Indicator of whether or not a selection change was necessary.
  */
  public final boolean performBaseAction(int nodeId)
  {
    toggledNode_ = nodeManager_.getNode(nodeId);
    nodeManager_.setFocusedNodeId(nodeId);
    toggledNode_.setVisibilityOfChildren(!toggledNode_.isOpen());
    Node selectedNode = nodeManager_.getSelectedNode();
    if (selectedNode != null)
    {
      if (!selectedNode.isVisible())
      {
        setSelectedNodeId(nodeId);
        return true;
      }
    }
    return false;
  }

  public boolean run()
  {
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    if (!isStaleNode(nodeId))
    {
      if (performBaseAction(nodeId))
      {
        // Save the selection of the toggle link in the history.
        addToHistory(getPerspectiveId(),getActionLinkForHistory());
      }
      // Do not save the toggle in history.
      return true;
    }
    setStaleBreadCrumb();
    return false;
  }

  public final Node getToggledNode()
  {
    return toggledNode_;
  }

  public ITransformer[] getTransformers()
  {
    return new ITransformer[] {new NodeIdTransformer(controller_)};
  }

  public abstract String getTreeContentVar();
  public abstract String getTreeContentPage();
  public abstract String getPropertiesContainerVar();
  public abstract String getPropertiesContainerPage();
  public abstract int getPerspectiveId();
}

/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
package org.eclipse.wst.ws.internal.explorer.platform.actions;

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.NodeSelectionTransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public abstract class SelectNodeAction extends NodeAction
{
  protected Node selectedNode_;

  public SelectNodeAction(Controller controller,NodeManager nodeManager)
  {
    super(controller,nodeManager);
    selectedNode_ = null;
  }

  public final void performBaseAction(int nodeId)
  {
    setSelectedNodeId(nodeId);
    selectedNode_ = nodeManager_.getSelectedNode();
  }

  public void performExtendedAction()
  {
	 //the extended classes can interject here  
  }
  
  public boolean run()
  {
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    if (!isStaleNode(nodeId))
    {
      // Ensure that the node is visible.
      makeNodeVisible(nodeManager_.getNode(nodeId));
      performBaseAction(nodeId);
      performExtendedAction();
      addToHistory(getPerspectiveId(),getActionLinkForHistory());
      return true;
    }
    setStaleBreadCrumb();
    return false;
  }

  public final Node getSelectedNode()
  {
    return selectedNode_;
  }
  
  public ITransformer[] getTransformers()
  {
    return new ITransformer[] {new NodeSelectionTransformer(controller_)};
  }

  // Selecting a node changes the treeview content and the properties container.
  public abstract String getTreeContentVar();
  public abstract String getTreeContentPage();
  public abstract String getPropertiesContainerVar();
  public abstract String getPropertiesContainerPage();
  public abstract int getPerspectiveId();
}

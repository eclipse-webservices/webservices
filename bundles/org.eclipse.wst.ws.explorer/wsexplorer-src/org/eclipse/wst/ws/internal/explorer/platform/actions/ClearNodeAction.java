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

package org.eclipse.wst.ws.internal.explorer.platform.actions;

import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;

public abstract class ClearNodeAction extends NodeAction
{
  public ClearNodeAction(Controller controller,NodeManager nodeManager)
  {
    super(controller,nodeManager);
  }

  public boolean run()
  {
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    if (!isStaleNode(nodeId))
    {
      Node node = nodeManager_.getNode(nodeId);
      TreeElement element = node.getTreeElement();
      element.disconnectAll();
      // Do not add this to the history.
      MessageQueue messageQueue = controller_.getCurrentPerspective().getMessageQueue();
      messageQueue.addMessage(controller_.getMessage("MSG_INFO_NODE_CLEARED",node.getNodeName()));
      return true;
    }
    return false;
  }

  public final String getActionLinkForHistory()
  {
    return null;
  }

  // Deleting a node from the tree results in changes to the treeview and status contents.
  public abstract String getTreeContentVar();
  public abstract String getTreeContentPage();
  public abstract String getPropertiesContainerVar();
  public abstract String getPropertiesContainerPage();
  public abstract String getStatusContentVar();
  public abstract String getStatusContentPage();
}

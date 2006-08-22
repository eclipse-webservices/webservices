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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.ws.internal.datamodel.Rel;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

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
      //element.disconnectAll();
      List elements2remove = new ArrayList();
      collectElements2Remove(element, elements2remove);
      for (Iterator it = elements2remove.iterator(); it.hasNext();)
      {
        TreeElement e = (TreeElement)it.next();
        e.getModel().removeElement(e);
      }
      nodeManager_.removeFromNodeTable(nodeId);
      // Do not add this to the history.
      MessageQueue messageQueue = controller_.getCurrentPerspective().getMessageQueue();
      messageQueue.addMessage(controller_.getMessage("MSG_INFO_NODE_CLEARED",node.getNodeName()));
      return true;
    }
    return false;
  }

  private void collectElements2Remove(TreeElement element, List elements2remove)
  {
    if (!elements2remove.contains(element))
    {
      elements2remove.add(element);
      Enumeration rels = element.getRels();
      while (rels.hasMoreElements())
      {
        Rel rel = (Rel)rels.nextElement();
        String relName = rel.getName();
        if (!ModelConstants.REL_OWNER.equals(relName))
        {
          Enumeration children = element.getElements(rel.getName());
          while (children.hasMoreElements())
          {
            collectElements2Remove((TreeElement)children.nextElement(), elements2remove);
          }
        }
      }
    }
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

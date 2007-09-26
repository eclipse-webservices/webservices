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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public class RefreshUDDINodesAction extends UDDIPropertiesFormAction
{
  private Vector staleNodes_;

  public RefreshUDDINodesAction(Controller controller)
  {
    super(controller);
    staleNodes_ = new Vector();
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String[] selectedNodeIds = parser.getParameterValues(ActionInputs.NODEID);
    // The client Javascript enforces the rule that at least one item was selected before the form can be submitted.
    propertyTable_.put(UDDIActionInputs.SELECTED_NODEIDS,selectedNodeIds);
    return true;
  }

  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    int collectorNodeId = getSelectedNavigatorNode().getNodeId();
    String[] selectedNodeIds = (String[])propertyTable_.get(UDDIActionInputs.SELECTED_NODEIDS);
    RefreshUDDINodeAction action = new RefreshUDDINodeAction(controller_);
    Hashtable propertyTable = action.getPropertyTable();
    for (int i=0;i<selectedNodeIds.length;i++)
    {
      propertyTable.put(ActionInputs.NODEID,selectedNodeIds[i]);
      if (action.verifyNodeData())
        action.run();
      else
      {
        int nodeId = Integer.parseInt(selectedNodeIds[i]);
        staleNodes_.addElement(navigatorManager.getNode(nodeId));
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_NODE_DATA_VALIDATION_FAILED",navigatorManager.getNode(nodeId).getNodeName()));
      }
    }
    navigatorManager.setSelectedNodeId(collectorNodeId);
    return true;
  }

  public final Vector getStaleNodes()
  {
    return staleNodes_;
  }
}

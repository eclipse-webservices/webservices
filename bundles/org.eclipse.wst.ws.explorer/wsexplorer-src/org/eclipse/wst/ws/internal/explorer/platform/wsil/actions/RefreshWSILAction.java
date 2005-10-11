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
package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;

public class RefreshWSILAction extends RefreshAction
{
  public RefreshWSILAction(Controller controller)
  {
    super(controller);
  }

  public boolean executeSingleLinkAction()
  {
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    Node selectedNode = nodeManager_.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    if (selectedElement instanceof WsilElement)
    {
      String wsilURL = ((WsilElement) selectedElement).getWsilUrl();
      if (((WsilElement) selectedElement).refresh())
      {
        wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_INFO_REFRESH_SUCCESSFUL", wsilURL));
        return true;
      }
      else
        wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_WSIL_NOT_FOUND", wsilURL));
    }
    return false;
  }
}

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
package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.RegFindServiceUUIDAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;

public class AddServiceToUDDIPerspectiveAction extends AddToUDDIPerspectiveAction
{
  public AddServiceToUDDIPerspectiveAction(Controller controller)
  {
    super(controller);
  }

  protected boolean executeSingleLinkAction()
  {
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    NodeManager nodeManager = favPerspective.getNodeManager();
    Node selectedNode = nodeManager.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    FavoritesUDDIServiceElement serElement = (FavoritesUDDIServiceElement) selectedElement;
    String serviceName = serElement.getName();
    String inquiryAPI = serElement.getInquiryURL();
    String serviceKey = serElement.getServiceKey();
    // create the registry in the UDDI perspective
    if (!createRegistryInUDDIPerspective(inquiryAPI, null, inquiryAPI, null, true))
      return false;
    RegFindServiceUUIDAction action = new RegFindServiceUUIDAction(controller_);
    Hashtable propertyTable = action.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY, serviceKey);
    propertyTable.put(UDDIActionInputs.QUERY_NAME, serviceName);
    if (!action.run())
    {
      favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_ERROR_ADD_TO_UDDI_PERSPECTIVE", serviceName));
      return false;
    }
    uddiPerspective.getNavigatorManager().getSelectedNode().setVisibilityOfChildren(true);
    favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_INFO_ADD_TO_UDDI_PERSPECTIVE_SUCCESSFUL", serviceName));
    return true;
  }
}

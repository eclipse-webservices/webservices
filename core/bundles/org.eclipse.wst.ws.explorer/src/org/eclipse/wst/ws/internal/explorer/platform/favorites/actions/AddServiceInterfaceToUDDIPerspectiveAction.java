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
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIServiceInterfaceElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.RegFindServiceInterfaceUUIDAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;

public class AddServiceInterfaceToUDDIPerspectiveAction extends AddToUDDIPerspectiveAction
{
  public AddServiceInterfaceToUDDIPerspectiveAction(Controller controller)
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
    FavoritesUDDIServiceInterfaceElement serIntElement = (FavoritesUDDIServiceInterfaceElement) selectedElement;
    String serIntName = serIntElement.getName();
    String inquiryAPI = serIntElement.getInquiryURL();
    String serIntKey = serIntElement.getServiceInterfaceKey();
    // create the registry in the UDDI perspective
    if (!createRegistryInUDDIPerspective(inquiryAPI, null, inquiryAPI, null, true))
      return false;
    RegFindServiceInterfaceUUIDAction action = new RegFindServiceInterfaceUUIDAction(controller_);
    Hashtable propertyTable = action.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY, serIntKey);
    propertyTable.put(UDDIActionInputs.QUERY_NAME, serIntName);
    if (!action.run())
    {
      favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_ERROR_ADD_TO_UDDI_PERSPECTIVE", serIntName));
      return false;
    }
    uddiPerspective.getNavigatorManager().getSelectedNode().setVisibilityOfChildren(true);
    favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_INFO_ADD_TO_UDDI_PERSPECTIVE_SUCCESSFUL", serIntName));
    return true;
  }
}

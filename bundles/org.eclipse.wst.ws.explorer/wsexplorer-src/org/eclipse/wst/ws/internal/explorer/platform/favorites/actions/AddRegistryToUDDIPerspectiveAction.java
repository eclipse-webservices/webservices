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
package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;

public class AddRegistryToUDDIPerspectiveAction extends AddToUDDIPerspectiveAction
{
  public AddRegistryToUDDIPerspectiveAction(Controller controller)
  {
    super(controller);
  }

  protected boolean executeSingleLinkAction()
  {
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    NodeManager nodeManager = favPerspective.getNodeManager();
    Node selectedNode = nodeManager.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    FavoritesUDDIRegistryElement regElement = (FavoritesUDDIRegistryElement) selectedElement;
    String registryName = regElement.getName();
    String inquiryAPI = regElement.getInquiryURL();
    String publishAPI = regElement.getPublishURL();
    String registrationURL = regElement.getRegistrationURL();
    if (!createRegistryInUDDIPerspective(inquiryAPI, publishAPI, registryName, registrationURL, false))
      return false;
    favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_INFO_ADD_TO_UDDI_PERSPECTIVE_SUCCESSFUL", registryName));
    return true;
  }
}

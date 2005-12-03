/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUserDefUDDIRegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies;

public class AddUserDefRegistryToUDDIPerspectiveAction extends AddToUDDIPerspectiveAction
{
  public AddUserDefRegistryToUDDIPerspectiveAction(Controller controller)
  {
    super(controller);
  }

  protected boolean executeSingleLinkAction()
  {
    int nodeID = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    NodeManager nodeManager = favPerspective.getNodeManager();
    Node selectedNode = nodeManager.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    FavoritesUserDefUDDIRegistryElement regElement = (FavoritesUserDefUDDIRegistryElement)selectedElement;
    String registryName = regElement.getName();
    String inquiryAPI = regElement.getInquiryURL();
    String publishAPI = regElement.getPublishURL();
    String defaultLogin = regElement.getDefaultLogin();
    String defaultPassword = regElement.getDefaultPassword();
    Taxonomies taxonomies = regElement.getTaxonomies();
    if (!createRegistryInUDDIPerspective(inquiryAPI, publishAPI, registryName, null, defaultLogin, defaultPassword, taxonomies, false))
      return false;
    favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_INFO_ADD_TO_UDDI_PERSPECTIVE_SUCCESSFUL", registryName));
    return true;
  }
}

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

import java.util.Enumeration;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesMainElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class RemoveFavoritesAction extends MultipleLinkAction
{
  public RemoveFavoritesAction(Controller controller)
  {
    super(controller);
  }

  public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID)
  {
    StringBuffer actionLink = new StringBuffer("favorites/actions/RemoveFavoritesActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID);
    actionLink.append('=');
    actionLink.append(nodeID);
    actionLink.append('&');
    actionLink.append(ActionInputs.TOOLID);
    actionLink.append('=');
    actionLink.append(toolID);
    actionLink.append('&');
    actionLink.append(ActionInputs.VIEWID);
    actionLink.append('=');
    actionLink.append(viewID);
    actionLink.append('&');
    actionLink.append(ActionInputs.VIEWTOOLID);
    actionLink.append('=');
    actionLink.append(viewToolID);
    return actionLink.toString();
  }

  public static String getBaseActionLink()
  {
    return "favorites/actions/RemoveFavoritesActionJSP.jsp";
  }

  protected boolean executeSingleLinkAction()
  {
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    NodeManager nodeManager = favPerspective.getNodeManager();
    String nodeIDString = (String) propertyTable_.get(ActionInputs.NODEID);
    int nodeID;
    try
    {
      nodeID = Integer.parseInt(nodeIDString);
    }
    catch (NumberFormatException nfe)
    {
      nodeID = nodeManager.getSelectedNodeId();
    }
    Node selectedNode = nodeManager.getNode(nodeID);
    if (selectedNode == null)
      return false;
    TreeElement selectedElement = selectedNode.getTreeElement();
    String pluginMetadataDirectory = controller_.getServletEngineStateLocation();
    boolean actionResult;
    if (selectedElement instanceof FavoritesElement)
      actionResult = ((FavoritesElement) selectedElement).getParentFolderElement().removeFavoriteByNodeID(nodeID, pluginMetadataDirectory);
    else if (selectedElement instanceof FavoritesFolderElement)
      actionResult = ((FavoritesFolderElement) selectedElement).removeAllFavorites(pluginMetadataDirectory);
    else if (selectedElement instanceof FavoritesMainElement)
    {
      Enumeration e;
      FavoritesMainElement mainElement = (FavoritesMainElement) selectedElement;
      e = mainElement.getElements(FavoritesModelConstants.REL_WSIL_FOLDER_NODE);
      actionResult = ((FavoritesFolderElement) e.nextElement()).removeAllFavorites(pluginMetadataDirectory);
      e = mainElement.getElements(FavoritesModelConstants.REL_WSDL_SERVICE_FOLDER_NODE);
      actionResult = actionResult && ((FavoritesFolderElement) e.nextElement()).removeAllFavorites(pluginMetadataDirectory);
      e = mainElement.getElements(FavoritesModelConstants.REL_UDDI_SERVICE_FOLDER_NODE);
      actionResult = actionResult && ((FavoritesFolderElement) e.nextElement()).removeAllFavorites(pluginMetadataDirectory);
      e = mainElement.getElements(FavoritesModelConstants.REL_UDDI_BUSINESS_FOLDER_NODE);
      actionResult = actionResult && ((FavoritesFolderElement) e.nextElement()).removeAllFavorites(pluginMetadataDirectory);
      e = mainElement.getElements(FavoritesModelConstants.REL_UDDI_SERVICE_INTERFACE_FOLDER_NODE);
      actionResult = actionResult && ((FavoritesFolderElement) e.nextElement()).removeAllFavorites(pluginMetadataDirectory);
      e = mainElement.getElements(FavoritesModelConstants.REL_UDDI_REGISTRY_FOLDER_NODE);
      actionResult = actionResult && ((FavoritesFolderElement) e.nextElement()).removeAllFavorites(pluginMetadataDirectory);
    }
    else
      actionResult = false;
    if (actionResult)
      favPerspective.getMessageQueue().addMessage(controller_.getMessage("MSG_INFO_NODE_CLEARED", selectedNode.getNodeName()));
    return actionResult;
  }

  public String getTreeContentVar()
  {
    return "favNavigatorContent";
  }

  public String getTreeContentPage()
  {
    return "favorites/fav_navigator_content.jsp";
  }

  public String getPropertiesContainerVar()
  {
    return "favPropertiesContainer";
  }

  public String getPropertiesContainerPage()
  {
    return "favorites/fav_properties_container.jsp";
  }

  public String getStatusContentVar()
  {
    return "favStatusContent";
  }

  public String getStatusContentPage()
  {
    return "favorites/fav_status_content.jsp";
  }
}

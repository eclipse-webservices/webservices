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

import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;

import java.util.Hashtable;

public class AddWSILToFavoritesAction extends AddToFavoritesAction
{
  public AddWSILToFavoritesAction(Controller controller)
  {
    super(controller);
  }

  public boolean favoriteExists()
  {
    String wsilURL = getWsilUrl();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_WSIL_URL, wsilURL);
    return favoriteExists(table, FavoritesModelConstants.REL_WSIL_FOLDER_NODE);
  }

  public boolean executeSingleLinkAction()
  {
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    String wsilURL = getWsilUrl();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_WSIL_URL, wsilURL);
    if (isMultipleLinkAction() && favoriteExists(table, FavoritesModelConstants.REL_WSIL_FOLDER_NODE))
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_FAVORITES_ALREADY_EXISTS", wsilURL));
      return false;
    }
    if (addToFavorites(table, FavoritesModelConstants.REL_WSIL_FOLDER_NODE))
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_INFO_ADD_TO_FAVORITES_SUCCESSFUL", wsilURL));
      return true;
    }
    else
    {
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_ADD_TO_FAVORITES", wsilURL));
      return false;
    }
  }

  private String getWsilUrl()
  {
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    Node selectedNode = nodeManager_.getNode(nodeID);
    WsilElement selectedElement = (WsilElement) selectedNode.getTreeElement();
    return selectedElement.getWsilUrl();
  }
}

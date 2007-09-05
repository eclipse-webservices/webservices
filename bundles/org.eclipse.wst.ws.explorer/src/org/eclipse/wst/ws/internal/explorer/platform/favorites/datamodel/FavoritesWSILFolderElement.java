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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.wsil.Link;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class FavoritesWSILFolderElement extends FavoritesFolderElement {

  public FavoritesWSILFolderElement(String name, Model model, NodeManager nodeManager) {
    super(name, model, nodeManager);
  }

  public void init(FavoritesMainElement favMainElement) {
    Link[] links = favMainElement.loadWSILs();
    for (int i = 0; i < links.length; i++) {
      Link link = links[i];
      FavoritesWSILElement favWSILElement = new FavoritesWSILElement(link.getLocation(), getModel(), link);
      connect(favWSILElement, FavoritesModelConstants.REL_WSIL_NODE, ModelConstants.REL_OWNER);
    }
  }

  public boolean addFavorite(Hashtable table) {
    String wsilURL = (String)table.get(FavoritesModelConstants.PROP_WSIL_URL);
    if (wsilURL == null)
      return false;
    FavoritesWSILElement e = getFavorite(wsilURL);
    if (e != null)
      removeFavorite(e);
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    Link link = favMainElement.addWSILLink(wsilURL);
    boolean saved = favMainElement.saveFavorites();
    if (saved) {
      FavoritesWSILElement favWSILElement = new FavoritesWSILElement(wsilURL, getModel(), link);
      connect(favWSILElement, FavoritesModelConstants.REL_WSIL_NODE, ModelConstants.REL_OWNER);
    }
    return saved;
  }

  public boolean favoriteExists(Hashtable table) {
    String wsilURL = (String)table.get(FavoritesModelConstants.PROP_WSIL_URL);
    if (wsilURL == null)
      return false;
    return (getFavorite(wsilURL) != null);
  }

  public boolean removeFavoriteByNodeID(int nodeID,String pluginMetadataDirectory) {
    Node selectedNode = nodeManager_.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    if (selectedElement instanceof FavoritesWSILElement)
      return (removeFavorite((FavoritesWSILElement)selectedElement) && favMainElement.saveFavorites());
    else
      return false;
  }

  private boolean removeFavorite(FavoritesWSILElement element) {
    Link link = element.getLink();
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    if (favMainElement.removeLink(link)) {
      element.disconnectAll();
      return true;
    }
    else
      return false;
  }

  public boolean removeAllFavorites(String pluginMetadataDirectory) {
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    Enumeration e = getAllFavorites();
    while(e.hasMoreElements()) {
      FavoritesWSILElement favWSILElement = (FavoritesWSILElement)e.nextElement();
      Link link = favWSILElement.getLink();
      favMainElement.removeLink(link);
    }
    disconnectRel(FavoritesModelConstants.REL_WSIL_NODE);
    return favMainElement.saveFavorites();
  }

  public Enumeration getAllFavorites() {
    return getElements(FavoritesModelConstants.REL_WSIL_NODE);
  }

  private FavoritesWSILElement getFavorite(String wsilUrl) {
    Enumeration e = getAllFavorites();
    while (e.hasMoreElements()) {
      FavoritesWSILElement wsilElement = (FavoritesWSILElement)e.nextElement();
      if (wsilUrl.equals(wsilElement.getWsilUrl()))
        return wsilElement;
    }
    return null;
  }
}

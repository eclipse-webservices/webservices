/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.wsil.Service;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class FavoritesWSDLServiceFolderElement extends FavoritesFolderElement
{

  public FavoritesWSDLServiceFolderElement(String name, Model model, NodeManager nodeManager) {
    super(name, model, nodeManager);
  }

  public void init(FavoritesMainElement favMainElement) {
    Service[] services = favMainElement.loadWSDLServices();
    for (int i = 0; i < services.length; i++) {
      Service service = services[i];
      FavoritesWSDLServiceElement favWSDLServiceElement = new FavoritesWSDLServiceElement((service.getDescriptions())[0].getLocation(), getModel(), service);
      connect(favWSDLServiceElement, FavoritesModelConstants.REL_WSDL_SERVICE_NODE, ModelConstants.REL_OWNER);
    }
  }

  public boolean addFavorite(Hashtable table) {
    String wsdlURL = (String)table.get(FavoritesModelConstants.PROP_WSDL_URL);
    if (wsdlURL == null)
      return false;
    FavoritesWSDLServiceElement e = getFavorite(wsdlURL);
    if (e != null)
      removeFavorite(e);
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    Service service = favMainElement.addWSDLService(wsdlURL);
    boolean saved = favMainElement.saveFavorites();
    if (saved) {
      FavoritesWSDLServiceElement favWSDLServiceElement = new FavoritesWSDLServiceElement(wsdlURL, getModel(), service);
      connect(favWSDLServiceElement, FavoritesModelConstants.REL_WSDL_SERVICE_NODE, ModelConstants.REL_OWNER);
    }
    return saved;
  }

  public boolean favoriteExists(Hashtable table) {
    String wsdlURL = (String)table.get(FavoritesModelConstants.PROP_WSDL_URL);
    if (wsdlURL == null)
      return false;
    return (getFavorite(wsdlURL) != null);
  }

  public boolean removeFavoriteByNodeID(int nodeID,String pluginMetadataDirectory) {
    Node selectedNode = nodeManager_.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    if (selectedElement instanceof FavoritesWSDLServiceElement)
      return (removeFavorite((FavoritesWSDLServiceElement)selectedElement) && favMainElement.saveFavorites());
    else
      return false;
    }

  private boolean removeFavorite(FavoritesWSDLServiceElement element) {
    Service service = ((FavoritesWSDLServiceElement)element).getService();
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    if (favMainElement.removeService(service)) {
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
      FavoritesWSDLServiceElement favWSDLServiceElement = (FavoritesWSDLServiceElement)e.nextElement();
      Service service = favWSDLServiceElement.getService();
      favMainElement.removeService(service);
    }
    disconnectRel(FavoritesModelConstants.REL_WSDL_SERVICE_NODE);
    return favMainElement.saveFavorites();
  }

  public Enumeration getAllFavorites() {
    return getElements(FavoritesModelConstants.REL_WSDL_SERVICE_NODE);
  }

  private FavoritesWSDLServiceElement getFavorite(String wsdlUrl) {
    Enumeration e = getAllFavorites();
    while (e.hasMoreElements()) {
      FavoritesWSDLServiceElement wsdlElement = (FavoritesWSDLServiceElement)e.nextElement();
      if (wsdlUrl.equals(wsdlElement.getWsdlUrl()))
        return wsdlElement;
    }
    return null;
  }
}

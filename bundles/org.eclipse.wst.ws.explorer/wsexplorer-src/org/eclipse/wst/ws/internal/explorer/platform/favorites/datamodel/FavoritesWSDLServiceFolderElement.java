/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel;

import org.eclipse.wst.ws.internal.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;

import java.util.Hashtable;
import java.util.Enumeration;
import org.apache.wsil.*;

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

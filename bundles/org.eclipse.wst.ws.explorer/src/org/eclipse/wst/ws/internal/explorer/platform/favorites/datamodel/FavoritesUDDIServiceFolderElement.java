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
import org.apache.wsil.Service;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

/**
* The data model element that represents 
* a WSIL document
*/
public class FavoritesUDDIServiceFolderElement extends FavoritesFolderElement
{

  public FavoritesUDDIServiceFolderElement(String name, Model model, NodeManager nodeManager) {
    super(name, model, nodeManager);
  }

  public void init(FavoritesMainElement favMainElement) {
    Service[] services = favMainElement.loadUDDIServices();
    for (int i = 0; i < services.length; i++) {
      Service service = services[i];
      FavoritesUDDIServiceElement favUDDIServiceElement = new FavoritesUDDIServiceElement((service.getServiceNames())[0].getText(), getModel(), service);
      connect(favUDDIServiceElement, FavoritesModelConstants.REL_UDDI_SERVICE_NODE, ModelConstants.REL_OWNER);
    }
  }

  public boolean addFavorite(Hashtable table) {
    String serviceName = (String)table.get(FavoritesModelConstants.PROP_UDDI_SERVICE_NAME);
    String inquiryAPI = (String)table.get(FavoritesModelConstants.PROP_UDDI_SERVICE_INQUIRY_API);
    String serviceKey = (String)table.get(FavoritesModelConstants.PROP_UDDI_SERVICE_KEY);
    if (serviceName == null || inquiryAPI == null || serviceKey == null)
      return false;
    FavoritesUDDIServiceElement e = getFavorite(inquiryAPI, serviceKey);
    if (e != null)
      removeFavorite(e);
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    Service service = favMainElement.addUDDIService(serviceName, inquiryAPI, serviceKey);
    boolean saved = favMainElement.saveFavorites();
    if (saved) {
      FavoritesUDDIServiceElement favUDDIServiceElement = new FavoritesUDDIServiceElement(serviceName, getModel(), service);
      connect(favUDDIServiceElement, FavoritesModelConstants.REL_UDDI_SERVICE_NODE, ModelConstants.REL_OWNER);
    }
    return saved;
  }

  public boolean favoriteExists(Hashtable table) {
    String inquiryAPI = (String)table.get(FavoritesModelConstants.PROP_UDDI_SERVICE_INQUIRY_API);
    String serviceKey = (String)table.get(FavoritesModelConstants.PROP_UDDI_SERVICE_KEY);
    if (inquiryAPI == null || serviceKey == null)
      return false;
    return (getFavorite(inquiryAPI, serviceKey) != null);
  }

  public boolean removeFavoriteByNodeID(int nodeID,String pluginMetadataDirectory) {
    Node selectedNode = nodeManager_.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    if (selectedElement instanceof FavoritesUDDIServiceElement)
      return (removeFavorite((FavoritesUDDIServiceElement)selectedElement) && favMainElement.saveFavorites());
    else
      return false;
  }

  private boolean removeFavorite(FavoritesUDDIServiceElement element) {
    Service service = ((FavoritesUDDIServiceElement)element).getService();
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
      FavoritesUDDIServiceElement favUDDIServiceElement = (FavoritesUDDIServiceElement)e.nextElement();
      Service service = favUDDIServiceElement.getService();
      favMainElement.removeService(service);
    }
    disconnectRel(FavoritesModelConstants.REL_UDDI_SERVICE_NODE);
    return favMainElement.saveFavorites();
  }

  public Enumeration getAllFavorites() {
    return getElements(FavoritesModelConstants.REL_UDDI_SERVICE_NODE);
  }

  private FavoritesUDDIServiceElement getFavorite(String inquiryAPI, String serviceKey) {
    Enumeration e = getAllFavorites();
    while (e.hasMoreElements()) {
      FavoritesUDDIServiceElement serElement = (FavoritesUDDIServiceElement)e.nextElement();
      if (inquiryAPI.equals(serElement.getInquiryURL()) &&
          serviceKey.equals(serElement.getServiceKey()))
        return serElement;
    }
    return null;
  }
}

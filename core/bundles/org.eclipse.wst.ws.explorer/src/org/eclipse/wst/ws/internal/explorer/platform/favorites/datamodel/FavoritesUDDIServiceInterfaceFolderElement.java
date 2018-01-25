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
public class FavoritesUDDIServiceInterfaceFolderElement extends FavoritesFolderElement
{

  public FavoritesUDDIServiceInterfaceFolderElement(String name, Model model, NodeManager nodeManager) {
    super(name, model, nodeManager);
  }

  public void init(FavoritesMainElement favMainElement) {
    Service[] services = favMainElement.loadUDDIServiceInterfaces();
    for (int i = 0; i < services.length; i++) {
      Service service = services[i];
      FavoritesUDDIServiceInterfaceElement favUDDISerIntElement = new FavoritesUDDIServiceInterfaceElement((service.getServiceNames())[0].getText(), getModel(), service);
      connect(favUDDISerIntElement, FavoritesModelConstants.REL_UDDI_SERVICE_INTERFACE_NODE, ModelConstants.REL_OWNER);
    }
  }

  public boolean addFavorite(Hashtable table) {
    String serIntName = (String)table.get(FavoritesModelConstants.PROP_UDDI_SERVICE_INTERFACE_NAME);
    String inquiryAPI = (String)table.get(FavoritesModelConstants.PROP_UDDI_SERVICE_INTERFACE_INQUIRY_API);
    String serIntKey = (String)table.get(FavoritesModelConstants.PROP_UDDI_SERVICE_INTERFACE_KEY);
    if (serIntName == null || inquiryAPI == null || serIntKey == null)
      return false;
    FavoritesUDDIServiceInterfaceElement e = getFavorite(inquiryAPI, serIntKey);
    if (e != null)
      removeFavorite(e);
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    Service service = favMainElement.addUDDIServiceInterface(serIntName, inquiryAPI, serIntKey);
    boolean saved = favMainElement.saveFavorites();
    if (saved) {
      FavoritesUDDIServiceInterfaceElement favUDDISerIntElement = new FavoritesUDDIServiceInterfaceElement(serIntName, getModel(), service);
      connect(favUDDISerIntElement, FavoritesModelConstants.REL_UDDI_SERVICE_INTERFACE_NODE, ModelConstants.REL_OWNER);
    }
    return saved;
  }

  public boolean favoriteExists(Hashtable table) {
    String inquiryAPI = (String)table.get(FavoritesModelConstants.PROP_UDDI_SERVICE_INTERFACE_INQUIRY_API);
    String serIntKey = (String)table.get(FavoritesModelConstants.PROP_UDDI_SERVICE_INTERFACE_KEY);
    if (inquiryAPI == null || serIntKey == null)
      return false;
    return (getFavorite(inquiryAPI, serIntKey) != null);
  }

  public boolean removeFavoriteByNodeID(int nodeID,String pluginMetadataDirectory) {
    Node selectedNode = nodeManager_.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    if (selectedElement instanceof FavoritesUDDIServiceInterfaceElement)
      return (removeFavorite((FavoritesUDDIServiceInterfaceElement)selectedElement) && favMainElement.saveFavorites());
    else
      return false;
  }

  private boolean removeFavorite(FavoritesUDDIServiceInterfaceElement element) {
    Service service = ((FavoritesUDDIServiceInterfaceElement)element).getService();
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
      FavoritesUDDIServiceInterfaceElement favUDDISerIntElement = (FavoritesUDDIServiceInterfaceElement)e.nextElement();
      Service service = favUDDISerIntElement.getService();
      favMainElement.removeService(service);
    }
    disconnectRel(FavoritesModelConstants.REL_UDDI_SERVICE_INTERFACE_NODE);
    return favMainElement.saveFavorites();
  }

  public Enumeration getAllFavorites() {
    return getElements(FavoritesModelConstants.REL_UDDI_SERVICE_INTERFACE_NODE);
  }

  private FavoritesUDDIServiceInterfaceElement getFavorite(String inquiryAPI, String serIntKey) {
    Enumeration e = getAllFavorites();
    while (e.hasMoreElements()) {
      FavoritesUDDIServiceInterfaceElement serIntElement = (FavoritesUDDIServiceInterfaceElement)e.nextElement();
      if (inquiryAPI.equals(serIntElement.getInquiryURL()) &&
           serIntKey.equals(serIntElement.getServiceInterfaceKey()))
        return serIntElement;
    }
    return null;
  }
}

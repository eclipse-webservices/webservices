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

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.wsil.Link;
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
public class FavoritesUDDIBusinessFolderElement extends FavoritesFolderElement
{
  public FavoritesUDDIBusinessFolderElement(String name, Model model, NodeManager nodeManager) {
    super(name, model, nodeManager);
  }

  public void init(FavoritesMainElement favMainElement) {
    Link[] links = favMainElement.loadUDDIBusinesses();
    for (int i = 0; i < links.length; i++) {
      Link link = links[i];
      FavoritesUDDIBusinessElement favUDDIBusinessElement = new FavoritesUDDIBusinessElement((link.getAbstracts())[0].getText(), getModel(), link);
      connect(favUDDIBusinessElement, FavoritesModelConstants.REL_UDDI_BUSINESS_NODE, ModelConstants.REL_OWNER);
    }
  }

  public boolean addFavorite(Hashtable table) {
    String businessName = (String)table.get(FavoritesModelConstants.PROP_UDDI_BUSINESS_NAME);
    String inquiryAPI = (String)table.get(FavoritesModelConstants.PROP_UDDI_BUSINESS_INQUIRY_API);
    String businessKey = (String)table.get(FavoritesModelConstants.PROP_UDDI_BUSINESS_KEY);
    if (businessName == null || inquiryAPI == null || businessKey == null)
      return false;
    FavoritesUDDIBusinessElement e = getFavorite(inquiryAPI, businessKey);
    if (e != null)
      removeFavorite(e);
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    Link link = favMainElement.addUDDIBusiness(businessName, inquiryAPI, businessKey);
    boolean saved = favMainElement.saveFavorites();
    if (saved) {
      FavoritesUDDIBusinessElement favUDDIBusinessElement = new FavoritesUDDIBusinessElement(businessName, getModel(), link);
      connect(favUDDIBusinessElement, FavoritesModelConstants.REL_UDDI_BUSINESS_NODE, ModelConstants.REL_OWNER);
    }
    return saved;
  }

  public boolean favoriteExists(Hashtable table) {
    String inquiryAPI = (String)table.get(FavoritesModelConstants.PROP_UDDI_BUSINESS_INQUIRY_API);
    String businessKey = (String)table.get(FavoritesModelConstants.PROP_UDDI_BUSINESS_KEY);
    if (inquiryAPI == null || businessKey == null)
      return false;
    return (getFavorite(inquiryAPI, businessKey) != null);
  }

  public boolean removeFavoriteByNodeID(int nodeID,String pluginMetadataDirectory) {
    Node selectedNode = nodeManager_.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    if (selectedElement instanceof FavoritesUDDIBusinessElement)
      return (removeFavorite((FavoritesUDDIBusinessElement)selectedElement) && favMainElement.saveFavorites());
    else
      return false;
  }

  private boolean removeFavorite(FavoritesUDDIBusinessElement element) {
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
      FavoritesUDDIBusinessElement favUDDIBusinessElement = (FavoritesUDDIBusinessElement)e.nextElement();
      Link link = favUDDIBusinessElement.getLink();
      favMainElement.removeLink(link);
    }
    disconnectRel(FavoritesModelConstants.REL_UDDI_BUSINESS_NODE);
    return favMainElement.saveFavorites();
  }

  public Enumeration getAllFavorites() {
    return getElements(FavoritesModelConstants.REL_UDDI_BUSINESS_NODE);
  }

  private FavoritesUDDIBusinessElement getFavorite(String inquiryAPI, String businessKey) {
    Enumeration e = getAllFavorites();
    while (e.hasMoreElements()) {
      FavoritesUDDIBusinessElement busElement = (FavoritesUDDIBusinessElement)e.nextElement();
      if (inquiryAPI.equals(busElement.getInquiryURL()) &&
          businessKey.equals(busElement.getBusinessKey()))
        return busElement;
    }
    return null;
  }
}

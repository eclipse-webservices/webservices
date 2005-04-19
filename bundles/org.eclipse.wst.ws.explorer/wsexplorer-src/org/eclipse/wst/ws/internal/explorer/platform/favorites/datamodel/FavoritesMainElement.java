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
import org.eclipse.wst.ws.internal.explorer.favorites.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.apache.wsil.*;

/**
* The data model element that represents 
* a WSIL document
*/
public class FavoritesMainElement extends TreeElement
{
  private Controller controller_;
  private FavoritesRegistryTypeWSE favRegistry_;

  public FavoritesMainElement(String name, Model model, Controller controller)
  {
    super(name, model);
    controller_ = controller;
    favRegistry_ = new FavoritesRegistryTypeWSE(
			controller.getDefaultFavoritesLocation(),
			controller.getServletEngineStateLocation()
	);
  }

  public boolean restoreFavoritesDefault()
  {
    favRegistry_.restoreFavoritesDefault();
    return saveFavorites();
  }

  public boolean saveFavorites()
  {
    try
    {
      favRegistry_.save();
      return true;
    }
    catch (Throwable t)
    {
      FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
      favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_ERROR_SAVE_FAVORITES_WSIL", favRegistry_.getWriteLocation()));
      return false;
    }
  }

  public Link addUDDIRegistry(String registryName, String inquiryAPI, String publishAPI, String registrationURL)
  {
    return favRegistry_.addUDDIRegistry(registryName, inquiryAPI, publishAPI, registrationURL);
  }

  public Link addUDDIBusiness(String businessName, String inquiryAPI, String businessKey)
  {
    return favRegistry_.addUDDIBusiness(businessName, inquiryAPI, businessKey);
  }

  public Service addUDDIService(String serviceName, String inquiryAPI, String serviceKey)
  {
    return favRegistry_.addUDDIService(serviceName, inquiryAPI, serviceKey);
  }

  public Service addUDDIServiceInterface(String serIntName, String inquiryAPI, String serIntKey)
  {
    return favRegistry_.addUDDIServiceInterface(serIntName, inquiryAPI, serIntKey);
  }

  public Service addWSDLService(String url)
  {
    return favRegistry_.addWSDLService(url);
  }

  public Link addWSILLink(String url)
  {
    return favRegistry_.addWSILLink(url);
  }

  public boolean removeService(Service service) {
    favRegistry_.removeService(service);
    return true;
  }

  public boolean removeLink(Link link) {
    favRegistry_.removeLink(link);
    return true;
  }

  public Link[] loadUDDIRegistries()
  {
    return favRegistry_.loadUDDIRegistries();
  }
    
  public Link[] loadUDDIBusinesses()
  {
    return favRegistry_.loadUDDIBusinesses();
  }

  public Service[] loadUDDIServices()
  {
    return favRegistry_.loadUDDIServices();
  }

  public Service[] loadUDDIServiceInterfaces()
  {
    return favRegistry_.loadUDDIServiceInterfaces();
  }

  public Service[] loadWSDLServices()
  {
    return favRegistry_.loadWSDLServices();
  }

  public Link[] loadWSILs()
  {
    return favRegistry_.loadWSILs();
  }
  
  public final FavoritesUDDIRegistryFolderElement getFavoritesUDDIRegistryFolderElement()
  {
    return (FavoritesUDDIRegistryFolderElement)(getElements(FavoritesModelConstants.REL_UDDI_REGISTRY_FOLDER_NODE).nextElement());
  }
}

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
package org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.model.v10.registry.Name;
import org.eclipse.wst.ws.internal.model.v10.registry.Registry;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry;
import org.eclipse.wst.ws.internal.registry.IRegistryManager;
import org.eclipse.wst.ws.internal.registry.RegistryService;
import org.eclipse.wst.ws.internal.registry.UDDIRegistryService;

/**
 * The data model element that represents a WSIL document
 */
public class FavoritesUserDefUDDIRegistryFolderElement extends FavoritesFolderElement
{
  public FavoritesUserDefUDDIRegistryFolderElement(String name, Model model, NodeManager nodeManager)
  {
    super(name, model, nodeManager);
  }

  public void init(FavoritesMainElement favMainElement)
  {
    RegistryService regService = RegistryService.instance();
    IRegistryManager regManager = regService.getDefaultRegistryManager();
    try
    {
      String[] regURIs = regManager.getRegistryURIs();
      for (int i = 0; i < regURIs.length; i++)
      {
        Registry reg = regManager.loadRegistry(regURIs[i]);
        if (reg instanceof UDDIRegistry)
        {
          UDDIRegistry uddiReg = (UDDIRegistry)reg;
          List names = uddiReg.getName();
          String displayName = names != null && !names.isEmpty() ? ((Name)names.get(0)).getValue() : "";
          FavoritesUserDefUDDIRegistryElement favUserDefUDDIRegElement = new FavoritesUserDefUDDIRegistryElement(displayName, getModel());
          favUserDefUDDIRegElement.setNames(names);
          favUserDefUDDIRegElement.setDescs(uddiReg.getDescription());
          favUserDefUDDIRegElement.setVersion(uddiReg.getVersion());
          favUserDefUDDIRegElement.setDefaultLogin(uddiReg.getDefaultLogin());
          favUserDefUDDIRegElement.setDefaultPassword(uddiReg.getDefaultPassword());
          favUserDefUDDIRegElement.setInquiryURL(uddiReg.getDiscoveryURL());
          favUserDefUDDIRegElement.setPublishURL(uddiReg.getPublicationURL());
          favUserDefUDDIRegElement.setSecureInquiryURL(uddiReg.getSecuredDiscoveryURL());
          favUserDefUDDIRegElement.setSecurePublishURL(uddiReg.getSecuredPublicationURL());
          Taxonomy[] taxonomies = regManager.loadTaxonomies(UDDIRegistryService.instance().getTaxonomyURIs(uddiReg));
          favUserDefUDDIRegElement.setTaxonomies(taxonomies);
          connect(favUserDefUDDIRegElement, FavoritesModelConstants.REL_USER_DEF_UDDI_REGISTRY_NODE, ModelConstants.REL_OWNER);
        }
      }
    }
    catch (CoreException ce)
    {
      // TODO: Better error reporting
      ce.printStackTrace();
    }
  }

  public boolean addFavorite(Hashtable table)
  {
    // do nothing
    return true;
  }

  public boolean favoriteExists(Hashtable table)
  {
    // do nothing
    return true;
  }

  public boolean removeFavoriteByNodeID(int nodeID, String pluginMetadataDirectory)
  {
    // do nothing
    return true;
  }

  public boolean removeAllFavorites(String pluginMetadataDirectory)
  {
    // do nothing
    return true;
  }

  public Enumeration getAllFavorites()
  {
    return getElements(FavoritesModelConstants.REL_USER_DEF_UDDI_REGISTRY_NODE);
  }
}

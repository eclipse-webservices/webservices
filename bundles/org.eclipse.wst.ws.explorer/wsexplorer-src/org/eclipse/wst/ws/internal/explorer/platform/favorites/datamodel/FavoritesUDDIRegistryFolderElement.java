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

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.wsil.Link;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.CategoryModel;
import org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils;

/**
* The data model element that represents 
* a WSIL document
*/
public class FavoritesUDDIRegistryFolderElement extends FavoritesFolderElement
{
  public FavoritesUDDIRegistryFolderElement(String name, Model model, NodeManager nodeManager) {
    super(name, model, nodeManager);
  }

  public void init(FavoritesMainElement favMainElement) {
    Link[] links = favMainElement.loadUDDIRegistries();
    for (int i = 0; i < links.length; i++) {
      Link link = links[i];
      FavoritesUDDIRegistryElement favUDDIRegsitryElement = new FavoritesUDDIRegistryElement((link.getAbstracts())[0].getText(), getModel(), link);
      connect(favUDDIRegsitryElement, FavoritesModelConstants.REL_UDDI_REGISTRY_NODE, ModelConstants.REL_OWNER);
    }
  }
  
  public boolean addFavorite(Hashtable table) {
    String registryName = (String)table.get(FavoritesModelConstants.PROP_UDDI_REGISTRY_NAME);
    String inquiryAPI = (String)table.get(FavoritesModelConstants.PROP_UDDI_REGISTRY_INQUIRY_API);
    String publishAPI = (String)table.get(FavoritesModelConstants.PROP_UDDI_REGISTRY_PUBLISH_API);
    String registrationURL = (String)table.get(FavoritesModelConstants.PROP_UDDI_REGISTRY_REGISTRATION_URL);
    Enumeration userDefinedCategories = (Enumeration)table.get(FavoritesModelConstants.PROP_UDDI_REGISTRY_CATEGORIES_ENUM);
    // Save the category data first and undo if necessary.
    if (userDefinedCategories != null)
    {
      String pluginMetadataDirectory = (String)table.get(FavoritesModelConstants.PROP_PLUGIN_METADATA_DIRECTORY);
      StringBuffer directoryBuffer = new StringBuffer();
      formCategoriesDirectory(directoryBuffer,pluginMetadataDirectory,registryName);
      File categoriesDirectoryFile = new File(directoryBuffer.toString());
      categoriesDirectoryFile.mkdirs();
      StringBuffer categoryFileName = new StringBuffer();
      while (userDefinedCategories.hasMoreElements())
      {
        CategoryModel categoryModel = (CategoryModel)userDefinedCategories.nextElement();        
        categoryFileName.setLength(0);
        String encodedCategoryKey = URLUtils.encode(categoryModel.getCategoryKey());
        categoryFileName.append(categoriesDirectoryFile.getAbsolutePath()).append(File.separatorChar).append(encodedCategoryKey).append(".txt");
        byte rc = categoryModel.saveData(categoryFileName.toString());
        if (rc != CategoryModel.OPERATION_SUCCESSFUL)
        {
          // Delete the category file if it exists.
          File categoryFile = new File(categoryFileName.toString());
          if (categoryFile.exists())
            categoryFile.delete();
        }
      }
      cleanupCategoryDirectories(categoriesDirectoryFile);
    }
    if (registryName == null || inquiryAPI == null || publishAPI == null)
      return false;
    FavoritesUDDIRegistryElement e = getFavorite(registryName);
    if (e != null)
      removeFavorite(e);
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    Link link = favMainElement.addUDDIRegistry(registryName, inquiryAPI, publishAPI, registrationURL);
    boolean saved = favMainElement.saveFavorites();
    if (saved) {
      FavoritesUDDIRegistryElement favUDDIRegsitryElement = new FavoritesUDDIRegistryElement(registryName, getModel(), link);
      connect(favUDDIRegsitryElement, FavoritesModelConstants.REL_UDDI_REGISTRY_NODE, ModelConstants.REL_OWNER);
    }
    return saved;
  }
  
  public static final void formCategoriesDirectory(StringBuffer categoriesDirectory,String pluginMetadataDirectory,String registryName)
  {
    // <metadata>/uddi/<registryId>/categories
	String encodedRegistryName = URLUtils.encode(registryName);
    categoriesDirectory.append(pluginMetadataDirectory).append("uddi").append(File.separatorChar).append(encodedRegistryName).append(File.separatorChar).append("categories");    
  }
  
  private final void clearRegistryMetadata(String pluginMetadataDirectory,String registryName)
  {
    StringBuffer categoriesDirectory = new StringBuffer();
    formCategoriesDirectory(categoriesDirectory,pluginMetadataDirectory,registryName);
    File categoriesDirectoryFile = new File(categoriesDirectory.toString());
    File[] categoryFiles = categoriesDirectoryFile.listFiles();
    if (categoryFiles != null)
    {
      for (int i=0;i<categoryFiles.length;i++)
        categoryFiles[i].delete();
    }
    cleanupCategoryDirectories(categoriesDirectoryFile);
  }
  
  private final void cleanupCategoryDirectories(File categoriesDirectoryFile)
  {
    // Delete the categories subdirectory if it is empty.
    String[] fileList;
    fileList = categoriesDirectoryFile.list();
    if (fileList == null || fileList.length == 0)
      categoriesDirectoryFile.delete();
    // Delete the <registryId> subdirectory if it is empty.
    File registryIdDirectoryFile = categoriesDirectoryFile.getParentFile();
    fileList = registryIdDirectoryFile.list();
    if (fileList == null || fileList.length == 0)
      registryIdDirectoryFile.delete();
    // Delete the uddi subdirectory if it is empty.
    File uddiDirectoryFile = registryIdDirectoryFile.getParentFile();
    fileList = uddiDirectoryFile.list();
    if (fileList == null || fileList.length == 0)
      uddiDirectoryFile.delete();
  }

  public boolean favoriteExists(Hashtable table) {
    String registryName = (String)table.get(FavoritesModelConstants.PROP_UDDI_REGISTRY_NAME);
    if (registryName == null)
      return false;
    return (getFavorite(registryName) != null);
  }

  public boolean removeFavoriteByNodeID(int nodeID,String pluginMetadataDirectory) {
    Node selectedNode = nodeManager_.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    FavoritesMainElement favMainElement = getFavoritesMainElement();
    if (selectedElement instanceof FavoritesUDDIRegistryElement)
    {
      clearRegistryMetadata(pluginMetadataDirectory,selectedElement.getName());
      boolean rc = removeFavorite((FavoritesUDDIRegistryElement)selectedElement);
      rc = rc && favMainElement.saveFavorites();
      return rc;
    }
    else
      return false;
  }

  private boolean removeFavorite(FavoritesUDDIRegistryElement element) {
    Link link = ((FavoritesUDDIRegistryElement)element).getLink();
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
      FavoritesUDDIRegistryElement favUDDIRegistryElement = (FavoritesUDDIRegistryElement)e.nextElement();
      clearRegistryMetadata(pluginMetadataDirectory,favUDDIRegistryElement.getName());
      Link link = favUDDIRegistryElement.getLink();
      favMainElement.removeLink(link);
    }
    disconnectRel(FavoritesModelConstants.REL_UDDI_REGISTRY_NODE);
    return favMainElement.saveFavorites();
  }

  public Enumeration getAllFavorites() {
    return getElements(FavoritesModelConstants.REL_UDDI_REGISTRY_NODE);
  }

  private FavoritesUDDIRegistryElement getFavorite(String registryName) {
    Enumeration e = getAllFavorites();
    while (e.hasMoreElements()) {
      FavoritesUDDIRegistryElement regElement = (FavoritesUDDIRegistryElement)e.nextElement();
      if (registryName.equals(regElement.getName()))
        return regElement;
    }
    return null;
  }
}

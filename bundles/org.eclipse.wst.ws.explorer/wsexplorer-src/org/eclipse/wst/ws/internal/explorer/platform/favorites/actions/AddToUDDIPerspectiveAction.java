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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIBusinessFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIRegistryFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIServiceFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIServiceInterfaceFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.OpenRegistryAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.CategoryModel;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.RegistryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIMainNode;
import org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;

public abstract class AddToUDDIPerspectiveAction extends MultipleLinkAction {
    public AddToUDDIPerspectiveAction(Controller controller) {
        super(controller);
    }

    public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID) {
        StringBuffer actionLink = new StringBuffer("favorites/actions/FavoritesAddToUDDIPerspectiveActionJSP.jsp?");
        actionLink.append(ActionInputs.NODEID);
        actionLink.append('=');
        actionLink.append(nodeID);
        actionLink.append('&');
        actionLink.append(ActionInputs.TOOLID);
        actionLink.append('=');
        actionLink.append(toolID);
        actionLink.append('&');
        actionLink.append(ActionInputs.VIEWID);
        actionLink.append('=');
        actionLink.append(viewID);
        actionLink.append('&');
        actionLink.append(ActionInputs.VIEWTOOLID);
        actionLink.append('=');
        actionLink.append(viewToolID);
        return actionLink.toString();
    }

    public static String getBaseActionLink() {
        return "favorites/actions/FavoritesAddToUDDIPerspectiveActionJSP.jsp";
    }

    protected boolean createRegistryInUDDIPerspective(String inquiryAPI, String publishAPI, String registryName, String registrationURL,boolean useExisting) {
        Vector registryNodes = getRegistryNodesByInquiryURL(inquiryAPI);
        if (registryNodes != null)
        {
          if (useExisting)
          {
            Node registryNode = (Node)registryNodes.elementAt(0);
            NodeManager nodeManager = registryNode.getNodeManager();
            nodeManager.setSelectedNodeId(registryNode.getNodeId());
            return true;
          }
        }
        
        // open the registry if it is not already opened in the UDDI perspective
        OpenRegistryAction openRegAction = new OpenRegistryAction(controller_);

        // populate the property table
        Hashtable propertyTable = openRegAction.getPropertyTable();
        if (Validator.validateString(registryName))
            propertyTable.put(UDDIActionInputs.REGISTRY_NAME,registryName);
        else
            return false;

        if (Validator.validateURL(inquiryAPI))
            propertyTable.put(UDDIActionInputs.INQUIRY_URL,inquiryAPI);
        else
            return false;

        if (Validator.validateURL(publishAPI))
            propertyTable.put(UDDIActionInputs.PUBLISH_URL,publishAPI);

        if (Validator.validateURL(registrationURL))
            propertyTable.put(UDDIActionInputs.REGISTRATION_URL,registrationURL);
            
        // If user-defined category metadata exists, search and make the associations.
        StringBuffer directoryBuffer = new StringBuffer();
        FavoritesUDDIRegistryFolderElement.formCategoriesDirectory(directoryBuffer,controller_.getServletEngineStateLocation(),registryName);
        File categoriesDirectory = new File(directoryBuffer.toString());
        File[] categoryFiles = null;
        if (categoriesDirectory.exists())
        {
          categoryFiles = categoriesDirectory.listFiles();
          if (categoryFiles.length > 0)
            propertyTable.put(UDDIActionInputs.CHECK_USER_DEFINED_CATEGORIES,Boolean.TRUE);
        }

        // run the action
        if (!openRegAction.run())
            return false;
        
        if (categoryFiles != null && categoryFiles.length > 0)
        {
          RegistryElement regElement = (RegistryElement)(controller_.getUDDIPerspective().getNavigatorManager().getSelectedNode().getTreeElement());
          regElement.setCheckForUserDefinedCategories(true);
          Enumeration userDefinedCategories = regElement.getUserDefinedCategories();
          linkCategoryModelsWithSavedData(registryName,regElement.getUserDefinedCategories());
        }
        return true;
    }
    
    public final void linkCategoryModelsWithSavedData(String registryName,Enumeration userDefinedCategories)
    {
      StringBuffer directoryBuffer = new StringBuffer();
      FavoritesUDDIRegistryFolderElement.formCategoriesDirectory(directoryBuffer,controller_.getServletEngineStateLocation(),registryName);
      linkCategoryModelsWithSavedData(userDefinedCategories,directoryBuffer.toString());
    }
    
    public final void linkCategoryModelsWithSavedData(Enumeration userDefinedCategories,String categoriesDirectory)
    {
      File categoriesDirectoryFile = new File(categoriesDirectory);
      File[] categoryFiles = null;
      if (categoriesDirectoryFile.exists())
      {
        categoryFiles = categoriesDirectoryFile.listFiles(); 
        if (userDefinedCategories != null)
        {
          while (userDefinedCategories.hasMoreElements())
          {
            CategoryModel categoryModel = (CategoryModel)userDefinedCategories.nextElement();
            String categoryKey = categoryModel.getCategoryKey();
            for (int i=0;i<categoryFiles.length;i++)
            {
              String categoryPropertiesFileName = categoryFiles[i].getName();
              int lastDotPos = categoryPropertiesFileName.lastIndexOf(".");
              String decodedCategoryPropertiesFileNameBase = URLUtils.decode(categoryPropertiesFileName.substring(0,lastDotPos));
              if (lastDotPos > 0 && categoryPropertiesFileName.endsWith(".properties") && categoryKey.equals(decodedCategoryPropertiesFileNameBase))
              {
                categoryModel.setDefaultDataFile(categoryFiles[i].getAbsolutePath());
                categoryModel.loadFromDelimiterFile();
                break;
              }
            }            
          }
        }
      }
    }

    protected Vector getRegistryNodesByInquiryURL(String inquiryURL)
    {
      Vector registryNodes = null;
      NodeManager navigatorManager = controller_.getUDDIPerspective().getNavigatorManager();
      UDDIMainNode uddiMainNode = (UDDIMainNode)(navigatorManager.getRootNode());
      Vector childNodes = uddiMainNode.getChildNodes();
      for (int i=0;i<childNodes.size();i++)
      {
        Node childNode = (Node)childNodes.elementAt(i);
        if (childNode instanceof RegistryNode)
        {
          RegistryElement regElement = (RegistryElement)childNode.getTreeElement();
          String currInquiryURL = regElement.getInquiryURL();
          if (currInquiryURL != null && currInquiryURL.equals(inquiryURL))
          {
            if (registryNodes == null)
              registryNodes = new Vector();
            registryNodes.addElement(childNode);
          }
        }        
      }
      return registryNodes;
    }
    
    public static AddToUDDIPerspectiveAction newAction(Controller controller) {
        TreeElement selectedElement = controller.getFavoritesPerspective().getNodeManager().getSelectedNode().getTreeElement();
        FavoritesFolderElement favoritesFolderElement = null;

        if (selectedElement instanceof FavoritesFolderElement) {
            favoritesFolderElement = (FavoritesFolderElement)selectedElement;
        }
        else if (selectedElement instanceof FavoritesElement) {
            favoritesFolderElement = ((FavoritesElement)selectedElement).getParentFolderElement();
        }
        else {
            return null;
        }

        // return an instance of AddToUDDIPerspectiveAction depending on the type of
        // favorites folder element being selected.
        if (favoritesFolderElement instanceof FavoritesUDDIRegistryFolderElement)
            return new AddRegistryToUDDIPerspectiveAction(controller);
        else if (favoritesFolderElement instanceof FavoritesUDDIBusinessFolderElement)
            return new AddBusinessToUDDIPerspectiveAction(controller);
        else if (favoritesFolderElement instanceof FavoritesUDDIServiceFolderElement)
            return new AddServiceToUDDIPerspectiveAction(controller);
        else if (favoritesFolderElement instanceof FavoritesUDDIServiceInterfaceFolderElement)
            return new AddServiceInterfaceToUDDIPerspectiveAction(controller);
        else
            return null;
    }
}

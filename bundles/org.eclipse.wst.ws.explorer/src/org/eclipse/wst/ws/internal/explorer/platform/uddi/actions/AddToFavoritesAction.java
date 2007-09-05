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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;


import java.util.Enumeration;
import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.BusinessElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.CategoryModel;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceInterfaceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.BusinessNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.RegistryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.ServiceInterfaceNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.ServiceNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;

public class AddToFavoritesAction extends UDDINodeAction
{
  public AddToFavoritesAction(Controller controller)
  {
    super(controller);
  }

  public static String getActionLink(int nodeId)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/CheckFavoriteExistsActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    return actionLink.toString();
  }

  public final boolean registryExists(String registryName,Node registryNode)
  {
    registryNode.getTreeElement();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_REGISTRY_NAME,registryName);
    return favoriteExists(table,FavoritesModelConstants.REL_UDDI_REGISTRY_FOLDER_NODE);
  }

  public final boolean businessExists(String businessName,Node businessNode)
  {
    BusinessElement busElement = (BusinessElement)businessNode.getTreeElement();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    String businessKey = busElement.getBusinessEntity().getBusinessKey();
    String inquiryURL = regElement.getInquiryURL();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_NAME,businessName);
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_INQUIRY_API,inquiryURL);
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_KEY, businessKey);
    return favoriteExists(table,FavoritesModelConstants.REL_UDDI_BUSINESS_FOLDER_NODE);
  }

   public final boolean serviceExists(String serviceName,Node serviceNode)
  {
    ServiceElement serviceElement = (ServiceElement)serviceNode.getTreeElement();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    String serviceKey = serviceElement.getBusinessService().getServiceKey();
    String inquiryURL = regElement.getInquiryURL();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_NAME,serviceName);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_INQUIRY_API,inquiryURL);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_KEY,serviceKey);
    return favoriteExists(table,FavoritesModelConstants.REL_UDDI_SERVICE_FOLDER_NODE);
  }

  public final boolean serviceInterfaceExists(String siName,Node siNode)
  {
    ServiceInterfaceElement siElement = (ServiceInterfaceElement)siNode.getTreeElement();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    String tModelKey = siElement.getTModel().getTModelKey();
    String inquiryURL = regElement.getInquiryURL();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_INTERFACE_NAME,siName);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_INTERFACE_INQUIRY_API,inquiryURL);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_INTERFACE_KEY,tModelKey);
    return favoriteExists(table,FavoritesModelConstants.REL_UDDI_SERVICE_INTERFACE_FOLDER_NODE);
  }

  public final boolean addRegistryToFavorites(String registryName,Node registryNode)
  {
    RegistryElement regElement = (RegistryElement)registryNode.getTreeElement();
    String inquiryURL = regElement.getInquiryURL();
    String publishURL = regElement.getPublishURL();
    if (publishURL == null)
      publishURL = "";
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_REGISTRY_PUBLISH_API,publishURL);
    table.put(FavoritesModelConstants.PROP_UDDI_REGISTRY_INQUIRY_API,inquiryURL);
    table.put(FavoritesModelConstants.PROP_UDDI_REGISTRY_NAME,registryName);
    Enumeration userDefinedCategoriesEnum = regElement.getUserDefinedCategories();
    if (userDefinedCategoriesEnum != null)
    {
      table.put(FavoritesModelConstants.PROP_UDDI_REGISTRY_CATEGORIES_ENUM,userDefinedCategoriesEnum);
      table.put(FavoritesModelConstants.PROP_PLUGIN_METADATA_DIRECTORY,controller_.getServletEngineStateLocation());
    }
    return addToFavorites(table,FavoritesModelConstants.REL_UDDI_REGISTRY_FOLDER_NODE);
  }

  public final boolean addBusinessToFavorites(String businessName,Node businessNode)
  {
    BusinessElement busElement = (BusinessElement)businessNode.getTreeElement();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    String businessKey = busElement.getBusinessEntity().getBusinessKey();
    String inquiryURL = regElement.getInquiryURL();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_NAME,businessName);
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_INQUIRY_API,inquiryURL);
    table.put(FavoritesModelConstants.PROP_UDDI_BUSINESS_KEY, businessKey);
    return addToFavorites(table,FavoritesModelConstants.REL_UDDI_BUSINESS_FOLDER_NODE);
  }
  
  public final boolean addServiceToFavorites(String serviceName,Node serviceNode)
  {
    ServiceElement serviceElement = (ServiceElement)serviceNode.getTreeElement();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    String serviceKey = serviceElement.getBusinessService().getServiceKey();
    String inquiryURL = regElement.getInquiryURL();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_NAME,serviceName);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_INQUIRY_API,inquiryURL);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_KEY,serviceKey);
    return addToFavorites(table,FavoritesModelConstants.REL_UDDI_SERVICE_FOLDER_NODE);
  }
  
  public final boolean addServiceInterfaceToFavorites(String siName,Node siNode)
  {
    ServiceInterfaceElement siElement = (ServiceInterfaceElement)siNode.getTreeElement();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    String tModelKey = siElement.getTModel().getTModelKey();
    String inquiryURL = regElement.getInquiryURL();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_INTERFACE_NAME,siName);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_INTERFACE_INQUIRY_API,inquiryURL);
    table.put(FavoritesModelConstants.PROP_UDDI_SERVICE_INTERFACE_KEY,tModelKey);
    return addToFavorites(table,FavoritesModelConstants.REL_UDDI_SERVICE_INTERFACE_FOLDER_NODE);
  }

  public boolean favoriteExists()
  {
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    Node node = nodeManager_.getNode(nodeId);
    String nodeName = node.getNodeName();
    if (node instanceof RegistryNode)
      return registryExists(nodeName,node);
    else if (node instanceof BusinessNode)
      return businessExists(nodeName,node);
    else if (node instanceof ServiceNode)
      return serviceExists(nodeName,node);
    else if (node instanceof ServiceInterfaceNode)
      return serviceInterfaceExists(nodeName,node);
    else
      return false;
  }

  public boolean run()
  {
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    Node node = nodeManager_.getNode(nodeId);
    String nodeName = node.getNodeName();
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    boolean addToFavoritesResult = false;
    if (node instanceof RegistryNode)
    {
      addToFavoritesResult = addRegistryToFavorites(nodeName,node);
      RegistryElement regElement = (RegistryElement)node.getTreeElement();
      Enumeration e = regElement.getUserDefinedCategories();
      if (e != null)
      {
        while (e.hasMoreElements())
        {
          CategoryModel categoryModel = (CategoryModel)e.nextElement();
          Throwable errorException = categoryModel.getErrorException();
          if (errorException != null)
          {
            String[] args = {categoryModel.getDisplayName(),errorException.getMessage()};
            messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_SAVING_CATEGORY_DATA",args));
          }
        }
      }
    }
    else if (node instanceof BusinessNode)
      addToFavoritesResult = addBusinessToFavorites(nodeName,node);
    else if (node instanceof ServiceNode)
      addToFavoritesResult = addServiceToFavorites(nodeName,node);
    else if (node instanceof ServiceInterfaceNode)
      addToFavoritesResult = addServiceInterfaceToFavorites(nodeName,node);
    if (addToFavoritesResult)
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_FAVORITE_ADDED",nodeName));
    else
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_FAVORITE_NOT_ADDED",nodeName));
    return addToFavoritesResult;      
  }
  
  protected boolean addToFavorites(Hashtable table, String rel)
  {
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    NodeManager favNodeManager = favPerspective.getNodeManager();
    TreeElement favRootElement = favNodeManager.getRootNode().getTreeElement();
    Enumeration e = favRootElement.getElements(rel);
    if (!e.hasMoreElements())
      return false;
    FavoritesFolderElement favFolderElement = (FavoritesFolderElement)e.nextElement();
    return favFolderElement.addFavorite(table);
  }

  protected boolean favoriteExists(Hashtable table, String rel)
  {
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    NodeManager favNodeManager = favPerspective.getNodeManager();
    TreeElement favRootElement = favNodeManager.getRootNode().getTreeElement();
    Enumeration e = favRootElement.getElements(rel);
    if (!e.hasMoreElements())
      return false;
    FavoritesFolderElement favFolderElement = (FavoritesFolderElement)e.nextElement();
    return favFolderElement.favoriteExists(table);
  }
  
  public final String getActionLinkForHistory()
  {
    return null;
  }
}

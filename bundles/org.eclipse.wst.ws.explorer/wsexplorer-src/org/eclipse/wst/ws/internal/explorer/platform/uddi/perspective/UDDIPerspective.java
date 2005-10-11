/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.servlet.ServletContext;
import org.eclipse.wst.ws.internal.datamodel.BasicModel;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.favorites.FavoritesRegistryTypeDefault;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.actions.AddRegistryToUDDIPerspectiveAction;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesMainElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIRegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIRegistryFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Perspective;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.OpenRegistryAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.RegFindServiceUUIDAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.SwitchPerspectiveFromUDDIAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.CategoryModel;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.UDDIMainElement;
import org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils;
import org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;
import org.eclipse.wst.ws.internal.parser.favorites.IFavoritesUDDIRegistry;
import org.uddi4j.datatype.tmodel.TModel;

public class UDDIPerspective extends Perspective
{
  private Hashtable knownRegistries_;
  private IFavoritesUDDIRegistry ibmPublicUDDITestRegistry_;
  private Model navigatorModel_;
  private NodeManager navigatorManager_;
  private int wsdlType_;
  private Hashtable categoryManagers_;
  String categoryTModelKey_;
  
  // Window sizes
  private String perspectiveContentFramesetCols_;
  private String savedPerspectiveContentFramesetCols_;
  private String actionsContainerFramesetRows_;
  private String savedActionsContainerFramesetRows_;

  private final String IBM_TEST_REG_INQUIRY_URL = "http://uddi.ibm.com/testregistry/inquiryapi";
  private final String IBM_TEST_REG_PUBLISH_URL = "https://uddi.ibm.com/testregistry/publishapi";

  public UDDIPerspective(Controller controller)
  {
    super("uddi",controller);
  }

  public final void initPerspective(ServletContext application)
  {
    String defaultFavorites = controller_.getDefaultFavoritesLocation();
	FavoritesRegistryTypeDefault favRegTypeDefault = new FavoritesRegistryTypeDefault(defaultFavorites);
    IFavoritesUDDIRegistry[] favRegistriesDefault = favRegTypeDefault.getFavoritesUDDIRegistries();
    knownRegistries_ = new Hashtable();
    for (int i=0;i<favRegistriesDefault.length;i++)
    {
      if (favRegistriesDefault[i].getInquiryURL().equals(IBM_TEST_REG_INQUIRY_URL) && favRegistriesDefault[i].getPublishURL().equals(IBM_TEST_REG_PUBLISH_URL))
        ibmPublicUDDITestRegistry_ = favRegistriesDefault[i];
      knownRegistries_.put(favRegistriesDefault[i].getInquiryURL(),favRegistriesDefault[i]);
    }
    if (ibmPublicUDDITestRegistry_ == null && favRegistriesDefault.length > 0)
      ibmPublicUDDITestRegistry_ = favRegistriesDefault[0];
    
    navigatorModel_ = new BasicModel("uddiModel");
    UDDIMainElement uddiMainElement = new UDDIMainElement(getMessage("NODE_NAME_UDDI_MAIN"),navigatorModel_);
    navigatorModel_.setRootElement(uddiMainElement);
    navigatorManager_ = new NodeManager(controller_);
    UDDIMainNode uddiMainNode = new UDDIMainNode(uddiMainElement,navigatorManager_);
    navigatorManager_.setRootNode(uddiMainNode);

    wsdlType_ = UDDIActionInputs.WSDL_TYPE_SERVICE_INTERFACE;
    categoryManagers_ = new Hashtable();
    
    // Initialize the default category models.
    if (application.getAttribute("commonCategoryModels") == null)
    {
      synchronized (application)
      {
        if (application.getAttribute("commonCategoryModels") == null)
        {
          Hashtable commonCategoryModels = new Hashtable();
		  
          CategoryModel naicsModel = new CategoryModel();
          naicsModel.setServletContext(application);
          naicsModel.setDefaultDataFile("/uddi/data/naics-data.txt");
          naicsModel.setDisplayName(getMessage("FORM_OPTION_CATEGORY_NAICS"));
          naicsModel.setTModelKey(TModel.NAICS_TMODEL_KEY);
          commonCategoryModels.put(TModel.NAICS_TMODEL_KEY,naicsModel);
          
          CategoryModel unspscModel = new CategoryModel();
		  unspscModel.setServletContext(application);
          unspscModel.setDefaultDataFile("/uddi/data/unspsc-data.txt");
          unspscModel.setDisplayName(getMessage("FORM_OPTION_CATEGORY_UNSPSC_73"));
          unspscModel.setTModelKey(TModel.UNSPSC_73_TMODEL_KEY);
          commonCategoryModels.put(TModel.UNSPSC_73_TMODEL_KEY,unspscModel);
          
          CategoryModel geoModel = new CategoryModel();
		  geoModel.setServletContext(application);
          geoModel.setDefaultDataFile("/uddi/data/geo-data.txt");
          geoModel.setDisplayName(getMessage("FORM_OPTION_CATEGORY_GEO"));
          geoModel.setTModelKey(TModel.ISO_CH_TMODEL_KEY);
          commonCategoryModels.put(TModel.ISO_CH_TMODEL_KEY,geoModel);
          
          CategoryModel dWCommunityModel = new CategoryModel();
          dWCommunityModel.setServletContext(application);
          dWCommunityModel.setDefaultDataFile("/uddi/data/dWCommunity-data.txt");
          dWCommunityModel.setDisplayName(getMessage("FORM_OPTION_CATEGORY_DWCOMMUNITY"));
          dWCommunityModel.setTModelKey("UUID:8F497C50-EB05-11D6-B618-000629DC0A53");
          commonCategoryModels.put("UUID:8F497C50-EB05-11D6-B618-000629DC0A53",dWCommunityModel);
          
          application.setAttribute("commonCategoryModels",commonCategoryModels);
        }
      }
    }
    categoryTModelKey_ = null;
    
    // Starting frameset sizes.
    if (!DirUtils.isRTL())
      perspectiveContentFramesetCols_ = "30%,*";
    else
      perspectiveContentFramesetCols_ = "*,30%";
    savedPerspectiveContentFramesetCols_ = perspectiveContentFramesetCols_;
    actionsContainerFramesetRows_ = "75%,*";
    savedActionsContainerFramesetRows_ = actionsContainerFramesetRows_;
  }
  
  public final Hashtable getKnownRegistries()
  {
    // We must always query the favorites to see if additional registries were added over time. This property cannot be cached.
    FavoritesMainElement favMainElement = (FavoritesMainElement)(controller_.getFavoritesPerspective().getNodeManager().getRootNode().getTreeElement());
    FavoritesUDDIRegistryFolderElement favRegFolderElement = favMainElement.getFavoritesUDDIRegistryFolderElement();
    Enumeration e = favRegFolderElement.getAllFavorites();
    while (e.hasMoreElements())
    {
      FavoritesUDDIRegistryElement favRegElement = (FavoritesUDDIRegistryElement)e.nextElement();
      String inquiryURL = favRegElement.getInquiryURL();
      if (knownRegistries_.get(inquiryURL) == null)
        knownRegistries_.put(inquiryURL,favRegElement.getIFavoritesUDDIRegistryInterface());
    }
    return knownRegistries_;
  }

  public final String getKnownRegistryPublishURL(String inquiryURL)
  {
    IFavoritesUDDIRegistry knownRegistry = (IFavoritesUDDIRegistry)getKnownRegistries().get(inquiryURL);
    if (knownRegistry != null)
    {
      String publishURL = knownRegistry.getPublishURL();
      if (Validator.validateURL(publishURL))
        return publishURL;
    }
    return null;
  }

  public final String getKnownRegistryRegistrationURL(String inquiryURL)
  {
    IFavoritesUDDIRegistry knownRegistry = (IFavoritesUDDIRegistry)getKnownRegistries().get(inquiryURL);
    if (knownRegistry != null)
    {
      String registrationURL = knownRegistry.getRegistrationURL();
      if (Validator.validateURL(registrationURL))
        return registrationURL;
    }
    return null;    
  }

  public final IFavoritesUDDIRegistry getIBMPublicUDDITestRegistry()
  {
    return ibmPublicUDDITestRegistry_;
  }
  
  public final void preloadUDDIRegistries(String[] inquiryURLs, String[] publishURLs)
  {
    if (inquiryURLs != null)
    {
      Hashtable knownRegistries = getKnownRegistries();
      StringBuffer directoryBuffer = new StringBuffer();
      for (int i = 0; i < inquiryURLs.length; i++)
      {
		String inquiryURL = URLUtils.decode(inquiryURLs[i]);
        String publishURL = null;
        String regName = null;
        String registrationURL = null;
        // continue to the next registry if inquiryURL does not exist
        if (!Validator.validateURL(inquiryURL))
          continue;
        // populate and run an OpenRegistryAction
        OpenRegistryAction action = new OpenRegistryAction(controller_);
        Hashtable propertyTable = action.getPropertyTable();
        IFavoritesUDDIRegistry knownRegistry = (IFavoritesUDDIRegistry)knownRegistries.get(inquiryURL);
        boolean checkForUserDefinedCategories = false;
        File directoryFile = null;
        if (knownRegistry != null)
        {
          publishURL = knownRegistry.getPublishURL();
          regName = knownRegistry.getName();
          registrationURL = knownRegistry.getRegistrationURL();
          directoryBuffer.setLength(0);
          FavoritesUDDIRegistryFolderElement.formCategoriesDirectory(directoryBuffer,controller_.getServletEngineStateLocation(),regName);
          directoryFile = new File(directoryBuffer.toString());
          if (directoryFile.exists() && directoryFile.listFiles().length > 0)
            checkForUserDefinedCategories = true;
        }
        if (regName == null)
          regName = inquiryURL;
        if (publishURL == null && publishURLs != null && i < publishURLs.length && publishURLs[i] != null) {
          publishURL = URLUtils.decode(publishURLs[i]);
        }  
        // populate the action
        propertyTable.put(UDDIActionInputs.INQUIRY_URL, inquiryURL);
        propertyTable.put(UDDIActionInputs.REGISTRY_NAME, regName);
        if (Validator.validateURL(publishURL))
          propertyTable.put(UDDIActionInputs.PUBLISH_URL, publishURL);
        if (Validator.validateURL(registrationURL))
          propertyTable.put(UDDIActionInputs.REGISTRATION_URL, registrationURL);
          
        // Check if private registry category information is available for this registry.
        directoryBuffer.setLength(0);
        String encodedInquiryURL = URLUtils.encode(inquiryURL);
        directoryBuffer.append(controller_.getServletEngineStateLocation()).append(encodedInquiryURL).append(".properties");
        directoryFile = new File(directoryBuffer.toString());
        if (directoryFile.exists())
        {
          try
          {
            Properties p = new Properties();
            FileInputStream fin = new FileInputStream(directoryFile);
            p.load(fin);
            fin.close();
            directoryFile.delete();
            String dataDirectory = p.getProperty(UDDIActionInputs.CATEGORIES_DIRECTORY);
            if (dataDirectory != null)
              propertyTable.put(UDDIActionInputs.CATEGORIES_DIRECTORY,dataDirectory);
            checkForUserDefinedCategories = true;
          }
          catch (IOException e)
          {
            checkForUserDefinedCategories = false;
          }
        }

        if (checkForUserDefinedCategories)
          propertyTable.put(UDDIActionInputs.CHECK_USER_DEFINED_CATEGORIES,Boolean.TRUE);
        else
          propertyTable.remove(UDDIActionInputs.CHECK_USER_DEFINED_CATEGORIES);
                
        // run the action
        action.run();
        
        RegistryElement regElement = (RegistryElement)(navigatorManager_.getSelectedNode().getTreeElement());
        Enumeration userDefinedCategories = regElement.getUserDefinedCategories();
        if (userDefinedCategories != null && userDefinedCategories.hasMoreElements())
        {
          AddRegistryToUDDIPerspectiveAction action2 = new AddRegistryToUDDIPerspectiveAction(controller_);
          String categoriesDirectory = regElement.getCategoriesDirectory();
          if (categoriesDirectory != null)
            action2.linkCategoryModelsWithSavedData(userDefinedCategories,categoriesDirectory);
          else
            action2.linkCategoryModelsWithSavedData(regElement.getName(),userDefinedCategories);
        }
      }
      if (inquiryURLs.length > 0)
        controller_.setCurrentPerspective(ActionInputs.PERSPECTIVE_UDDI);
    }
  }
  
  // Preconditions: 
  // serviceNames and serviceKeys have length >= 1.
  // regNode != null
  private final void preloadServicesForRegistry(RegistryNode regNode,String[] serviceNames,String[] serviceKeys)
  {
    int regNodeId = regNode.getNodeId();
    for (int i=0;i<serviceKeys.length;i++)
    {
      navigatorManager_.setSelectedNodeId(regNodeId);
      RegFindServiceUUIDAction action = new RegFindServiceUUIDAction(controller_);
      Hashtable propertyTable = action.getPropertyTable();
      propertyTable.put(UDDIActionInputs.QUERY_NAME,serviceNames[i]);
      propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY,serviceKeys[i]);
      action.run();
      navigatorManager_.makeSelectedNodeVisible();
    }
  }
  
  public final void preloadServices(String[] inquiryURLs,String[] serviceNames,String[] serviceKeys)
  {
    // The algorithm will search for each service key in each inquiry URL.
    if (serviceKeys != null && serviceNames != null && serviceNames.length == serviceKeys.length)
    {
      for (int i=0;i<serviceKeys.length;i++)
      {
        if (inquiryURLs != null)
        {
          // Eliminate duplicate inquiry URLs.
          Hashtable inquiryURLsHash = new Hashtable();
          for (int j=0;j<inquiryURLs.length;j++)
            inquiryURLsHash.put(inquiryURLs[j],Boolean.TRUE);
          // Loop through all the available registries and use them if possible.
          Node uddiMainNode = navigatorManager_.getRootNode();
          Vector registryNodes = uddiMainNode.getChildNodes();
          for (int k=0;k<registryNodes.size();k++)
          {
            RegistryNode regNode = (RegistryNode)registryNodes.elementAt(k);
            RegistryElement regElement = (RegistryElement)regNode.getTreeElement();
            String existingRegistryInquiryURL = regElement.getInquiryURL();
            if (inquiryURLsHash.get(existingRegistryInquiryURL) != null)
            {
              preloadServicesForRegistry(regNode,serviceNames,serviceKeys);
              inquiryURLsHash.remove(existingRegistryInquiryURL);
            }
          }
          Enumeration inquiryURLsEnum = inquiryURLsHash.keys();
          while (inquiryURLsEnum.hasMoreElements())
          {
            String inquiryURL = (String)inquiryURLsEnum.nextElement();
            preloadUDDIRegistries(new String[] {inquiryURL},null);
            RegistryNode regNode = (RegistryNode)navigatorManager_.getSelectedNode();
            preloadServicesForRegistry(regNode,serviceNames,serviceKeys);
          }
        }
      }
    }
  }

  public final void setCategoryTModelKey(String categoryTModelKey)
  {
    categoryTModelKey_ = categoryTModelKey;
  }
  
  public final String getCategoryTModelKey()
  {
    return categoryTModelKey_;
  }
  
  private final void initCategoryBrowser(CategoryModel categoryModel,NodeManager nodeManager)
  {
    TreeElement categoryRootElement = (TreeElement)categoryModel.getRootElement();
    RootCategoryNode rootCategoryNode = new RootCategoryNode(categoryRootElement,nodeManager);
    nodeManager.setRootNode(rootCategoryNode);
    rootCategoryNode.createChildren();
  }
  
  public final NodeManager getCategoryManager()
  {
    return (NodeManager)categoryManagers_.get(categoryTModelKey_);
  }
  
  public final NodeManager getCategoryManager(CategoryModel categoryModel)
  {
    String tModelKey = categoryModel.getTModelKey();
    NodeManager categoryManager = (NodeManager)categoryManagers_.get(tModelKey);
    if (categoryManager == null)
    {
      categoryManager = new NodeManager(controller_);
      initCategoryBrowser(categoryModel,categoryManager);
      categoryManagers_.put(tModelKey,categoryManager);      
    }
    return categoryManager;
  }

  public final NodeManager getNavigatorManager()
  {
    return navigatorManager_;
  }
  
  public final NodeManager getNodeManager()
  {
    return getNavigatorManager();
  }

  public final void setWSDLType(int wsdlType)
  {
    wsdlType_ = wsdlType;
  }

  public final int getWSDLType()
  {
    return wsdlType_;
  }

  public final String getPerspectiveContentPage()
  {
    return "uddi/uddi_perspective_content.jsp";
  }

  public final int getPerspectiveId()
  {
    return ActionInputs.PERSPECTIVE_UDDI;
  }
  
  public final String getFramesetsFile()
  {
    return "uddi/scripts/uddiframesets.jsp";
  }
  
  public final String getProcessFramesetsForm()
  {
    return "uddi/forms/ProcessUDDIFramesetsForm.jsp";
  }

  public final String getPanesFile()
  {
    return "uddi/scripts/uddipanes.jsp";
  }
  
  public final String getTreeContentVar()
  {
    return "navigatorContent";
  }

  public final String getTreeContentPage()
  {
    return "uddi/navigator_content.jsp";
  }

  public final String getPropertiesContainerVar()
  {
    return "propertiesContainer";
  }

  public final String getPropertiesContainerPage()
  {
    return "uddi/properties_container.jsp";
  }

  public final String getStatusContentVar()
  {
    return "statusContent";
  }

  public final String getStatusContentPage()
  {
    return "uddi/status_content.jsp";
  }
  
  public final String getSavePerspectiveActionLink()
  {
    //return SaveUDDIPerspectiveAction.getActionLink();
    return "";
  }
  
  public final String getPerspectiveContentFramesetCols()
  {
    return perspectiveContentFramesetCols_;
  }
  
  public final void setPerspectiveContentFramesetCols(String cols)
  {
    perspectiveContentFramesetCols_ = cols;
  }
  
  public final void setSavedPerspectiveContentFramesetCols(String cols)
  {
    savedPerspectiveContentFramesetCols_ = cols;
  }
  
  public final String getSavedPerspectiveContentFramesetCols()
  {
    return savedPerspectiveContentFramesetCols_;
  }
  
  public final String getActionsContainerFramesetRows()
  {
    return actionsContainerFramesetRows_;
  }
  
  public final void setActionsContainerFramesetRows(String rows)
  {
    actionsContainerFramesetRows_ = rows;
  }
  
  public final void setSavedActionsContainerFramesetRows(String rows)
  {
    savedActionsContainerFramesetRows_ = rows;
  }
  
  public final String getSavedActionsContainerFramesetRows()
  {
    return savedActionsContainerFramesetRows_;
  }
  
  public final String getSwitchPerspectiveFormActionLink(int targetPerspectiveId,boolean forHistory)
  {
    return SwitchPerspectiveFromUDDIAction.getFormActionLink(targetPerspectiveId,forHistory);
  }
}

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
package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.tmodel.*;
import org.uddi4j.transport.*;
import org.uddi4j.response.*;
import org.uddi4j.util.*;
import org.uddi4j.*;

import java.util.*;
import java.net.*;

public class OpenRegistryAction extends UDDIPropertiesFormAction
{
  public OpenRegistryAction(Controller controller)
  {
    super(controller);
    // Select UDDI Main.
  }

  protected final boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String registryName = parser.getParameter(UDDIActionInputs.REGISTRY_NAME);
    String inquiryURL = parser.getParameter(UDDIActionInputs.INQUIRY_URL);
    String publishURL = parser.getParameter(UDDIActionInputs.PUBLISH_URL);
    String username = parser.getParameter(UDDIActionInputs.UDDI_USERNAME);
    String password = parser.getParameter(UDDIActionInputs.UDDI_PASSWORD);
    String checkUserDefinedCategoriesChecked = parser.getParameter(UDDIActionInputs.CHECK_USER_DEFINED_CATEGORIES);
    String categoriesDirectory = parser.getParameter(UDDIActionInputs.CATEGORIES_DIRECTORY);

    // Validate the data.
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    FormTool formTool = getSelectedFormTool();
    if (registryName != null)
      propertyTable_.put(UDDIActionInputs.REGISTRY_NAME,registryName);

    if (!Validator.validateString(registryName))
    {
      inputsValid = false;
      formTool.flagError(UDDIActionInputs.REGISTRY_NAME);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_REGISTRY_NAME"));
    }

    if (inquiryURL != null)
      propertyTable_.put(UDDIActionInputs.INQUIRY_URL,inquiryURL);
    if (!Validator.validateURL(inquiryURL))
    {
      inputsValid = false;
      formTool.flagError(UDDIActionInputs.INQUIRY_URL);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_INQUIRY_URL"));
    }

    if (publishURL != null)
      propertyTable_.put(UDDIActionInputs.PUBLISH_URL, publishURL);
    
    if (username != null)
      propertyTable_.put(UDDIActionInputs.UDDI_USERNAME, username);

    if (password != null)
      propertyTable_.put(UDDIActionInputs.UDDI_PASSWORD, password);

    if (checkUserDefinedCategoriesChecked != null)
      propertyTable_.put(UDDIActionInputs.CHECK_USER_DEFINED_CATEGORIES,checkUserDefinedCategoriesChecked);
    else
      removeProperty(UDDIActionInputs.CHECK_USER_DEFINED_CATEGORIES);
      
    if (categoriesDirectory != null)
      propertyTable_.put(UDDIActionInputs.CATEGORIES_DIRECTORY,categoriesDirectory);
    else
      removeProperty(UDDIActionInputs.CATEGORIES_DIRECTORY);

    formTool.updatePropertyTable(propertyTable_);
    return inputsValid;
  }

  public final void gatherWSUserDefinedCategories(UDDIProxy proxy,Hashtable categoryModels)
  {
    try
    {
      CategoryBag categoryBag = new CategoryBag();
      categoryBag.add(new KeyedReference("","categorization",TModel.TYPES_TMODEL_KEY));
      TModelList tModelList = proxy.find_tModel("%",categoryBag,null,null,100);
      TModelInfos tModelInfos = tModelList.getTModelInfos();
      Vector tModelKeyVector = new Vector();
      for (int i=0;i<tModelInfos.size();i++)
      {
        TModelInfo tModelInfo = tModelInfos.get(i);
        tModelKeyVector.addElement(tModelInfo.getTModelKey());
      }
      TModelDetail tModelDetail = proxy.get_tModelDetail(tModelKeyVector);
      Vector tModelVector = tModelDetail.getTModelVector();
      for (int i=0;i<tModelVector.size();i++)
      {
        TModel tModel = (TModel)tModelVector.elementAt(i);
        boolean checked = false;
        String displayName = null;
        String categoryKey = null;
        categoryBag = tModel.getCategoryBag();
        for (int j=0;j<categoryBag.size();j++)
        {
          KeyedReference kr = categoryBag.get(j);
          String krTModelKey = kr.getTModelKey();
          if (krTModelKey.equalsIgnoreCase(TModel.GENERAL_KEYWORDS_TMODEL_KEY))
          {
            String krKeyName = kr.getKeyName();
            if (krKeyName.equals("urn:x-ibm:uddi:customTaxonomy:key"))
              categoryKey = kr.getKeyValue();
            else if (krKeyName.equals("urn:x-ibm:uddi:customTaxonomy:displayName"))
              displayName = kr.getKeyValue();
          }
          else if (krTModelKey.equalsIgnoreCase(TModel.TYPES_TMODEL_KEY))
            checked = kr.getKeyValue().equals("checked");            
        }
        if (categoryKey != null)
        {
          if (displayName == null)
            displayName = tModel.getNameString();
          CategoryModel categoryModel = new CategoryModel();
          categoryModel.setCategoryKey(categoryKey);
          categoryModel.enableChecked(checked);
          categoryModel.setDisplayName(displayName);
          String tModelKey = tModel.getTModelKey();
          categoryModel.setTModelKey(tModelKey);
          categoryModels.put(tModelKey,categoryModel);
        }
      }
    }
    catch (UDDIException e)
    {
    }
    catch (TransportException e)
    {
    }
  }
  
  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    try
    {
      String registryName = (String)propertyTable_.get(UDDIActionInputs.REGISTRY_NAME);
      String inquiryURL = (String)propertyTable_.get(UDDIActionInputs.INQUIRY_URL);
      String publishURL = (String)propertyTable_.get(UDDIActionInputs.PUBLISH_URL);
      String registrationURL = (String)propertyTable_.get(UDDIActionInputs.REGISTRATION_URL);
      String username = (String)propertyTable_.get(UDDIActionInputs.UDDI_USERNAME);
      String password = (String)propertyTable_.get(UDDIActionInputs.UDDI_PASSWORD);
      boolean checkForUserDefinedCategoriesChecked = (propertyTable_.get(UDDIActionInputs.CHECK_USER_DEFINED_CATEGORIES) != null);
      String categoriesDirectory = (String)propertyTable_.get(UDDIActionInputs.CATEGORIES_DIRECTORY);

      String recognizedPublishURL = uddiPerspective.getKnownRegistryPublishURL(inquiryURL);
      String recognizedRegistrationURL = uddiPerspective.getKnownRegistryRegistrationURL(inquiryURL);
      
      if (publishURL == null)
        publishURL = recognizedPublishURL;
      if (registrationURL == null)
        registrationURL = recognizedRegistrationURL;
        
      UDDIMainNode uddiMainNode = (UDDIMainNode)(controller_.getUDDIPerspective().getNavigatorManager().getRootNode());
      UDDIMainElement uddiMainElement = (UDDIMainElement)uddiMainNode.getTreeElement();
      Properties props = new Properties();
      props.put(ActionInputs.TRANSPORT_CLASS_NAME, ActionInputs.TRASPORT_CLASS);
      UDDIProxy proxy = new UDDIProxy(props);
      proxy.setInquiryURL(new URL(inquiryURL));
      RegistryElement registryElement = new RegistryElement(proxy,inquiryURL,registryName,uddiMainElement.getModel());
      // Set registry element's publish URL and registration URL provided by external callers (e.g. favorites).
      if (publishURL != null)
        registryElement.setCachedPublishURL(publishURL);
      if (registrationURL != null)
        registryElement.setRegistrationURL(registrationURL);
      if (username != null)
      	registryElement.setUserId(username);
      if (password != null)
      	registryElement.setCred(password);
      registryElement.setCheckForUserDefinedCategories(checkForUserDefinedCategoriesChecked);
      if (checkForUserDefinedCategoriesChecked)
      {
        Hashtable categoryModels = new Hashtable();
        gatherWSUserDefinedCategories(proxy,categoryModels);
        if (categoryModels.size() > 0)
          registryElement.setUserDefinedCategories(categoryModels);
        else
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_NO_USER_DEFINED_CATEGORIES_FOUND"));
      }
      registryElement.setCategoriesDirectory(categoriesDirectory);
      uddiMainElement.connect(registryElement,UDDIModelConstants.REL_REGISTRIES,ModelConstants.REL_OWNER);
      NodeManager nodeManager = uddiMainNode.getNodeManager();
      int newRegistryNodeId = uddiMainNode.getChildNode(registryElement).getNodeId();
      // Select the new registry node and add the select action to the history.
      nodeManager.setSelectedNodeId(newRegistryNodeId);
      Node registryNode = nodeManager.getSelectedNode();
      ToolManager toolManager = registryNode.getCurrentToolManager();
      addToHistory(ActionInputs.PERSPECTIVE_UDDI,toolManager.getSelectedTool().getSelectToolActionHref(true));
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_REGISTRY_OPENED",new String[]{registryName,inquiryURL}));
      return true;
    }
    catch (MalformedURLException e)
    {
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("MalformedURLException");
      messageQueue.addMessage(e.getMessage());
      FormTool formTool = getSelectedFormTool();
      formTool.flagError(UDDIActionInputs.INQUIRY_URL);
    }
    /*
    catch (FormInputException e)
    {
      messageQueue.addMessage(e.getMessage());
    }
    */
    return false;
  }
}

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

import java.io.BufferedReader;
import java.io.StringReader;
import java.text.ParseException;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.CategoryModel;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;

public class UpdateRegistryAction extends UpdateAction
{
  public UpdateRegistryAction(Controller controller)
  {
    super(controller,false);
  }
  
  protected boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String nameModifiedState = parser.getParameter(UDDIActionInputs.NAME_MODIFIED);
    String name = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_REGISTRY_NAME);
    String[] userDefinedCategoryTModelKeys = parser.getParameterValues(UDDIActionInputs.CATEGORY_TMODEL_KEY);
    String[] userDefinedCategoryData = parser.getParameterValues(UDDIActionInputs.CATEGORY_FILENAME);
    
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    
    if (nameModifiedState != null && name != null)
    {
      boolean isModified = Boolean.valueOf(nameModifiedState).booleanValue();
      String newName;
      if (isModified)
        newName = name;
      else
        newName = (String)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_REGISTRY_NAME);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_REGISTRY_NAME,newName);
      if (!Validator.validateString(name))
      {
        inputsValid = false;
        formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_REGISTRY_NAME);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_NAME"));
      }
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_REGISTRY_NAME);
    
    if (userDefinedCategoryTModelKeys != null && userDefinedCategoryData != null)
    {
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      for (int i=0;i<userDefinedCategoryTModelKeys.length;i++)
      {
        CategoryModel userDefinedCategory = regElement.getUserDefinedCategory(userDefinedCategoryTModelKeys[i]);
//      TODO: Move UDDIPreferenceContext down to org.eclipse.wst.ws 
//      UDDIPreferenceContext context = WebServicePlugin.getInstance().getUDDIPreferenceContext();        
//      userDefinedCategory.setColumnDelimiter(context.getUddiCatDataColumnDelimiter());
//      userDefinedCategory.setStringDelimiter(context.getUddiCatDataStringDelimiter());
        userDefinedCategory.setColumnDelimiter("#");
        userDefinedCategory.setStringDelimiter("\"");
        String displayName = userDefinedCategory.getDisplayName();
        if (userDefinedCategoryData[i] == null || userDefinedCategoryData[i].length() < 1)
        {
          inputsValid = false;
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_USER_DEFINED_CATEGORY_DATA_FILE",displayName));
          formToolPI.flagRowError(UDDIActionInputs.USER_DEFINED_CATEGORIES,userDefinedCategoryTModelKeys[i]);
        }
        else
        {
          byte rc = userDefinedCategory.loadData(new BufferedReader(new StringReader(userDefinedCategoryData[i])));
          switch (rc)
          {
            case CategoryModel.OPERATION_SUCCESSFUL:
              messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_USER_DEFINED_CATEGORY_DATA_LOADED",displayName));
              break;
            case CategoryModel.ERROR_FILE:
              inputsValid = false;
              ParseException pe = (ParseException)userDefinedCategory.getErrorException();
              String[] args1 = {String.valueOf(pe.getErrorOffset()),displayName,pe.getMessage(),userDefinedCategory.getCategoryKey()};
              messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_USER_DEFINED_CATEGORY_DATA_FILE_FORMAT",args1));
              formToolPI.flagRowError(UDDIActionInputs.USER_DEFINED_CATEGORIES,userDefinedCategoryTModelKeys[i]);
              break;              
            case CategoryModel.ERROR_CATEGORY_KEY:
            default:
              inputsValid = false;
              String[] args2 = {displayName,userDefinedCategory.getErrorException().getMessage(),userDefinedCategory.getCategoryKey()};
              messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_USER_DEFINED_CATEGORY_DATA_FILE_KEY",args2));
              formToolPI.flagRowError(UDDIActionInputs.USER_DEFINED_CATEGORIES,userDefinedCategoryTModelKeys[i]);              
          }
        }
      }
    }
    return inputsValid;
  }

  public final boolean refreshFromRegistry()
  {
    return true;
  }
  
  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    String name = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_REGISTRY_NAME);
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    regElement.setName(name);
    regNode_.getTreeElement().setName(name);
    messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_REGISTRY_UPDATED",name));
    return true;
  }
}
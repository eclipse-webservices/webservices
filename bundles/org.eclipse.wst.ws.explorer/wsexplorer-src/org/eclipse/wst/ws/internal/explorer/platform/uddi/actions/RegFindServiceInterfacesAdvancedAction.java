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

import org.eclipse.wst.ws.internal.explorer.platform.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.binding.*;
import org.uddi4j.datatype.service.*;
import org.uddi4j.datatype.tmodel.*;
import org.uddi4j.response.*;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.*;
import org.uddi4j.UDDIException;

import java.util.*;
import java.net.*;

public class RegFindServiceInterfacesAdvancedAction extends FindAction
{
  public RegFindServiceInterfacesAdvancedAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES,String.valueOf(UDDIActionInputs.QUERY_STYLE_ADVANCED));
    propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_SEARCH_SET,String.valueOf(UDDIActionInputs.QUERY_MAX_SEARCH_SET));
    propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_RESULTS,String.valueOf(UDDIActionInputs.QUERY_MAX_RESULTS));
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String useServiceChecked = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_USE_SERVICE);
    String[] serviceNodeIds = parser.getParameterValues(UDDIActionInputs.NODEID_SERVICE);
    String ownedChecked = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_OWNED);
    String name = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME);
    String[] idTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_TYPE);
    String[] idKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_NAME);
    String[] idKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_VALUE);
    String[] catTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_TYPE);
    String[] catKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_NAME);
    String[] catKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_VALUE);
    String[] findQualifierValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_FINDQUALIFIER);
    String maxSearchSet = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_SEARCH_SET);
    String maxResults = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_RESULTS);

    // Validate the data.
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    regNode_.getTreeElement();

    if (useServiceChecked != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_USE_SERVICE,useServiceChecked);
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_USE_SERVICE);
    
    if (serviceNodeIds != null)
    {
      // Although we already have the "submitted" data, deletions occur on the client side and thus the need to track this using hidden form elements.
      Vector siService = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE);
      if (siService == null)
        siService = new Vector();
      else
        siService.removeAllElements();
      Vector siServiceCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE_COPY);
      // The browser will enforce the rule of having only one service in this list.
      ListElement listElement = (ListElement)siServiceCopy.elementAt(0);
      siService.addElement(listElement);
      BusinessService bs = (BusinessService)listElement.getObject();
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE,siService);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_BUSINESS_SERVICE,bs);
    }
    else
    {
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE);
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_BUSINESS_SERVICE);
      if (!subQueryInitiated_ && useServiceChecked != null)
      {
        inputsValid = false;
        formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_SERVICE"));
      }
    }
    
    if (ownedChecked != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_OWNED,ownedChecked);
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_OWNED);

    if (name != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME,name);

    if (idTypes != null && idKeyNames != null && idKeyValues != null)
    {
      IdentifierBag idBag = new IdentifierBag();
      for (int i=0;i<idKeyNames.length;i++)
      {
        KeyedReference kr = new KeyedReference(idKeyNames[i],idKeyValues[i],idTypes[i]);
        idBag.add(kr);
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS,idBag);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS);

    if (catTypes != null && catKeyNames != null && catKeyValues != null)
    {
      CategoryBag catBag = new CategoryBag();
      for (int i=0;i<catTypes.length;i++)
      {
        KeyedReference kr = new KeyedReference(catKeyNames[i],catKeyValues[i],catTypes[i]);
        catBag.add(kr);
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_CATEGORIES,catBag);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_CATEGORIES);

    if (findQualifierValues != null)
    {
      FindQualifiers findQualifiers = new FindQualifiers();
      Vector findQualifierVector = new Vector();
      for (int i=0;i<findQualifierValues.length;i++)
      {
        FindQualifier findQualifier = new FindQualifier(findQualifierValues[i]);
        findQualifierVector.addElement(findQualifier);
      }
      findQualifiers.setFindQualifierVector(findQualifierVector);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_FINDQUALIFIERS,findQualifiers);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_FINDQUALIFIERS);

    if (maxSearchSet != null)
    {
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_SEARCH_SET,maxSearchSet);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_SEARCH_SET,maxSearchSet);
    }
    if (!subQueryInitiated_ && !Validator.validateInteger(maxSearchSet))
    {
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_SEARCH_SET);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_MAX_SEARCH_SET"));
      inputsValid = false;
    }

    if (maxResults != null)
    {
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_RESULTS,maxResults);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_RESULTS,maxResults);
    }
    if (!subQueryInitiated_ && !Validator.validateInteger(maxResults))
    {
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_RESULTS);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_MAX_RESULTS"));
      inputsValid = false;
    }
    return inputsValid;
  }
  
  public final boolean overrideAuthenticationValidation()
  {
    return (propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_USE_SERVICE) != null);
  }

  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    try
    {
      boolean shouldAddQueryNode = (propertyTable_.get(UDDIActionInputs.QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE) == null);      
      boolean useService = (propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_USE_SERVICE) != null);
      BusinessService bs = (BusinessService)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_BUSINESS_SERVICE);
      String name = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME);
      FindQualifiers findQualifiers = (FindQualifiers)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_FINDQUALIFIERS);
      IdentifierBag identifierBag = (IdentifierBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS);
      CategoryBag categoryBag = (CategoryBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_CATEGORIES);
      boolean owned = (propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_OWNED) != null);
      String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
      int maxSearchSet = Integer.parseInt((String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_SEARCH_SET));
      int maxResults = Integer.parseInt((String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_RESULTS));

      // The action can be run under the context of either a registry or a query node.
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      UDDIProxy proxy = regElement.getProxy();
      Vector tModelKeyVector = new Vector();
      
      if (owned)
      {
        if (!regElement.isLoggedIn())
          regElement.performLogin(publishURL,userId,password);
        tModelKeyVector = findAllOwnedTModels(proxy, regElement.getAuthInfoString(), maxResults);
      }
      if (useService)
      {
        BindingTemplates bindingTemplates = bs.getBindingTemplates();
        boolean endSearch = false;
        if (bindingTemplates != null)
        {
          for (int i=0;i<bindingTemplates.size();i++)
          {
            BindingTemplate bt = bindingTemplates.get(i);
            TModelInstanceDetails tModelInstanceDetails = bt.getTModelInstanceDetails();
            for (int j=0;j<tModelInstanceDetails.size();j++)
            {
              TModelInstanceInfo tModelInstanceInfo = tModelInstanceDetails.get(j);
              if (tModelKeyVector.size() < maxResults)
                tModelKeyVector.addElement(tModelInstanceInfo.getTModelKey());
              else
              {
                endSearch = true;
                break;
              }
            }
            if (endSearch)
              break;
          }
        }
      }
      else
      {  
        TModelList tModelList = proxy.find_tModel(name,categoryBag,identifierBag,findQualifiers,maxSearchSet);
        TModelInfos tModelInfos = tModelList.getTModelInfos();
        for (int i=0;i<tModelInfos.size();i++)
        {
          TModelInfo tModelInfo = tModelInfos.get(i);
          if (tModelKeyVector.size() < maxResults)
            tModelKeyVector.addElement(tModelInfo.getTModelKey());
          else
            break;
        }
      }
      
      int finalNumberOfTModelKeys = tModelKeyVector.size();
      if (finalNumberOfTModelKeys > 0)
      {
        if (shouldAddQueryNode)
        {
          Vector finalTModelVector = new Vector();
          int currentIndex = 0;
          int windowSize = finalNumberOfTModelKeys;
          while (currentIndex < tModelKeyVector.size())
          {
            int subListToIndex = Math.min(currentIndex+windowSize,tModelKeyVector.size());
            try
            {
              TModelDetail tModelDetail = proxy.get_tModelDetail(new Vector(tModelKeyVector.subList(currentIndex,subListToIndex)));
              Vector tModelVector = tModelDetail.getTModelVector();
              for (int i=0;i<tModelVector.size();i++)
                finalTModelVector.addElement(tModelVector.elementAt(i));
              if (tModelDetail.getTruncatedBoolean())
                windowSize = tModelVector.size();
              currentIndex += windowSize;
            }
            catch (UDDIException e)
            {
              DispositionReport dr = e.getDispositionReport();
              if (((Result) dr.getResultVector().get(0)).getErrInfo().getErrCode() == DispositionReport.E_invalidKeyPassed)
              {
                String errInfoText = ((Result) dr.getResultVector().get(0)).getErrInfo().getText();
                for (int i=currentIndex;i<subListToIndex;i++)
                {
                  String tModelKey = (String)tModelKeyVector.elementAt(i);
                  if (errInfoText.indexOf(tModelKey) != -1 || errInfoText.indexOf(tModelKey.toUpperCase()) != -1 || errInfoText.indexOf(tModelKey.toLowerCase()) != -1)
                  {
                    tModelKeyVector.removeElementAt(i);
                    break;
                  }
                }
              }
              else
                throw e;
            }
            catch (TransportException e)
            {
              throw e;
            }
          }
          
          int finalNumberOfTModels = finalTModelVector.size();
          if (finalNumberOfTModels > 0)
          {
            TModel[] tModelArray = new TModel[finalNumberOfTModels];
            finalTModelVector.toArray(tModelArray);
            String queryName = (String)propertyTable_.get(UDDIActionInputs.QUERY_NAME);
            queryElement_ = new QueryElement(tModelArray,queryName,regElement.getModel());
            addQueryNode();
            messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICE_INTERFACES_FOUND",String.valueOf(tModelArray.length)));
          }
          else
            throw new FormInputException(uddiPerspective.getMessage("MSG_ERROR_NO_SERVICE_INTERFACES_FOUND"));
        }
        return true;
      }
      else
        throw new FormInputException(uddiPerspective.getMessage("MSG_ERROR_NO_SERVICE_INTERFACES_FOUND"));
    }
    catch (UDDIException e)
    {
      messageQueue.addMessage(uddiPerspective.getController().getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("UDDIException");
      messageQueue.addMessage(e.toString());
    }
    catch (TransportException e)
    {
      handleUnexpectedException(uddiPerspective,messageQueue,"TransportException",e);
    }
    catch (MalformedURLException e)
    {
      handleUnexpectedException(uddiPerspective,messageQueue,"MalformedURLException",e);
    }
    catch (FormInputException e)
    {
      messageQueue.addMessage(e.getMessage());
    }
    return false;
  }
  
  // Copied directly from UDDIWSDLProxy's findAllServiceInterfaces() method with minor modifications. The hardcoded strings should NOT be translated.
  private final Vector findAllOwnedTModels(UDDIProxy proxy, String authInfoString, int maxResults) throws UDDIException, TransportException
  {
    RegisteredInfo registeredInfo = null;

    // Get the complete list of owned (registered) info.
    // (A RegisteredInfo consists of BusinessInfos and TModelInfos.
    // BusinessInfos also contain ServiceInfos.)
    registeredInfo = proxy.get_registeredInfo(authInfoString);

    //Create a Vector to add the TModelKeys from the registeredInfo.
    Vector tModelKeyList = new Vector();

    //Get the TModelInfos from the registeredInfo.
    TModelInfos tModelInfos = registeredInfo.getTModelInfos();

    //Get all the TModelKeys from within the TModelInfos
    for (int i = 0; i < tModelInfos.size(); i++)
    {
      TModelInfo tModelInfo = tModelInfos.get(i);
      if (tModelKeyList.size() < maxResults)
        tModelKeyList.addElement(tModelInfo.getTModelKey());
      else
        break;
    }    
    return tModelKeyList;
  }  
}

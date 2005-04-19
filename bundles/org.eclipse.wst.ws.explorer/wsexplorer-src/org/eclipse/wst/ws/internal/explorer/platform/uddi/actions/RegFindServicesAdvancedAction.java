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
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.*;
import org.uddi4j.transport.TransportException;
import org.uddi4j.datatype.*;
import org.uddi4j.util.*;
import org.uddi4j.UDDIException;

import java.util.*;
import java.net.*;

public class RegFindServicesAdvancedAction extends FindAction
{
  public RegFindServicesAdvancedAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_SERVICES,String.valueOf(UDDIActionInputs.QUERY_STYLE_ADVANCED));
    propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_SEARCH_SET,String.valueOf(UDDIActionInputs.QUERY_MAX_SEARCH_SET));
    propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_RESULTS,String.valueOf(UDDIActionInputs.QUERY_MAX_RESULTS));
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String ownedChecked = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_OWNED);
    String[] busNodeIds = parser.getParameterValues(UDDIActionInputs.NODEID_BUSINESS);
    String[] languages = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE);
    String[] names = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME);
    String[] catTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_TYPE);
    String[] catKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_NAME);
    String[] catKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_VALUE);
    String[] siNodeIds = parser.getParameterValues(UDDIActionInputs.NODEID_SERVICE_INTERFACE);
    String[] findQualifierValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_FINDQUALIFIER);
    String maxSearchSet = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_SEARCH_SET);
    String maxResults = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_RESULTS);

    // Validate the data.
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();

    if (ownedChecked != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_OWNED,ownedChecked);
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_OWNED);

    if (busNodeIds != null)
    {
      // Although we already have the "submitted" data, deletions occur on the client side and thus the need to track this using hidden form elements.
      Vector serviceBusiness = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS);
      if (serviceBusiness == null)
        serviceBusiness = new Vector();
      else
        serviceBusiness.removeAllElements();
      Vector serviceBusinessCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS_COPY);
      // The browser will enforce the rule of having only one business in this list.
      ListElement listElement = (ListElement)serviceBusinessCopy.elementAt(0);
      serviceBusiness.addElement(listElement);
      BusinessEntity sp = (BusinessEntity)listElement.getObject();
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS,serviceBusiness);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_PROVIDER,sp);
    }
    else
    {
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS);
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_PROVIDER);
    }

    if (languages != null && names != null)
    {
      Vector nameVector = new Vector();
      for (int i=0;i<names.length;i++)
      {
        Name uddi4jName;
        if (languages[i].length() > 0)
          uddi4jName = new Name(names[i],languages[i]);
        else
          uddi4jName = new Name(names[i]);
        nameVector.addElement(uddi4jName);
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES,nameVector);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);

    if (catTypes != null && catKeyNames != null && catKeyValues != null)
    {
      CategoryBag catBag = new CategoryBag();
      for (int i=0;i<catTypes.length;i++)
      {
        KeyedReference kr = new KeyedReference(catKeyNames[i],catKeyValues[i],catTypes[i]);
        catBag.add(kr);
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES,catBag);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES);

    if (siNodeIds != null)
    {
      // Although we already have the "submitted" data, deletions occur on the client side and thus the need to track this using hidden form elements.
      Vector serviceServiceInterfaces = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES);
      if (serviceServiceInterfaces == null)
        serviceServiceInterfaces = new Vector();
      else
        serviceServiceInterfaces.removeAllElements();
      Vector serviceServiceInterfacesCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES_COPY);
      Vector tModelKeyStringVector = new Vector();
      Hashtable resultHash = new Hashtable();
      for (int i=0;i<siNodeIds.length;i++)
        resultHash.put(siNodeIds[i],Boolean.TRUE);
      for (int i=0;i<serviceServiceInterfacesCopy.size();i++)
      {
        ListElement listElement = (ListElement)serviceServiceInterfacesCopy.elementAt(i);
        if (resultHash.get(String.valueOf(listElement.getTargetNodeId())) != null)
        {
          serviceServiceInterfaces.addElement(listElement);
          TModel tModel = (TModel)listElement.getObject();
          tModelKeyStringVector.addElement(tModel.getTModelKey());
        }
      }
      TModelBag tModelBag = new TModelBag(tModelKeyStringVector);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES,serviceServiceInterfaces);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_TMODELBAG,tModelBag);
    }
    else
    {
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES);
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_TMODELBAG);
    }

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
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_FINDQUALIFIERS,findQualifiers);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_FINDQUALIFIERS);

    if (maxSearchSet != null)
    {
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_SEARCH_SET,maxSearchSet);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_SEARCH_SET,maxSearchSet);
    }
    if (!subQueryInitiated_ && !Validator.validateInteger(maxSearchSet))
    {
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_SEARCH_SET);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_MAX_SEARCH_SET"));
      inputsValid = false;
    }

    if (maxResults != null)
    {
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_RESULTS,maxResults);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_RESULTS,maxResults);
    }
    if (!subQueryInitiated_ && !Validator.validateInteger(maxResults))
    {
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_RESULTS);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_MAX_RESULTS"));
      inputsValid = false;
    }
    return inputsValid;
  }

  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    try
    {
      boolean shouldAddQueryNode = (propertyTable_.get(UDDIActionInputs.QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE) == null);      
      FindQualifiers findQualifiers = (FindQualifiers)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_FINDQUALIFIERS);
      BusinessEntity sp = (BusinessEntity)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_PROVIDER);
      Vector nameVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);
      CategoryBag categoryBag = (CategoryBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES);
      TModelBag tModelBag = (TModelBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_TMODELBAG);
      boolean owned = (propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_OWNED) != null);
      String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
      int maxSearchSet = Integer.parseInt((String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_SEARCH_SET));
      int maxResults = Integer.parseInt((String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_MAX_RESULTS));

      // The action can be run under the context of either a registry or a query node.
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      if (owned && !regElement.isLoggedIn())
        regElement.performLogin(publishURL,userId,password);
        
      UDDIProxy proxy = regElement.getProxy();
      String businessKey;
      if (sp != null)
        businessKey = sp.getBusinessKey();
      else
        businessKey = "";

      int finalNumberOfServiceKeys;
      Vector serviceKeyVector;
      if (owned)
      {
        serviceKeyVector = findAllOwnedServices(proxy,regElement.getAuthInfoString(),businessKey,maxResults);
        finalNumberOfServiceKeys = serviceKeyVector.size();
      }
      else
      {
        ServiceList serviceList = proxy.find_service(businessKey,nameVector,categoryBag,tModelBag,findQualifiers,maxSearchSet);
        ServiceInfos sInfos = serviceList.getServiceInfos();
        finalNumberOfServiceKeys = Math.min(maxResults,sInfos.size());
        serviceKeyVector = new Vector();
        for (int i=0;i<finalNumberOfServiceKeys;i++)
        {
          ServiceInfo sInfo = sInfos.get(i);
          serviceKeyVector.addElement(sInfo.getServiceKey());
        }
      }
      
      if (finalNumberOfServiceKeys > 0)
      {      
        if (shouldAddQueryNode)
        {
          Vector finalBusServiceVector = new Vector();
          int currentIndex = 0;
          int windowSize = finalNumberOfServiceKeys;
          while (currentIndex < serviceKeyVector.size())
          {
            int subListToIndex = Math.min(currentIndex+windowSize,serviceKeyVector.size());
            try
            {
              ServiceDetail serviceDetail = proxy.get_serviceDetail(new Vector(serviceKeyVector.subList(currentIndex,subListToIndex)));
              Vector busServiceVector = serviceDetail.getBusinessServiceVector();
              for (int i=0;i<busServiceVector.size();i++)
                finalBusServiceVector.addElement(busServiceVector.elementAt(i));
              if (serviceDetail.getTruncatedBoolean())
                windowSize = busServiceVector.size();
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
                  String serviceKey = (String)serviceKeyVector.elementAt(i);
                  if (errInfoText.indexOf(serviceKey) != -1 || errInfoText.indexOf(serviceKey.toUpperCase()) != -1 || errInfoText.indexOf(serviceKey.toLowerCase()) != -1)
                  {
                    serviceKeyVector.removeElementAt(i);
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
          
          int finalNumberOfBusinessServices = finalBusServiceVector.size();
          if (finalNumberOfBusinessServices > 0)
          {
            BusinessService[] bsArray = new BusinessService[finalNumberOfBusinessServices];
            finalBusServiceVector.toArray(bsArray);
            String queryName = (String)propertyTable_.get(UDDIActionInputs.QUERY_NAME);
            queryElement_ = new QueryElement(bsArray,queryName,regElement.getModel());
            addQueryNode();
            messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICES_FOUND",String.valueOf(bsArray.length)));
          }
          else
            throw new FormInputException(uddiPerspective.getMessage("MSG_ERROR_NO_SERVICES_FOUND"));
        }
        return true;
      }
      else
        throw new FormInputException(uddiPerspective.getMessage("MSG_ERROR_NO_SERVICES_FOUND"));
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
    catch (FormInputException e)
    {
      messageQueue.addMessage(e.getMessage());
    }
    catch (MalformedURLException e)
    {
      handleUnexpectedException(uddiPerspective,messageQueue,"MalformedURLException",e);
    }
    return false;
  }
  
  // Copied directly from UDDIWSDLProxy's findAllOwnedServices() method with minor modifications. The hardcoded strings should NOT be translated.
  private final Vector findAllOwnedServices(UDDIProxy proxy,String authInfoString,String businessKey,int maxResults) throws UDDIException, TransportException
  {
    RegisteredInfo registeredInfo = null;

    // Get the complete list of owned (registered) info.
    // (A RegisteredInfo consists of BusinessInfos and TModelInfos.
    // BusinessInfos also contain ServiceInfos.)
    registeredInfo = proxy.get_registeredInfo(authInfoString);

    //Create a vector to add the serviceKeys from the registeredInfo to.
    Vector serviceKeyList = new Vector();

    //Get the businessInfos from the registeredInfo.
    BusinessInfos businessInfos = registeredInfo.getBusinessInfos();

    //Get all the serviceKeys from the serviceInfos within the businessInfos,
    //and add them to the serviceKeyList.
    boolean endSearch = false;
    for (int i = 0; i < businessInfos.size(); i++)
    {
      BusinessInfo businessInfo = businessInfos.get(i);
      String currentBusinessKey = businessInfo.getBusinessKey();
      if (businessKey.length() > 0 && currentBusinessKey.equals(businessKey))
        endSearch = true;
      ServiceInfos serviceInfos = businessInfo.getServiceInfos();
      for (int j = 0; j < serviceInfos.size(); j++)
      {
        ServiceInfo serviceInfo = serviceInfos.get(j);
        if (serviceInfo.getBusinessKey().equals(currentBusinessKey))
        {
          if (serviceKeyList.size() < maxResults)
            serviceKeyList.add(serviceInfo.getServiceKey());
          else
          {
            endSearch = true;
            break;
          }
        }
      }
      if (endSearch)
        break;
    }    
    return serviceKeyList;
  }
}

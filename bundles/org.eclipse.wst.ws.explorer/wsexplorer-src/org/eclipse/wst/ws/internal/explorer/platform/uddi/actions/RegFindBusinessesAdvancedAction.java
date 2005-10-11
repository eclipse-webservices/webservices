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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.actions.FormInputException;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.QueryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.Name;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.BusinessInfo;
import org.uddi4j.response.BusinessInfos;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.DiscoveryURL;
import org.uddi4j.util.DiscoveryURLs;
import org.uddi4j.util.FindQualifier;
import org.uddi4j.util.FindQualifiers;
import org.uddi4j.util.IdentifierBag;
import org.uddi4j.util.KeyedReference;
import org.uddi4j.util.TModelBag;

public class RegFindBusinessesAdvancedAction extends FindAction
{
  public RegFindBusinessesAdvancedAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_BUSINESSES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_BUSINESSES,String.valueOf(UDDIActionInputs.QUERY_STYLE_ADVANCED));    
    propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_SEARCH_SET,String.valueOf(UDDIActionInputs.QUERY_MAX_SEARCH_SET));
    propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_RESULTS,String.valueOf(UDDIActionInputs.QUERY_MAX_RESULTS));
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String ownedChecked = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_OWNED);
    String[] languages = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE);
    String[] names = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME);
    String[] idTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_TYPE);
    String[] idKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_NAME);
    String[] idKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_VALUE);
    String[] catTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_TYPE);
    String[] catKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_NAME);
    String[] catKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_VALUE);
    String[] siNodeIds = parser.getParameterValues(UDDIActionInputs.NODEID_SERVICE_INTERFACE);
    String[] discoveryURLValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DISCOVERYURL);
    String[] findQualifierValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_FINDQUALIFIER);
    String maxSearchSet = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_SEARCH_SET);
    String maxResults = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_MAX_RESULTS);

    // Validate the data.
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    getSelectedNavigatorNode();
    getRegistryNode();
    regNode_.getTreeElement();

    if (ownedChecked != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_OWNED,ownedChecked);
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_OWNED);

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
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES,nameVector);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES);

    if (idTypes != null && idKeyNames != null && idKeyValues != null)
    {
      IdentifierBag idBag = new IdentifierBag();
      for (int i=0;i<idKeyNames.length;i++)
      {
        KeyedReference kr = new KeyedReference(idKeyNames[i],idKeyValues[i],idTypes[i]);
        idBag.add(kr);
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_IDENTIFIERS,idBag);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_IDENTIFIERS);

    if (catTypes != null && catKeyNames != null && catKeyValues != null)
    {
      CategoryBag catBag = new CategoryBag();
      for (int i=0;i<catTypes.length;i++)
      {
        KeyedReference kr = new KeyedReference(catKeyNames[i],catKeyValues[i],catTypes[i]);
        catBag.add(kr);
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_CATEGORIES,catBag);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_CATEGORIES);

    if (siNodeIds != null)
    {
      // Although we already have the "submitted" data, deletions occur on the client side and thus the need to track this using hidden form elements.
      Vector busServiceInterfaces = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES);
      if (busServiceInterfaces == null)
        busServiceInterfaces = new Vector();
      else
        busServiceInterfaces.removeAllElements();
      Vector busServiceInterfacesCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES_COPY);
      Vector tModelKeyStringVector = new Vector();
      Hashtable resultHash = new Hashtable();
      for (int i=0;i<siNodeIds.length;i++)
        resultHash.put(siNodeIds[i],Boolean.TRUE);
      for (int i=0;i<busServiceInterfacesCopy.size();i++)
      {
        ListElement listElement = (ListElement)busServiceInterfacesCopy.elementAt(i);
        if (resultHash.get(String.valueOf(listElement.getTargetNodeId())) != null)
        {
          busServiceInterfaces.addElement(listElement);
          TModel tModel = (TModel)listElement.getObject();
          tModelKeyStringVector.addElement(tModel.getTModelKey());
        }
      }
      TModelBag tModelBag = new TModelBag(tModelKeyStringVector);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES,busServiceInterfaces);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_TMODELBAG,tModelBag);
    }
    else
    {
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES);
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_TMODELBAG);
    }

    if (discoveryURLValues != null)
    {
      DiscoveryURLs discoveryURLs = new DiscoveryURLs();
      Vector discoveryURLVector = new Vector();
      for (int i=0;i<discoveryURLValues.length;i++)
      {
        DiscoveryURL discoveryURL = new DiscoveryURL(discoveryURLValues[i],"");
        discoveryURLVector.addElement(discoveryURL);
      }
      discoveryURLs.setDiscoveryURLVector(discoveryURLVector);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS,discoveryURLs);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS);

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
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_FINDQUALIFIERS,findQualifiers);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_FINDQUALIFIERS);

    if (maxSearchSet != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_SEARCH_SET,maxSearchSet);
    if (!subQueryInitiated_ && !Validator.validateInteger(maxSearchSet))
    {
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_SEARCH_SET);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_MAX_SEARCH_SET"));
      inputsValid = false;
    }

    if (maxResults != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_RESULTS,maxResults);
    if (!subQueryInitiated_ && !Validator.validateInteger(maxResults))
    {
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_RESULTS);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_MAX_RESULTS"));
      inputsValid = false;
    }
    return inputsValid;
  }

  private DiscoveryURLs dupDiscoveryURLs(DiscoveryURLs discoveryURLs, String useType, String dupUseType) {
    if (discoveryURLs == null)
      return discoveryURLs;
    Vector discoveryURLVector = discoveryURLs.getDiscoveryURLVector();
    if (discoveryURLVector == null)
      return discoveryURLs;
    Vector newDiscoveryURLVector = new Vector(discoveryURLVector);
    Enumeration e = discoveryURLVector.elements();
    while (e.hasMoreElements()) {
      DiscoveryURL discoveryURL = (DiscoveryURL)e.nextElement();
      if (discoveryURL.getUseType().equals(useType)) {
        newDiscoveryURLVector.add(new DiscoveryURL(discoveryURL.getText(), dupUseType));
      }
    }
    DiscoveryURLs newDiscoveryURLs = new DiscoveryURLs();
    newDiscoveryURLs.setDiscoveryURLVector(newDiscoveryURLVector);
    return newDiscoveryURLs;
  }

  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    try
    {
      boolean shouldAddQueryNode = (propertyTable_.get(UDDIActionInputs.QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE) == null);      
      FindQualifiers findQualifiers = (FindQualifiers)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_FINDQUALIFIERS);
      Vector nameVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES);
      DiscoveryURLs discoveryURLs = (DiscoveryURLs)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS);
      IdentifierBag identifierBag = (IdentifierBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_IDENTIFIERS);
      CategoryBag categoryBag = (CategoryBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_CATEGORIES);
      TModelBag tModelBag = (TModelBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_TMODELBAG);
      boolean owned = (propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_OWNED) != null);
      String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
      int maxSearchSet = Integer.parseInt((String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_SEARCH_SET));
      int maxResults = Integer.parseInt((String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_MAX_RESULTS));

      // The action can be run under the context of either a registry or a query node.
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      if (owned && !regElement.isLoggedIn())
        regElement.performLogin(publishURL,userId,password);

      UDDIProxy proxy = regElement.getProxy();

      BusinessInfos busInfos = null;
      if (owned)
        busInfos = proxy.get_registeredInfo(regElement.getAuthInfoString()).getBusinessInfos();
      else
        busInfos = proxy.find_business(nameVector, dupDiscoveryURLs(discoveryURLs,"",UDDIActionInputs.DISCOVERY_URL_TYPE), identifierBag, categoryBag, tModelBag, findQualifiers, maxSearchSet).getBusinessInfos();
      
      int finalNumberOfBusinessEntities = Math.min(maxResults, busInfos.size());
      Vector businessKeys = new Vector();
      for (int i = 0; i < finalNumberOfBusinessEntities; i++)
      {
        BusinessInfo busInfo = (BusinessInfo)busInfos.get(i);
        businessKeys.addElement(busInfo.getBusinessKey());
      }

      if (finalNumberOfBusinessEntities > 0)
      {
        if (shouldAddQueryNode)
        {
          Vector beVector = proxy.get_businessDetail(businessKeys).getBusinessEntityVector();
          BusinessEntity[] beList = new BusinessEntity[beVector.size()];
          beVector.toArray(beList);
          String queryName = (String)propertyTable_.get(UDDIActionInputs.QUERY_NAME);
          queryElement_ = new QueryElement(beList,queryName,regElement.getModel());
          addQueryNode();
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_BUSINESSES_FOUND",String.valueOf(beList.length)));
        }
        return true;
      }
      else
        throw new FormInputException(uddiPerspective.getMessage("MSG_ERROR_NO_BUSINESSES_FOUND"));
    }
    catch (TransportException e)
    {
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("TransportException");
      messageQueue.addMessage(e.getMessage());
    }
    catch (UDDIException e)
    {
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("UDDIException");
      messageQueue.addMessage(e.toString());
    }
    catch (FormInputException e)
    {
      messageQueue.addMessage(e.getMessage());
    }
    catch (MalformedURLException e)
    {
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("MalformedURLException");
      messageQueue.addMessage(e.getMessage());
    }
    return false;
  }
}

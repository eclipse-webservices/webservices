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
import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.Description;
import org.uddi4j.datatype.Name;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.DiscoveryURL;
import org.uddi4j.util.DiscoveryURLs;
import org.uddi4j.util.IdentifierBag;
import org.uddi4j.util.KeyedReference;

public class RegPublishBusinessAdvancedAction extends PublishAction
{
  public RegPublishBusinessAdvancedAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_BUSINESSES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_BUSINESSES,String.valueOf(UDDIActionInputs.QUERY_STYLE_ADVANCED));    
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String[] nameLanguages = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE);
    String[] names = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME);
    String[] descriptionLanguages = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION_LANGUAGE);
    String[] descriptions = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION);
    String[] idTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_TYPE);
    String[] idKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_NAME);
    String[] idKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_VALUE);
    String[] catTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_TYPE);
    String[] catKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_NAME);
    String[] catKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_VALUE);
    String[] discoveryURLValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DISCOVERYURL);

    // Validate the data.
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    Hashtable languageHash = new Hashtable();
    if (nameLanguages != null && names != null)
    {
      Vector nameVector = new Vector();
      // UDDI's save API (Appendix C of the UDDI V2 Programmers API Specification) requires the following:
      // 1) Only the first row can have a blank language.
      // 2) Only 1 name per language.
      String[] parameters = new String[2];
      parameters[0] = uddiPerspective.getMessage("FORM_LABEL_NAME");
      for (int i=0;i<names.length;i++)
      {
        parameters[1] = String.valueOf(i+1);
        Name uddi4jName;
        if (nameLanguages[i].length() > 0)
          uddi4jName = new Name(names[i],nameLanguages[i]);
        else
        {
          uddi4jName = new Name(names[i]);
          if (i != 0 && !subQueryInitiated_)
          {
            inputsValid = false;
            formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES,i);
            messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_BLANK_LANGUAGE",parameters));
          }
        }
        if (languageHash.get(nameLanguages[i]) != null)
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_DUPLICATE_LANGUAGE",parameters));
        }
        else
          languageHash.put(nameLanguages[i],Boolean.TRUE);
        if (names[i].trim().length() < 1)
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_INVALID_TEXT",parameters));
        }
        nameVector.addElement(uddi4jName);
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES,nameVector);
    }
    else
    {
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES);
      if (!subQueryInitiated_)
      {
        inputsValid = false;
        formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_NO_NAMES"));
      }
    }

    if (descriptionLanguages != null && descriptions != null)
    {
      Vector descriptionVector = new Vector();
      String[] parameters = new String[2];
      parameters[0] = uddiPerspective.getMessage("FORM_LABEL_DESCRIPTION");
      languageHash.clear();
      for (int i=0;i<descriptions.length;i++)
      {
        parameters[1] = String.valueOf(i+1);
        Description uddi4jDescription;
        if (descriptionLanguages[i].length() > 0)
          uddi4jDescription = new Description(descriptions[i],descriptionLanguages[i]);
        else
        {
          uddi4jDescription = new Description(descriptions[i]);
          if (i != 0 && !subQueryInitiated_)
          {
            inputsValid = false;
            formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS,i);
            messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_BLANK_LANGUAGE",parameters));
          }
        }
        if (languageHash.get(descriptionLanguages[i]) != null)
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_DUPLICATE_LANGUAGE",parameters));
        }
        else
          languageHash.put(descriptionLanguages[i],Boolean.TRUE);
        if (descriptions[i].trim().length() < 1)
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_INVALID_TEXT",parameters));
        }
        descriptionVector.addElement(uddi4jDescription);
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS,descriptionVector);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS);

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
      
    if (discoveryURLValues != null)
    {
      DiscoveryURLs discoveryURLs = new DiscoveryURLs();
      String[] parameters = new String[2];
      parameters[0] = uddiPerspective.getMessage("FORM_LABEL_DISCOVERYURL");
      for (int i=0;i<discoveryURLValues.length;i++)
      {
        parameters[1] = String.valueOf(i+1);
        if (!subQueryInitiated_ && !Validator.validateURL(discoveryURLValues[i]))
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_INVALID_TEXT",parameters));
        }
        discoveryURLs.add(new DiscoveryURL(discoveryURLValues[i],""));
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS,discoveryURLs);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS);      

    return inputsValid;
  }

  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    try
    {
      String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
      Vector nameVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES);
      Vector descriptionVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS);
      IdentifierBag identifierBag = (IdentifierBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_IDENTIFIERS);
      CategoryBag categoryBag = (CategoryBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_CATEGORIES);
      DiscoveryURLs discoveryURLs = (DiscoveryURLs)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS);

      // The action can be run under the context of either a registry or a query node.
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      if (!regElement.isLoggedIn())
        regElement.performLogin(publishURL,userId,password);
      UDDIProxy proxy = regElement.getProxy();

      BusinessEntity be = new BusinessEntity();
      be.setBusinessKey("");
      be.setNameVector(nameVector);
      be.setDescriptionVector(descriptionVector);
      be.setIdentifierBag(identifierBag);
      be.setCategoryBag(categoryBag);
      be.setDiscoveryURLs(discoveryURLs);
      Vector beVector = new Vector();
      beVector.add(be);

      be = (BusinessEntity)proxy.save_business(regElement.getAuthInfoString(), beVector).getBusinessEntityVector().get(0);
      addPublishedItemNode(be,regElement);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_BUSINESS_PUBLISHED",be.getDefaultNameString()));
      return true;
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
    catch (MalformedURLException e)
    {
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("MalformedURLException");
      messageQueue.addMessage(e.getMessage());
    }
    return false;
  }
}

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

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.transport.TransportException;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.*;
import org.uddi4j.util.*;

import java.util.*;
import java.net.*;

public class UpdateBusinessAction extends UpdateAction
{
  private boolean isUpdate_;
  public UpdateBusinessAction(Controller controller)
  {
    super(controller,true);
    isUpdate_ = true;
  }
  
  protected boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String uuidKey = parser.getParameter(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY);
    String[] discoveryURLModifiedStates = parser.getParameterValues(UDDIActionInputs.DISCOVERYURL_MODIFIED);
    String[] discoveryURLViewIds = parser.getParameterValues(UDDIActionInputs.DISCOVERYURL_VIEWID);
    String[] discoveryURLs = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DISCOVERYURL);
    String[] nameModifiedStates = parser.getParameterValues(UDDIActionInputs.NAME_MODIFIED);
    String[] nameViewIds = parser.getParameterValues(UDDIActionInputs.NAME_VIEWID);
    String[] nameLanguages = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE);
    String[] names = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME);
    String[] descriptionModifiedStates = parser.getParameterValues(UDDIActionInputs.DESCRIPTION_MODIFIED);
    String[] descriptionViewIds = parser.getParameterValues(UDDIActionInputs.DESCRIPTION_VIEWID);
    String[] descriptionLanguages = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION_LANGUAGE);
    String[] descriptions = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION);
    String[] idModifiedStates = parser.getParameterValues(UDDIActionInputs.IDENTIFIER_MODIFIED);
    String[] idViewIds = parser.getParameterValues(UDDIActionInputs.IDENTIFIER_VIEWID);
    String[] idTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_TYPE);
    String[] idKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_NAME);
    String[] idKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_VALUE);
    String[] catModifiedStates = parser.getParameterValues(UDDIActionInputs.CATEGORY_MODIFIED);
    String[] catViewIds = parser.getParameterValues(UDDIActionInputs.CATEGORY_VIEWID);
    String[] catTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_TYPE);
    String[] catKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_NAME);
    String[] catKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_VALUE);
    
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    
    if (uuidKey != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY,uuidKey);
    
    
    if (discoveryURLModifiedStates != null && discoveryURLViewIds != null && discoveryURLs != null)
    {
      Vector oldDiscoveryURLListElementVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS);
      Vector newDiscoveryURLListElementVector = new Vector();
      String[] parameters = new String[2];
      parameters[0] = uddiPerspective.getMessage("FORM_LABEL_DISCOVERYURL");
      for (int i=0;i<discoveryURLs.length;i++)
      {
        parameters[1] = String.valueOf(i+1);
        DiscoveryURL discoveryURL = new DiscoveryURL(discoveryURLs[i],"");
        if (!Validator.validateURL(discoveryURLs[i]))
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_INVALID_TEXT",parameters));
        }
        
        int discoveryURLViewId = Integer.parseInt(discoveryURLViewIds[i]);
        boolean isModified = Boolean.valueOf(discoveryURLModifiedStates[i]).booleanValue();
        
        if (discoveryURLViewId == -1 || isModified)
          newDiscoveryURLListElementVector.addElement(new ListElement(discoveryURL));
        else
          newDiscoveryURLListElementVector.addElement(oldDiscoveryURLListElementVector.elementAt(discoveryURLViewId));     
      }
      reindexListElementVector(newDiscoveryURLListElementVector);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS,newDiscoveryURLListElementVector);       
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS);
    
    Hashtable languageHash = new Hashtable();
    if (nameModifiedStates != null && nameViewIds != null && nameLanguages != null && names != null)
    {
      Vector oldNameListElementVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES);
      Vector newNameListElementVector = new Vector();
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
          if (i != 0)
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
        if (!Validator.validateString(names[i]))
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_INVALID_TEXT",parameters));
        }
        
        int nameViewId = Integer.parseInt(nameViewIds[i]);
        boolean isModified = Boolean.valueOf(nameModifiedStates[i]).booleanValue();
      
        if (nameViewId == -1 || isModified)
          newNameListElementVector.addElement(new ListElement(uddi4jName));
        else
          newNameListElementVector.addElement(oldNameListElementVector.elementAt(nameViewId));
      }
      reindexListElementVector(newNameListElementVector);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES,newNameListElementVector);
    }
    else
    {
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES);
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_NO_NAMES"));
    }
    
    if (descriptionModifiedStates != null && descriptionViewIds != null && descriptionLanguages != null && descriptions != null)
    {
      Vector oldDescriptionListElementVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS);
      Vector newDescriptionListElementVector = new Vector();
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
          if (i != 0)
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
        if (!Validator.validateString(descriptions[i]))
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_INVALID_TEXT",parameters));
        }
        
        int descriptionViewId = Integer.parseInt(descriptionViewIds[i]);
        boolean isModified = Boolean.valueOf(descriptionModifiedStates[i]).booleanValue();
      
        if (descriptionViewId == -1 || isModified)
          newDescriptionListElementVector.addElement(new ListElement(uddi4jDescription));
        else
          newDescriptionListElementVector.addElement(oldDescriptionListElementVector.elementAt(descriptionViewId));
      }
      reindexListElementVector(newDescriptionListElementVector);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS,newDescriptionListElementVector);      
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS);
      
    if (idModifiedStates != null && idViewIds != null && idTypes != null && idKeyNames != null && idKeyValues != null)
    {
      Vector oldIdListElementVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_IDENTIFIERS);
      Vector newIdListElementVector = new Vector();
      for (int i=0;i<idKeyNames.length;i++)
      {
        KeyedReference kr = new KeyedReference(idKeyNames[i],idKeyValues[i],idTypes[i]);
        int idViewId = Integer.parseInt(idViewIds[i]);
        boolean isModified = Boolean.valueOf(idModifiedStates[i]).booleanValue();
        if (idViewId == -1 || isModified)
          newIdListElementVector.addElement(new ListElement(kr));
        else
          newIdListElementVector.addElement(oldIdListElementVector.elementAt(idViewId));
      }
      reindexListElementVector(newIdListElementVector);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_IDENTIFIERS,newIdListElementVector);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_IDENTIFIERS);
      
    if (catModifiedStates != null && catViewIds != null && catTypes != null && catKeyNames != null && catKeyValues != null)
    {
      Vector oldCatListElementVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_CATEGORIES);
      Vector newCatListElementVector = new Vector();
      for (int i=0;i<catTypes.length;i++)
      {
        KeyedReference kr = new KeyedReference(catKeyNames[i],catKeyValues[i],catTypes[i]);
        int catViewId = Integer.parseInt(catViewIds[i]);
        boolean isModified = Boolean.valueOf(catModifiedStates[i]).booleanValue();
        if (catViewId == -1 || isModified)
          newCatListElementVector.addElement(new ListElement(kr));
        else
          newCatListElementVector.addElement(oldCatListElementVector.elementAt(catViewId));
      }
      reindexListElementVector(newCatListElementVector);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_CATEGORIES,newCatListElementVector);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_CATEGORIES);            
    return inputsValid;
  }

  public final boolean refreshFromRegistry()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    uddiPerspective.getMessageQueue();
    try
    {
      String uuidKey = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY);
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      UDDIProxy proxy = regElement.getProxy();
      Vector beVector = proxy.get_businessDetail(uuidKey).getBusinessEntityVector();
      BusinessEntity be;
      if (beVector.size() > 0)
        be = (BusinessEntity)beVector.get(0);
      else
        be = null;
      if (be != null)
      {
        propertyTable_.put(UDDIActionInputs.LATEST_OBJECT,be);
        return true;
      }
    }
    catch (TransportException e)
    {
      // Feedback via Javascript popups.
    }
    catch (UDDIException e)
    {
      // Feedback via Javascript popups.
    }
    isUpdate_ = false;
    return false;
  }
    
  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    try
    {
      BusinessEntity be = (BusinessEntity)propertyTable_.get(UDDIActionInputs.LATEST_OBJECT);
      String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
      Vector discoveryURLListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS);
      Vector nameListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES);
      Vector descriptionListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS);
      Vector idListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_IDENTIFIERS);
      Vector catListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_CATEGORIES);
      
      Vector discoveryURLVector = new Vector();
      if (discoveryURLListElementVector != null)
      {
        for (int i=0;i<discoveryURLListElementVector.size();i++)
        {
          ListElement listElement = (ListElement)discoveryURLListElementVector.elementAt(i);
          DiscoveryURL discoveryURL = (DiscoveryURL)listElement.getObject();
          discoveryURLVector.addElement(discoveryURL);
        }
      }
        
      Vector nameVector = new Vector();
      for (int i=0;i<nameListElementVector.size();i++)
      {
        ListElement listElement = (ListElement)nameListElementVector.elementAt(i);
        Name name = (Name)listElement.getObject();
        nameVector.addElement(name);
      }
      
      Vector descriptionVector = null;
      if (descriptionListElementVector != null)
      {
        descriptionVector = new Vector();
        for (int i=0;i<descriptionListElementVector.size();i++)
        {
          ListElement listElement = (ListElement)descriptionListElementVector.elementAt(i);
          Description description = (Description)listElement.getObject();
          descriptionVector.addElement(description);
        }
      }
      
      IdentifierBag idBag = null;
      if (idListElementVector != null)
      {
        idBag = new IdentifierBag();
        for (int i=0;i<idListElementVector.size();i++)
        {
          ListElement listElement = (ListElement)idListElementVector.elementAt(i);
          KeyedReference kr = (KeyedReference)listElement.getObject();
          idBag.add(kr);
        }
      }
      
      CategoryBag catBag = null;
      if (catListElementVector != null)
      {
        catBag = new CategoryBag();
        for (int i=0;i<catListElementVector.size();i++)
        {
          ListElement listElement = (ListElement)catListElementVector.elementAt(i);
          KeyedReference kr = (KeyedReference)listElement.getObject();
          catBag.add(kr);
        }
      }

      if (discoveryURLVector.size() > 0)
        be.setDiscoveryURLs(new DiscoveryURLs(discoveryURLVector));
      else
        be.setDiscoveryURLs(null);
      be.setNameVector(nameVector);
      be.setDescriptionVector(descriptionVector);
      be.setIdentifierBag(idBag);
      be.setCategoryBag(catBag);
      
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      if (!regElement.isLoggedIn())
        regElement.performLogin(publishURL,userId,password);
      UDDIProxy proxy = regElement.getProxy();
      Vector beVector = new Vector();
      beVector.add(be);
      be = (BusinessEntity)proxy.save_business(regElement.getAuthInfoString(), beVector).getBusinessEntityVector().get(0);
      refreshNode(be);
      if (isUpdate_)      
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_BUSINESS_UPDATED",be.getDefaultNameString()));
      else
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_BUSINESS_PUBLISHED",be.getDefaultNameString()));
      return true;
    }
    catch (TransportException e)
    {
      handleUnexpectedException(uddiPerspective,messageQueue,"TransportException",e);
    }
    catch (UDDIException e)
    {
      messageQueue.addMessage(uddiPerspective.getController().getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("UDDIException");
      messageQueue.addMessage(e.toString());
    }
    catch (MalformedURLException e)
    {
      handleUnexpectedException(uddiPerspective,messageQueue,"MalformedURLException",e);
    }    
    return false;
  }
}

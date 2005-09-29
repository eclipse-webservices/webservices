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
import org.eclipse.wst.ws.internal.explorer.platform.uddi.util.Uddi4jHelper;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.datatype.*;
import org.uddi4j.transport.TransportException;
import org.uddi4j.response.*;
import org.uddi4j.util.*;
import org.uddi4j.UDDIException;

import javax.wsdl.WSDLException;
import java.util.*;
import java.net.*;

public class UpdateServiceInterfaceAction extends UpdateAction
{
  private boolean isUpdate_;
  public UpdateServiceInterfaceAction(Controller controller)
  {
    super(controller,true);
    isUpdate_ = true;
  }
  
  protected boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String uuidKey = parser.getParameter(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY);
    String wsdlURLModifiedState = parser.getParameter(UDDIActionInputs.WSDL_URL_MODIFIED);
    String wsdlURL = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL);
    String nameModifiedState = parser.getParameter(UDDIActionInputs.NAME_MODIFIED);
    String name = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME);
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
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY,uuidKey);
    
    if (wsdlURLModifiedState != null && wsdlURL != null)
    {
      ListElement wsdlURLListElement = (ListElement)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL);      
      boolean isModified = Boolean.valueOf(wsdlURLModifiedState).booleanValue();
      if (isModified)
        wsdlURLListElement = new ListElement(wsdlURL);
      else
        wsdlURL = (String)wsdlURLListElement.getObject();
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL,wsdlURLListElement);
      if (!Validator.validateURL(wsdlURL))
      {
        inputsValid = false;
        formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_WSDL_URL"));
      }
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL);
    
    if (nameModifiedState != null && name != null)
    {
      ListElement nameListElement = (ListElement)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME);
      boolean isModified = Boolean.valueOf(nameModifiedState).booleanValue();
      if (isModified)
        nameListElement = new ListElement(name);
      else
        name = (String)nameListElement.getObject();
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME,nameListElement);
      if (!Validator.validateString(name))
      {
        inputsValid = false;
        formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_NAME"));
      }
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME);
      
    Hashtable languageHash = new Hashtable();
    if (descriptionModifiedStates != null && descriptionViewIds != null && descriptionLanguages != null && descriptions != null)
    {
      Vector oldDescriptionListElementVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS);
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
            formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS,i);
            messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_BLANK_LANGUAGE",parameters));
          }
        }
        if (languageHash.get(descriptionLanguages[i]) != null)
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_DUPLICATE_LANGUAGE",parameters));
        }
        else
          languageHash.put(descriptionLanguages[i],Boolean.TRUE);
        if (!Validator.validateString(descriptions[i]))
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS,i);
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
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS,newDescriptionListElementVector);      
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS);
      
    if (idModifiedStates != null && idViewIds != null && idTypes != null && idKeyNames != null && idKeyValues != null)
    {
      Vector oldIdListElementVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS);
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
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS,newIdListElementVector);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS);
      
    if (catModifiedStates != null && catViewIds != null && catTypes != null && catKeyNames != null && catKeyValues != null)
    {
      Vector oldCatListElementVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_CATEGORIES);
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
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_CATEGORIES,newCatListElementVector);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_CATEGORIES);            
    return inputsValid;
  }

  public final boolean refreshFromRegistry()
  {
    controller_.getUDDIPerspective();
    try
    {
      String uuidKey = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY);
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      UDDIProxy proxy = regElement.getProxy();
      TModelDetail tModelDetail = proxy.get_tModelDetail(uuidKey);
      Vector tModelVector = tModelDetail.getTModelVector();
      if (tModelVector.size() > 0)
      {
        propertyTable_.put(UDDIActionInputs.LATEST_OBJECT,tModelVector.elementAt(0));
        return true;
      }
    }
    catch (UDDIException e)
    {
      // Feedback via Javascript popups.
    }
    catch (TransportException e)
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
      String uuidKey = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY);      
      String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
      ListElement wsdlURLListElement = (ListElement)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL);
      ListElement nameListElement = (ListElement)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME);
      Vector descriptionListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS);
      Vector idListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS);
      Vector catListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_CATEGORIES);
      
      String wsdlURL = (String)wsdlURLListElement.getObject();
      
      String name = (String)nameListElement.getObject();
      
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

      TModel tModel = (new Uddi4jHelper()).newTModel(wsdlURL);
      if (isUpdate_)
        tModel.setTModelKey(uuidKey);
      tModel.setName(name);      
      tModel.setDescriptionVector(descriptionVector);
      tModel.setIdentifierBag(idBag);
      tModel.setCategoryBag(catBag);
      
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      if (!regElement.isLoggedIn())
        regElement.performLogin(publishURL,userId,password);
      UDDIProxy proxy = regElement.getProxy();
      Vector tModelVector = new Vector();
      tModelVector.add(tModel);
      tModel = (TModel)proxy.save_tModel(regElement.getAuthInfoString(), tModelVector).getTModelVector().get(0);
      refreshNode(tModel);
      if (isUpdate_)      
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICE_INTERFACE_UPDATED",tModel.getNameString()));
      else
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICE_INTERFACE_PUBLISHED",tModel.getNameString()));
      return true;
    }
    catch (WSDLException e)
    {
      handleUnexpectedException(uddiPerspective,messageQueue,"WSDLException",e);
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

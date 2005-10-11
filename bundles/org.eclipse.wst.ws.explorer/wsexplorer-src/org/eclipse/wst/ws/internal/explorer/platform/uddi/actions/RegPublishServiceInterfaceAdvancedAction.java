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
import javax.wsdl.WSDLException;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.util.Uddi4jHelper;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.Description;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.IdentifierBag;
import org.uddi4j.util.KeyedReference;

public class RegPublishServiceInterfaceAdvancedAction extends PublishAction
{
  public RegPublishServiceInterfaceAdvancedAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES,String.valueOf(UDDIActionInputs.QUERY_STYLE_ADVANCED));    
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String wsdlURL = parser.getParameter(ActionInputs.QUERY_INPUT_WSDL_URL);
    String name = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME);
    String[] descriptionLanguages = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION_LANGUAGE);
    String[] descriptions = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION);
    String[] idTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_TYPE);
    String[] idKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_NAME);
    String[] idKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_VALUE);
    String[] catTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_TYPE);
    String[] catKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_NAME);
    String[] catKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_VALUE);

    // Validate the data.
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    Hashtable languageHash = new Hashtable();

    if (wsdlURL != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL,wsdlURL);

    if (!subQueryInitiated_ && !Validator.validateString(wsdlURL))
    {
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_WSDL_URL"));
    }

    if (name != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME,name);

    if (!subQueryInitiated_ && !Validator.validateString(name))
    {
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_NAME"));
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
        if (descriptions[i].trim().length() < 1)
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_INVALID_TEXT",parameters));
        }
        descriptionVector.addElement(uddi4jDescription);
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS,descriptionVector);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS);

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
      String wsdlURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL);
      String name = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME);
      Vector descriptionVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS);
      IdentifierBag identifierBag = (IdentifierBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS);
      CategoryBag categoryBag = (CategoryBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_CATEGORIES);

      // The action can be run under the context of either a registry or a query node.
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      if (!regElement.isLoggedIn())
        regElement.performLogin(publishURL,userId,password);
      UDDIProxy proxy = regElement.getProxy();

      TModel tModel = (new Uddi4jHelper()).newTModel(wsdlURL);
      tModel.setName(name);
      tModel.setDescriptionVector(descriptionVector);
      tModel.setIdentifierBag(identifierBag);
      
      // The category bag may contain wsdlSpec.
      CategoryBag defaultCategoryBag = tModel.getCategoryBag();
      Vector defaultKeyedReferenceVector = null;
      if (defaultCategoryBag != null)
      {
        if (categoryBag == null)
          categoryBag = defaultCategoryBag;
        else
        {
          defaultKeyedReferenceVector = defaultCategoryBag.getKeyedReferenceVector();
          for (int i=0;i<defaultKeyedReferenceVector.size();i++)
          {
            KeyedReference kr = (KeyedReference)defaultKeyedReferenceVector.elementAt(i);
            if (categoryBag != null)
              categoryBag.add(kr);
          }
        }
      }
      tModel.setCategoryBag(categoryBag);

      Vector tModelVector = new Vector();
      tModelVector.add(tModel);
      tModel = (TModel)proxy.save_tModel(regElement.getAuthInfoString(), tModelVector).getTModelVector().get(0);
      propertyTable_.put(UDDIActionInputs.QUERY_OUTPUT_SAVED_TMODEL, tModel);
      addPublishedItemNode(tModel,regElement);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICE_INTERFACE_PUBLISHED",tModel.getNameString()));
      return true;
    }
    catch (WSDLException e)
    {
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("WSDLException");
      messageQueue.addMessage(e.getMessage());
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

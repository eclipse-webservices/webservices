/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070419 182864   gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
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
import org.uddi4j.datatype.Name;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.KeyedReference;

public class RegPublishServiceAdvancedAction extends PublishAction
{
  public RegPublishServiceAdvancedAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_SERVICES,String.valueOf(UDDIActionInputs.QUERY_STYLE_ADVANCED));    
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String busNodeIds = parser.getParameter(UDDIActionInputs.NODEID_BUSINESS);
    String[] serIntIds = parser.getParameterValues(UDDIActionInputs.NODEID_SERVICE_INTERFACE);
    String wsdlURL = parser.getParameter(ActionInputs.QUERY_INPUT_WSDL_URL);
    String[] nameLanguages = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE);
    String[] names = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME);    
    String[] descriptionLanguages = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION_LANGUAGE);
    String[] descriptions = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION);
    String[] catTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_TYPE);
    String[] catKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_NAME);
    String[] catKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_VALUE);

    // Validate the data.
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    if (busNodeIds != null)
    {
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
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY,sp.getBusinessKey());
    }
    else
    {
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS);
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_PROVIDER);
      if (!subQueryInitiated_)
      {
        inputsValid = false;
        formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_BUSINESS"));
      }      
    }
    
    if (serIntIds != null && serIntIds.length > 0)
    {
      Vector serviceInterfaces = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES);
      if (serviceInterfaces == null)
        serviceInterfaces = new Vector();
      else
        serviceInterfaces.removeAllElements();
      Vector serviceInterfacesCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES_COPY);
      Vector tmodels = new Vector();
      for (Iterator it = serviceInterfacesCopy.iterator(); it.hasNext();)
      {
        ListElement listElement = (ListElement)it.next();
        int targetNodeId = listElement.getTargetNodeId();
        for (int i = 0; i < serIntIds.length; i++)
        {
          try
          {
            if (targetNodeId == Integer.parseInt(serIntIds[i]))
            {
              serviceInterfaces.add(listElement);
              tmodels.add(listElement.getObject());
              break;
            }
          }
          catch (NumberFormatException nfe)
          {
          }
        }
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES, serviceInterfaces);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_TMODEL, tmodels);
    }
    else
    {
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES);
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_TMODEL);
    }
    
    if (wsdlURL != null)
    {
      propertyTable_.put(ActionInputs.QUERY_INPUT_WSDL_URL,wsdlURL);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL,wsdlURL);
    }

    if (!subQueryInitiated_ && !Validator.validateString(wsdlURL))
    {
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_WSDL_URL"));
    }

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
            formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES,i);
            messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_BLANK_LANGUAGE",parameters));
          }
        }
        if (languageHash.get(nameLanguages[i]) != null)
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_DUPLICATE_LANGUAGE",parameters));
        }
        else
          languageHash.put(nameLanguages[i],Boolean.TRUE);
        if (names[i].trim().length() < 1)
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_INVALID_TEXT",parameters));
        }
        nameVector.addElement(uddi4jName);
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES,nameVector);
    }
    else
    {
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);
      if (!subQueryInitiated_)
      {
        inputsValid = false;
        formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);
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
            formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS,i);
            messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_BLANK_LANGUAGE",parameters));
          }
        }
        if (languageHash.get(descriptionLanguages[i]) != null)
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_DUPLICATE_LANGUAGE",parameters));
        }
        else
          languageHash.put(descriptionLanguages[i],Boolean.TRUE);
        if (descriptions[i].trim().length() < 1)
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS,i);
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_ROW_INVALID_TEXT",parameters));
        }
        descriptionVector.addElement(uddi4jDescription);
      }
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS,descriptionVector);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS);

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
      BusinessEntity be = (BusinessEntity)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_PROVIDER);
      String businessKey = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY);
      Vector tModelVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_TMODEL);
      String wsdlURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL);
      Vector nameVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);
      Vector descriptionVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS);
      CategoryBag categoryBag = (CategoryBag)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES);

      // The action can be run under the context of either a registry or a query node.
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      if (!regElement.isLoggedIn())
        regElement.performLogin(publishURL,userId,password);
      UDDIProxy proxy = regElement.getProxy();

      Uddi4jHelper uddi4jHelper = new Uddi4jHelper();
      Definition def = uddi4jHelper.getWSDLDefinition(wsdlURL);

      BusinessService bs;
      if (tModelVector != null && tModelVector.size() > 0)
        bs = uddi4jHelper.newBusinessService(wsdlURL, def, (TModel[])tModelVector.toArray(new TModel[0]));
      else
      {
        RegPublishServiceInterfaceSimpleAction regPublishSIAction = new RegPublishServiceInterfaceSimpleAction(controller_);
        int currentNodeId = getSelectedNavigatorNode().getNodeId();
        NodeManager nodeManager = regNode_.getNodeManager();
        TModel[] tModels = null;
        if (uddi4jHelper.isMonolithicWSDL(def))
        {
          tModels = new TModel[1];
          tModels[0] = uddi4jHelper.newTModel(wsdlURL, def);
        }
        else
        {
          String[] imports = uddi4jHelper.getImports(def, wsdlURL);
          tModels = new TModel[imports.length];
          for (int i = 0; i < tModels.length; i++)
            tModels[i] = uddi4jHelper.newTModel(imports[i]);
        }
        Hashtable tModelsTable = new Hashtable();
        if (tModels != null)
        {
          for (int i=0;i<tModels.length;i++)
          {
            boolean inputsValid = regPublishSIAction.populatePropertyTable(uddi4jHelper.getWSDL(tModels[i]), tModels[i]);
            if (inputsValid)
            {
              regPublishSIAction.run();
              Object savedTModel = regPublishSIAction.getPropertyTable().get(UDDIActionInputs.QUERY_OUTPUT_SAVED_TMODEL);
              if (savedTModel != null)
                tModelsTable.put(((TModel)savedTModel).getNameString(), savedTModel);
              // Reselect the current node.
              nodeManager.setSelectedNodeId(currentNodeId);
            }
          }
        }
        bs = uddi4jHelper.newBusinessService(wsdlURL, def, tModelsTable);
      }
      
      if (be != null)
        bs.setBusinessKey(be.getBusinessKey());
      else if (businessKey != null)
        bs.setBusinessKey(businessKey);
      bs.setNameVector(nameVector);
      bs.setDescriptionVector(descriptionVector);
      bs.setCategoryBag(categoryBag);
      Vector bsVector = new Vector();
      bsVector.add(bs);
      regElement.handlePreInvocation(bs);
      bs = (BusinessService)proxy.save_service(regElement.getAuthInfoString(), bsVector).getBusinessServiceVector().get(0);
      addPublishedItemNode(bs,regElement);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICE_PUBLISHED",bs.getDefaultNameString()));
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
    	if(UDDIExceptionHandler.requiresReset(e)){
    		RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    		regElement.setDefaults();
    	}
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

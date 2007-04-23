/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.RegPublishTool;
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
import org.uddi4j.response.ServiceDetail;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.KeyedReference;

public class UpdateServiceAction extends UpdateAction
{
  private boolean isUpdate_;
  public UpdateServiceAction(Controller controller)
  {
    super(controller,true);
    isUpdate_ = true;
  }

  protected boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String uuidBusinessKey = parser.getParameter(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY);
    String uuidServiceKey = parser.getParameter(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY);
    String wsdlURLModifiedState = parser.getParameter(UDDIActionInputs.WSDL_URL_MODIFIED);
    String wsdlURL = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL);
    String[] nameModifiedStates = parser.getParameterValues(UDDIActionInputs.NAME_MODIFIED);
    String[] nameViewIds = parser.getParameterValues(UDDIActionInputs.NAME_VIEWID);
    String[] nameLanguages = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME_LANGUAGE);
    String[] names = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_NAME);
    String[] descriptionModifiedStates = parser.getParameterValues(UDDIActionInputs.DESCRIPTION_MODIFIED);
    String[] descriptionViewIds = parser.getParameterValues(UDDIActionInputs.DESCRIPTION_VIEWID);
    String[] descriptionLanguages = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION_LANGUAGE);
    String[] descriptions = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_DESCRIPTION);
    String[] catModifiedStates = parser.getParameterValues(UDDIActionInputs.CATEGORY_MODIFIED);
    String[] catViewIds = parser.getParameterValues(UDDIActionInputs.CATEGORY_VIEWID);
    String[] catTypes = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_TYPE);
    String[] catKeyNames = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_NAME);
    String[] catKeyValues = parser.getParameterValues(UDDIActionInputs.QUERY_INPUT_ADVANCED_CATEGORY_KEY_VALUE);

    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    if (uuidBusinessKey != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY,uuidBusinessKey);

    if (uuidServiceKey != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY,uuidServiceKey);

    if (wsdlURLModifiedState != null && wsdlURL != null)
    {
      ListElement wsdlURLListElement = (ListElement)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL);
      boolean isModified = Boolean.valueOf(wsdlURLModifiedState).booleanValue();
      if (isModified)
        wsdlURLListElement = new ListElement(wsdlURL);
      else
        wsdlURL = (String)wsdlURLListElement.getObject();
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL,wsdlURLListElement);
      if (!Validator.validateURL(wsdlURL))
      {
        inputsValid = false;
        formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_WSDL_URL"));
      }
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL);

    Hashtable languageHash = new Hashtable();
    if (nameModifiedStates != null && nameViewIds != null && nameLanguages != null && names != null)
    {
      Vector oldNameListElementVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);
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
        if (!Validator.validateString(names[i]))
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES,i);
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
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES,newNameListElementVector);
    }
    else
    {
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_NO_NAMES"));
    }

    if (descriptionModifiedStates != null && descriptionViewIds != null && descriptionLanguages != null && descriptions != null)
    {
      Vector oldDescriptionListElementVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS);
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
        if (!Validator.validateString(descriptions[i]))
        {
          inputsValid = false;
          formToolPI.flagRowError(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS,i);
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
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS,newDescriptionListElementVector);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS);

    if (catModifiedStates != null && catViewIds != null && catTypes != null && catKeyNames != null && catKeyValues != null)
    {
      Vector oldCatListElementVector = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES);
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
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES,newCatListElementVector);
    }
    else
      removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES);
    return inputsValid;
  }

  public final boolean refreshBusinessFromRegistry()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    uddiPerspective.getMessageQueue();
    try
    {
      String uuidBusinessKey = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY);
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      UDDIProxy proxy = regElement.getProxy();
      Vector beVector = proxy.get_businessDetail(uuidBusinessKey).getBusinessEntityVector();
      BusinessEntity be;
      if (beVector.size() > 0)
        be = (BusinessEntity)beVector.get(0);
      else
        be = null;
      if (be != null)
      {
        propertyTable_.put(UDDIActionInputs.LATEST_BUSINESS,be);
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
    return false;
  }
  
  public final boolean refreshFromRegistry()
  {
    controller_.getUDDIPerspective();
    try
    {
      String uuidServiceKey = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY);
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      UDDIProxy proxy = regElement.getProxy();
      ServiceDetail serviceDetail = proxy.get_serviceDetail(uuidServiceKey);
      Vector businessServiceVector = serviceDetail.getBusinessServiceVector();
      if (businessServiceVector.size() > 0)
      {
        propertyTable_.put(UDDIActionInputs.LATEST_OBJECT,businessServiceVector.elementAt(0));
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
  
  public final RegPublishTool setupRegPublishTool()
  {
    // Prepare for re-publishing a now-stale service whose business has also been eliminated.
    RegPublishTool regPublishTool = regNode_.getRegPublishTool();
    regPublishTool.setProperty(UDDIActionInputs.SUBQUERY_KEY,"");
    regPublishTool.setProperty(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICES));
    regPublishTool.setProperty(UDDIActionInputs.QUERY_STYLE_SERVICES,String.valueOf(UDDIActionInputs.QUERY_STYLE_ADVANCED));
    
    regPublishTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL,propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL));
    
    Vector nameListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);
    Vector descriptionListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS);
    Vector catListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES);
    
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
    else
      regPublishTool.removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS);
    
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
    else
      regPublishTool.removeProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES);
    
    regPublishTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES,nameVector);
    if (descriptionVector != null)
      regPublishTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS,descriptionVector);
    if (catBag != null)
      regPublishTool.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES,catBag);
    return regPublishTool;
  }

  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();    
    try
    {
      BusinessEntity be = (BusinessEntity)propertyTable_.get(UDDIActionInputs.LATEST_BUSINESS);
      String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
      String uuidServiceKey = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY);
      ListElement wsdlURLListElement = (ListElement)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL);
      Vector nameListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_NAMES);
      Vector descriptionListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS);
      Vector catListElementVector = (Vector)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES);

      String wsdlURL = (String)wsdlURLListElement.getObject();
            
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

      if (!regElement.isLoggedIn())
      	regElement.performLogin(publishURL,userId,password);
      UDDIProxy proxy = regElement.getProxy();

      // Note: The JSP will prevent this routine from executing if be is null.
      Uddi4jHelper uddi4jHelper = new Uddi4jHelper();
      Definition def = uddi4jHelper.getWSDLDefinition(wsdlURL);
      
      // Publish/update the service interfaces first.
      RegPublishServiceInterfaceSimpleAction regPublishSIAction = new RegPublishServiceInterfaceSimpleAction(controller_);
      int currentNodeId = getSelectedNavigatorNode().getNodeId();
      NodeManager nodeManager = regNode_.getNodeManager();

      Hashtable tModelsTable = new Hashtable();
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
            {
      	      tModelsTable.put(((TModel)savedTModel).getNameString(), savedTModel);
      	    }
            // Reselect the current node.
      	    nodeManager.setSelectedNodeId(currentNodeId);
          }
      	}
      }
      
      BusinessService busService = uddi4jHelper.newBusinessService(wsdlURL, def, tModelsTable);
      if (isUpdate_)
        busService.setServiceKey(uuidServiceKey);
      busService.setBusinessKey(be.getBusinessKey());
      busService.setNameVector(nameVector);
      busService.setDescriptionVector(descriptionVector);
      busService.setCategoryBag(catBag);
      regElement.handlePreInvocation(busService);      
      Vector bsVector = new Vector();
      bsVector.add(busService);
      busService = (BusinessService)proxy.save_service(regElement.getAuthInfoString(), bsVector).getBusinessServiceVector().get(0);
      refreshNode(busService);
      if (isUpdate_)
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICE_UPDATED",busService.getDefaultNameString()));
      else
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICE_PUBLISHED",busService.getDefaultNameString()));
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

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

import java.net.MalformedURLException;
import java.util.Hashtable;
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
import org.uddi4j.datatype.Name;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.transport.TransportException;

public class RegPublishServiceSimpleAction extends PublishAction
{
  public RegPublishServiceSimpleAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_SERVICES,String.valueOf(UDDIActionInputs.QUERY_STYLE_SIMPLE));    
  }

  protected boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {    
    String busNodeIds = parser.getParameter(UDDIActionInputs.NODEID_BUSINESS);
    String wsdlURL = parser.getParameter(ActionInputs.QUERY_INPUT_WSDL_URL);
    String name = parser.getParameter(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME);
    String description = parser.getParameter(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_DESCRIPTION);

    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    if (busNodeIds != null)
    {
      Vector serviceBusiness = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS);
      if (serviceBusiness == null)
        serviceBusiness = new Vector();
      else
        serviceBusiness.removeAllElements();
      Vector serviceBusinessCopy = (Vector)formToolPI.getProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS_COPY);
      // The browser will enforce the rule of having only one business in this list.
      ListElement listElement = (ListElement)serviceBusinessCopy.elementAt(0);
      serviceBusiness.addElement(listElement);
      BusinessEntity be = (BusinessEntity)listElement.getObject();
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS,serviceBusiness);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_SERVICE_PROVIDER,be);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY,be.getBusinessKey());
    }
    else
    {
      removeProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS);
      removeProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_SERVICE_PROVIDER);
      if (!subQueryInitiated_)
      {
        inputsValid = false;
        formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS);
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_BUSINESS"));
      }
    }

    if (wsdlURL != null)
    {
      propertyTable_.put(ActionInputs.QUERY_INPUT_WSDL_URL,wsdlURL);
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_WSDL_URL,wsdlURL);
    }

    if (!subQueryInitiated_ && !Validator.validateString(wsdlURL))
    {
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_WSDL_URL);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_WSDL_URL"));
    }

    if (name != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME,name);

    if (!subQueryInitiated_ && !Validator.validateString(name))
    {
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_NAME"));
    }

    if (description != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_DESCRIPTION,description);

    return inputsValid;
  }

  public final boolean run()
  {
    String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
    String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
    String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
    BusinessEntity be = (BusinessEntity)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_SERVICE_PROVIDER);
    String businessKey = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY);
    String wsdlURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_WSDL_URL);
    String name = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME);
    String description = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_DESCRIPTION);
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    try
    {
      UDDIProxy proxy = regElement.getProxy();
      if (!regElement.isLoggedIn())
        regElement.performLogin(publishURL,userId,password);
      
      Uddi4jHelper uddi4jHelper = new Uddi4jHelper();
      Definition def = uddi4jHelper.getWSDLDefinition(wsdlURL);
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

      BusinessService bs = uddi4jHelper.newBusinessService(wsdlURL, def, tModelsTable);
      if (be != null)
        bs.setBusinessKey(be.getBusinessKey());
      else if (businessKey != null)
        bs.setBusinessKey(businessKey);
      bs.setDefaultName(new Name(name));
      bs.setDefaultDescriptionString(description);
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

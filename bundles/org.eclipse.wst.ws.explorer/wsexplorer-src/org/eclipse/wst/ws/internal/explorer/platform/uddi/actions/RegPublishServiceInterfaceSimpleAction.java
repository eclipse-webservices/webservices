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

import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.util.Uddi4jHelper;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.transport.TransportException;
import org.uddi4j.datatype.tmodel.TModel;

import javax.wsdl.WSDLException;
import java.util.*;
import java.net.*;

public class RegPublishServiceInterfaceSimpleAction extends PublishAction
{
  public RegPublishServiceInterfaceSimpleAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES,String.valueOf(UDDIActionInputs.QUERY_STYLE_SIMPLE));    
  }

  protected boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String wsdlURL = parser.getParameter(ActionInputs.QUERY_INPUT_WSDL_URL);
    String name = parser.getParameter(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME);
    String description = parser.getParameter(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_DESCRIPTION);

    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    if (wsdlURL != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_WSDL_URL,wsdlURL);

    if (!subQueryInitiated_ && !Validator.validateString(wsdlURL))
    {
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_WSDL_URL);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_WSDL_URL"));
    }

    if (name != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME,name);

    if (!subQueryInitiated_ && !Validator.validateString(name))
    {
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_NAME"));
    }

    if (description != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_DESCRIPTION,description);

    return inputsValid;
  }
  
  public final boolean populatePropertyTable(String wsdlURL, TModel tModel)
  {
    boolean inputsValid = true;
    String name = tModel.getNameString();
    String description = tModel.getDefaultDescriptionString();
    if (wsdlURL != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_WSDL_URL,wsdlURL);
    else
      inputsValid = false;
    
    if (name != null) 
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME,name);
    else
      inputsValid = false;
    
    if (description != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_DESCRIPTION,description);
    else
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_DESCRIPTION,"");
    return inputsValid;
  }

  public final boolean run()
  {
    String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
    String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
    String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
    String wsdlURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_WSDL_URL);
    String name = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME);
    String description = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_DESCRIPTION);
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    try
    {
      UDDIProxy proxy = regElement.getProxy();
      if (!regElement.isLoggedIn())
        regElement.performLogin(publishURL,userId,password);

      TModel tModel = (new Uddi4jHelper()).newTModel(wsdlURL);
      tModel.setName(name);
      tModel.setDefaultDescriptionString(description);

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
      e.printStackTrace();
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("WSDLException");
      messageQueue.addMessage(e.getMessage());
    }
    catch (TransportException e)
    {
      e.printStackTrace();
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
      e.printStackTrace();
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("MalformedURLException");
      messageQueue.addMessage(e.getMessage());
    }    
    return false;
  }
}

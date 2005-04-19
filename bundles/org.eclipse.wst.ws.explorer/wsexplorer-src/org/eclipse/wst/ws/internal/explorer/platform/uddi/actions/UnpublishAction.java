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

import org.uddi4j.transport.TransportException;
import org.uddi4j.response.*;
import org.uddi4j.*;
import org.uddi4j.client.UDDIProxy;

import java.net.*;

public class UnpublishAction extends UDDIPropertiesFormAction
{
  protected RegistryNode registryNode_;
  protected boolean isLoggedIn_;

  public UnpublishAction(Controller controller)
  {
    super(controller);
    registryNode_ = getRegistryNode();
  }

  protected final boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    RegistryElement regElement = (RegistryElement)registryNode_.getTreeElement();
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    FormTool formTool = getSelectedFormTool();

    // if not yet logged in, validate the parameters needed to log in
    if (!regElement.isLoggedIn())
    {
      String publishURL = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
      String userId = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
      String password = parser.getParameter(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);

      boolean inputsValid = true;
      if (publishURL != null)
        propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL, publishURL);
        
      if (!Validator.validateURL(publishURL))
      {
        formTool.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
        inputsValid = false;
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_PUBLISH_URL"));
      }

      if (userId != null)
        propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID,userId);
        
      if (!Validator.validateString(userId))
      {
        formTool.flagError(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
        inputsValid = false;
        messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_USERID"));
      }

      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD, password);
      
      formTool.updatePropertyTable(propertyTable_);

      return inputsValid;
    }

    return true;
  }

  public boolean run()
  {
    RegistryElement registryElement = (RegistryElement)registryNode_.getTreeElement();
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    NodeManager navigatorManager = uddiPerspective.getNavigatorManager();
    Node selectedNode = navigatorManager.getSelectedNode();
    TreeElement selectedElement = selectedNode.getTreeElement();

    try
    {
      if (!registryElement.isLoggedIn())
      {
        // if not yet logged in, log in first
        String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
        String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
        String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
        
        registryElement.performLogin(publishURL,userId,password);
      }

      String selectedElementKey = selectedElement.getKey();
      String unpublishTypeMessageKey = null;
      UDDIProxy proxy = registryElement.getProxy();
      String authInfo = registryElement.getAuthInfoString();
      DispositionReport dr = null;
      if (selectedElement instanceof BusinessElement)
      {
        unpublishTypeMessageKey = "MSG_INFO_BUSINESS_UNPUBLISHED";
        dr = proxy.delete_business(authInfo,selectedElementKey);
      }
      else if (selectedElement instanceof ServiceElement)
      {
        unpublishTypeMessageKey = "MSG_INFO_SERVICE_UNPUBLISHED";
        dr = proxy.delete_service(authInfo,selectedElementKey);
      }
      else if (selectedElement instanceof ServiceInterfaceElement)
      {
        unpublishTypeMessageKey = "MSG_INFO_SERVICE_INTERFACE_UNPUBLISHED";
        dr = proxy.delete_tModel(authInfo,selectedElementKey);
      }
      if (!dr.success())
        throw new Exception(dr.toString());
      else
      {
        selectedElement.disconnectAll();
        messageQueue.addMessage(uddiPerspective.getMessage(unpublishTypeMessageKey,selectedElement.getName()));
        return true;
      }
    }
    catch (TransportException e)
    {
      handleUnexpectedException(uddiPerspective,messageQueue,"TransportException",e);
      return false;
    }
    catch (UDDIException e)
    {
      messageQueue.addMessage(uddiPerspective.getController().getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("UDDIException");
      messageQueue.addMessage(e.toString());
      return false;
    }
    catch (MalformedURLException e)
    {
      handleUnexpectedException(uddiPerspective,messageQueue,"MalformedURLException",e);
      return false;
    }
    catch (Exception e)
    {
      handleUnexpectedException(uddiPerspective, messageQueue, "Exception", e);
      return false;
    }
  }
}

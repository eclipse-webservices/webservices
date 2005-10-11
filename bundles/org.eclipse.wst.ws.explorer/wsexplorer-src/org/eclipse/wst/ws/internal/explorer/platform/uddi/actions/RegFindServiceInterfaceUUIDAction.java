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

import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.actions.FormInputException;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.QueryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.Validator;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.TModelDetail;
import org.uddi4j.transport.TransportException;

public class RegFindServiceInterfaceUUIDAction extends FindAction
{
  public RegFindServiceInterfaceUUIDAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_SERVICE_INTERFACES,String.valueOf(UDDIActionInputs.QUERY_STYLE_UUID));    
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String uuidKey = parser.getParameter(UDDIActionInputs.QUERY_INPUT_UUID_KEY);
    // Validate the data.
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    if (uuidKey != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY,uuidKey);

    if (!subQueryInitiated_ && !Validator.validateString(uuidKey))
    {
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_UUID_KEY"));
    }
    return inputsValid;
  }

  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    try
    {
      boolean shouldAddQueryNode = (propertyTable_.get(UDDIActionInputs.QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE) == null);      
      String uuidKey = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY);

      // The action can be run under the context of either a registry or a query node or even UDDIMainNode.
      if (regNode_ == null)
        regNode_ = getRegistryNode((Node)propertyTable_.get(UDDIActionInputs.REFRESH_NODE));
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      UDDIProxy proxy = regElement.getProxy();
      TModelDetail tModelDetail = proxy.get_tModelDetail(uuidKey);
      Vector tModelVector = tModelDetail.getTModelVector();
      if (tModelVector.size() > 0)
      {
        TModel tModel = (TModel)tModelVector.elementAt(0);
        if (shouldAddQueryNode)
        {
          TModel[] tModelArray = new TModel[1];
          tModelArray[0] = tModel;
          String queryName = (String)propertyTable_.get(UDDIActionInputs.QUERY_NAME);
          queryElement_ = new QueryElement(tModelArray,queryName,regElement.getModel());
          addQueryNode();
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICE_INTERFACES_FOUND",String.valueOf(tModelArray.length)));
        }
        propertyTable_.put(UDDIActionInputs.LATEST_OBJECT,tModel);
        return true;
      }
      else
        throw new FormInputException(uddiPerspective.getMessage("MSG_ERROR_NO_SERVICE_INTERFACES_FOUND"));
    }
    catch (UDDIException e)
    {
      messageQueue.addMessage(uddiPerspective.getController().getMessage("MSG_ERROR_UNEXPECTED"));
      messageQueue.addMessage("UDDIException");
      messageQueue.addMessage(e.toString());
    }
    catch (TransportException e)
    {
      handleUnexpectedException(uddiPerspective,messageQueue,"TransportException",e);
    }
    catch (FormInputException e)
    {
      messageQueue.addMessage(e.getMessage());
    }
    return false;
  }
}

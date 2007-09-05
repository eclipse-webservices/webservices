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
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.transport.TransportException;

public class RegFindBusinessUUIDAction extends FindAction
{
  public RegFindBusinessUUIDAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_BUSINESSES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_BUSINESSES,String.valueOf(UDDIActionInputs.QUERY_STYLE_UUID));    
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String uuidKey = parser.getParameter(UDDIActionInputs.QUERY_INPUT_UUID_KEY);

    // Validate the data.
    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    
    if (uuidKey != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY,uuidKey);
      
    if (!subQueryInitiated_ && !Validator.validateString(uuidKey))
    {
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY);
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
      String uuidKey = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY);
      
      // The action can be run under the context of either a registry or a query node or even UDDIMainNode.
      if (regNode_ == null)
        regNode_ = getRegistryNode((Node)propertyTable_.get(UDDIActionInputs.REFRESH_NODE));
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      UDDIProxy proxy = regElement.getProxy();
      BusinessEntity[] beList = new BusinessEntity[1];
      Vector beVector = proxy.get_businessDetail(uuidKey).getBusinessEntityVector();
      if (beVector.size() > 0)
      {
        beVector.toArray(beList);
        if (shouldAddQueryNode)
        {
          String queryName = (String)propertyTable_.get(UDDIActionInputs.QUERY_NAME);
          queryElement_ = new QueryElement(beList,queryName,regElement.getModel());
          addQueryNode();
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_BUSINESSES_FOUND",String.valueOf(beList.length)));
        }
        propertyTable_.put(UDDIActionInputs.LATEST_OBJECT,beList[0]);
        return true;
      }
      else
        throw new FormInputException(uddiPerspective.getMessage("MSG_ERROR_NO_BUSINESSES_FOUND"));
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
    catch (FormInputException e)
    {
      messageQueue.addMessage(e.getMessage());
    }
    return false;
  }
}

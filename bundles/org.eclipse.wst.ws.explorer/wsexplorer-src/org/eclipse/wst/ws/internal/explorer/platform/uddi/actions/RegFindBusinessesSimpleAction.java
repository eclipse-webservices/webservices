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
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.QueryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.Name;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.response.BusinessInfo;
import org.uddi4j.response.BusinessInfos;
import org.uddi4j.transport.TransportException;

public class RegFindBusinessesSimpleAction extends FindAction
{
  public RegFindBusinessesSimpleAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_BUSINESSES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_BUSINESSES,String.valueOf(UDDIActionInputs.QUERY_STYLE_SIMPLE));    
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String name = parser.getParameter(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME);

    // Validate the data.
    boolean inputsValid = true;

    if (name != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME,name);
    else
    {
      // This shouldn't occur.
      inputsValid = false;
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
      String name = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME);
      Vector nameVector = new Vector();
      nameVector.addElement(new Name(name));

      // The action can be run under the context of either a registry or a query node.
      getSelectedNavigatorNode();
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      UDDIProxy proxy = regElement.getProxy();
      
      BusinessInfos busInfos = proxy.find_business(nameVector, null, null, null, null, null, UDDIActionInputs.QUERY_MAX_SEARCH_SET).getBusinessInfos();
      int finalNumberOfBusinessEntities = Math.min(UDDIActionInputs.QUERY_MAX_RESULTS, busInfos.size());
      Vector businessKeys = new Vector();
      for (int i = 0; i < finalNumberOfBusinessEntities; i++)
      {
        BusinessInfo busInfo = busInfos.get(i);
        businessKeys.addElement(busInfo.getBusinessKey());
      }

      if (finalNumberOfBusinessEntities > 0)
      {
        if (shouldAddQueryNode)
        {
          Vector beVector = proxy.get_businessDetail(businessKeys).getBusinessEntityVector();
          BusinessEntity[] beList = new BusinessEntity[beVector.size()];
          beVector.toArray(beList);
          String queryName = (String)propertyTable_.get(UDDIActionInputs.QUERY_NAME);
          queryElement_ = new QueryElement(beList,queryName,regElement.getModel());
          addQueryNode();
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_BUSINESSES_FOUND",String.valueOf(beList.length)));
        }
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

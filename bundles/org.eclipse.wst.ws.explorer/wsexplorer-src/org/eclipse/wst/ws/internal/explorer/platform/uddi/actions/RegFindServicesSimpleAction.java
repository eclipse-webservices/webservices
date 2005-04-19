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

import org.eclipse.wst.ws.internal.explorer.platform.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.*;
import org.uddi4j.response.*;
import org.uddi4j.transport.TransportException;
import org.uddi4j.UDDIException;

import java.util.*;

public class RegFindServicesSimpleAction extends FindAction
{
  public RegFindServicesSimpleAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_SERVICES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_SERVICES,String.valueOf(UDDIActionInputs.QUERY_STYLE_SIMPLE));    
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String name = parser.getParameter(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME);

    // Validate the data.
    boolean inputsValid = true;

    if (name != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME,name);
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
      String name = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_NAME);
      Vector nameVector = new Vector();
      nameVector.addElement(new Name(name));

      // The action can be run under the context of either a registry or a query node.
      Node selectedNode = getSelectedNavigatorNode();
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      UDDIProxy proxy = regElement.getProxy();
      ServiceList serviceList = proxy.find_service("",nameVector,null,null,null,UDDIActionInputs.QUERY_MAX_SEARCH_SET);
      
      ServiceInfos sInfos = serviceList.getServiceInfos();
      int finalNumberOfBusinessServices = Math.min(UDDIActionInputs.QUERY_MAX_RESULTS,sInfos.size());
      Vector serviceKeyVector = new Vector();
      for (int i=0;i<finalNumberOfBusinessServices;i++)
      {
        ServiceInfo sInfo = sInfos.get(i);
        serviceKeyVector.addElement(sInfo.getServiceKey());
      }
        
      if (finalNumberOfBusinessServices > 0)
      {
        if (shouldAddQueryNode)
        {
          ServiceDetail serviceDetail = proxy.get_serviceDetail(serviceKeyVector);
          Vector businessServiceVector = serviceDetail.getBusinessServiceVector();
          String queryName = (String)propertyTable_.get(UDDIActionInputs.QUERY_NAME);
          BusinessService[] bsArray = new BusinessService[finalNumberOfBusinessServices];
          businessServiceVector.toArray(bsArray);
          queryElement_ = new QueryElement(bsArray,queryName,regElement.getModel());
          addQueryNode();
          messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_SERVICES_FOUND",String.valueOf(bsArray.length)));
        }
        return true;
      }
      else
        throw new FormInputException(uddiPerspective.getMessage("MSG_ERROR_NO_SERVICES_FOUND"));
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

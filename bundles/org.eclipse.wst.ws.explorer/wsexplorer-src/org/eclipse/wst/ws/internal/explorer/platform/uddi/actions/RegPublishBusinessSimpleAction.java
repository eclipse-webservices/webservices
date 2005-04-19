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

import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.transport.TransportException;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.*;

import java.util.*;
import java.net.*;

public class RegPublishBusinessSimpleAction extends PublishAction
{
  public RegPublishBusinessSimpleAction(Controller controller)
  {
    super(controller);
    propertyTable_.put(UDDIActionInputs.QUERY_ITEM,String.valueOf(UDDIActionInputs.QUERY_ITEM_BUSINESSES));
    propertyTable_.put(UDDIActionInputs.QUERY_STYLE_BUSINESSES,String.valueOf(UDDIActionInputs.QUERY_STYLE_SIMPLE));    
  }

  protected boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    String name = parser.getParameter(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME);
    String description = parser.getParameter(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_DESCRIPTION);

    boolean inputsValid = true;
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();

    if (name != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME,name);

    if (!subQueryInitiated_ && !Validator.validateString(name))
    {
      inputsValid = false;
      formToolPI.flagError(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_ERROR_INVALID_NAME"));
    }

    if (description != null)
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_DESCRIPTION,description);

    return inputsValid;
  }

  public final boolean run()
  {
    String publishURL = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PUBLISH_URL);
    String userId = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_USERID);
    String password = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_ADVANCED_PASSWORD);
    String name = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_NAME);
    String description = (String)propertyTable_.get(UDDIActionInputs.QUERY_INPUT_SIMPLE_BUSINESS_DESCRIPTION);
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
    try
    {
      UDDIProxy proxy = regElement.getProxy();
      if (!regElement.isLoggedIn())
        regElement.performLogin(publishURL,userId,password);

      BusinessEntity be = new BusinessEntity();
      be.setBusinessKey("");
      be.setDefaultName(new Name(name));
      be.setDefaultDescriptionString(description);
      Vector beVector = new Vector();
      beVector.add(be);

      be = (BusinessEntity)proxy.save_business(regElement.getAuthInfoString(), beVector).getBusinessEntityVector().get(0);
      addPublishedItemNode(be,regElement);
      messageQueue.addMessage(uddiPerspective.getMessage("MSG_INFO_BUSINESS_PUBLISHED",be.getDefaultNameString()));
      return true;
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

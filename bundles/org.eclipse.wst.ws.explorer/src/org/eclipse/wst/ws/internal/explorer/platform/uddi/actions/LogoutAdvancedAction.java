/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060427   136449 brunssen@us.ibm.com - Vince Brunssen  
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.uddi4j.UDDIException;
import org.uddi4j.transport.TransportException;

public class LogoutAdvancedAction extends PublishAction
{
  public LogoutAdvancedAction(Controller controller)
  {
    super(controller);
    regNode_.getLoginTool().setToRegistryLink();    
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    // Validate the data.
    boolean inputsValid = true;
    return inputsValid;
  }

  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    try
    {
      // The action can be run under the context of either a registry or a query node.
      RegistryElement regElement = (RegistryElement)regNode_.getTreeElement();
      if (regElement.isLoggedIn()) {
        regElement.performLogout();
        regElement.remove();
        regNode_.getNodeManager().setSelectedNodeId(0);  
      }
      
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
    return false;
  }
}

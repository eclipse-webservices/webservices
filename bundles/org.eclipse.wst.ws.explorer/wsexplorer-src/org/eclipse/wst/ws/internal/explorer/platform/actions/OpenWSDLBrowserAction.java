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

package org.eclipse.wst.ws.internal.explorer.platform.actions;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;

public class OpenWSDLBrowserAction extends Action
{
  public OpenWSDLBrowserAction()
  {
  }

  public static final String getActionLink(String sessionId,int wsdlType)
  {
    StringBuffer actionLink = new StringBuffer("wsdl_browser.jsp?");
    actionLink.append(ActionInputs.SESSIONID).append('=').append(sessionId);
    actionLink.append('&').append(ActionInputs.WSDL_TYPE).append('=').append(wsdlType);
    return actionLink.toString();
  }

  public static final String getActionLinkForService(String sessionId)
  {
    return getActionLink(sessionId,ActionInputs.WSDL_TYPE_SERVICE);
  }
  
  public static final String getActionLinkForServiceInterface(String sessionId)
  {
    return getActionLink(sessionId,ActionInputs.WSDL_TYPE_SERVICE_INTERFACE);
  }

  public final boolean populatePropertyTable(HttpServletRequest request)
  {
    return true;
  }

  public final boolean run()
  {
    return true;
  }
}

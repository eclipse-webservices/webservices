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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.actions.Action;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLActionInputs;

public class OpenXSDInfoDialogAction extends Action
{
  public OpenXSDInfoDialogAction()
  {
  }

  public static final String getActionLink(String sessionId,int nodeId,String fragId)
  {
    StringBuffer actionLink = new StringBuffer("wsdl/fragment/XSDInfoDialog.jsp?");
    actionLink.append(ActionInputs.SESSIONID).append('=').append(sessionId);
    actionLink.append('&').append(ActionInputs.NODEID).append('=').append(nodeId);
    actionLink.append('&').append(WSDLActionInputs.FRAGMENT_ID).append('=').append(fragId);
    return actionLink.toString();
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

/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.actions;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;

public class ProxyLoadPageAction extends Action
{
  // Get the action given a relative targetPage URL. e.g. "forms/LongLoadingForm.jsp"
  public static final String getActionLink(String targetPage)
  {
    return getActionLink(null,targetPage);
  }
  
  // Get the action given a sessionId and a relative targetPage URL.
  public static final String getActionLink(String sessionId,String targetPage)
  {
    StringBuffer actionLink = new StringBuffer("forms/ProxyLoadPage.jsp?");
    if (sessionId != null)
      actionLink.append(ActionInputs.SESSIONID).append('=').append(sessionId).append('&');
    actionLink.append(ActionInputs.TARGET_PAGE).append('=').append(targetPage);
    return actionLink.toString();
  }
  
  public boolean populatePropertyTable(HttpServletRequest request)
  {
    return true;
  }
  
  public boolean run()
  {
    return true;
  }
}

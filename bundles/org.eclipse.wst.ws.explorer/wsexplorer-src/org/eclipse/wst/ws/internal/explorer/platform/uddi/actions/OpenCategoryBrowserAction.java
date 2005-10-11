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

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.actions.Action;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;

public class OpenCategoryBrowserAction extends Action
{
  public OpenCategoryBrowserAction()
  {
  }

  private static final void addSessionAndTModelKey(StringBuffer result,String sessionId,String categoryTModelKey)
  {
    result.append(ActionInputs.SESSIONID).append('=').append(sessionId).append('&').append(UDDIActionInputs.CATEGORY_TMODEL_KEY).append('=').append(categoryTModelKey);
  }
  
  // uddi/category_browser.jsp?sessionId=...&categoryTModelKey=...
  public static final String getActionLink(String sessionId,String categoryTModelKey)
  {
    StringBuffer actionLink = new StringBuffer("uddi/category_browser.jsp?");
    addSessionAndTModelKey(actionLink,sessionId,categoryTModelKey);
    return actionLink.toString();
  }
  
  public static final String getCategoryContentPage(String sessionId,String categoryTModelKey)
  {
    StringBuffer actionLink = new StringBuffer("uddi/category_content.jsp?");
    addSessionAndTModelKey(actionLink,sessionId,categoryTModelKey);
    return actionLink.toString();
  }
  
  public static final String getWildCardActionLink(String sessionId)
  {
    return getActionLink(sessionId,"%");
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

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

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.actions.Action;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;

public class TransferToAddCategoryDataAction extends Action
{
  public TransferToAddCategoryDataAction()
  {
  }

  // uddi/actions/TransferToAddCategoryDataActionJSP.jsp?sessionId=...&categoryTModelKey=...
  public static final String getActionLink(String sessionId,String categoryTModelKey)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/TransferToAddCategoryDataActionJSP.jsp?");
    actionLink.append(ActionInputs.SESSIONID).append('=').append(sessionId).append('&').append(UDDIActionInputs.CATEGORY_TMODEL_KEY).append('=').append(categoryTModelKey);
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

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
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;

public class SelectCategoryNodeAction extends SelectNodeAction
{
  public SelectCategoryNodeAction(Controller controller)
  {
    super(controller,controller.getUDDIPerspective().getCategoryManager());
  }

  // uddi/actions/SelectCategoryNodeAction.jsp?sessionId=...&nodeId=...
  public static String getActionLink(String sessionId,int nodeId)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/SelectCategoryNodeActionJSP.jsp?");
    actionLink.append(ActionInputs.SESSIONID).append('=').append(sessionId);
    actionLink.append('&').append(ActionInputs.NODEID).append('=').append(nodeId);
    return actionLink.toString();
  }
  
  public final String getActionLinkForHistory()
  {
    // Do not add this action to the history.
    return null;
  }
  
  public final boolean run()
  {
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    performBaseAction(nodeId);
    return true;
  }

  public final String getTreeContentVar()
  {
    return "categoryBrowserWindowContent";
  }

  public final String getTreeContentPage()
  {
    return null;
  }

  public final String getPropertiesContainerVar()
  {
    return null;
  }

  public final String getPropertiesContainerPage()
  {
    return null;
  }
  
  public final int getPerspectiveId()
  {
    return ActionInputs.PERSPECTIVE_UDDI;
  }
}

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

import org.eclipse.wst.ws.internal.explorer.platform.actions.ToggleNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;

public class ToggleCategoryNodeAction extends ToggleNodeAction
{
  public ToggleCategoryNodeAction(Controller controller)
  {
    super(controller,controller.getUDDIPerspective().getCategoryManager());
  }

  // uddi/actions/ToggleNavigatorNodeAction.jsp?sId=...&nodeId=...&open=...
  public static final String getActionLink(String sessionId,int nodeId,boolean open)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/ToggleCategoryNodeActionJSP.jsp?");
    actionLink.append(ActionInputs.SESSIONID).append('=').append(sessionId);
    actionLink.append('&').append(ActionInputs.NODEID).append('=').append(nodeId);
    actionLink.append('&').append(ActionInputs.OPEN).append('=').append(open?ActionInputs.OPEN_NODE:ActionInputs.CLOSE_NODE);
    return actionLink.toString();
  }

  public final boolean run()
  {
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    performBaseAction(nodeId);
    return true;
  }

  public final String getActionLinkForHistory()
  {
    return null;
  }

  public final String getTreeContentVar()
  {
    return "categoryBrowserWindowContent";
  }

  public final String getTreeContentPage()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    return OpenCategoryBrowserAction.getCategoryContentPage(controller_.getSessionId(),uddiPerspective.getCategoryTModelKey());
  }

  public final String getTreeContainerPage()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    return OpenCategoryBrowserAction.getActionLink(controller_.getSessionId(),uddiPerspective.getCategoryTModelKey());
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

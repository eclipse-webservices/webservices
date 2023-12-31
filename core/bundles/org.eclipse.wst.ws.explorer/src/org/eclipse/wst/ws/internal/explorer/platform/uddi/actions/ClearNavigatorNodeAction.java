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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.ClearNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;

public class ClearNavigatorNodeAction extends ClearNodeAction
{
  public ClearNavigatorNodeAction(Controller controller)
  {
    super(controller,controller.getUDDIPerspective().getNavigatorManager());
    propertyTable_.put(ActionInputs.NODEID,String.valueOf(controller.getUDDIPerspective().getNavigatorManager().getSelectedNodeId()));
  }

  // uddi/actions/ClearNavigatorNodeAction.jsp?nodeId=...
  public static String getActionLink(int nodeId)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/ClearNavigatorNodeActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    return actionLink.toString();
  }

  public final String getTreeContentVar()
  {
    return "navigatorContent";
  }

  public final String getTreeContentPage()
  {
    return "uddi/navigator_content.jsp";
  }

  public final String getPropertiesContainerVar()
  {
    return "propertiesContainer";
  }

  public final String getPropertiesContainerPage()
  {
    return "uddi/properties_container.jsp";
  }

  public final String getStatusContentVar()
  {
    return "statusContent";
  }

  public final String getStatusContentPage()
  {
    return "uddi/status_content.jsp";
  }
}

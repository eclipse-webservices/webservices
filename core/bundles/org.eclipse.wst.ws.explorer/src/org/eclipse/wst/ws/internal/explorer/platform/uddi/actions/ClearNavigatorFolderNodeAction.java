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

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;

public class ClearNavigatorFolderNodeAction extends ClearNavigatorNodeAction
{
  public ClearNavigatorFolderNodeAction(Controller controller)
  {
    super(controller);
  }

  // uddi/actions/ClearNavigatorFolderNodeAction.jsp?nodeId=...
  public static String getActionLink(int nodeId)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/ClearNavigatorFolderNodeActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    return actionLink.toString();
  }

  public final boolean run()
  {
    int nodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    if (!isStaleNode(nodeId))
    {
      Node node = nodeManager_.getNode(nodeId);
      TreeElement element = node.getTreeElement();
      element.disconnectRel(element.getPropertyAsString(ModelConstants.REL_CHILDREN));
    }
    return false;
  }
}

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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.ActionTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.SelectWSDLPropertiesToolAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.WSDLAddToFavoritesAction;

public class WSDLAddToFavoritesTool extends ActionTool {
  public WSDLAddToFavoritesTool(ToolManager toolManager, String alt) {
    super(toolManager, "images/favorites_enabled.gif", "images/favorites_highlighted.gif", alt);
  }

  public String getSelectToolActionHref(boolean forHistory) {
    Node selectedNode = toolManager_.getNode();
    return SelectWSDLPropertiesToolAction.getActionLink(selectedNode.getNodeId(), toolId_, selectedNode.getViewId(), selectedNode.getViewToolId(), forHistory);
  }

  public String getActionLink() {
    Node selectedNode = toolManager_.getNode();
    return WSDLAddToFavoritesAction.getActionLink(selectedNode.getNodeId());
  }
}
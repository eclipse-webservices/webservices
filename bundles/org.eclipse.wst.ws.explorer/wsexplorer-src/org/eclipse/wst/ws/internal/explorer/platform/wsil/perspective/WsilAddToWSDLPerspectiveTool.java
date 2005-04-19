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

package org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.*;

public class WsilAddToWSDLPerspectiveTool extends AddToWSDLPerspectiveTool {

  public WsilAddToWSDLPerspectiveTool(ToolManager toolManager, String alt) {
    super(toolManager,alt);
  }

  public String getSelectToolActionHref(boolean forHistory) {
    Node selectedNode = toolManager_.getNode();
    return SelectWSILToolAction.getActionLink(selectedNode.getNodeId(), selectedNode.getToolManager().getSelectedToolId(), selectedNode.getViewId(), toolId_, forHistory);
  }

  public String getActionLink() {
    Node selectedNode = toolManager_.getNode();
    return WsilAddToWSDLPerspectiveAction.getActionLink(selectedNode.getNodeId(), selectedNode.getToolManager().getSelectedToolId(), selectedNode.getViewId(), toolId_);
  }
}

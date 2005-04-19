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

import org.eclipse.wst.ws.internal.explorer.platform.perspective.ImportToWorkbenchTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.SelectWSILToolAction;

public class WSILImportWSILToWorkbenchTool extends ImportToWorkbenchTool
{
  public WSILImportWSILToWorkbenchTool(ToolManager toolManager, String alt)
  {
    super(toolManager, alt);
  }

  public String getSelectToolActionHref(boolean forHistory)
  {
    Node selectedNode = toolManager_.getNode();
    return SelectWSILToolAction.getActionLink(selectedNode.getNodeId(), toolId_, selectedNode.getViewId(), selectedNode.getViewToolId(), forHistory);
  }

  public final String getFormLink()
  {
    return "wsil/forms/WSILImportWSILToWorkbenchForm.jsp";
  }
}

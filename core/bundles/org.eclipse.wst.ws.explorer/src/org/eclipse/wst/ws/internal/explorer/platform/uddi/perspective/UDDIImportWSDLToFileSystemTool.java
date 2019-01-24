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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.ImportToFileSystemTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.SelectPropertiesToolAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.UDDIImportWSDLToFileSystemAction;

public class UDDIImportWSDLToFileSystemTool extends ImportToFileSystemTool
{
  public UDDIImportWSDLToFileSystemTool(ToolManager toolManager,String alt)
  {
    super(toolManager,alt);
  }

  public final String getSelectToolActionHref(boolean forHistory)
  {
    Node node = toolManager_.getNode();
    return SelectPropertiesToolAction.getActionLink(node.getNodeId(),toolId_,node.getViewId(),node.getViewToolId(),forHistory);
  }

  public final String getActionLink()
  {
    Node node = toolManager_.getNode();
    return UDDIImportWSDLToFileSystemAction.getActionLink(node.getNodeId(),toolId_,node.getViewId(),node.getViewToolId());
  }
}

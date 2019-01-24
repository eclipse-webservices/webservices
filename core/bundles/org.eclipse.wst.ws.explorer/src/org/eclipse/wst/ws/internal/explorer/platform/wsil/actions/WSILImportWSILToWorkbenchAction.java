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

package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ImportToFileSystemAction;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ImportToWorkbenchAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;

public class WSILImportWSILToWorkbenchAction extends ImportToWorkbenchAction
{
  public WSILImportWSILToWorkbenchAction(Controller controller)
  {
    super(controller);
  }

  public FormTool getSelectedFormTool()
  {
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    return (FormTool)wsilPerspective.getNodeManager().getSelectedNode().getToolManager().getSelectedTool();
  }

  public ImportToFileSystemAction newImportToFileSystemAction()
  {
    ImportWSILToFileSystemAction action = new ImportWSILToFileSystemAction(controller_);
    Hashtable table = action.getPropertyTable();
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    NodeManager nodeManager = wsilPerspective.getNodeManager();
    Node selectedNode = nodeManager.getSelectedNode();
    table.put(ActionInputs.NODEID, String.valueOf(selectedNode.getNodeId()));
    return action;
  }

  public final String getStatusContentVar()
  {
    return controller_.getWSILPerspective().getStatusContentVar();
  }
    
  public final String getStatusContentPage()
  {
    return controller_.getWSILPerspective().getStatusContentPage();
  }    
}

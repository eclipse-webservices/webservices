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

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ImportToFileSystemAction;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ImportToWorkbenchAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;

public class UDDIImportWSDLToWorkbenchAction extends ImportToWorkbenchAction
{
  public UDDIImportWSDLToWorkbenchAction(Controller controller)
  {
    super(controller);
  }

  public FormTool getSelectedFormTool()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    return (FormTool)uddiPerspective.getNavigatorManager().getSelectedNode().getCurrentToolManager().getSelectedTool();
  }

    public ImportToFileSystemAction newImportToFileSystemAction() {
        UDDIImportWSDLToFileSystemAction action = new UDDIImportWSDLToFileSystemAction(controller_);
        Hashtable table = action.getPropertyTable();

        UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
        NodeManager nodeManager = uddiPerspective.getNavigatorManager();
        Node selectedNode = nodeManager.getSelectedNode();

        table.put(ActionInputs.NODEID, String.valueOf(selectedNode.getNodeId()));

        return action;
    }

  public final String getStatusContentVar()
  {
    return controller_.getUDDIPerspective().getStatusContentVar();
  }

  public final String getStatusContentPage()
  {
    return controller_.getUDDIPerspective().getStatusContentPage();
  }
}

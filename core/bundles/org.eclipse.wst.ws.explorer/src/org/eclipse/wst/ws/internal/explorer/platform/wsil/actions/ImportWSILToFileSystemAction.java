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

import java.io.OutputStream;
import org.apache.wsil.WSILDocument;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ImportToFileSystemAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement;

public class ImportWSILToFileSystemAction extends ImportToFileSystemAction {
    private WSILDocument wsilDoc_;
    private WsilElement wsilElement_;
    private NodeManager nodeManager_;

    public ImportWSILToFileSystemAction(Controller controller) {
        super(controller);
        nodeManager_ = controller.getWSILPerspective().getNodeManager();
    }

    public static final String getActionLink(int nodeId,int toolId,int viewId,int viewToolId)
    {
      return ImportToFileSystemAction.getActionLink(nodeId,toolId,viewId,viewToolId,"wsil/actions/ImportWSILToFileSystemActionJSP.jsp");
    }

    public boolean write(OutputStream os) {
        if (wsilDoc_ != null) {
            return writeWSILDocument(os, wsilDoc_);
        }
        else {
            return false;
        }
    }

    public String getDefaultFileName() {
        String wsilFileName = wsilElement_.getWsilUrl();
        return wsilFileName.substring(wsilFileName.lastIndexOf('/') + 1, wsilFileName.length());
    }

    public boolean run() {
        int nodeID = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));

        Node selectedNode = nodeManager_.getNode(nodeID);
        TreeElement selectedElement = selectedNode.getTreeElement();
        if (selectedElement instanceof WsilElement) {
            wsilElement_ = (WsilElement)selectedElement;
            wsilDoc_ = ((WsilElement)selectedElement).getWSILDocument();
            return true;
        }
        else {
            return false;
        }
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

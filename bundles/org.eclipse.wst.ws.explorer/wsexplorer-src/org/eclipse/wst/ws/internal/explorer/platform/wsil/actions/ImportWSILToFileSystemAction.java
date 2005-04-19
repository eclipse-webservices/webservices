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

package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement;

import org.apache.wsil.WSILDocument;
import java.io.OutputStream;

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

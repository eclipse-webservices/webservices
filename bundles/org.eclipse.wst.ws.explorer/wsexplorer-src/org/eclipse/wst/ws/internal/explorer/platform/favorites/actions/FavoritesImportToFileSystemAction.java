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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import java.io.OutputStream;
import javax.wsdl.Definition;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ImportToFileSystemAction;
import org.eclipse.wst.ws.internal.explorer.platform.actions.WSDLFileNameHelper;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesWSDLServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.util.Uddi4jHelper;

public class FavoritesImportToFileSystemAction extends ImportToFileSystemAction {

    private Definition def_;
    private String wsdlFileName_;

    public FavoritesImportToFileSystemAction(Controller controller) {
        super(controller);
    }

    public static final String getActionLink(int nodeId,int toolId,int viewId,int viewToolId)
    {
      return ImportToFileSystemAction.getActionLink(nodeId,toolId,viewId,viewToolId,"favorites/actions/FavortiesImportToFileSystemActionJSP.jsp");
    }

    public boolean write(OutputStream os) {
        if (def_ != null) {
            return writeWSDLDefinition(os, def_);
        }
        else {
            return false;
        }
    }

    public String getDefaultFileName() {
        return wsdlFileName_;
    }

    public boolean run() {
        int nodeID = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));

        // return false if nodeID == 0, that is the main node
        if (nodeID == 0)
            return false;

        FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
        NodeManager nodeManager = favPerspective.getNodeManager();
        Node selectedNode = nodeManager.getNode(nodeID);
        TreeElement selectedElement = selectedNode.getTreeElement();

        String wsdlURL = ((FavoritesWSDLServiceElement)selectedElement).getWsdlUrl();
        wsdlFileName_ = WSDLFileNameHelper.getWSDLFileName(wsdlURL);
        try {
          def_ = (new Uddi4jHelper()).getWSDLDefinition(wsdlURL);
        }
        catch (Throwable t) {
            favPerspective.getMessageQueue().addMessage(t.getMessage());
            return false;
        }
        return true;
    }

    public final String getStatusContentVar()
    {
      return controller_.getFavoritesPerspective().getStatusContentVar();
    }

    public final String getStatusContentPage()
    {
      return controller_.getFavoritesPerspective().getStatusContentPage();
    }
}

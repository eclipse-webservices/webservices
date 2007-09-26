/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class FavoritesWSDLServiceNode extends FavoritesNavigatorNode {
    public FavoritesWSDLServiceNode(TreeElement treeElement, NodeManager nodeManager, int nodeDepth) {
        super(treeElement, nodeManager, nodeDepth, "favorites/images/wsdl_service_node.gif");
    }

    protected void initTools() {
        Controller controller = nodeManager_.getController();
        FavoritesPerspective favPerspective = controller.getFavoritesPerspective();
        new FavoritesWSDLDetailsTool(toolManager_, favPerspective.getMessage("ALT_FAVORITES_WSDL_DETAILS"));
        new FavoritesImportToWorkbenchTool(toolManager_, controller.getMessage("ALT_IMPORT_WSDL_TO_WORKBENCH"));
        new FavoritesImportToFileSystemTool(toolManager_, controller.getMessage("ALT_WSDL_IMPORT_TO_FS"));
        new FavoritesLaunchWebServiceWizardTool(toolManager_, controller.getMessage("ALT_LAUNCH_WEB_SERVICE_WIZARD"));
        new FavoritesAddToWSDLPerspectiveTool(toolManager_, favPerspective.getMessage("ALT_ADD_WSDL_TO_WSDL_PERSPECTIVE"));
    }
}

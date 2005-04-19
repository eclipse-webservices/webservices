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

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class WsilNode extends WsilNavigatorNode {

    public WsilNode(TreeElement treeElement, NodeManager nodeManager, int nodeDepth) {
        super(treeElement, nodeManager, nodeDepth, "wsil/images/wsil_node.gif");
    }

    protected void initTools() {
        Controller controller = nodeManager_.getController();
        WSILPerspective wsilPerspective = controller.getWSILPerspective();
        new WsilDetailsTool(toolManager_, wsilPerspective.getMessage("ALT_WSIL_DETAILS"));
        new ListWSDLServicesTool(toolManager_, wsilPerspective.getMessage("ALT_LIST_WSDL_SERVICES"));
        new ListUDDIServicesTool(toolManager_, wsilPerspective.getMessage("ALT_LIST_UDDI_SERVICES"));
        new ListUDDIBusinessTool(toolManager_, wsilPerspective.getMessage("ALT_LIST_UDDI_LINKS"));
        new ListWSILLinksTool(toolManager_, wsilPerspective.getMessage("ALT_LIST_WSIL_LINKS"));
        new WSILImportWSILToWorkbenchTool(toolManager_, wsilPerspective.getMessage("ATL_IMPORT_WSIL_TO_WORKBENCH"));
        new ImportWSILToFileSystemTool(toolManager_, controller.getMessage("ALT_WSIL_IMPORT_TO_FS"));
        new AddWSILToFavoritesTool(toolManager_, wsilPerspective.getMessage("ALT_ADD_WSIL_TO_FAVORITES"));
    }
}

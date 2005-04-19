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

import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ViewTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.SelectWSILToolAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WsilSetDefaultViewTool;

public class ListWSDLServicesTool extends ViewTool {
    public ListWSDLServicesTool(ToolManager toolManager, String alt) {
        super(toolManager, "wsil/images/list_WSDL_service_enabled.gif", "wsil/images/list_WSDL_service_highlighted.gif", alt);
    }

    public String getSelectToolActionHref(boolean forHistory) {
        Node selectedNode = toolManager_.getNode();
        return SelectWSILToolAction.getActionLink(selectedNode.getNodeId(), toolId_, selectedNode.getViewId(), selectedNode.getViewToolId(), forHistory);
    }

    protected void addSetDefaultViewTool(ToolManager viewToolManager, int index) {
        new WsilSetDefaultViewTool(viewToolManager,  toolManager_.getNode().getNodeManager().getController().getMessage("ALT_BACK_TO_TOP"));
    }

    protected void addTools(ToolManager viewToolManager, int index) {
        Controller controller = toolManager_.getNode().getNodeManager().getController();
        WSILPerspective wsilPerspective = controller.getWSILPerspective();
        new WsdlServiceDetailsTool(viewToolManager, wsilPerspective.getMessage("ALT_WSIL_SERVICE_DETAILS"));
        new WsilImportWSDLToWorkbenchTool(viewToolManager, controller.getMessage("ALT_IMPORT_WSDL_TO_WORKBENCH"));
        new ImportWSDLToFileSystemTool(viewToolManager, controller.getMessage("ALT_WSDL_IMPORT_TO_FS"));
        new WsilLaunchWebServiceWizardTool(viewToolManager, controller.getMessage("ALT_LAUNCH_WEB_SERVICE_WIZARD"));
        new WsilAddToWSDLPerspectiveTool(viewToolManager, wsilPerspective.getMessage("ALT_ADD_WSDL_TO_WSDL_PERSPECTIVE"));
        new AddWSDLServiceToFavoritesTool(viewToolManager, wsilPerspective.getMessage("ALT_ADD_WSDL_SERVICE_TO_FAVORITES"));
    }

    public String getFormLink() {
        return "wsil/views/WsdlServicesView.jsp";
    }
}

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

import org.eclipse.wst.ws.internal.explorer.platform.actions.ProxyLoadPageAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ViewTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.SelectWSILToolAction;

public class ListUDDIServicesTool extends ViewTool {
    public ListUDDIServicesTool(ToolManager toolManager, String alt) {
        super(toolManager, "wsil/images/list_UDDI_service_enabled.gif", "wsil/images/list_UDDI_service_highlighted.gif", alt);
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
        new UddiServiceDetailsTool(viewToolManager, wsilPerspective.getMessage("ALT_WSIL_SERVICE_DETAILS"));
        new WsilImportWSDLToWorkbenchTool(viewToolManager, controller.getMessage("ALT_IMPORT_WSDL_TO_WORKBENCH"));
        new ImportWSDLToFileSystemTool(viewToolManager, controller.getMessage("ALT_WSDL_IMPORT_TO_FS"));
        new WsilLaunchWebServiceWizardTool(viewToolManager, controller.getMessage("ALT_LAUNCH_WEB_SERVICE_WIZARD"));
        new AddServiceToUDDIPerspectiveTool(viewToolManager, wsilPerspective.getMessage("ALT_ADD_SERVICE_TO_UDDI_PERSPECTIVE"));
        new AddServiceToFavoritesTool(viewToolManager, wsilPerspective.getMessage("ALT_ADD_SERVICE_TO_FAVORITES"));
        new RefreshUDDIServiceTool(viewToolManager, wsilPerspective.getMessage("ALT_REFRESH_UDDI_SERVICE"));
    }

    public String getFormLink() {
        return ProxyLoadPageAction.getActionLink("wsil/views/UddiServicesView.jsp");
    }

}

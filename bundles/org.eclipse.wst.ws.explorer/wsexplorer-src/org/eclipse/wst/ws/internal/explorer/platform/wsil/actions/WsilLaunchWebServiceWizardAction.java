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

import org.eclipse.wst.ws.internal.explorer.platform.actions.LaunchWebServiceWizardAction;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilUddiServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilWsdlServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.ListUDDIServicesTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.ListWSDLServicesTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;

public class WsilLaunchWebServiceWizardAction extends LaunchWebServiceWizardAction {
    public WsilLaunchWebServiceWizardAction(Controller controller) {
        super(controller);
    }

    public static LaunchWebServiceWizardAction newAction(Controller controller) {
        return new WsilLaunchWebServiceWizardAction(controller);
    }

    public FormTool getSelectedFormTool() {
        WSILPerspective wsilPerspective = controller_.getWSILPerspective();
        return (FormTool)wsilPerspective.getNodeManager().getSelectedNode().getToolManager().getSelectedTool();
    }

    public boolean run() {
        WSILPerspective wsilPerspective = controller_.getWSILPerspective();
        NodeManager nodeManager = wsilPerspective.getNodeManager();
        Node selectedNode = nodeManager.getSelectedNode();
        TreeElement selectedElement = selectedNode.getTreeElement();
        Tool selectedTool = selectedNode.getToolManager().getSelectedTool();
        int viewID = selectedNode.getViewId();

        ListElement le = null;
        String url = null;
        if (selectedTool instanceof ListWSDLServicesTool) {
            le = ((WsilElement)selectedElement).getAllWSDLServices().getElementWithViewId(viewID);
            url = ((WsilWsdlServiceElement)le.getObject()).getWSDLServiceURL();
        }
        else if (selectedTool instanceof ListUDDIServicesTool) {
            le = ((WsilElement)selectedElement).getAllUDDIServices().getElementWithViewId(viewID);
            url = ((WsilUddiServiceElement)le.getObject()).getWsdlUrl();
        }
        else
            return false;
        return launchWizard(url);
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

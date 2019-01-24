/*******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.SelectWSDLPropertiesToolAction;

public class OpenWSDLTool extends FormTool {

    public OpenWSDLTool(ToolManager toolManager, String alt) {
        super(toolManager, "wsdl/images/open_wsdl_enabled.gif", "wsdl/images/open_wsdl_highlighted.gif", alt);
    }

    protected void initDefaultProperties() {
    }

    public String getSelectToolActionHref(boolean forHistory) {
        Node selectedNode = toolManager_.getNode();
        return SelectWSDLPropertiesToolAction.getActionLink(selectedNode.getNodeId(), toolId_, selectedNode.getViewId(), selectedNode.getViewToolId(), forHistory);
    }

    public String getFormLink() {
        return "wsdl/forms/OpenWSDLForm.jsp";
    }

}

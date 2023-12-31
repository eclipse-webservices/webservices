/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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

import org.eclipse.wst.ws.internal.explorer.platform.actions.ClearNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;

public class ClearWSILAction extends ClearNodeAction {

    public ClearWSILAction(Controller controller)
    {
        super(controller, controller.getWSILPerspective().getNodeManager());
        propertyTable_.put(ActionInputs.NODEID, String.valueOf(controller.getWSILPerspective().getNodeManager().getSelectedNodeId()));
    }

    public static String getActionLink(int nodeID)
    {
        StringBuffer actionLink = new StringBuffer("wsil/actions/ClearWSILActionJSP.jsp?");
        actionLink.append(ActionInputs.NODEID);
        actionLink.append('=');
        actionLink.append(nodeID);
        return actionLink.toString();
    }

    public String getTreeContentVar() {
        return "wsilNavigatorContent";
    }

    public String getTreeContentPage() {
        return "wsil/wsil_navigator_content.jsp";
    }

    public String getPropertiesContainerVar() {
        return "wsilPropertiesContainer";
    }

    public String getPropertiesContainerPage() {
        return "wsil/wsil_properties_container.jsp";
    }

    public String getStatusContentVar() {
        return "wsilStatusContent";
    }

    public String getStatusContentPage() {
        return "wsil/wsil_status_content.jsp";
    }

}

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

package org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ImportToFileSystemTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.ImportWSILToFileSystemAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.SelectWSILToolAction;

public class ImportWSILToFileSystemTool extends ImportToFileSystemTool {
    public ImportWSILToFileSystemTool(ToolManager toolManager, String alt) {
        super(toolManager, alt);
    }

    public String getSelectToolActionHref(boolean forHistory) {
        Node selectedNode = toolManager_.getNode();
        return SelectWSILToolAction.getActionLink(selectedNode.getNodeId(), toolId_, selectedNode.getViewId(), selectedNode.getViewToolId(), forHistory);
    }

    public String getActionLink() {
        return ImportWSILToFileSystemAction.getActionLink(toolManager_.getNode().getNodeId(), toolId_, ActionInputs.VIEWID_DEFAULT, ActionInputs.VIEWTOOLID_DEFAULT);
    }

}

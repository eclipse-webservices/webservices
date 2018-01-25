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

package org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.SelectWSILNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.WsilToggleNodeAction;

public abstract class WsilNavigatorNode extends Node {

    public WsilNavigatorNode(TreeElement treeElement, NodeManager nodeManager, int nodeDepth, String imagePath) {
        super(treeElement, nodeManager, nodeDepth, imagePath);
    }

    protected String getToggleNodeActionHref() {
        return WsilToggleNodeAction.getActionLink(nodeId_,isOpen_);
    }

    protected String getLinkActionHref() {
        return SelectWSILNodeAction.getActionLink(nodeId_, false);
    }

}

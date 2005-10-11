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

import org.eclipse.wst.ws.internal.datamodel.ElementAdapter;
import org.eclipse.wst.ws.internal.datamodel.RelAddEvent;
import org.eclipse.wst.ws.internal.datamodel.RelRemoveEvent;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilModelConstants;

public class WsilMainNode extends WsilNavigatorNode {

    public WsilMainNode(TreeElement treeElement, NodeManager nodeManager) {
        super(treeElement, nodeManager, 1, "images/root_main.gif");
        treeElement.addListener(new ElementAdapter() {
                                                   public void relAdded(RelAddEvent event) {
                                                       String rel = event.getOutBoundRelName();
                                                       if (rel.equals(WsilModelConstants.REL_WSIL)) {
                                                           WsilNode wsilNode = new WsilNode((TreeElement)event.getParentElement(), nodeManager_, nodeDepth_ + 1);
                                                           addChild(wsilNode);
                                                       }
                                                   }

                                                   public void relRemoved(RelRemoveEvent event) {
                                                       TreeElement childElement = null;
                                                       if (event.getInBoundRelName().equals(WsilModelConstants.REL_WSIL)) {
                                                           childElement = (TreeElement)event.getInboundElement();
                                                       }
                                                       if (event.getOutBoundRelName().equals(WsilModelConstants.REL_WSIL)) {
                                                           childElement = (TreeElement)event.getOutBoundElement();
                                                       }
                                                       removeChildNode(childElement);
                                                   }
                                               });
    }

    protected void initTools() {
        WSILPerspective wsilPerspective = nodeManager_.getController().getWSILPerspective();
        new OpenWSILTool(toolManager_, wsilPerspective.getMessage("ALT_OPEN_WSIL"));
    }
}

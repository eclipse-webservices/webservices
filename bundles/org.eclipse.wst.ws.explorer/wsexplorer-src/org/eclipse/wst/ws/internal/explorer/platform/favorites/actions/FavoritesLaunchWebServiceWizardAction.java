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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.LaunchWebServiceWizardAction;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesWSDLServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class FavoritesLaunchWebServiceWizardAction extends LaunchWebServiceWizardAction {

    public FavoritesLaunchWebServiceWizardAction(Controller controller) {
        super(controller);
    }

    public static LaunchWebServiceWizardAction newAction(Controller controller) {
        return new FavoritesLaunchWebServiceWizardAction(controller);
    }

    public FormTool getSelectedFormTool() {
        FavoritesPerspective favoritesPerspective = controller_.getFavoritesPerspective();
        return (FormTool)favoritesPerspective.getNodeManager().getSelectedNode().getToolManager().getSelectedTool();
    }

    public boolean run() {
        FavoritesPerspective favoritesPerspective = controller_.getFavoritesPerspective();
        NodeManager nodeManager = favoritesPerspective.getNodeManager();
        Node selectedNode = nodeManager.getSelectedNode();
        TreeElement selectedElement = selectedNode.getTreeElement();
        if (selectedElement instanceof FavoritesWSDLServiceElement)
            return launchWizard(((FavoritesWSDLServiceElement)selectedElement).getWsdlUrl());
        else
            return false;
    }
    
    public final String getStatusContentVar()
    {
      return controller_.getFavoritesPerspective().getStatusContentVar();
    }
    
    public final String getStatusContentPage()
    {
      return controller_.getFavoritesPerspective().getStatusContentPage();
    }
}

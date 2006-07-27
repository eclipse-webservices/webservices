/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUserDefUDDIRegistryFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;

public class SelectFavoritesUserDefUDDIRegistryNodeAction extends SelectFavoritesNodeAction
{
    public SelectFavoritesUserDefUDDIRegistryNodeAction(Controller controller)
    {
        super(controller);
    }

    public void performExtendedAction()
    {
    	TreeElement selectedElement = selectedNode_.getTreeElement();
    	((FavoritesUserDefUDDIRegistryFolderElement)selectedElement).refresh();
    }
    
    
    public static String getActionLink(int nodeID, boolean keepHistory)
    {
        StringBuffer actionLink = new StringBuffer("favorites/actions/SelectFavoritesUserDefUDDIRegistryNodeActionJSP.jsp?");
        actionLink.append(ActionInputs.NODEID);
        actionLink.append('=');
        actionLink.append(nodeID);
        if (keepHistory) {
            actionLink.append('&');
            actionLink.append(ActionInputs.ISHISTORY);
            actionLink.append("=1");
        }
        return actionLink.toString();
    }
    
}

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
package org.eclipse.wst.ws.internal.explorer.platform.wsil.actions;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.ListUDDIBusinessTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.ListUDDIServicesTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.ListWSDLServicesTool;

public abstract class AddToFavoritesAction extends MultipleLinkAction {
    protected NodeManager nodeManager_;

    public AddToFavoritesAction(Controller controller)
    {
        super(controller);
        nodeManager_ = controller.getWSILPerspective().getNodeManager();
    }

    public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID)
    {
        StringBuffer actionLink = new StringBuffer("wsil/actions/WsilCheckFavoriteExistsActionJSP.jsp?");
        actionLink.append(ActionInputs.NODEID);
        actionLink.append('=');
        actionLink.append(nodeID);
        actionLink.append('&');
        actionLink.append(ActionInputs.TOOLID);
        actionLink.append('=');
        actionLink.append(toolID);
        actionLink.append('&');
        actionLink.append(ActionInputs.VIEWID);
        actionLink.append('=');
        actionLink.append(viewID);
        actionLink.append('&');
        actionLink.append(ActionInputs.VIEWTOOLID);
        actionLink.append('=');
        actionLink.append(viewToolID);
        return actionLink.toString();
    }

    public static String getBaseActionLink() {
        return "wsil/actions/WsilCheckFavoriteExistsActionJSP.jsp";
    }

    public static AddToFavoritesAction newAction(HttpServletRequest request, Controller controller) {
      String nodeID = request.getParameter(ActionInputs.NODEID);
      NodeManager nodeManager = controller.getWSILPerspective().getNodeManager();
      Tool selectedTool = nodeManager.getNode(Integer.parseInt(nodeID)).getToolManager().getSelectedTool();
      if (selectedTool instanceof ListWSDLServicesTool)
        return new AddWSDLServiceToFavoritesAction(controller);
      else if (selectedTool instanceof ListUDDIServicesTool)
        return new AddServiceToFavoritesAction(controller);
      else if (selectedTool instanceof ListUDDIBusinessTool)
        return new AddBusinessToFavoritesAction(controller);
      else
        return new AddWSILToFavoritesAction(controller);
    }

    protected boolean addToFavorites(Hashtable table, String rel) {
        FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
        NodeManager favNodeManager = favPerspective.getNodeManager();
        TreeElement favRootElement = favNodeManager.getRootNode().getTreeElement();
        Enumeration e = favRootElement.getElements(rel);
        if (!e.hasMoreElements()) return false;
        FavoritesFolderElement favFolderElement = (FavoritesFolderElement)e.nextElement();
        return favFolderElement.addFavorite(table);
    }

    protected boolean favoriteExists(Hashtable table, String rel) {
        FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
        NodeManager favNodeManager = favPerspective.getNodeManager();
        TreeElement favRootElement = favNodeManager.getRootNode().getTreeElement();
        Enumeration e = favRootElement.getElements(rel);
        if (!e.hasMoreElements()) return false;
        FavoritesFolderElement favFolderElement = (FavoritesFolderElement)e.nextElement();
        return favFolderElement.favoriteExists(table);
    }

    public abstract boolean favoriteExists();
}

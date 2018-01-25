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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.actions;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.actions.LinkAction;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesMainElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class FavoritesRestoreDefaultFavoritesAction extends LinkAction
{
  public FavoritesRestoreDefaultFavoritesAction(Controller controller)
  {
    super(controller);
  }

  public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID)
  {
    StringBuffer actionLink = new StringBuffer("favorites/actions/RestoreDefaultFavoritesConfirmJSP.jsp");
    return actionLink.toString();
  }

  protected boolean processLinkParameters(HttpServletRequest request)
  {
    return true;
  }

  public boolean run()
  {
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    NodeManager nodeManager = favPerspective.getNodeManager();
    Node mainNode = nodeManager.getRootNode();
    FavoritesMainElement mainElement = (FavoritesMainElement)mainNode.getTreeElement();
    boolean actionResult = mainElement.restoreFavoritesDefault();
    if (actionResult)
    {
      Vector folderNodes = mainNode.getChildNodes();
      for (int i = 0; i < folderNodes.size(); i++)
      {
        Node folderNode = (Node)folderNodes.elementAt(i);
        TreeElement treeElement = folderNode.getTreeElement();
        if (treeElement instanceof FavoritesFolderElement)
        {
          FavoritesFolderElement folderElement = (FavoritesFolderElement)treeElement;
          folderElement.removeAllFavorites(controller_.getServletEngineStateLocation());
          folderElement.init(mainElement);
        }
      }
      favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_INFO_FAVORITES_RESTORED_TO_DEFAULT"));
      return true;
    }
    else
    {
      favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_ERROR_OPEN_FAVORITES_BACKUP"));
      return false;
    }
  }
}

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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.actions.LinkAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;

public class WSDLAddToFavoritesAction extends LinkAction {
  public WSDLAddToFavoritesAction(Controller controller) {
    super(controller);
  }

  protected boolean processLinkParameters(HttpServletRequest request) {
    String nodeID = request.getParameter(ActionInputs.NODEID);
    try {
      Integer.parseInt(nodeID);
      propertyTable_.put(ActionInputs.NODEID, nodeID);
      return true;
    }
    catch (Throwable t) {
      return false;
    }
  }

  public static String getActionLink(int nodeID) {
    StringBuffer actionLink = new StringBuffer("wsdl/actions/WSDLCheckFavoriteExistsActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID);
    actionLink.append('=');
    actionLink.append(nodeID);
    return actionLink.toString();
  }

  public boolean favoriteExists() {
    String wsdlUrl = getWsdlUrl();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_WSDL_URL, wsdlUrl);
    FavoritesFolderElement favFolderElement = getFavWSDLFolderElement();
    return favFolderElement.favoriteExists(table);
  }

  public boolean run() {
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    String wsdlUrl = getWsdlUrl();
    Hashtable table = new Hashtable();
    table.put(FavoritesModelConstants.PROP_WSDL_URL, wsdlUrl);
    FavoritesFolderElement favFolderElement = getFavWSDLFolderElement();
    if (favFolderElement.addFavorite(table)) {
      wsdlPerspective.getMessageQueue().addMessage(wsdlPerspective.getMessage("MSG_INFO_ADD_TO_FAVORITES_SUCCESSFUL", wsdlUrl));
      return true;
    }
    else {
      wsdlPerspective.getMessageQueue().addMessage(wsdlPerspective.getMessage("MSG_ERROR_ADD_TO_FAVORITES", wsdlUrl));
      return false;
    }
  }

  private String getWsdlUrl() {
    String nodeID = (String)propertyTable_.get(ActionInputs.NODEID);
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    NodeManager wsdlNodeManager = wsdlPerspective.getNodeManager();
    WSDLElement selectedElement = (WSDLElement)wsdlNodeManager.getNode(Integer.parseInt(nodeID)).getTreeElement();
    return selectedElement.getWsdlUrl();
  }

  private FavoritesFolderElement getFavWSDLFolderElement() {
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    NodeManager favNodeManager = favPerspective.getNodeManager();
    TreeElement favRootElement = favNodeManager.getRootNode().getTreeElement();
    Enumeration e = favRootElement.getElements(FavoritesModelConstants.REL_WSDL_SERVICE_FOLDER_NODE);
    return (FavoritesFolderElement)e.nextElement();
  }
}

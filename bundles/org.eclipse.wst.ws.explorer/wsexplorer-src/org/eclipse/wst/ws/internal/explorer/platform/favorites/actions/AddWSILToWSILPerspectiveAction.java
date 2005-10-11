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

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesWSILElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.OpenWSILAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilActionInputs;

public class AddWSILToWSILPerspectiveAction extends MultipleLinkAction
{
  public AddWSILToWSILPerspectiveAction(Controller controller)
  {
    super(controller);
  }

  public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID)
  {
    StringBuffer actionLink = new StringBuffer("favorites/actions/AddWSILToWSILPerspectiveActionJSP.jsp?");
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

  public static String getBaseActionLink()
  {
    return "favorites/actions/AddWSILToWSILPerspectiveActionJSP.jsp";
  }

  protected boolean executeSingleLinkAction()
  {
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    NodeManager nodeManager = favPerspective.getNodeManager();
    Node selectedNode = nodeManager.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    FavoritesWSILElement wsilElement = (FavoritesWSILElement) selectedElement;
    String wsilURL = wsilElement.getWsilUrl();
    OpenWSILAction openWSILAction = new OpenWSILAction(controller_);
    Hashtable propertyTable = openWSILAction.getPropertyTable();
    propertyTable.put(WsilActionInputs.WSIL_URL, wsilURL);
    propertyTable.put(WsilActionInputs.WSIL_INSPECTION_TYPE, String.valueOf(WsilActionInputs.WSIL_DETAILS));
    boolean actionResult = openWSILAction.run();
    if (actionResult)
      favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_INFO_ADD_WSIL_TO_WSIL_PERSPECTIVE_SUCCESSFUL", wsilURL));
    else
      favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_ERROR_ADD_WSIL_TO_WSIL_PERSPECTIVE", wsilURL));
    return actionResult;
  }
}

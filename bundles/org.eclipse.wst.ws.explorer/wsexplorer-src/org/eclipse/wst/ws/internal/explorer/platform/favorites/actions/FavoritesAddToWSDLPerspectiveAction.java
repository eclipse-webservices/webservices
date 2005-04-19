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
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesWSDLServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective.FavoritesPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.OpenWSDLAction;

import java.util.Hashtable;

public class FavoritesAddToWSDLPerspectiveAction extends MultipleLinkAction
{
  public FavoritesAddToWSDLPerspectiveAction(Controller controller)
  {
    super(controller);
  }

  public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID)
  {
    StringBuffer actionLink = new StringBuffer("favorites/actions/FavoritesAddToWSDLPerspectiveActionJSP.jsp?");
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
    return "favorites/actions/FavoritesAddToWSDLPerspectiveActionJSP.jsp";
  }

  protected boolean executeSingleLinkAction()
  {
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    FavoritesPerspective favPerspective = controller_.getFavoritesPerspective();
    NodeManager nodeManager = favPerspective.getNodeManager();
    Node selectedNode = nodeManager.getNode(nodeID);
    FavoritesWSDLServiceElement wsdlElement = (FavoritesWSDLServiceElement) selectedNode.getTreeElement();
    String wsdlUrl = wsdlElement.getWsdlUrl();
    OpenWSDLAction openWSDLAction = new OpenWSDLAction(controller_);
    Hashtable propertyTable = openWSDLAction.getPropertyTable();
    propertyTable.put(ActionInputs.QUERY_INPUT_WSDL_URL, wsdlUrl);
    boolean actionResult = openWSDLAction.run();
    if (actionResult)
      favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_INFO_ADD_WSDL_TO_WSDL_PERSPECTIVE_SUCCESSFUL", wsdlUrl));
    else
      favPerspective.getMessageQueue().addMessage(favPerspective.getMessage("MSG_ERROR_ADD_WSDL_TO_WSDL_PERSPECTIVE", wsdlUrl));
    return actionResult;
  }
}

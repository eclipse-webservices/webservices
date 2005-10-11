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

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilWsilLinkElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.transformer.WSILViewSelectionTransformer;

public class OpenWSILLinkAction extends MultipleLinkAction
{
  public OpenWSILLinkAction(Controller controller)
  {
    super(controller);
  }

  public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID)
  {
    StringBuffer actionLink = new StringBuffer("wsil/actions/OpenWSILLinkActionJSP.jsp?");
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
    return "wsil/actions/OpenWSILLinkActionJSP.jsp";
  }

  public ITransformer[] getTransformers()
  {
    ITransformer[] parentTransformers = super.getTransformers();
    ITransformer[] transformers = new ITransformer[parentTransformers.length+1];
    System.arraycopy(parentTransformers, 0, transformers, 0, parentTransformers.length);
    transformers[transformers.length-1] = new WSILViewSelectionTransformer(controller_, WsilModelConstants.LIST_MANAGER_WSIL_LINKS, ActionInputs.VIEWID, WSILViewSelectionTransformer.WSIL_LINK);
    return transformers;
  }

  protected boolean executeSingleLinkAction()
  {
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    int viewID = Integer.parseInt((String) propertyTable_.get(ActionInputs.VIEWID));
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    NodeManager nodeManager = wsilPerspective.getNodeManager();
    Node selectedNode = nodeManager.getNode(nodeID);
    TreeElement selectedElement = selectedNode.getTreeElement();
    if (!(selectedElement instanceof WsilElement))
      return false;
    ListElement le = ((WsilElement) selectedElement).getAllWSILLinks().getElementWithViewId(viewID);
    WsilWsilLinkElement link = (WsilWsilLinkElement) le.getObject();
    String wsilURL = link.getWSILLinkLocation();
    // populate and run the OpenWSILAction
    OpenWSILAction action = new OpenWSILAction(controller_);
    Hashtable table = action.getPropertyTable();
    table.put(WsilActionInputs.WSIL_URL, wsilURL);
    table.put(WsilActionInputs.WSIL_INSPECTION_TYPE, String.valueOf(WsilActionInputs.WSIL_DETAILS));
    return action.run();
  }
}

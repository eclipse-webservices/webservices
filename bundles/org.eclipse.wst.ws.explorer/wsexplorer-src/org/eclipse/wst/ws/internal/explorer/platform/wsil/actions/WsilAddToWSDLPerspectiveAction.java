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
import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.OpenWSDLAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel.WsilWsdlServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.WSILPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.transformer.WSILViewSelectionTransformer;

public class WsilAddToWSDLPerspectiveAction extends MultipleLinkAction
{
  public WsilAddToWSDLPerspectiveAction(Controller controller)
  {
    super(controller);
  }

  public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID)
  {
    StringBuffer actionLink = new StringBuffer("wsil/actions/WsilAddToWSDLPerspectiveActionJSP.jsp?");
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
    return "wsil/actions/WsilAddToWSDLPerspectiveActionJSP.jsp";
  }

  public ITransformer[] getTransformers()
  {
    ITransformer[] parentTransformers = super.getTransformers();
    ITransformer[] transformers = new ITransformer[parentTransformers.length+1];
    System.arraycopy(parentTransformers, 0, transformers, 0, parentTransformers.length);
    transformers[transformers.length-1] = new WSILViewSelectionTransformer(controller_, WsilModelConstants.LIST_MANAGER_WSDL_SERVICES, ActionInputs.VIEWID, WSILViewSelectionTransformer.WSDL_SERVICE);
    return transformers;
  }

  protected boolean executeSingleLinkAction()
  {
    int nodeID = Integer.parseInt((String) propertyTable_.get(ActionInputs.NODEID));
    int viewID = Integer.parseInt((String) propertyTable_.get(ActionInputs.VIEWID));
    WSILPerspective wsilPerspective = controller_.getWSILPerspective();
    Node selectedNode = wsilPerspective.getNodeManager().getNode(nodeID);
    WsilElement wsilElement = (WsilElement) selectedNode.getTreeElement();
    WsilWsdlServiceElement wsilWsdlServiceElement = (WsilWsdlServiceElement) wsilElement.getAllWSDLServices().getElementWithViewId(viewID).getObject();
    String wsdlUrl = wsilWsdlServiceElement.getWSDLServiceURL();
    OpenWSDLAction openWSDLAction = new OpenWSDLAction(controller_);
    Hashtable propertyTable = openWSDLAction.getPropertyTable();
    propertyTable.put(ActionInputs.QUERY_INPUT_WSDL_URL, wsdlUrl);
    boolean actionResult = openWSDLAction.run();
    if (actionResult)
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_INFO_ADD_WSDL_TO_WSDL_PERSPECTIVE_SUCCESSFUL", wsdlUrl));
    else
      wsilPerspective.getMessageQueue().addMessage(wsilPerspective.getMessage("MSG_ERROR_ADD_WSDL_TO_WSDL_PERSPECTIVE", wsdlUrl));
    return actionResult;
  }
}

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

import org.eclipse.wst.ws.internal.explorer.platform.actions.LinkAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*;

import java.util.Vector;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.servlet.http.*;

public class RefreshWSDLAction extends LinkAction {

  public RefreshWSDLAction(Controller controller) {
    super(controller);
  }

  public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID) {
    StringBuffer actionLink = new StringBuffer("wsdl/actions/WSDLRefreshActionJSP.jsp?");
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

  protected boolean processLinkParameters(HttpServletRequest request) {
    NodeManager nodeManager = controller_.getWSDLPerspective().getNodeManager();
    String nodeIDString = request.getParameter(ActionInputs.NODEID);
    int nodeID;
    try {
      nodeID = Integer.parseInt(nodeIDString);
    }
    catch (NumberFormatException nfe) {
      nodeID = nodeManager.getSelectedNodeId();
    }
    Node node = nodeManager.getNode(nodeID);
    while (node != null && !(node instanceof WSDLMainNode)) {
      if (node instanceof WSDLNode) {
        propertyTable_.put(ActionInputs.NODEID, String.valueOf(node.getNodeId()));
        return true;
      }
      node = node.getParent();
    }
    return false;
  }

  public boolean run() {
    int nodeID = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    MessageQueue messageQueue = wsdlPerspective.getMessageQueue();
    NodeManager nodeManager = wsdlPerspective.getNodeManager();
    Node wsdlNode = nodeManager.getNode(nodeID);
    WSDLElement wsdlElement = (WSDLElement)wsdlNode.getTreeElement();
    Definition definitionCopy = wsdlElement.getDefinition();
    Vector schemaListCopy = wsdlElement.getSchemaList();
    wsdlElement.setDefinition(null);
    wsdlElement.setSchemaList(new Vector());
    int selectedNodeID = nodeManager.getSelectedNode().getNodeId();

    try {
      // open WSDL
      Vector errorMessages = wsdlElement.loadWSDL();
      if (errorMessages.size() > 0) {
        messageQueue.addMessage(wsdlPerspective.getMessage("MSG_ERROR_XSD_VALIDATION"));
        for (int i=0;i<errorMessages.size();i++)
          messageQueue.addMessage((String)errorMessages.elementAt(i));
      }            
    }
    catch (WSDLException e)
    {
      messageQueue.addMessage(wsdlPerspective.getMessage("MSG_ERROR_OPEN_WSDL",wsdlElement.getWsdlUrl()));          
      handleUnexpectedException(wsdlPerspective,messageQueue,"WSDLException",e);
      wsdlElement.setDefinition(definitionCopy);
      wsdlElement.setSchemaList(schemaListCopy);
      return false;
    }

    // build the datamodel for this WSDLElement
    wsdlElement.buildModel();

    // Select a new node if the previous selected node no longer exists
    // If there is more than 1 service node, select the WSDL node. If there is more than 1 binding node, select the service node.
    // If there is just the one service and binding node, select the binding node.
    Node node = nodeManager.getNode(selectedNodeID);
    if (node == null) {
      selectedNodeID = wsdlNode.getNodeId();
      Vector serviceNodes = wsdlNode.getChildNodes();
      if (serviceNodes.size() == 1)
      {
        Node serviceNode = (Node)serviceNodes.elementAt(0);
        Vector bindingNodes = serviceNode.getChildNodes();
        if (bindingNodes.size() == 1)
        {
          Node bindingNode = (Node)bindingNodes.elementAt(0);
          selectedNodeID = bindingNode.getNodeId();
        }
        else
          selectedNodeID = serviceNode.getNodeId();
      }
      nodeManager.setSelectedNodeId(selectedNodeID);
      nodeManager.makeNodeVisible(nodeManager.getNode(selectedNodeID));
    }
    messageQueue.addMessage(wsdlPerspective.getMessage("MSG_INFO_REFRESH_WSDL_SUCCESSFUL", wsdlElement.getWsdlUrl()));
    return true;
  }
}

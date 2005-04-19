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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.ClearNodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLMainNode;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLNode;

import javax.servlet.http.*;

public class ClearWSDLAction extends ClearNodeAction {

  public ClearWSDLAction(Controller controller) {
    super(controller, controller.getWSDLPerspective().getNodeManager());
  }

  public static String getActionLink(int nodeID) {
    StringBuffer actionLink = new StringBuffer("wsdl/actions/ClearWSDLActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID);
    actionLink.append('=');
    actionLink.append(nodeID);
    return actionLink.toString();
  }

  protected boolean processLinkParameters(HttpServletRequest request) {
    String nodeIDString = request.getParameter(ActionInputs.NODEID);
    int nodeID;
    try {
      nodeID = Integer.parseInt(nodeIDString);
    }
    catch (NumberFormatException nfe) {
      nodeID = nodeManager_.getSelectedNodeId();
    }
    boolean paramValid = false;
    Node node = nodeManager_.getNode(nodeID);
    if (node instanceof WSDLMainNode)
      paramValid = true;
    else if (node instanceof WSDLNode)
      paramValid = true;
    else {
      while (node != null && !(node instanceof WSDLMainNode)) {
        node = node.getParent();
        if (node instanceof WSDLNode) {
          nodeID = node.getNodeId();
          paramValid = true;
          break;
        }
      }
    }
    propertyTable_.put(ActionInputs.NODEID, String.valueOf(nodeID));
    return paramValid;
  }

  public boolean run() {
    if (super.run()) {
      nodeManager_.setSelectedNodeId(nodeManager_.getRootNode().getNodeId());
      return true;
    }
    else
      return false;
  }

  public String getTreeContentVar() {
    return "wsdlNavigatorContent";
  }

  public String getTreeContentPage() {
    return "wsdl/wsdl_navigator_content.jsp";
  }

  public String getPropertiesContainerVar() {
    return "wsdlPropertiesContainer";
  }

  public String getPropertiesContainerPage() {
    return "wsdl/wsdl_properties_container.jsp";
  }

  public String getStatusContentVar() {
    return "wsdlStatusContent";
  }

  public String getStatusContentPage() {
    return "wsdl/wsdl_status_content.jsp";
  }
}

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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import java.io.OutputStream;
import javax.wsdl.Definition;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ImportToFileSystemAction;
import org.eclipse.wst.ws.internal.explorer.platform.actions.WSDLFileNameHelper;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;

public class WSDLImportWSDLToFileSystemAction extends ImportToFileSystemAction {
  private Definition definition_;
  private String defaultWSDLFileName_;

  public WSDLImportWSDLToFileSystemAction(Controller controller) {
    super(controller);
    definition_ = null;
    defaultWSDLFileName_ = "temp.wsdl";
  }

  public static final String getActionLink(int nodeId,int toolId,int viewId,int viewToolId) {
    return ImportToFileSystemAction.getActionLink(nodeId,toolId,viewId,viewToolId,"wsdl/actions/WSDLImportWSDLToFileSystemActionJSP.jsp");
  }

  public final boolean write(OutputStream os) {
    return writeWSDLDefinition(os, definition_);
  }

  public final String getDefaultFileName() {
    return defaultWSDLFileName_;
  }

  public final boolean run() {
    int nodeID;
    try {
      nodeID = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    }
    catch (NumberFormatException nfe) {
        return false;
    }
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    NodeManager nodeManager = wsdlPerspective.getNodeManager();
    Node node = nodeManager.getNode(nodeID);
    WSDLElement wsdlElement = (WSDLElement)node.getTreeElement();
    definition_ = wsdlElement.getDefinition();
    defaultWSDLFileName_ = WSDLFileNameHelper.getWSDLFileName(wsdlElement.getWsdlUrl());
    return (definition_ != null);
  }

  public final String getStatusContentVar() {
    return controller_.getWSDLPerspective().getStatusContentVar();
  }

  public final String getStatusContentPage() {
    return controller_.getWSDLPerspective().getStatusContentPage();
  }
}

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

import java.util.Vector;
import javax.wsdl.WSDLException;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;

public class OpenWSDLAction extends WSDLPropertiesFormAction {

  public OpenWSDLAction(Controller controller) {
    super(controller);
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException {
    String wsdlUrl = parser.getParameter(ActionInputs.QUERY_INPUT_WSDL_URL);
    FormTool formTool = getSelectedFormTool();
    propertyTable_.put(ActionInputs.QUERY_INPUT_WSDL_URL, wsdlUrl);
    formTool.updatePropertyTable(propertyTable_);
    return true;
  }

  public boolean run() {
    String wsdlUrl = (String)propertyTable_.get(ActionInputs.QUERY_INPUT_WSDL_URL);
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    MessageQueue messageQueue = wsdlPerspective.getMessageQueue();
    NodeManager nodeManager = wsdlPerspective.getNodeManager();
    Node rootNode = nodeManager.getRootNode();
    TreeElement rootElement = rootNode.getTreeElement();

    // create a WSDLElement representing the WSDL
    WSDLElement wsdlElement = new WSDLElement(wsdlUrl, rootElement.getModel(), wsdlUrl);
    try
    {
      // open WSDL
      Vector errorMessages = wsdlElement.loadWSDL();
      if (errorMessages.size() > 0)
      {
        messageQueue.addMessage(wsdlPerspective.getMessage("MSG_ERROR_XSD_VALIDATION"));
        for (int i=0;i<errorMessages.size();i++)
          messageQueue.addMessage((String)errorMessages.elementAt(i));
      }            
    }
    catch (WSDLException e)
    {
      messageQueue.addMessage(wsdlPerspective.getMessage("MSG_ERROR_OPEN_WSDL",wsdlUrl));          
      handleUnexpectedException(wsdlPerspective,messageQueue,"WSDLException",e);
      return false;
    }
    rootElement.connect(wsdlElement,WSDLModelConstants.REL_WSDL, ModelConstants.REL_OWNER);

    // build the datamodel for this WSDLElement
    wsdlElement.buildModel();

    // If there is more than 1 service node, select the WSDL node. If there is more than 1 binding node, select the service node.
    // If there is just the one service and binding node, select the binding node.
    // select the new WSDL node
    Node wsdlNode = rootNode.getChildNode(wsdlElement);
    Vector serviceNodes = wsdlNode.getChildNodes();
    int newSelectedNodeId = wsdlNode.getNodeId();
    if (serviceNodes.size() == 1)
    {
      Node serviceNode = (Node)serviceNodes.elementAt(0);
      Vector bindingNodes = serviceNode.getChildNodes();
      if (bindingNodes.size() == 1)
      {
        Node bindingNode = (Node)bindingNodes.elementAt(0);
        newSelectedNodeId = bindingNode.getNodeId();
      }
      else
        newSelectedNodeId = serviceNode.getNodeId();
    }
    nodeManager.setSelectedNodeId(newSelectedNodeId);
    nodeManager.makeNodeVisible(nodeManager.getNode(newSelectedNodeId));
    addToHistory(ActionInputs.PERSPECTIVE_WSDL,SelectWSDLPropertiesToolAction.getActionLink(newSelectedNodeId, 0, ActionInputs.VIEWID_DEFAULT, ActionInputs.VIEWTOOLID_DEFAULT, true));
    messageQueue.addMessage(wsdlPerspective.getMessage("MSG_INFO_OPEN_WSDL_SUCCESSFUL", wsdlUrl));
    return true;
  }
}

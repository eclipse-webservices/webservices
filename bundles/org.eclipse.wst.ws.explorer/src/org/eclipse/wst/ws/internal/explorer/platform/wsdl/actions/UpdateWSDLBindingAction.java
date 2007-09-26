/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Tool;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLBindingElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.InvokeWSDLOperationTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLBindingNode;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;

public class UpdateWSDLBindingAction extends WSDLPropertiesFormAction
{
  public UpdateWSDLBindingAction(Controller controller)
  {
    super(controller);
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    String[] nodeIds = parser.getParameterValues(ActionInputs.NODEID);
    if (nodeIds == null)
      nodeIds = new String[0];
    propertyTable_.put(ActionInputs.NODEID, nodeIds);
    String[] endpoints = parser.getParameterValues(WSDLActionInputs.END_POINT);
    if (endpoints == null)
      endpoints = new String[0];
    propertyTable_.put(WSDLActionInputs.END_POINT, endpoints);
    return true;
  }

  public boolean run()
  {
    String[] nodeIds = getPropertyAsStringArray(ActionInputs.NODEID);
    String[] endpoints = getPropertyAsStringArray(WSDLActionInputs.END_POINT);
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    NodeManager nodeManager = wsdlPerspective.getNodeManager();
    for (int i = 0; i < nodeIds.length; i++)
    {
      try
      {
        Node bindingNode = nodeManager.getNode(Integer.parseInt(nodeIds[i]));
        if (bindingNode instanceof WSDLBindingNode)
        {
          WSDLBindingElement bindingElement = (WSDLBindingElement)bindingNode.getTreeElement();
          String[] endpointsCopy = endpoints;
          bindingElement.setEndPoints(endpointsCopy);
          if (endpointsCopy.length <= 0)
            endpointsCopy = bindingElement.getEndPoints();
          if (endpointsCopy.length > 0)
          {
            Vector operationNodes = bindingNode.getChildNodes();
            for (Iterator it = operationNodes.iterator(); it.hasNext();)
            {
              Node operationNode = (Node)it.next();
              List tools = operationNode.getCurrentToolManager().getTools();
              for (Iterator toolsIterator = tools.iterator(); toolsIterator.hasNext();)
              {
                Tool tool = (Tool)toolsIterator.next();
                if (tool instanceof InvokeWSDLOperationTool)
                  ((InvokeWSDLOperationTool)tool).setEndPoint(endpointsCopy[0]);
              }
            }
          }
        }
      }
      catch (NumberFormatException nfe)
      {
      }
    }
    MessageQueue messageQueue = wsdlPerspective.getMessageQueue();
    messageQueue.addMessage(wsdlPerspective.getMessage("MSG_INFO_UPDATE_WSDL_BINDING_SUCCESSFUL"));
    return true;
  }
}

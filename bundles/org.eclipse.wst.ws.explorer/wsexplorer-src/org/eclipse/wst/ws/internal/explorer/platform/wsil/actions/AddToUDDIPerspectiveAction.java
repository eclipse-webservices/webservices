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

import org.eclipse.wst.ws.internal.explorer.platform.actions.Action;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.actions.OpenRegistryAction;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.RegistryElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.RegistryNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIMainNode;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective.*;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.*;

public abstract class AddToUDDIPerspectiveAction extends MultipleLinkAction
{
  public AddToUDDIPerspectiveAction(Controller controller)
  {
    super(controller);
  }

  public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID)
  {
    StringBuffer actionLink = new StringBuffer("wsil/actions/WsilAddToUDDIPerspectiveActionJSP.jsp?");
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
    return "wsil/actions/WsilAddToUDDIPerspectiveActionJSP.jsp";
  }

  public static Action newAction(HttpServletRequest request, Controller controller)
  {
    String nodeID = request.getParameter(ActionInputs.NODEID);
    NodeManager nodeManager = controller.getWSILPerspective().getNodeManager();
    Tool selectedTool = nodeManager.getNode(Integer.parseInt(nodeID)).getToolManager().getSelectedTool();
    if (selectedTool instanceof ListUDDIServicesTool)
      return new AddServiceToUDDIPerspectiveAction(controller);
    else if (selectedTool instanceof ListUDDIBusinessTool)
      return new AddBusinessToUDDIPerspectiveAction(controller);
    else
      return new NullAction();
  }

  protected boolean createRegistryInUDDIPerspective(String inquiryAPI, String publishAPI, String registryName)
  {
    Vector registryNodes = getRegistryNodesByInquiryURL(inquiryAPI);
    if (registryNodes != null)
    {
      // Always attempt to use an existing registry.
      Node registryNode = (Node) registryNodes.elementAt(0);
      NodeManager nodeManager = registryNode.getNodeManager();
      nodeManager.setSelectedNodeId(registryNode.getNodeId());
      return true;
    }
    // open the registry if it is not already opened in the UDDI perspective
    OpenRegistryAction openRegAction = new OpenRegistryAction(controller_);
    // populate the property table
    Hashtable propertyTable = openRegAction.getPropertyTable();
    if (Validator.validateString(registryName))
      propertyTable.put(UDDIActionInputs.REGISTRY_NAME, registryName);
    else
      return false;
    if (Validator.validateURL(inquiryAPI))
      propertyTable.put(UDDIActionInputs.INQUIRY_URL, inquiryAPI);
    else
      return false;
    // run the action
    if (!openRegAction.run())
      return false;
    return true;
  }

  protected Vector getRegistryNodesByInquiryURL(String inquiryURL)
  {
    Vector registryNodes = null;
    NodeManager navigatorManager = controller_.getUDDIPerspective().getNavigatorManager();
    UDDIMainNode uddiMainNode = (UDDIMainNode) (navigatorManager.getRootNode());
    Vector childNodes = uddiMainNode.getChildNodes();
    for (int i = 0; i < childNodes.size(); i++)
    {
      Node childNode = (Node) childNodes.elementAt(i);
      if (childNode instanceof RegistryNode)
      {
        RegistryElement regElement = (RegistryElement) childNode.getTreeElement();
        if (regElement.getInquiryURL().equals(inquiryURL))
        {
          if (registryNodes == null)
            registryNodes = new Vector();
          registryNodes.addElement(childNode);
        }
      }
    }
    return registryNodes;
  }
}

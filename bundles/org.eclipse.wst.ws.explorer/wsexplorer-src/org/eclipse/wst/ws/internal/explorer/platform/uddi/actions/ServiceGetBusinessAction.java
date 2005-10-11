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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.actions.NodeAction;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.ServiceNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.uddi4j.datatype.service.BusinessService;

public class ServiceGetBusinessAction extends NodeAction
{
  private ServiceNode serviceNode_;
  public ServiceGetBusinessAction(Controller controller)
  {
    super(controller,controller.getUDDIPerspective().getNavigatorManager());
    serviceNode_ = null;
  }

  // uddi/actions/ServiceGetBusinessActionJSP.jsp?nodeId=...
  public static String getActionLink(int nodeId)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/ServiceGetBusinessActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    return actionLink.toString();
  }

  public final boolean validateService()
  {
    int serviceNodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    serviceNode_ = (ServiceNode)nodeManager_.getNode(serviceNodeId);
    ServiceElement serviceElement = (ServiceElement)serviceNode_.getTreeElement();
    RegFindServiceUUIDAction verifyAction = new RegFindServiceUUIDAction(controller_);
    Hashtable propertyTable = verifyAction.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE,Boolean.TRUE);
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_KEY,serviceElement.getBusinessService().getServiceKey());
    boolean result = verifyAction.run();
    if (result)
      propertyTable_.put(UDDIActionInputs.LATEST_OBJECT,propertyTable.get(UDDIActionInputs.LATEST_OBJECT));
    return result;
  }

  public final String getActionLinkForHistory()
  {
    return null;
  }

  public final boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    BusinessService bs = (BusinessService)propertyTable_.get(UDDIActionInputs.LATEST_OBJECT);

    RegFindBusinessUUIDAction action = new RegFindBusinessUUIDAction(controller_);
    Hashtable propertyTable = action.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_NAME,uddiPerspective.getMessage("NODE_NAME_SERVICE_BUSINESS",serviceNode_.getNodeName()));
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY,bs.getBusinessKey());

    return action.run();
  }
}

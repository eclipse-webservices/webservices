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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceInterfaceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.ServiceInterfaceNode;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.util.TModelBag;

public class ServiceInterfaceGetServicesAction extends UDDINodeAction
{
  private ServiceInterfaceNode siNode_;
  public ServiceInterfaceGetServicesAction(Controller controller)
  {
    super(controller);
    siNode_ = null;
  }

  // uddi/actions/ServiceInterfacGetServicesActionJSP.jsp?nodeId=...
  public static String getActionLink(int nodeId)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/ServiceInterfaceGetServicesActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    return actionLink.toString();
  }

  public final boolean validateServiceInterface()
  {
    int siNodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    siNode_ = (ServiceInterfaceNode)nodeManager_.getNode(siNodeId);
    ServiceInterfaceElement siElement = (ServiceInterfaceElement)siNode_.getTreeElement();
    RegFindServiceInterfaceUUIDAction verifyAction = new RegFindServiceInterfaceUUIDAction(controller_);
    Hashtable propertyTable = verifyAction.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE,Boolean.TRUE);
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY,siElement.getTModel().getTModelKey());
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
    TModel tModel = (TModel)propertyTable_.get(UDDIActionInputs.LATEST_OBJECT);

    RegFindServicesAdvancedAction action = new RegFindServicesAdvancedAction(controller_);
    Hashtable propertyTable = action.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_NAME,uddiPerspective.getMessage("NODE_NAME_SI_SERVICES",siNode_.getNodeName()));
    
    ListElement siListElement = new ListElement(tModel);
    siListElement.setTargetViewToolInfo(siNode_.getNodeId(),siNode_.getToolManager().getSelectedToolId(),siNode_.getViewId());
    Vector siListVector = new Vector();
    siListVector.addElement(siListElement);
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES,siListVector);
    
    Vector tModelKeyStringVector = new Vector();
    tModelKeyStringVector.addElement(tModel.getTModelKey());
    TModelBag tModelBag = new TModelBag(tModelKeyStringVector);
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_TMODELBAG,tModelBag);
    
    return action.run();
  }
}

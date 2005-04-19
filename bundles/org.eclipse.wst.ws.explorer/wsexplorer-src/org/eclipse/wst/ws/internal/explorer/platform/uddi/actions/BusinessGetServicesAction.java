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

import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.*;

import org.uddi4j.datatype.business.BusinessEntity;

import java.util.*;

public class BusinessGetServicesAction extends UDDINodeAction
{
  private BusinessNode busNode_;
  public BusinessGetServicesAction(Controller controller)
  {
    super(controller);
    busNode_ = null;
  }

  // uddi/actions/BusinessGetServicesActionJSP.jsp?nodeId=...
  public static String getActionLink(int nodeId)
  {
    StringBuffer actionLink = new StringBuffer("uddi/actions/BusinessGetServicesActionJSP.jsp?");
    actionLink.append(ActionInputs.NODEID).append('=').append(nodeId);
    return actionLink.toString();
  }

  public final boolean validateBusiness()
  {
    int busNodeId = Integer.parseInt((String)propertyTable_.get(ActionInputs.NODEID));
    busNode_ = (BusinessNode)nodeManager_.getNode(busNodeId);
    BusinessElement busElement = (BusinessElement)busNode_.getTreeElement();
    RegFindBusinessUUIDAction verifyAction = new RegFindBusinessUUIDAction(controller_);
    Hashtable propertyTable = verifyAction.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE,Boolean.TRUE);
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY,busElement.getBusinessEntity().getBusinessKey());
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
    BusinessEntity sp = (BusinessEntity)propertyTable_.get(UDDIActionInputs.LATEST_OBJECT);

    RegFindServicesAdvancedAction action = new RegFindServicesAdvancedAction(controller_);
    Hashtable propertyTable = action.getPropertyTable();
    propertyTable.put(UDDIActionInputs.QUERY_NAME,uddiPerspective.getMessage("NODE_NAME_BUSINESS_SERVICES",busNode_.getNodeName()));
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_PROVIDER,sp);    
    
    ListElement businessListElement = new ListElement(sp);
    businessListElement.setTargetViewToolInfo(busNode_.getNodeId(),busNode_.getToolManager().getSelectedToolId(),busNode_.getViewId());
    Vector businessListVector = new Vector();
    businessListVector.addElement(businessListElement);
    propertyTable.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS,businessListVector);
    
    
    return action.run();
  }
}
